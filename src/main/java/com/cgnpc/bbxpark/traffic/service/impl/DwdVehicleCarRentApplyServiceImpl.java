
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
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleCarRentApply;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMaintain;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleCarRentApplyParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleCarRentApplyTemp;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleMaintainTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleCarRentApplyRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleCarRentApplyService;
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
 * @Description 租车记录服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleCarRentApplyServiceImpl extends ServiceImpl<DwdVehicleCarRentApplyRepository, DwdVehicleCarRentApply> implements IDwdVehicleCarRentApplyService {

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
            DwdVehicleCarRentApplyListener listener = new DwdVehicleCarRentApplyListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleCarRentApplyParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehicleCarRentApplyParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleCarRentApply> list = addList.stream().map(p -> {
                DwdVehicleCarRentApply record = BeanUtil.toBean(p,DwdVehicleCarRentApply.class);
                record.setFileId(fi.getId());
                record.setApplyDate(DateUtils.parseDate(p.getApplyDateStr()));
                record.setCreateTime(DateUtils.parseDate(p.getCreateTimeStr()));
                record.setUpdateTime(DateUtils.parseDate(p.getUpdateTimeStr()));
                if(ObjectUtil.isNotEmpty(p.getRentTime())){
                    String[] rentStr = p.getRentTime().split("_");
                    record.setRentStartTime(DateUtils.parseDate(rentStr[0]));
                    record.setRentEndTime(rentStr.length > 1 ? DateUtils.parseDate(rentStr[1]) : DateUtils.parseDate(rentStr[0]));
                }
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
            returnModels.add("租车记录导入异常，原因是:"+e.getMessage());
            log.error("租车记录导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleCarRentApply>lambdaQuery().eq(DwdVehicleCarRentApply::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/1476935763477766";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleCarRentApplyTemp> tempList = HttpClientUtil.postForList(url, jsonBody, DwdVehicleCarRentApplyTemp.class);
        List<DwdVehicleCarRentApply> infos = tempList.stream().map(item->{
            DwdVehicleCarRentApply info = new DwdVehicleCarRentApply();
            info.setId(item.getId());
            info.setInstanceTitle(item.getInstance_title());
            info.setApplicant(item.getApplicant());
            info.setUseDept(item.getUse_dept());
            info.setApplyDate(DateUtils.parseDate(item.getApply_date()));
            info.setRentType(item.getRent_type());
            info.setRentTime(item.getRent_time());
            if(ObjectUtil.isNotEmpty(info.getRentTime())){
                String[] rentStr = info.getRentTime().split("_");
                info.setRentStartTime(DateUtils.parseDate(rentStr[0]));
                info.setRentEndTime(rentStr.length > 1 ? DateUtils.parseDate(rentStr[1]) : DateUtils.parseDate(rentStr[0]));
            }
            info.setRentDays(NumberUtil.toDouble(item.getRent_days(), 0d));
            info.setDetailInfo(item.getDetail_info());
            info.setAttachment(item.getAttachment());
            info.setCarUser(item.getCar_user());
            info.setRentReason(item.getRent_reason());
            info.setSubmitter(item.getSubmitter());
            info.setSubmitterOrg(item.getSubmitter_org());
            info.setCreateTime(DateUtils.parseDate(item.getCreate_time()));
            info.setUpdateTime(DateUtils.parseDate(item.getUpdate_time()));
            info.setCurrentApproveNode(item.getCurrent_approve_node());
            info.setInstanceStatus(item.getInstance_status());
            info.setApproveResult(item.getApprove_result());
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }

}
