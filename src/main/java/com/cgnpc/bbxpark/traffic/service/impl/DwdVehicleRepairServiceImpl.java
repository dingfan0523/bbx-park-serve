
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
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMaintain;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleRepair;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleMaintainTemp;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleRepairParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleRepairTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleRepairRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleRepairService;
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
 * @Description 车辆维修信息服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleRepairServiceImpl extends ServiceImpl<DwdVehicleRepairRepository, DwdVehicleRepair> implements IDwdVehicleRepairService {

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
            DwdVehicleRepairListener listener = new DwdVehicleRepairListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleRepairParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehicleRepairParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleRepair> list = addList.stream().map(p -> {
                DwdVehicleRepair record = BeanUtil.toBean(p,DwdVehicleRepair.class);
                record.setFileId(fi.getId());
                record.setApplyDate(DateUtils.parseDate(p.getApplyDateStr()));
                record.setConfirmFinishTime(DateUtils.parseDate(p.getConfirmFinishTimeStr()));
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
            returnModels.add("车辆维修信息导入异常，原因是:"+e.getMessage());
            log.error("车辆维修信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleRepair>lambdaQuery().eq(DwdVehicleRepair::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/2795653118034044";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleRepairTemp> tempList = HttpClientUtil.postForList(url, jsonBody, DwdVehicleRepairTemp.class);
        List<DwdVehicleRepair> infos = tempList.stream().map(item->{
            DwdVehicleRepair info = new DwdVehicleRepair();
            info.setId(item.getId());
            info.setPlateNum(item.getPlate_num());
            info.setVehicleDept(item.getVehicle_dept());
            info.setWorkOrderNo(item.getWork_order_no());
            info.setDispatcher(item.getDispatcher());
            info.setReporter(item.getReporter());
            info.setRepairStatus(item.getRepair_status());
            info.setApplyDate(DateUtils.parseDate(item.getApply_date()));
            info.setCurrentHandler(item.getCurrent_handler());
            info.setConfirmFinishTime(DateUtils.parseDate(item.getConfirm_finish_time()));
            info.setRepairItemName(item.getRepair_item_name());
            info.setCarDeliverer(item.getCar_deliverer());
            info.setSettlementParty(item.getSettlement_party());
            info.setItemCode(item.getItem_code());
            info.setItemName(item.getItem_name());
            info.setItemType(item.getItem_type());
            info.setBrand(item.getBrand());
            info.setSpecInfo(item.getSpec_info());
            info.setModel(item.getModel());
            info.setUnit(item.getUnit());
            info.setApplicableVehicle(item.getApplicable_vehicle());
            info.setSource(item.getSource());
            info.setProduceArea(item.getProduce_area());
            info.setQuantity(NumberUtil.toDouble(item.getQuantity(), 0d));
            info.setPartMark(item.getPart_mark());
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }

}
