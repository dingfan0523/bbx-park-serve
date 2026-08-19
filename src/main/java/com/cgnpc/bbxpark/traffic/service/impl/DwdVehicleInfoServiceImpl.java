
package com.cgnpc.bbxpark.traffic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
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
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleInfo;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleInfoParam;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleInfoTemp;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleInfoRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleInfoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description 车辆信息服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleInfoServiceImpl extends ServiceImpl<DwdVehicleInfoRepository, DwdVehicleInfo> implements IDwdVehicleInfoService {

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
            DwdVehicleInfoListener listener = new DwdVehicleInfoListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleInfoParam.class, listener).autoTrim(true).headRowNumber(0).sheet(0).doRead();
            List<DwdVehicleInfoParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleInfo> list = addList.stream().map(p -> {
                DwdVehicleInfo record = BeanUtil.toBean(p,DwdVehicleInfo.class);
                record.setFileId(fi.getId());
                record.setNextInspectDate(DateUtils.parseDate(p.getNextInspectDateStr()));
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
            returnModels.add("车辆信息导入异常，原因是:"+e.getMessage());
            log.error("车辆信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleInfo>lambdaQuery().eq(DwdVehicleInfo::getFileId, fileId));
    }

    @Override
    public Boolean importData(){
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/6433832857565504";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleInfoTemp> tempList = HttpClientUtil.postForList(url, jsonBody,DwdVehicleInfoTemp.class);
        List<DwdVehicleInfo> infos = tempList.stream().map(item->{
            DwdVehicleInfo info = new DwdVehicleInfo();
            info.setId(item.getId());
            info.setLicensePlate(item.getLicense_plate());
            info.setVehicleType(item.getVehicle_type());
            info.setVin(item.getVin());
            info.setVehicleModel(item.getVehicle_model());
            info.setDepartment(item.getDepartment());
            info.setUseStatus(item.getUse_status());
            info.setRunStatus(item.getRun_status());
            info.setMileage(NumberUtil.toDouble(item.getMileage(), 0d));
            info.setNextInspectDate(DateUtils.parseDate(item.getNext_inspect_date()));
            info.setImportTime(DateUtils.parseDate(item.getImport_time()));
            return info;
        }).collect(Collectors.toList());
        return this.saveBatch(infos);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // 接口地址
        String url = "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/6433832857565504";
        // 构造 JSON 请求体 (可根据需要修改 start_time 和 end_time)
        String jsonBody = "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }";
        List<DwdVehicleInfoTemp> tempList = HttpClientUtil.postForList(url, jsonBody,DwdVehicleInfoTemp.class);
        tempList.forEach(item->{
            System.out.println(JSONUtil.parse(item));
        });
    }

}
