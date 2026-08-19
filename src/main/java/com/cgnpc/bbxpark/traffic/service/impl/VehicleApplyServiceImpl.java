
package com.cgnpc.bbxpark.traffic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleRepair;
import com.cgnpc.bbxpark.traffic.domain.VehicleApply;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleRepairParam;
import com.cgnpc.bbxpark.traffic.dto.VehicleApplyParam;
import com.cgnpc.bbxpark.traffic.mapper.VehicleApplyRepository;
import com.cgnpc.bbxpark.traffic.service.IVehicleApplyService;
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


@Service
@Slf4j
public class VehicleApplyServiceImpl extends ServiceImpl<VehicleApplyRepository, VehicleApply> implements IVehicleApplyService {
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
            VehicleApplyListener listener = new VehicleApplyListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), VehicleApplyParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<VehicleApplyParam> addList = listener.getAddList();
            //校验通过数据
            List<VehicleApply> list = addList.stream().map(p -> {
                VehicleApply record = BeanUtil.toBean(p,VehicleApply.class);
                record.setFileId(fi.getId());
                if(StringUtils.isNotEmpty(p.getUseTime1Str())){
                    record.setUseTime1(DateUtils.parseDate(p.getUseTime1Str()));
                }
                if(StringUtils.isNotEmpty(p.getUseTime2Str())){
                    record.setUseTime2(DateUtils.parseDate(p.getUseTime2Str()));
                }
                if(StringUtils.isNotEmpty(p.getCreateTimeStr())){
                    record.setCreateTime(DateUtils.parseDate(p.getCreateTimeStr()));
                }
                if(StringUtils.isNotEmpty(p.getUpdateTimeStr())){
                    record.setUpdateTime(DateUtils.parseDate(p.getUpdateTimeStr()));
                }
                record.setImportTime(new Date());
                return record;
            }).collect(Collectors.toList());

            busExecutorService.execute(() -> this.saveBatch(list));
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            fileService.remove(fi.getId());
            returnModels.add("电召车出车记录导入异常，原因是:"+e.getMessage());
            log.error("电召车出车记录导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<VehicleApply>lambdaQuery().eq(VehicleApply::getFileId, fileId));
    }
}
