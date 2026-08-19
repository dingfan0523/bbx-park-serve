
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.CommentFileUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.config.minio.configure.service.FileCenterService;
import com.cgnpc.bbxpark.config.minio.model.FileModel;
import com.cgnpc.bbxpark.meeting.domain.ThirdMeetingRecord;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.meeting.dto.param.ThirdMeetingRecordImportParam;
import com.cgnpc.bbxpark.meeting.service.IThirdMeetingRecordService;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
public class ThirdMeetingRecordImportService {
    private static final long FILE_SIZE = 5;

    @Autowired
    private IThirdMeetingRecordService thirdMeetingRecordService;
    @Autowired
    private FileCenterService fileCenterService;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IUserApiService userApiService;

    private Long saveFile(String name,String url,Integer type){
        File file = new File();
        file.setType(type);
        file.setName(name);
        file.setUrl(url);
        file.setCreateBy(userApiService.getCurrentStaffName());
        fileService.save(file);
        return file.getId();
    }

    /**
     * 集团会议数据导入
     * @return 错误信息
     */
    public ImportReturnModel importMeeting(MultipartFile file, Integer type) {
        //文件格式校验
        CommentFileUtils.uploadVerify(FILE_SIZE, file);
        //同文件重复上传校验
        AssertUtils.isFalse(fileService.exist(file.getOriginalFilename(),type),"上传文件重复");
        //上传文件数据
        FileModel fileModel = fileCenterService.upload(file, null, WebFrameworkUtils.getHeaderTenantId());
        Long fileId = saveFile(file.getOriginalFilename(),fileModel.getUrl(),type);
        //反参数据
        List<String> returnModels = new ArrayList<>();
        boolean success = false;
        try{
            //读取并校验excel数据
            List<ThirdMeetingRecord> list = analysisAndCheckExcelData(returnModels, file);
            if (CollUtil.isEmpty(returnModels) && checkRepeatData(list,returnModels)) {
                //保存数据
                Long tenantId = WebFrameworkUtils.getHeaderTenantId();
                Date date = new Date();
                list.forEach(r->{
                    r.setFileId(fileId);
                    r.setTenantId(tenantId);
                    r.setRecordTime(date);
                    r.setDuration(calculateDuration(r.getStartTime(),r.getEndTime()));
                });
                thirdMeetingRecordService.saveBatch(list);
                returnModels.add("导入成功"+list.size()+"条");
                success = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(!success){
                fileService.remove(fileId);
            }
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }


    /**
     * 解析导入信息
     * @param file 文件
     */
    private List<ThirdMeetingRecord> analysisAndCheckExcelData(List<String> returnModels, MultipartFile file) {
        try {
            ThirdMeetingRecordImportListener listener = new ThirdMeetingRecordImportListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), ThirdMeetingRecordImportParam.class, listener).headRowNumber(1).sheet(0).doRead();
            List<ThirdMeetingRecordImportParam> addList = listener.getAddList();

            List<ThirdMeetingRecordImportParam> importList = addList.stream().filter(f -> StrUtil.isNotEmpty(f.getErrMessage())).collect(Collectors.toList());
            //校验sheet存在非法数据
            if (CollUtil.isNotEmpty(importList)) {
                returnModels.addAll(importList.stream().map(ThirdMeetingRecordImportParam::getErrMessage).collect(Collectors.toList()));
                return Collections.emptyList();
            }
            //校验通过数据
            return addList.stream().map(p -> {
                ThirdMeetingRecord record = BeanUtil.toBean(p,ThirdMeetingRecord.class);
                record.setStartTime(DateUtil.parse(p.getStartTime(), "yyyy-MM-dd HH:mm"));
                record.setEndTime(DateUtil.parse(p.getEndTime(), "yyyy-MM-dd HH:mm"));
                return record;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            returnModels.add("菜品导入异常，原因是:"+e.getMessage());
            log.error("菜品导入异常，原因是{}", e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 校验重复数据
     */
    private boolean checkRepeatData(List<ThirdMeetingRecord> list,List<String> returnModels) {
        List<String> conferIds = list.stream().map(ThirdMeetingRecord::getConferId).collect(Collectors.toList());
        List<List<String>> batches = partition(conferIds,200);
        for (List<String> batch:batches){
            List<ThirdMeetingRecord> records = thirdMeetingRecordService.list(Wrappers.<ThirdMeetingRecord>lambdaQuery().in(ThirdMeetingRecord::getConferId,batch)
                    .eq(ThirdMeetingRecord::getTenantId,WebFrameworkUtils.getHeaderTenantId()).eq(ThirdMeetingRecord::getDeleted, Delete.NORMAL.getKey()).select(ThirdMeetingRecord::getConferId));
            if(CollectionUtils.isNotEmpty(records)){
                records.forEach(record->{
                    returnModels.add("会议ID["+record.getConferId()+"]已存在");
                });
                return false;
            }
        }
        return true;
    }

    /**
     * 计算两个时间的时长(单位:小时)
     * @param start 开始时间
     * @param end 结束时间
     * @return 时长
     */
    private static BigDecimal calculateDuration(Date start, Date end){
        if(start == null || end == null){
            return BigDecimal.ZERO;
        }
        long duration = DateUtil.between(start,end, DateUnit.MINUTE);
        return NumberUtil.round(duration /60.0,2);
    }

    /**
     * 数据分段拆分
     * @param list 原数据
     * @param size 拆分大小
     * @return 数据
     */
    private static List<List<String>> partition(List<String> list,int size){
        List<List<String>> parts = new ArrayList<>();
        int total = list.size();
        for(int i=0; i < total; i+=size){
            parts.add(list.subList(i,Math.min(i + size,total)));
        }
        return parts;
    }
}
