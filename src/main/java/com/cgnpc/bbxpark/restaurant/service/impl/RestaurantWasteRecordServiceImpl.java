
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantWasteRecord;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantWasteRecordParam;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantWasteRecordRepository;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantWasteRecordService;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.cud.core.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


/**
 * 餐厅垃圾
 */
@Slf4j
@Service
public class RestaurantWasteRecordServiceImpl extends ServiceImpl<RestaurantWasteRecordRepository, RestaurantWasteRecord> implements IRestaurantWasteRecordService {

    @Autowired
    @Qualifier("asyncEventBusExecutor")
    private Executor busExecutorService;

    @Autowired
    private IFileService fileService;

    @Override
    public ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File fi) {
        List<String> returnModels = new ArrayList<>();
        boolean success = false;
        try {
            RestaurantWasteRecordListener listener = new RestaurantWasteRecordListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), RestaurantWasteRecordParam.class, listener).autoTrim(true).headRowNumber(1).sheet(0).doRead();
            List<RestaurantWasteRecordParam> addList = listener.getAddList();
            //todo:校验数据库重复数据
            //校验通过数据
            List<RestaurantWasteRecord> list = addList.stream().map(p -> {
                RestaurantWasteRecord record = BeanUtil.toBean(p,RestaurantWasteRecord.class);
                record.setFileId(fi.getId());
                record.setCreatorId(fi.getCreatorId());
                record.setCreateTime(fi.getCreateTime());
                record.setUpdatorId(fi.getUpdatorId());
                record.setTenantId(fi.getTenantId());
                record.setUpdateTime(fi.getUpdateTime());
                record.setType(p.getType().contains("厨") ? "厨余垃圾" :"餐余垃圾");
                record.setHandleTime(DateUtil.parse(p.getHandleTimeStr() + " 00:00:00", "yyyy-MM-dd HH:mm"));
                return record;
            }).collect(Collectors.toList());
            if(checkRepeatData(list.get(0).getHandleTime(), list.get(list.size() - 1).getHandleTime())){
                fileService.remove(fi.getId());
                throw new BaseException("上传数据与已上传的文件数据发生时间重复");
            }
            busExecutorService.execute(() -> {
                this.saveBatch(list);
            });
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            fileService.remove(fi.getId());
            returnModels.add("餐料垃圾处理信息导入异常，原因是:"+e.getMessage());
            log.error("餐料垃圾处理信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.update(Wrappers.<RestaurantWasteRecord>lambdaUpdate().set(RestaurantWasteRecord::getDeleted, Status.disabled.getKey()).eq(RestaurantWasteRecord::getFileId, fileId));
    }


    private Boolean checkRepeatData(Date time1, Date time2){
        List<RestaurantWasteRecord> records =  this.list(Wrappers.<RestaurantWasteRecord>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), RestaurantWasteRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(RestaurantWasteRecord::getDeleted, Status.enabled.getKey())
                .orderByDesc(RestaurantWasteRecord::getHandleTime)
                .last("limit 1"));
        if(CollectionUtil.isEmpty(records)){
            return false;
        }
        return time1.before(records.get(0).getHandleTime()) || time2.before(records.get(0).getHandleTime());
    }
}
