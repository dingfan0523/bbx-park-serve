
package com.cgnpc.bbxpark.traffic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.HttpClientUtil;
import com.cgnpc.bbxpark.common.utils.NumberUtil;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleRepair;
import com.cgnpc.bbxpark.traffic.domain.VehicleLineOrderInfo;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleRepairTemp;
import com.cgnpc.bbxpark.traffic.dto.VehicleApplyParam;
import com.cgnpc.bbxpark.traffic.dto.VehicleLineOrderInfoParam;
import com.cgnpc.bbxpark.traffic.dto.VehicleLineOrderInfoTemp;
import com.cgnpc.bbxpark.traffic.mapper.VehicleLineOrderInfoRepository;
import com.cgnpc.bbxpark.traffic.service.IVehicleLineOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


@Service
@Slf4j
public class VehicleLineOrderInfoServiceImpl extends ServiceImpl<VehicleLineOrderInfoRepository, VehicleLineOrderInfo> implements IVehicleLineOrderInfoService {
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
            VehicleLineOrderInfoListener listener = new VehicleLineOrderInfoListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), VehicleApplyParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<VehicleLineOrderInfoParam> addList = listener.getAddList();
            //校验通过数据
            List<VehicleLineOrderInfo> list = addList.stream().map(p -> {
                VehicleLineOrderInfo record = BeanUtil.toBean(p,VehicleLineOrderInfo.class);
                record.setFileId(fi.getId());
                if(StringUtils.isNotEmpty(p.getTaskDateStr())){
                    record.setTaskDate(DateUtils.parseDate(p.getTaskDateStr()));
                }
                if(StringUtils.isNotEmpty(p.getActualPayTimeStr())){
                    record.setActualPayTime(DateUtils.parseDate(p.getActualPayTimeStr()));
                }
                if(StringUtils.isNotEmpty(p.getOrderAmount())){
                    record.setOrderAmount(new BigDecimal(p.getOrderAmount()));
                }
                if(StringUtils.isNotEmpty(p.getTotalAmount())){
                    record.setTotalAmount(new BigDecimal(p.getTotalAmount()));
                }
                record.setImportTime(new Date());
                return record;
            }).collect(Collectors.toList());

            busExecutorService.execute(() -> this.saveBatch(list));
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            fileService.remove(fi.getId());
            returnModels.add("便民班车订单表导入异常，原因是:"+e.getMessage());
            log.error("便民班车订单表导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<VehicleLineOrderInfo>lambdaQuery().eq(VehicleLineOrderInfo::getFileId, fileId));
    }

    @Override
    public Boolean importData() {
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/3215662707355007";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<VehicleLineOrderInfoTemp> tempList = HttpClientUtil.postForList(url, jsonBody, VehicleLineOrderInfoTemp.class);
        List<VehicleLineOrderInfo> infos = tempList.stream().map(item->{
            VehicleLineOrderInfo info = new VehicleLineOrderInfo();
            info.setId(item.getId());
            info.setRunOrg(item.getRun_org());
            info.setLineGroup(item.getLine_group());
            info.setLineName(item.getLine_name());
            info.setLineDirection(item.getLine_direction());
            info.setTaskDate(DateUtils.parseDate(item.getTask_date()));
            info.setDepartTime(item.getDepart_time());
            info.setReserveNum(Integer.parseInt(item.getReserve_num()));
            info.setUnpaidNum(Integer.parseInt(item.getUnpaid_num()));
            info.setPaidNum(Integer.parseInt(item.getPaid_num()));
            info.setTotalAmount(new BigDecimal(item.getTotal_amount()));
            info.setOrderUser(item.getOrder_user());
            info.setOrderUserDept(item.getOrder_user_dept());
            info.setReservePhone(item.getReserve_phone());
            info.setStartStation(item.getStart_station());
            info.setEndStation(item.getEnd_station());
            info.setRideNum(Integer.parseInt(item.getRide_num()));
            info.setChildNum(Integer.parseInt(item.getChild_num()));
            info.setOrderAmount(new BigDecimal(item.getOrder_amount()));
            info.setOrderStatus(item.getOrder_status());
            info.setPayType(item.getPay_type());
            info.setActualPayTime(DateUtils.parseDate(item.getActual_pay_time()));
            info.setRemark(item.getRemark());
            info.setFlightTrainInfo(item.getFlight_train_info());
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }
}
