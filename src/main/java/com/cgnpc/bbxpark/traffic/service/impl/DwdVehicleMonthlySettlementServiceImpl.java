
package com.cgnpc.bbxpark.traffic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.service.IFileService;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMonthlySettlement;
import com.cgnpc.bbxpark.traffic.dto.DwdVehicleMonthlySettlementParam;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleMonthlySettlementRepository;
import com.cgnpc.bbxpark.traffic.service.IDwdVehicleMonthlySettlementService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/***
 * @Description 车辆费用结算信息服务实现
 * @author huangyongtao
 * @date 2026/5/27 16:36
 */
@Slf4j
@Service
public class DwdVehicleMonthlySettlementServiceImpl extends ServiceImpl<DwdVehicleMonthlySettlementRepository, DwdVehicleMonthlySettlement> implements IDwdVehicleMonthlySettlementService {

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
            //2025年10月苍南核电交通服务费用结算表
            Date settlementDate = getDate(file.getOriginalFilename());
            DwdVehicleMonthlySettlementListener listener = new DwdVehicleMonthlySettlementListener();
            ZipSecureFile.setMinInflateRatio(-1.0d);
            EasyExcel.read(file.getInputStream(), DwdVehicleMonthlySettlementParam.class, listener).autoTrim(true).headRowNumber(3).sheet(0).doRead();
            List<DwdVehicleMonthlySettlementParam> addList = listener.getAddList();
            //校验通过数据
            List<DwdVehicleMonthlySettlement> list = addList.stream().map(p -> {
                DwdVehicleMonthlySettlement record = BeanUtil.toBean(p,DwdVehicleMonthlySettlement.class);
                record.setFileId(fi.getId());
                record.setPurchaseDate(DateUtils.parseDate(p.getPurchaseDateStr()));
                record.setImportTime(new Date());
                record.setSettlementDate(settlementDate);
                return record;
            }).collect(Collectors.toList());

            busExecutorService.execute(() -> {
                this.saveBatch(list);
            });
            returnModels.add("导入成功"+list.size()+"条");
            success = true;
        } catch (Exception e) {
            fileService.remove(fi.getId());
            returnModels.add("车辆费用结算信息导入异常，原因是:"+e.getMessage());
            log.error("车辆费用结算信息导入异常，原因是{}", e.getMessage());
        }
        return ImportReturnModel.builder().success(success).messageList(returnModels).build();
    }

    @Override
    public Boolean remove(Long fileId) {
        if(ObjectUtil.isEmpty(fileId)){
            return true;
        }
        return this.remove(Wrappers.<DwdVehicleMonthlySettlement>lambdaQuery().eq(DwdVehicleMonthlySettlement::getFileId, fileId));
    }

    private Date getDate(String fileName){
        Pattern pattern = Pattern.compile("(\\d{4})年(\\d{1,2})月");
        Matcher matcher = null;
        if (fileName != null) {
            matcher = pattern.matcher(fileName);
        }
        Integer year = null;
        Integer month = null;
        if (matcher != null && matcher.find()) {
            year = Integer.parseInt(matcher.group(1));  // 2025
            month = Integer.parseInt(matcher.group(2)); // 10
        }
        if(year == null){
            throw new BaseException("文件名没有年月信息");
        }
        return DateUtil.parse(String.format("%04d-%02d-01", year, month));
    }

}
