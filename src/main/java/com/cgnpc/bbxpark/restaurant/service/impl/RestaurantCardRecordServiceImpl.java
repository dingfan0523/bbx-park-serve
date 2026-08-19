
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
import com.cgnpc.bbxpark.restaurant.domain.RestaurantCardRecord;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInboundRecord;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantCardRecordParam;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantCardRecordRepository;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantCardRecordService;
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
 * 餐厅一卡通
 */
@Slf4j
@Service
public class RestaurantCardRecordServiceImpl extends ServiceImpl<RestaurantCardRecordRepository, RestaurantCardRecord> implements IRestaurantCardRecordService {

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
            RestaurantCardRecordListener listener = new RestaurantCardRecordListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), RestaurantCardRecordParam.class, listener).headRowNumber(0).autoTrim(true).sheet(0).doRead();
            List<RestaurantCardRecordParam> addList = listener.getAddList();
            //校验通过数据
            List<RestaurantCardRecord> list = addList.stream().map(p -> {
                RestaurantCardRecord record = BeanUtil.toBean(p,RestaurantCardRecord.class);
                record.setFileId(fi.getId());
                record.setCreatorId(fi.getCreatorId());
                record.setCreateTime(fi.getCreateTime());
                record.setUpdatorId(fi.getUpdatorId());
                record.setTenantId(fi.getTenantId());
                record.setUpdateTime(fi.getUpdateTime());
                record.setOccurTime(DateUtil.parse(p.getOccurTimeStr(), "yyyy-MM-dd HH:mm:ss"));
                return record;
            }).collect(Collectors.toList());
            //todo:校验数据库重复数据
            if(checkRepeatData(list.get(0).getOccurTime(), list.get(list.size() - 1).getOccurTime())){
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
            returnModels.add("一卡通消费信息导入异常，原因是:"+e.getMessage());
            log.error("一卡通消费信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.update(Wrappers.<RestaurantCardRecord>lambdaUpdate().set(RestaurantCardRecord::getDeleted, Status.disabled.getKey()).eq(RestaurantCardRecord::getFileId, fileId));
    }

    private Boolean checkRepeatData(Date time1, Date time2){
        List<RestaurantCardRecord> records =  this.list(Wrappers.<RestaurantCardRecord>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), RestaurantCardRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(RestaurantCardRecord::getDeleted, Status.enabled.getKey())
                .orderByDesc(RestaurantCardRecord::getOccurTime)
                .last("limit 1"));
        if(CollectionUtil.isEmpty(records)){
            return false;
        }
        return time1.before(records.get(0).getOccurTime()) || time2.before(records.get(0).getOccurTime());
    }
}
