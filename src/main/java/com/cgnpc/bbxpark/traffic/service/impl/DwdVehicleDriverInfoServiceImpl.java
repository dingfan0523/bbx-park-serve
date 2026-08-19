
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
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleDriverInfo;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleDriverInfoParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleDriverInfoTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleDriverInfoRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleDriverInfoService;
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
 * @Description 司机服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleDriverInfoServiceImpl extends ServiceImpl<DwdVehicleDriverInfoRepository, DwdVehicleDriverInfo> implements IDwdVehicleDriverInfoService {

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
            DwdVehicleDriverInfoListener listener = new DwdVehicleDriverInfoListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleDriverInfoParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehicleDriverInfoParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleDriverInfo> list = addList.stream().map(p -> {
                DwdVehicleDriverInfo record = BeanUtil.toBean(p,DwdVehicleDriverInfo.class);
                record.setFileId(fi.getId());
                record.setWorkHours(DateUtils.parseDate(p.getWorkHoursStr()));
                record.setEntryTime(DateUtils.parseDate(p.getEntryTimeStr()));
                record.setLicenseExpireTime(DateUtils.parseDate(p.getLicenseExpireTimeStr()));
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
            returnModels.add("司机表导入异常，原因是:"+e.getMessage());
            log.error("司机表导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleDriverInfo>lambdaQuery().eq(DwdVehicleDriverInfo::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/2174452675350471";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleDriverInfoTemp> tempList = HttpClientUtil.postForList(url, jsonBody, DwdVehicleDriverInfoTemp.class);
        List<DwdVehicleDriverInfo> infos = tempList.stream().map(item->{
            DwdVehicleDriverInfo info = new DwdVehicleDriverInfo();
            info.setId(item.getId());
            info.setDriverName(item.getDriver_name());
            info.setDriverTeam(item.getDriver_team());
            info.setPhone(item.getPhone());
            info.setGender(item.getGender());
            info.setAge(NumberUtil.toInt(item.getAge()));
            info.setWorkHours(DateUtils.parseDate(item.getWork_hours()));
            info.setEntryTime(DateUtils.parseDate(item.getEntry_time()));
            info.setGnhWorkYears(NumberUtil.toDouble(item.getGnh_work_years(), 0d));
            info.setNationWorkYears(NumberUtil.toDouble(item.getNation_work_years(), 0d));
            info.setWorkStatus(item.getWork_status());
            info.setDriverLicenseNo(item.getDriver_license_no());
            info.setLicenseExpireTime(DateUtils.parseDate(item.getLicense_expire_time()));
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }

}
