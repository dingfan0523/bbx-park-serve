
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInventoryRecord;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantInventoryRecordParam;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantInventoryRecordRepository;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantInventoryRecordService;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


/**
 * 餐厅物料库存
 */
@Slf4j
@Service
public class RestaurantInventoryRecordServiceImpl extends ServiceImpl<RestaurantInventoryRecordRepository, RestaurantInventoryRecord> implements IRestaurantInventoryRecordService {

    @Autowired
    @Qualifier("asyncEventBusExecutor")
    private Executor busExecutorService;

    @Autowired
    private IFileService fileService;

    @Override
    public ImportReturnModel analysisAndCheckExcelData(MultipartFile file, File fi) {
        this.remove(Wrappers.<RestaurantInventoryRecord>lambdaQuery().eq(RestaurantInventoryRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
        List<String> returnModels = new ArrayList<>();
        boolean success = false;
        try {
            RestaurantInventoryRecordListener listener = new RestaurantInventoryRecordListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), RestaurantInventoryRecordParam.class, listener).autoTrim(true).headRowNumber(1).sheet(0).doRead();
            List<RestaurantInventoryRecordParam> addList = listener.getAddList();
            List<RestaurantInventoryRecord> list = addList.stream().map(p -> {
                RestaurantInventoryRecord record = BeanUtil.toBean(p,RestaurantInventoryRecord.class);
                record.setFileId(fi.getId());
                record.setCreatorId(fi.getCreatorId());
                record.setCreateTime(fi.getCreateTime());
                record.setUpdatorId(fi.getUpdatorId());
                record.setTenantId(fi.getTenantId());
                record.setUpdateTime(fi.getUpdateTime());
                return record;
            }).collect(Collectors.toList());
            //todo:删除数据
            removeData();
            busExecutorService.execute(() -> {
                this.saveBatch(list);
            });
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            fileService.remove(fi.getId());
            returnModels.add("餐料库存信息导入异常，原因是:"+e.getMessage());
            log.error("餐料库存信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.update(Wrappers.<RestaurantInventoryRecord>lambdaUpdate().set(RestaurantInventoryRecord::getDeleted, Status.disabled.getKey()).eq(RestaurantInventoryRecord::getFileId, fileId));
    }

    private void removeData(){
       this.remove((Wrappers.<RestaurantInventoryRecord>lambdaQuery()
               .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), RestaurantInventoryRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId())));
    }

}
