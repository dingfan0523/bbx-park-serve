
package com.cgnpc.bbxpark.traffic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.HttpClientUtil;
import com.cgnpc.bbxpark.common.utils.NumberUtil;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleCarTaskRecord;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleCarTaskRecordParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleCarTaskRecordTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleCarTaskRecordRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleCarTaskRecordService;
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

/***
 * @Description 车辆行驶记录服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleCarTaskRecordServiceImpl extends ServiceImpl<DwdVehicleCarTaskRecordRepository, DwdVehicleCarTaskRecord> implements IDwdVehicleCarTaskRecordService {

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
            DwdVehicleCarTaskRecordListener listener = new DwdVehicleCarTaskRecordListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleCarTaskRecordParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehicleCarTaskRecordParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleCarTaskRecord> list = addList.stream().map(p -> {
                DwdVehicleCarTaskRecord record = BeanUtil.toBean(p,DwdVehicleCarTaskRecord.class);
                record.setFileId(fi.getId());
                record.setDepartTime(DateUtils.parseDate(p.getDepartTimeStr()));
                record.setReturnTime(DateUtils.parseDate(p.getReturnTimeStr()));
                record.setImportTime(new Date());
                return record;
            }).collect(Collectors.toList());

            busExecutorService.execute(() -> {
                this.saveBatch(list);
            });
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            fileService.remove(fi.getId());
            returnModels.add("车辆行驶记录导入异常，原因是:"+e.getMessage());
            log.error("车辆行驶记录导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleCarTaskRecord>lambdaQuery().eq(DwdVehicleCarTaskRecord::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/4145760724848090";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleCarTaskRecordTemp> tempList = HttpClientUtil.postForList(url, jsonBody, DwdVehicleCarTaskRecordTemp.class);
        List<DwdVehicleCarTaskRecord> infos = tempList.stream().map(item->{
            DwdVehicleCarTaskRecord info = new DwdVehicleCarTaskRecord();
            info.setId(item.getId());
            info.setCarCompany(item.getCar_company());
            info.setCarPlate(item.getCar_plate());
            info.setDriverName(item.getDriver_name());
            info.setTaskType(item.getTask_type());
            info.setTaskDetail(item.getTask_detail());
            info.setDepartTime(DateUtils.parseDate(item.getDepart_time()));
            info.setBeforeMileage(item.getBefore_mileage());
            info.setReturnTime(DateUtils.parseDate(item.getReturn_time()));
            info.setAfterMileage(item.getAfter_mileage());
            info.setSingleMileage(NumberUtil.toDouble(item.getSingle_mileage()));
            info.setUseDept(item.getUse_dept());
            info.setCarUser(item.getCar_user());
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }

}
