
package com.cgnpc.bbxpark.energy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.ExcelExportUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecord;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecordCount;
import com.cgnpc.bbxpark.energy.dto.model.*;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordCountParam;
import com.cgnpc.bbxpark.energy.mapper.MeterPersonRecordCountRepository;
import com.cgnpc.bbxpark.energy.service.IBranchDeviceService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordCountService;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/***
 * @Description 抄表人工抄表记录统计服务实现
 * @author huangyongtao
 * @date 2025/4/21 9:32
 */
@Service("meterPersonRecordCountService")
public class MeterPersonRecordCountServiceImpl extends ServiceImpl<MeterPersonRecordCountRepository, MeterPersonRecordCount> implements IMeterPersonRecordCountService {

    @Autowired
    private IMeterPersonRecordService meterPersonRecordService;

    @Autowired
    private IBranchDeviceService branchDeviceService;

    @Autowired
    @Qualifier("kafkaMsgThreadPool")
    private Executor executorService;

    @Override
    public MeterRecordCountModel findNewTime(MeterRecordCountParam param) {
        AssertUtils.notNull(param.getReadingType(), "能源类型不能为空");
        AssertUtils.notNull(param.getMeterMethod(), "抄表方式不能为空");
        MeterRecordCountModel model = new MeterRecordCountModel();
        MeterPersonRecord record = meterPersonRecordService.getOne(Wrappers.<MeterPersonRecord>lambdaQuery().eq(MeterPersonRecord::getReadingType, param.getReadingType())
                .orderByDesc(MeterPersonRecord::getCreateTime)
                .last("limit 1"));
        if(ObjectUtil.isNotEmpty(record)){
            model.setNewTime(record.getCreateTime());
        }
        return model;
    }

    @Override
    public MeterRecordCountModel energyCount(MeterRecordCountParam param) {
        checkParam(param);
        int scale = getScale(param.getReadingType());
        Date currentDate = DateUtil.date();
        MeterRecordCountModel countModel = new MeterRecordCountModel();
        List<BranchDeviceModel> branchDevices = getBranchDevices(Arrays.asList(param.getBranchParam().getId()));
        if (CollectionUtil.isEmpty(branchDevices)){
            return countModel;
        }
        List<Long> deviceIds = branchDevices.stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
        int finalScale = scale;
        CompletableFuture<Void> handleWeekFuture = CompletableFuture.runAsync(() -> handleWeek(countModel, deviceIds, finalScale, currentDate), executorService);
        CompletableFuture<Void> handleMonthFuture = CompletableFuture.runAsync(() -> handleMonth(countModel, deviceIds, finalScale, currentDate), executorService);
        CompletableFuture<Void> handleYearFuture = CompletableFuture.runAsync(() -> handleYear(countModel, deviceIds, finalScale, currentDate), executorService);
        //同步
        CompletableFuture.allOf(handleWeekFuture, handleMonthFuture, handleYearFuture).join();
        return countModel;
    }


    @Override
    public List<MeterRecordBranchCountModel> energyBranchCount(MeterRecordCountParam param) {
        checkParam(param);
        return energyBranchCount(param,DateUtil.date(),Collections.singletonList(param.getBranchParam().getId()));
    }

    @Override
    public List<MeterRecordBranchCountModel> energyBranchCount(MeterRecordCountParam param, Date date,List<Long> branchIds) {
        AssertUtils.notNull(param.getTimeType(), "当前支路时间类型不能为空");
        int scale = getScale(param.getReadingType());
        List<MeterRecordBranchCountModel> branchCountModels = new ArrayList<>();
        List<BranchDeviceModel> branchDevices = getBranchDevices(branchIds);
        if (CollectionUtil.isEmpty(branchDevices)){
            return branchCountModels;
        }
        List<Long> deviceIds = branchDevices.stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
        switch (param.getTimeType()){
            case "month":
                handleMonthBranch(branchCountModels, date);
                break;
            case "year":
                handleYearBranch(branchCountModels, date, deviceIds, scale);
                break;
            default:
                throw GenericException.fail("timeType参数错误");
        }
        return branchCountModels;
    }

    @Override
    public Boolean energyBranchCountExport(HttpServletResponse response, MeterRecordCountParam param) {
        checkParam(param);
        //生成excel
        String type = "month".equals(param.getTimeType()) ? "当月逐日" : "当年逐月";
        int scale = getScale(param.getReadingType());
        String title = param.getBranchParam().getBranchName() + "支路用能趋势-" + type + "趋势（人工抄表）";
        List<MeterRecordBranchMonthCountExportModel> monthExportModels = new ArrayList<>();
        List<MeterRecordBranchYearCountExportModel> yearExportModels = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(param.getBranchCountParams())){
            param.getBranchCountParams().forEach(model->{
                if("month".equals(param.getTimeType())){
                    MeterRecordBranchMonthCountExportModel monthExportModel = new MeterRecordBranchMonthCountExportModel();
                    monthExportModel.setTime(model.getTime());
                    monthExportModel.setValue(new BigDecimal(model.getValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    monthExportModel.setLastValue(new BigDecimal(model.getLastValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    monthExportModel.setLastYearValue(new BigDecimal(model.getLastYearValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    monthExportModels.add(monthExportModel);
                }else{
                    MeterRecordBranchYearCountExportModel yearExportModel = new MeterRecordBranchYearCountExportModel();
                    yearExportModel.setTime(model.getTime());
                    yearExportModel.setValue(new BigDecimal(model.getValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    yearExportModel.setLastValue(new BigDecimal(model.getLastValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    yearExportModels.add(yearExportModel);
                }
            });
        }
        if("month".equals(param.getTimeType())){
            ExcelExportUtils.exportExcel(response, title, monthExportModels, MeterRecordBranchMonthCountExportModel.class, title);
        }else{
            ExcelExportUtils.exportExcel(response, title, yearExportModels, MeterRecordBranchYearCountExportModel.class, title);
        }
        return true;
    }

    @Override
    public List<MeterRecordSonBranchCountModel> energySonBranchCount(MeterRecordCountParam param) {
        checkParam(param);
        List<MeterRecordSonBranchCountModel> sonBranchCountModels = new ArrayList<>();
        if(CollectionUtil.isEmpty(param.getSonBranchParams())){
            return sonBranchCountModels;
        }
        AssertUtils.notNull(param.getSonTimeType(), "当前子支路时间类型不能为空");
        int scale = getScale(param.getReadingType());
        Date currentDate = DateUtil.date();
        List<Long> ids = param.getSonBranchParams().stream().map(EnergyBranchParam::getId).collect(Collectors.toList());
        List<BranchDeviceModel> branchDevices = getBranchDevices(ids);
        Map<Long, List<BranchDeviceModel>> branchDeviceMap = CollectionUtil.isEmpty(branchDevices) ? new HashMap<>() : branchDevices.stream().collect(Collectors.groupingBy(BranchDeviceModel::getBranchId));
        switch (param.getSonTimeType()){
            case "month":
                handleMonthSonBranch(sonBranchCountModels, param.getSonBranchParams(), branchDeviceMap, currentDate, scale);
                break;
            case "year":
                handleYearSonBranch(sonBranchCountModels, param.getSonBranchParams(), branchDeviceMap, currentDate, scale);
                break;
            default:
                throw GenericException.fail("timeType参数错误");
        }
        return sonBranchCountModels;
    }

    @Override
    public List<MeterRecordSonBranchCountModel> appEnergySonBranchCount(MeterRecordCountParam param) {
        checkParam(param);
        AssertUtils.notNull(param.getStartDate(), "开始时间不能为空");
        AssertUtils.notNull(param.getEndDate(), "结束时间不能为空");
        List<MeterRecordSonBranchCountModel> sonBranchCountModels = new ArrayList<>();
        if(CollectionUtil.isEmpty(param.getSonBranchParams())){
            return sonBranchCountModels;
        }
        AssertUtils.notNull(param.getSonTimeType(), "当前子支路时间类型不能为空");
        int scale = getScale(param.getReadingType());
        Date currentDate = param.getStartDate();
        List<Long> ids = param.getSonBranchParams().stream().map(EnergyBranchParam::getId).collect(Collectors.toList());
        List<BranchDeviceModel> branchDevices = getBranchDevices(ids);
        Map<Long, List<BranchDeviceModel>> branchDeviceMap = CollectionUtil.isEmpty(branchDevices) ? new HashMap<>() : branchDevices.stream().collect(Collectors.groupingBy(BranchDeviceModel::getBranchId));
        switch (param.getSonTimeType()){
            case "month":
                appHandleMonthSonBranch(sonBranchCountModels, param.getSonBranchParams(), branchDeviceMap, currentDate, scale);
                break;
            case "year":
                appHandleYearSonBranch(sonBranchCountModels, param.getSonBranchParams(), branchDeviceMap, currentDate, scale);
                break;
            default:
                throw GenericException.fail("timeType参数错误");
        }
        return sonBranchCountModels;
    }

    @Override
    public Boolean energySonBranchCountExport(HttpServletResponse response, MeterRecordCountParam param) {
        checkParam(param);
        //生成excel
        String type = "month".equals(param.getSonTimeType()) ? "当月" : "当年";
        int scale = getScale(param.getReadingType());
        String title = param.getBranchParam().getBranchName() + "支路子支路" + type + "用能统计（人工抄表）";
        List<MeterRecordSonBranchMonthCountExportModel> monthExportModels = new ArrayList<>();
        List<MeterRecordSonBranchYearCountExportModel> yearExportModels = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(param.getSonBranchCountParams())){
            param.getSonBranchCountParams().forEach(model->{
                if("month".equals(param.getSonTimeType())){
                    MeterRecordSonBranchMonthCountExportModel monthExportModel = new MeterRecordSonBranchMonthCountExportModel();
                    monthExportModel.setBranchName(model.getBranchName());
                    monthExportModel.setValue(new BigDecimal(model.getValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    monthExportModel.setLastValue(new BigDecimal(model.getLastValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    monthExportModel.setLastYearValue(new BigDecimal(model.getLastYearValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    monthExportModels.add(monthExportModel);
                }else{
                    MeterRecordSonBranchYearCountExportModel yearExportModel = new MeterRecordSonBranchYearCountExportModel();
                    yearExportModel.setBranchName(model.getBranchName());
                    yearExportModel.setValue(new BigDecimal(model.getValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    yearExportModel.setLastValue(new BigDecimal(model.getLastValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue());
                    yearExportModels.add(yearExportModel);
                }
            });
        }
        if("month".equals(param.getSonTimeType())){
            ExcelExportUtils.exportExcel(response, title, monthExportModels, MeterRecordSonBranchMonthCountExportModel.class, title);
        }else{
            ExcelExportUtils.exportExcel(response, title, yearExportModels, MeterRecordSonBranchYearCountExportModel.class, title);
        }
        return true;
    }

    private void handleMonthSonBranch(List<MeterRecordSonBranchCountModel> sonBranchCountModels, List<EnergyBranchParam> sonBranchParams, Map<Long, List<BranchDeviceModel>> branchDeviceMap, Date currentDate, int scale){
        for (EnergyBranchParam param : sonBranchParams) {
            MeterRecordSonBranchCountModel countModel = new MeterRecordSonBranchCountModel();
            countModel.setBranchName(param.getBranchName());
            if(branchDeviceMap.containsKey(param.getId())){
                List<Long> deviceIds = branchDeviceMap.get(param.getId()).stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
                MeterRecordCountModel recordCountModel = new MeterRecordCountModel();
                handleMonth(recordCountModel, deviceIds,  scale, currentDate);

                countModel.setValue(recordCountModel.getMonthValue());
                countModel.setLastValue(recordCountModel.getLastMonthValue());
                countModel.setLastYearValue(recordCountModel.getLastYearMonthValue());
            }
            sonBranchCountModels.add(countModel);
        }
    }

    private void appHandleMonthSonBranch(List<MeterRecordSonBranchCountModel> sonBranchCountModels, List<EnergyBranchParam> sonBranchParams, Map<Long, List<BranchDeviceModel>> branchDeviceMap, Date currentDate, int scale){
        for (EnergyBranchParam param : sonBranchParams) {
            MeterRecordSonBranchCountModel countModel = new MeterRecordSonBranchCountModel();
            countModel.setBranchName(param.getBranchName());
            if(branchDeviceMap.containsKey(param.getId())){
                List<Long> deviceIds = branchDeviceMap.get(param.getId()).stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
                MeterRecordCountModel recordCountModel = new MeterRecordCountModel();
                appHandleMonth(recordCountModel, deviceIds,  scale, currentDate);
                countModel.setValue(recordCountModel.getMonthValue());
            }
            sonBranchCountModels.add(countModel);
        }
    }
    private void handleYearSonBranch(List<MeterRecordSonBranchCountModel> sonBranchCountModels, List<EnergyBranchParam> sonBranchParams, Map<Long, List<BranchDeviceModel>> branchDeviceMap, Date currentDate, int scale){
        for (EnergyBranchParam param : sonBranchParams) {
            MeterRecordSonBranchCountModel countModel = new MeterRecordSonBranchCountModel();
            countModel.setBranchName(param.getBranchName());
            if(branchDeviceMap.containsKey(param.getId())){
                List<Long> deviceIds = branchDeviceMap.get(param.getId()).stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
                MeterRecordCountModel recordCountModel = new MeterRecordCountModel();
                handleYear(recordCountModel, deviceIds,  scale, currentDate);
                countModel.setValue(recordCountModel.getYearValue());
                countModel.setLastValue(recordCountModel.getLastYearValue());
            }
            sonBranchCountModels.add(countModel);
        }
    }

    private void appHandleYearSonBranch(List<MeterRecordSonBranchCountModel> sonBranchCountModels, List<EnergyBranchParam> sonBranchParams, Map<Long, List<BranchDeviceModel>> branchDeviceMap, Date currentDate, int scale){
        for (EnergyBranchParam param : sonBranchParams) {
            MeterRecordSonBranchCountModel countModel = new MeterRecordSonBranchCountModel();
            countModel.setBranchName(param.getBranchName());
            if(branchDeviceMap.containsKey(param.getId())){
                List<Long> deviceIds = branchDeviceMap.get(param.getId()).stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
                MeterRecordCountModel recordCountModel = new MeterRecordCountModel();
                appHandleYear(recordCountModel, deviceIds,  scale, currentDate);
                countModel.setValue(recordCountModel.getYearValue());
            }
            sonBranchCountModels.add(countModel);
        }
    }

    private void handleMonthBranch(List<MeterRecordBranchCountModel> branchCountModels, Date currentDate){
        for (DateTime monthDate : DateUtil.rangeToList(DateUtil.beginOfMonth(currentDate), DateUtil.endOfMonth(currentDate), DateField.DAY_OF_MONTH)) {
            MeterRecordBranchCountModel countModel = new MeterRecordBranchCountModel();
            countModel.setTime(DateUtil.format(monthDate, "MM-dd"));
            branchCountModels.add(countModel);
        }
    }

    private void handleYearBranch(List<MeterRecordBranchCountModel> branchCountModels, Date currentDate, List<Long> deviceIds, int scale){
        //今年数据
        List<MeterPersonRecordCount> countList = findYearCountList(deviceIds, currentDate);
        Map<Long, List<MeterPersonRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //去年数据
        List<MeterPersonRecordCount> lastCountList = findYearCountList(deviceIds, DateUtil.offset(currentDate, DateField.YEAR, -1));
        Map<Long, List<MeterPersonRecordCount>> lastYearCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //前年最后一个月的数据
        Date lasLastYear = DateUtil.offset(currentDate, DateField.YEAR, -2);
        Date lastLastYearEnd = DateUtil.endOfYear(lasLastYear);
        List<MeterPersonRecordCount> lastLastCountList = findMonthCountList(deviceIds, DateUtil.endOfYear(lastLastYearEnd));
        Map<Long, List<MeterPersonRecordCount>> lastLastYearCountMap = CollectionUtil.isNotEmpty(lastLastCountList) ? lastLastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();

        for (DateTime monthDate : DateUtil.rangeToList(DateUtil.beginOfYear(DateUtil.date()), DateUtil.endOfYear(DateUtil.date()), DateField.MONTH)) {
            int month = DateUtil.month(monthDate) + 1;
            MeterRecordBranchCountModel countModel = new MeterRecordBranchCountModel();
            countModel.setTime(DateUtil.format(monthDate, "M月"));
            BigDecimal monthValue = BigDecimal.ZERO;
            BigDecimal lastMonthValue = BigDecimal.ZERO;
            for(Long deviceId : deviceIds){
                Map<Integer, List<MeterPersonRecordCount> > countGroupMap = countMap.containsKey(deviceId) ? countMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.month(p.getCountTime()) + 1)) : new HashMap<>();
                Map<Integer, List<MeterPersonRecordCount> > lastYearCountGroupMap = lastYearCountMap.containsKey(deviceId) ? lastYearCountMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.month(p.getCountTime()) + 1)) : new HashMap<>();
                //前年最后一个月的抄表值
                BigDecimal lastLastYearMonthValue = lastLastYearCountMap.containsKey(deviceId) ? lastLastYearCountMap.get(deviceId).get(0).getReadingValue() : null;
                //去年最后一个月的抄表值
                BigDecimal lastYearMonthValue = lastYearCountGroupMap.containsKey(12) ? lastYearCountGroupMap.get(12).get(0).getReadingValue() : null;
                //今年的抄表值
                BigDecimal value = countGroupMap.containsKey(month) ? countGroupMap.get(month).get(0).getReadingValue() : null;
                BigDecimal lastValue = countGroupMap.containsKey(month-1) ? countGroupMap.get(month-1).get(0).getReadingValue() : null;

                //去年的抄表值
                BigDecimal yearValue = lastYearCountGroupMap.containsKey(month) ? lastYearCountGroupMap.get(month).get(0).getReadingValue() : null;
                BigDecimal lastYearValue = lastYearCountGroupMap.containsKey(month-1) ? lastYearCountGroupMap.get(month-1).get(0).getReadingValue() : null;
                if(month == 1){
                    lastValue = lastYearMonthValue;
                    lastYearValue = lastLastYearMonthValue;
                }
                if(value != null && lastValue != null){
                    monthValue = monthValue.add(value.subtract(lastValue));
                }
                if(yearValue != null && lastYearValue != null){
                    lastMonthValue = lastMonthValue.add(yearValue.subtract(lastYearValue));
                }
            }
            if(monthValue.compareTo(BigDecimal.ZERO) != 0){
                countModel.setValue(monthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            if(lastMonthValue.compareTo(BigDecimal.ZERO) != 0){
                countModel.setLastValue(lastMonthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            branchCountModels.add(countModel);
        }
    }

    @Override
    public Boolean executePersonReadingCount() {
        Date date = DateUtil.date();
        Date yesterday = DateUtil.offsetDay(date, -1);
        //因为endTime带毫秒，这个时间存入mysql数据库变成了今天的00:00:00
        Date endTime = DateUtil.endOfDay(yesterday);
        String endTimeStr = DateUtil.formatDateTime(endTime);
        //去掉endTime的毫秒时间
        Date endNewTime = DateUtil.parse(endTimeStr);
        List<MeterPersonRecord> yesMeterPersonRecordList = findMeterPersonRecordList(yesterday);
        if(CollectionUtil.isEmpty(yesMeterPersonRecordList)){
            return true;
        }
        Map<Long, List<MeterPersonRecord>> yesMap = yesMeterPersonRecordList.stream().collect(Collectors.groupingBy(MeterPersonRecord::getTenantId));
        List<MeterPersonRecordCount> counts = new ArrayList<>();
        yesMap.forEach((tenantId, meterPersonRecords) -> {
            Map<Long, List<MeterPersonRecord>> deviceMap = meterPersonRecords.stream().collect(Collectors.groupingBy(MeterPersonRecord::getDeviceId));
            deviceMap.forEach((deviceId,  records) -> {
                MeterPersonRecordCount count = new MeterPersonRecordCount();
                count.setTenantId(tenantId);
                count.setCreateTime(date);
                count.setUpdateTime(date);
                count.setUpdatorId("0");
                count.setCreatorId("0");
                count.setDeviceId(deviceId);
                count.setCountTime(endNewTime);
                count.setReadingValue(records.get(0).getReadingValue());
                counts.add(count);
            });
        });
        if(CollectionUtil.isEmpty(counts)){
            return true;
        }
        this.remove(Wrappers.<MeterPersonRecordCount>lambdaQuery().eq(MeterPersonRecordCount::getCountTime, endNewTime));
        return this.saveBatch(counts);
    }

    private void checkParam(MeterRecordCountParam param) {
        AssertUtils.notNull(param.getBranchParam(), "当前支路信息不能为空");
        AssertUtils.notNull(param.getReadingType(), "能源类型不能为空");
        AssertUtils.notNull(param.getMeterMethod(), "抄表方式不能为空");
    }

    private List<MeterPersonRecord> findMeterPersonRecordList(Date time){
        Date startTime = DateUtil.beginOfDay(time);
        Date endTime = DateUtil.endOfDay(time);
        return meterPersonRecordService.list(Wrappers.<MeterPersonRecord>lambdaQuery()
                .between(MeterPersonRecord::getCreateTime, startTime, endTime).orderByDesc(MeterPersonRecord::getCreateTime));
    }

    private void handleYear(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本年数据
        List<MeterPersonRecordCount> countList = findYearCountList(deviceIds, currentDate);
        Map<Long, List<MeterPersonRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上年数据
        List<MeterPersonRecordCount> lastCountList = findYearCountList(deviceIds, DateUtil.offset(currentDate, DateField.YEAR, -1));
        Map<Long, List<MeterPersonRecordCount>> lastCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上上年数据
        List<MeterPersonRecordCount> lastLastCountList = findYearCountList(deviceIds,  DateUtil.offset(currentDate, DateField.YEAR, -2));
        Map<Long, List<MeterPersonRecordCount>> lastLastCountMap = CollectionUtil.isNotEmpty(lastLastCountList) ? lastLastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();

        BigDecimal yearValue = BigDecimal.ZERO;
        BigDecimal lastYearValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal value = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValue = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastLastValue = lastLastCountMap.containsKey(deviceId) ? lastLastCountMap.get(deviceId).get(0).getReadingValue() : null;
            if(value != null && lastValue != null){
                yearValue = yearValue.add(value.subtract(lastValue));
            }
            if(lastLastValue != null && lastValue != null){
                lastYearValue = lastYearValue.add(lastValue.subtract(lastLastValue));
            }
        }

        if(yearValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setYearValue(yearValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(lastYearValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastYearValue(lastYearValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(yearValue.compareTo(BigDecimal.ZERO) != 0 && lastYearValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastYearDifferValue(yearValue.subtract(lastYearValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    private void appHandleYear(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本年数据
        List<MeterPersonRecordCount> countList = findYearCountList(deviceIds, currentDate);
        Map<Long, List<MeterPersonRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上年数据
        List<MeterPersonRecordCount> lastCountList = findYearCountList(deviceIds, DateUtil.offset(currentDate, DateField.YEAR, -1));
        Map<Long, List<MeterPersonRecordCount>> lastCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        BigDecimal yearValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal value = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValue = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            if(value != null && lastValue != null){
                yearValue = yearValue.add(value.subtract(lastValue));
            }
        }

        if(yearValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setYearValue(yearValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    private List<MeterPersonRecordCount> findYearCountList(List<Long> deviceIds, Date time){
        return findMeterPersonRecordCountList(deviceIds, DateUtil.beginOfYear(time), DateUtil.endOfYear(time));
    }

    private void handleMonth(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本月数据
        List<MeterPersonRecordCount> countList = findMonthCountList(deviceIds, currentDate);
        Map<Long, List<MeterPersonRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上月数据
        List<MeterPersonRecordCount> lastCountList = findMonthCountList(deviceIds, DateUtil.offsetMonth(currentDate, -1));
        Map<Long, List<MeterPersonRecordCount>> lastCountMap =  CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上上月数据
        List<MeterPersonRecordCount> lastLastCountList = findMonthCountList(deviceIds, DateUtil.offsetMonth(currentDate, -2));
        Map<Long, List<MeterPersonRecordCount>> lastLastCountMap = CollectionUtil.isNotEmpty(lastLastCountList) ? lastLastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //去年本月数据
        Date lastYearTime = DateUtil.offset(currentDate, DateField.YEAR, -1);
        List<MeterPersonRecordCount> lastYearCountList = findMonthCountList(deviceIds, lastYearTime);
        Map<Long, List<MeterPersonRecordCount>> lastYearCountMap = CollectionUtil.isNotEmpty(lastYearCountList) ? lastYearCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //去年上月数据
        List<MeterPersonRecordCount> lastLastYearCountList = findMonthCountList(deviceIds, DateUtil.offsetMonth(lastYearTime, -1));
        Map<Long, List<MeterPersonRecordCount>> lastLastYearCountMap = CollectionUtil.isNotEmpty(lastLastYearCountList) ? lastLastYearCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();

        BigDecimal monthValue = BigDecimal.ZERO;
        BigDecimal lastMonthValue = BigDecimal.ZERO;
        BigDecimal lastYearMonthValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal value = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValue = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastLastValue = lastLastCountMap.containsKey(deviceId) ? lastLastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastYearValue = lastYearCountMap.containsKey(deviceId) ? lastYearCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastLastYearValue = lastLastYearCountMap.containsKey(deviceId) ? lastLastYearCountMap.get(deviceId).get(0).getReadingValue() : null;
            if(value != null && lastValue != null){
                monthValue = monthValue.add(value.subtract(lastValue));
            }
            if(lastLastValue != null && lastValue != null){
                lastMonthValue = lastMonthValue.add(lastValue.subtract(lastLastValue));
            }
            if(lastYearValue != null && lastLastYearValue != null){
                lastYearMonthValue = lastYearMonthValue.add(lastYearValue.subtract(lastLastYearValue));
            }
        }

        if(monthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setMonthValue(monthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(lastMonthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastMonthValue(lastMonthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(monthValue.compareTo(BigDecimal.ZERO) != 0 && lastMonthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastMonthDifferValue(monthValue.subtract(lastMonthValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(lastYearMonthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastYearMonthValue(lastYearMonthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(monthValue.compareTo(BigDecimal.ZERO) != 0 && lastYearMonthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastYearMonthDifferValue(monthValue.subtract(lastYearMonthValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    private void appHandleMonth(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本月数据
        List<MeterPersonRecordCount> countList = findMonthCountList(deviceIds, currentDate);
        Map<Long, List<MeterPersonRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上月数据
        List<MeterPersonRecordCount> lastCountList = findMonthCountList(deviceIds, DateUtil.offsetMonth(currentDate, -1));
        Map<Long, List<MeterPersonRecordCount>> lastCountMap =  CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();

        BigDecimal monthValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal value = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValue = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            if(value != null && lastValue != null){
                monthValue = monthValue.add(value.subtract(lastValue));
            }
        }
        if(monthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setMonthValue(monthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    private List<MeterPersonRecordCount> findMonthCountList(List<Long> deviceIds, Date time){
        return findMeterPersonRecordCountList(deviceIds, DateUtil.beginOfMonth(time), DateUtil.endOfMonth(time));
    }

    private void handleWeek(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本周数据
        List<MeterPersonRecordCount> countList = findWeekCountList(deviceIds, currentDate);
        Map<Long, List<MeterPersonRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上周数据
        List<MeterPersonRecordCount> lastCountList = findWeekCountList(deviceIds, DateUtil.offsetWeek(currentDate, -1));
        Map<Long, List<MeterPersonRecordCount>> lastCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        //上上周数据
        List<MeterPersonRecordCount> lastLastCountList = findWeekCountList(deviceIds, DateUtil.offsetWeek(currentDate, -2));
        Map<Long, List<MeterPersonRecordCount>> lastLastCountMap = CollectionUtil.isNotEmpty(lastLastCountList) ? lastLastCountList.stream().collect(Collectors.groupingBy(MeterPersonRecordCount::getDeviceId)) : new HashMap<>();
        BigDecimal weekValue = BigDecimal.ZERO;
        BigDecimal lastWeekValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal value = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValue = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastLastValue = lastLastCountMap.containsKey(deviceId) ? lastLastCountMap.get(deviceId).get(0).getReadingValue() : null;
            if(value != null && lastValue != null){
                weekValue = weekValue.add(value.subtract(lastValue));
            }
            if(lastLastValue != null && lastValue != null){
                lastWeekValue = lastWeekValue.add(lastValue.subtract(lastLastValue));
            }
        }

        if(weekValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setWeekValue(weekValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(lastWeekValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastWeekValue(lastWeekValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        if(weekValue.compareTo(BigDecimal.ZERO) != 0 && lastWeekValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setLastWeekDifferValue(weekValue.subtract(lastWeekValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }
    private List<MeterPersonRecordCount> findWeekCountList(List<Long> deviceIds, Date time){
        return findMeterPersonRecordCountList(deviceIds, DateUtil.beginOfWeek(time), DateUtil.endOfWeek(time));
    }

    private List<MeterPersonRecordCount> findMeterPersonRecordCountList(List<Long> deviceIds, Date startTime, Date endTime){
        return this.list(Wrappers.<MeterPersonRecordCount>lambdaQuery()
                .in(MeterPersonRecordCount::getDeviceId, deviceIds)
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeterPersonRecordCount::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .between(MeterPersonRecordCount::getCountTime, startTime, endTime)
                .orderByDesc(MeterPersonRecordCount::getCountTime));
    }

    private int getScale(String readingType){
        int scale = 3;
        if(DeviceReadingTypeEnum.ELECTRICITY.getCode().equals(readingType)){
            scale  = 2;
        }
        return scale;
    }

    private List<BranchDeviceModel> getBranchDevices(List<Long> ids){
        List<BranchDeviceModel> branchDevices = branchDeviceService.getBranchDevice(ids);
        return branchDevices;
    }

    public static void main(String[] args) {
        Double a = 0.00;
        Double b = 0.00;
        System.out.println(a < 0.01);
        List<String> list = new ArrayList<>();
        List<Date> listDate = new ArrayList<>();
        for (DateTime monthDate : DateUtil.rangeToList(DateUtil.beginOfMonth(DateUtil.date()), DateUtil.endOfMonth(DateUtil.date()), DateField.DAY_OF_MONTH)) {
            list.add(DateUtil.format(monthDate, "MM-dd"));
            listDate.add(monthDate);
        }
        System.out.println(list);
        System.out.println(listDate);

        List<String> list1 = new ArrayList<>();
        for (DateTime monthDate : DateUtil.rangeToList(DateUtil.beginOfYear(DateUtil.date()), DateUtil.endOfYear(DateUtil.date()), DateField.MONTH)) {
            list1.add(DateUtil.format(monthDate, "M月"));
        }
        System.out.println(list1);
        System.out.println(DateUtil.month(DateUtil.parse("2024-01-30 11:00:00", "yyyy-MM-dd HH:mm:ss")));

        BigDecimal random = new BigDecimal(100.0000);
        BigDecimal randomAdd = new BigDecimal(RandomUtil.randomDouble(20))
                .setScale(2, RoundingMode.HALF_UP); // 保留两位小数
        System.out.println(random.add(randomAdd).doubleValue());

        Date date = DateUtil.date();
        Date yesterday = DateUtil.offsetDay(date, -1);
        Date endTime = DateUtil.endOfDay(yesterday);
        System.out.println(DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));

        BigDecimal weekValue = BigDecimal.ZERO;
        BigDecimal bigDecimal1 = new BigDecimal("999999999999.00");
        BigDecimal bigDecimal2 = new BigDecimal("-25.00");

        System.out.println(weekValue.compareTo(weekValue) + "大小");
        System.out.println(weekValue.compareTo(bigDecimal1) + "大小");
        System.out.println(weekValue.compareTo(bigDecimal2) + "大小");

        System.out.println(DateUtil.dayOfWeek(DateUtil.parse("2025-09-28 11:00:00", "yyyy-MM-dd HH:mm:ss")));
        System.out.println(DateUtil.dayOfMonth(DateUtil.parse("2025-09-02 11:00:00", "yyyy-MM-dd HH:mm:ss")));
        System.out.println(Integer.valueOf("0000011"));
    }
}
