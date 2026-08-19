
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
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleInfo;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMaintain;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleInfoTemp;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleMaintainParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleMaintainTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleMaintainRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleMaintainService;
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
 * @Description 车辆保养信息服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleMaintainServiceImpl extends ServiceImpl<DwdVehicleMaintainRepository, DwdVehicleMaintain> implements IDwdVehicleMaintainService {

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
            DwdVehicleMaintainListener listener = new DwdVehicleMaintainListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleMaintainParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehicleMaintainParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleMaintain> list = addList.stream().map(p -> {
                DwdVehicleMaintain record = BeanUtil.toBean(p,DwdVehicleMaintain.class);
                record.setFileId(fi.getId());
                record.setLastMaintainDate(DateUtils.parseDate(p.getLastMaintainDateStr()));
                record.setMaintainDate(DateUtils.parseDate(p.getMaintainDateStr()));
                record.setApplyDate(DateUtils.parseDate(p.getApplyDateStr()));
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
            returnModels.add("车辆保养信息导入异常，原因是:"+e.getMessage());
            log.error("车辆保养信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleMaintain>lambdaQuery().eq(DwdVehicleMaintain::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/3283619158559751";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleMaintainTemp> tempList = HttpClientUtil.postForList(url, jsonBody,DwdVehicleMaintainTemp.class);
        List<DwdVehicleMaintain> infos = tempList.stream().map(item->{
            DwdVehicleMaintain info = new DwdVehicleMaintain();
            info.setId(item.getId());
            info.setPlateNum(item.getPlate_num());
            info.setVehicleDept(item.getVehicle_dept());
            info.setVehicleType(item.getVehicle_type());
            info.setLastMaintainDate(DateUtils.parseDate(item.getMaintain_date()));
            info.setMaintainDate(DateUtils.parseDate(item.getMaintain_date()));
            info.setInMileage(NumberUtil.toDouble(item.getIn_mileage(), 0d));
            info.setApplyDate(DateUtils.parseDate(item.getApply_date()));
            info.setApplyDept(item.getApply_dept());
            info.setMaintainLocation(item.getMaintain_location());
            info.setEstimateCost(NumberUtil.toDouble(item.getEstimate_cost(), 0d));
            info.setSponsor(item.getSponsor());
            info.setMaintainStatus(item.getMaintain_status());
            info.setCarDeliverer(item.getCar_deliverer());
            info.setMaintainItemName(item.getMaintain_item_name());
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
            info.setPrice(NumberUtil.toDouble(item.getPrice(), 0d));
            info.setItemAmount(NumberUtil.toDouble(item.getItem_amount(), 0d));
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }


}
