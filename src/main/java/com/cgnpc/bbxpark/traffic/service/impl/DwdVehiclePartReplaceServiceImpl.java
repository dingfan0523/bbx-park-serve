
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
import com.cgnpc.bbxpark.traffic.domain.DwdVehiclePartReplace;
import com.cgnpc.bbxpark.traffic.dto.DwdVehiclePartReplaceParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehiclePartReplaceTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehiclePartReplaceRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehiclePartReplaceService;
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
 * @Description 轮胎更换记录服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehiclePartReplaceServiceImpl extends ServiceImpl<DwdVehiclePartReplaceRepository, DwdVehiclePartReplace> implements IDwdVehiclePartReplaceService {

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
            DwdVehiclePartReplaceListener listener = new DwdVehiclePartReplaceListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehiclePartReplaceParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehiclePartReplaceParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehiclePartReplace> list = addList.stream().map(p -> {
                DwdVehiclePartReplace record = BeanUtil.toBean(p,DwdVehiclePartReplace.class);
                record.setFileId(fi.getId());
                record.setApplyDate(DateUtils.parseDate(p.getApplyDateStr()));
                record.setReplaceDate(DateUtils.parseDate(p.getReplaceDateStr()));
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
            returnModels.add("轮胎更换记录导入异常，原因是:"+e.getMessage());
            log.error("轮胎更换记录导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehiclePartReplace>lambdaQuery().eq(DwdVehiclePartReplace::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/3387856045378678";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehiclePartReplaceTemp> tempList = HttpClientUtil.postForList(url, jsonBody, DwdVehiclePartReplaceTemp.class);
        List<DwdVehiclePartReplace> infos = tempList.stream().map(item->{
            DwdVehiclePartReplace info = new DwdVehiclePartReplace();
            info.setId(item.getId());
            info.setPlateNum(item.getPlate_num());
            info.setVehicleDept(item.getVehicle_dept());
            info.setVehicleType(item.getVehicle_type());
            info.setMileage(NumberUtil.toDouble(item.getMileage(), 0d));
            info.setApplyDate(DateUtils.parseDate(item.getApply_date()));
//            info.setReplaceCount(NumberUtil.toInt(item.getReplace_count()));
            info.setReporter(item.getReporter());
            info.setReplaceDate(DateUtils.parseDate(item.getReplace_date()));
            info.setVehicleAdminSign(item.getVehicle_admin_sign());
            info.setReplaceReason(item.getReplace_reason());
            info.setCarDeliverer(item.getCar_deliverer());
            info.setProcessStatus(item.getProcess_status());
            info.setCurrentHandlerId(item.getCurrent_handler_id());
            info.setRemark(item.getRemark());
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }


}
