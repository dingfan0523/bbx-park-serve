
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
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInboundRecord;
import com.cgnpc.bbxpark.restaurant.dto.param.RestaurantInboundRecordParam;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantInboundRecordRepository;
import com.cgnpc.bbxpark.restaurant.service.IRestaurantInboundRecordService;
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
 * 餐厅物料入库
 */
@Slf4j
@Service
public class RestaurantInboundRecordServiceImpl extends ServiceImpl<RestaurantInboundRecordRepository, RestaurantInboundRecord> implements IRestaurantInboundRecordService {

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
            RestaurantInboundRecordListener listener = new RestaurantInboundRecordListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), RestaurantInboundRecordParam.class, listener).headRowNumber(3).autoTrim(true).sheet(0).doRead();
            List<RestaurantInboundRecordParam> addList = listener.getAddList();
            //todo:校验数据库重复数据
            //校验通过数据
            List<RestaurantInboundRecord> list = addList.stream().map(p -> {
                RestaurantInboundRecord record = BeanUtil.toBean(p,RestaurantInboundRecord.class);
                record.setFileId(fi.getId());
                record.setCreatorId(fi.getCreatorId());
                record.setCreateTime(fi.getCreateTime());
                record.setUpdatorId(fi.getUpdatorId());
                record.setTenantId(fi.getTenantId());
                record.setUpdateTime(fi.getUpdateTime());
                record.setInboundTime(DateUtil.parse(p.getInboundTimeStr()+ " 00:00:00", "yyyy/MM/dd HH:mm:ss"));
                return record;
            }).collect(Collectors.toList());
            //todo:校验数据库重复数据
            if(checkRepeatData(list.get(0).getInboundTime(), list.get(list.size() - 1).getInboundTime())){
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
            returnModels.add("入库餐料信息导入异常，原因是:"+e.getMessage());
            log.error("入库餐料信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.update(Wrappers.<RestaurantInboundRecord>lambdaUpdate().set(RestaurantInboundRecord::getDeleted, Status.disabled.getKey()).eq(RestaurantInboundRecord::getFileId, fileId));
    }

    private Boolean checkRepeatData(Date time1, Date time2){
        List<RestaurantInboundRecord> records =  this.list(Wrappers.<RestaurantInboundRecord>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), RestaurantInboundRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(RestaurantInboundRecord::getDeleted, Status.enabled.getKey())
                .orderByDesc(RestaurantInboundRecord::getInboundTime)
                .last("limit 1"));
        if(CollectionUtil.isEmpty(records)){
            return false;
        }
        return time1.before(records.get(0).getInboundTime()) || time2.before(records.get(0).getInboundTime());
    }
}
