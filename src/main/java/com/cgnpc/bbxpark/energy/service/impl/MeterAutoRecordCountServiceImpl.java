
package com.cgnpc.bbxpark.energy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.ExcelExportUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecord;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecordCount;
import com.cgnpc.bbxpark.energy.dto.model.*;
import com.cgnpc.bbxpark.energy.dto.param.EnergyBranchParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordCountParam;
import com.cgnpc.bbxpark.energy.mapper.MeterAutoRecordCountRepository;
import com.cgnpc.bbxpark.energy.service.IBranchDeviceService;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordCountService;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordService;
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
 * @Description 抄表自动上报记录统计服务实现
 * @author huangyongtao
 * @date 2025/4/21 9:30
 */
@Service("meterAutoRecordCountService")
public class MeterAutoRecordCountServiceImpl extends ServiceImpl<MeterAutoRecordCountRepository, MeterAutoRecordCount> implements IMeterAutoRecordCountService {

    @Autowired
    private IMeterAutoRecordService meterAutoRecordService;

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
        MeterAutoRecord record = meterAutoRecordService.getOne(Wrappers.<MeterAutoRecord>lambdaQuery().eq(MeterAutoRecord::getReadingType, param.getReadingType())
                                .orderByDesc(MeterAutoRecord::getCreateTime)
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
        AssertUtils.notNull(param.getTimeType(), "当前支路时间类型不能为空");
        int scale = getScale(param.getReadingType());
        Date currentDate = DateUtil.date();
        List<MeterRecordBranchCountModel> branchCountModels = new ArrayList<>();
        List<BranchDeviceModel> branchDevices = getBranchDevices(Arrays.asList(param.getBranchParam().getId()));
        if (CollectionUtil.isEmpty(branchDevices)){
            return branchCountModels;
        }
        List<Long> deviceIds = branchDevices.stream().map(BranchDeviceModel::getDeviceId).collect(Collectors.toList());
        switch (param.getTimeType()){
            case "month":
                handleMonthBranch(branchCountModels, currentDate, deviceIds, scale);
                break;
            case "year":
                handleYearBranch(branchCountModels, currentDate, deviceIds, scale);
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
        String title = param.getBranchParam().getBranchName() + "支路用能趋势-" + type + "趋势（自动上报）";
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
                handleAppMonthSonBranch(sonBranchCountModels, param.getSonBranchParams(), branchDeviceMap, currentDate, scale);
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
        String title = param.getBranchParam().getBranchName() + "支路子支路" + type + "用能统计（自动上报）";
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

    @Override
    public Boolean executeAutoReadingCount() {
        Date date = DateUtil.date();
        Date yesterday = DateUtil.offsetDay(date, -1);
        //这个时间存入数据库变成了今天的00:00:00
        Date endTime = DateUtil.endOfDay(yesterday);
        String endTimeStr = DateUtil.formatDateTime(endTime);
        Date endNewTime = DateUtil.parse(endTimeStr);
        List<MeterAutoRecord> yesMeterAutoRecordList = findMeterAutoRecordList(yesterday);
        List<MeterAutoRecord> oldYesMeterAutoRecordList = findMeterAutoRecordList(DateUtil.offsetDay(yesterday, -1));
        if(CollectionUtil.isEmpty(yesMeterAutoRecordList)){
            return true;
        }
        Map<Long, List<MeterAutoRecord>> oldYesMap = CollectionUtil.isEmpty(oldYesMeterAutoRecordList) ? new HashMap<>() : oldYesMeterAutoRecordList.stream().collect(Collectors.groupingBy(MeterAutoRecord::getTenantId));
        Map<Long, List<MeterAutoRecord>> yesMap = yesMeterAutoRecordList.stream().collect(Collectors.groupingBy(MeterAutoRecord::getTenantId));
        List<MeterAutoRecordCount> counts = new ArrayList<>();
        yesMap.forEach((tenantId, meterAutoRecords) -> {
            Map<Long, List<MeterAutoRecord>> deviceMap = meterAutoRecords.stream().collect(Collectors.groupingBy(MeterAutoRecord::getDeviceId));
            deviceMap.forEach((deviceId,  records) -> {
                MeterAutoRecordCount count = new MeterAutoRecordCount();
                count.setTenantId(tenantId);
                count.setCountTime(endNewTime);
                count.setCreateTime(date);
                count.setUpdateTime(date);
                count.setUpdatorId("0");
                count.setCreatorId("0");
                count.setDeviceId(deviceId);
                count.setReadingValue(records.get(0).getReadingValue());
                count.setEnergyConsumption(new BigDecimal(0));
                if(CollectionUtil.isNotEmpty(oldYesMap.get(tenantId))){
                    Map<Long, List<MeterAutoRecord>> oldDeviceMap = oldYesMap.get(tenantId).stream().collect(Collectors.groupingBy(MeterAutoRecord::getDeviceId));
                    if(oldDeviceMap.containsKey(deviceId)){
                        count.setEnergyConsumption(count.getReadingValue().subtract(oldDeviceMap.get(deviceId).get(0).getReadingValue()));
                    }
                }
                counts.add(count);
            });
        });
        if(CollectionUtil.isEmpty(counts)){
            return true;
        }
        this.remove(Wrappers.<MeterAutoRecordCount>lambdaQuery().eq(MeterAutoRecordCount::getCountTime, endNewTime));
        return this.saveBatch(counts);
    }

    private List<MeterAutoRecord> findMeterAutoRecordList(Date time){
        Date startTime = DateUtil.beginOfDay(time);
        Date endTime = DateUtil.endOfDay(time);
        return meterAutoRecordService.list(Wrappers.<MeterAutoRecord>lambdaQuery()
                .between(MeterAutoRecord::getCreateTime, startTime, endTime).orderByDesc(MeterAutoRecord::getCreateTime));
    }

    private List<MeterAutoRecordCount> findMeterAutoRecordCountList(List<Long> deviceIds, Date startTime, Date endTime){
        return this.list(Wrappers.<MeterAutoRecordCount>lambdaQuery()
                .in(MeterAutoRecordCount::getDeviceId, deviceIds)
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MeterAutoRecordCount::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .between(MeterAutoRecordCount::getCountTime, startTime, endTime)
                .orderByDesc(MeterAutoRecordCount::getCountTime));
    }

    private void checkParam(MeterRecordCountParam param) {
        AssertUtils.notNull(param.getBranchParam(), "当前支路信息不能为空");
        AssertUtils.notNull(param.getReadingType(), "能源类型不能为空");
        AssertUtils.notNull(param.getMeterMethod(), "抄表方式不能为空");
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

    private void handleWeek(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本周数据
        List<MeterAutoRecordCount> countList = findWeekCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //上周数据
        List<MeterAutoRecordCount> lastCountList = findWeekCountList(deviceIds, DateUtil.offsetWeek(currentDate, -1));
        Map<Long, List<MeterAutoRecordCount>> lastCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        BigDecimal weekValue = BigDecimal.ZERO;
        BigDecimal lastWeekValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal valueEnd = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal valueStart = countMap.containsKey(deviceId) && countMap.get(deviceId).size() > 1 ? countMap.get(deviceId).get(countMap.get(deviceId).size() - 1).getReadingValue() : null;
            BigDecimal lastValueEnd = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValueStart = lastCountMap.containsKey(deviceId) && lastCountMap.get(deviceId).size() > 1 ? lastCountMap.get(deviceId).get(lastCountMap.get(deviceId).size() -1).getReadingValue() : null;
            if(valueEnd != null && valueStart != null){
                weekValue = weekValue.add(valueEnd.subtract(valueStart));
            }
            if(lastValueEnd != null && lastValueStart != null){
                lastWeekValue = lastWeekValue.add(lastValueEnd.subtract(lastValueStart));
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
    private List<MeterAutoRecordCount> findWeekCountList(List<Long> deviceIds, Date time){
        return findMeterAutoRecordCountList(deviceIds, DateUtil.beginOfWeek(time), DateUtil.endOfWeek(time));
    }

    private void handleMonth(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本月数据
        List<MeterAutoRecordCount> countList = findMonthCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //上月数据
        List<MeterAutoRecordCount> lastCountList = findMonthCountList(deviceIds, DateUtil.offsetMonth(currentDate, -1));
        Map<Long, List<MeterAutoRecordCount>> lastCountMap =  CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //去年本月数据
        Date lastYearTime = DateUtil.offset(currentDate, DateField.YEAR, -1);
        List<MeterAutoRecordCount> lastYearCountList = findMonthCountList(deviceIds, lastYearTime);
        Map<Long, List<MeterAutoRecordCount>> lastYearCountMap = CollectionUtil.isNotEmpty(lastYearCountList) ? lastYearCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();

        BigDecimal monthValue = BigDecimal.ZERO;
        BigDecimal lastMonthValue = BigDecimal.ZERO;
        BigDecimal lastYearMonthValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal valueEnd = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal valueStart = countMap.containsKey(deviceId) && countMap.get(deviceId).size() > 1 ? countMap.get(deviceId).get(countMap.get(deviceId).size() - 1).getReadingValue() : null;
            BigDecimal lastValueEnd = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValueStart = lastCountMap.containsKey(deviceId) && lastCountMap.get(deviceId).size() > 1 ? lastCountMap.get(deviceId).get(lastCountMap.get(deviceId).size() -1).getReadingValue() : null;
            BigDecimal lastYearValueEnd = lastYearCountMap.containsKey(deviceId) ? lastYearCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastYearValueStart = lastYearCountMap.containsKey(deviceId) && lastYearCountMap.get(deviceId).size() > 1 ? lastYearCountMap.get(deviceId).get(lastYearCountMap.get(deviceId).size() -1).getReadingValue() : null;
            if(valueEnd != null && valueStart != null){
                monthValue = monthValue.add(valueEnd.subtract(valueStart));
            }
            if(lastValueEnd != null && lastValueStart != null){
                lastMonthValue = lastMonthValue.add(lastValueEnd.subtract(lastValueStart));
            }
            if(lastYearValueEnd != null && lastYearValueStart != null){
                lastYearMonthValue = lastYearMonthValue.add(lastYearValueEnd.subtract(lastYearValueStart));
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
        List<MeterAutoRecordCount> countList = findMonthCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        BigDecimal monthValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal valueEnd = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal valueStart = countMap.containsKey(deviceId) && countMap.get(deviceId).size() > 1 ? countMap.get(deviceId).get(countMap.get(deviceId).size() - 1).getReadingValue() : null;
            if(valueEnd != null && valueStart != null){
                monthValue = monthValue.add(valueEnd.subtract(valueStart));
            }
        }
        if(monthValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setMonthValue(monthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    private List<MeterAutoRecordCount> findMonthCountList(List<Long> deviceIds, Date time){
        return findMeterAutoRecordCountList(deviceIds, DateUtil.beginOfMonth(time), DateUtil.endOfMonth(time));
    }

    private void handleYear(MeterRecordCountModel countModel, List<Long> deviceIds, int scale, Date currentDate){
        //本年数据
        List<MeterAutoRecordCount> countList = findYearCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //上年数据
        List<MeterAutoRecordCount> lastCountList = findYearCountList(deviceIds, DateUtil.offset(currentDate, DateField.YEAR, -1));
        Map<Long, List<MeterAutoRecordCount>> lastCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();

        BigDecimal yearValue = BigDecimal.ZERO;
        BigDecimal lastYearValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal valueEnd = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal valueStart = countMap.containsKey(deviceId) && countMap.get(deviceId).size() > 1 ? countMap.get(deviceId).get(countMap.get(deviceId).size() - 1).getReadingValue() : null;
            BigDecimal lastValueEnd = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal lastValueStart = lastCountMap.containsKey(deviceId) && lastCountMap.get(deviceId).size() > 1 ? lastCountMap.get(deviceId).get(lastCountMap.get(deviceId).size() -1).getReadingValue() : null;
            if(valueEnd != null && valueStart != null){
                yearValue = yearValue.add(valueEnd.subtract(valueStart));
            }
            if(lastValueEnd != null && lastValueStart != null){
                lastYearValue = lastYearValue.add(lastValueEnd.subtract(lastValueStart));
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
        List<MeterAutoRecordCount> countList = findYearCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        BigDecimal yearValue = BigDecimal.ZERO;
        for(Long deviceId : deviceIds){
            BigDecimal valueEnd = countMap.containsKey(deviceId) ? countMap.get(deviceId).get(0).getReadingValue() : null;
            BigDecimal valueStart = countMap.containsKey(deviceId) && countMap.get(deviceId).size() > 1 ? countMap.get(deviceId).get(countMap.get(deviceId).size() - 1).getReadingValue() : null;
            if(valueEnd != null && valueStart != null){
                yearValue = yearValue.add(valueEnd.subtract(valueStart));
            }
        }
        if(yearValue.compareTo(BigDecimal.ZERO) != 0){
            countModel.setYearValue(yearValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }

    private List<MeterAutoRecordCount> findYearCountList(List<Long> deviceIds, Date time){
        return findMeterAutoRecordCountList(deviceIds, DateUtil.beginOfYear(time), DateUtil.endOfYear(time));
    }

    private void handleMonthBranch(List<MeterRecordBranchCountModel> branchCountModels, Date currentDate, List<Long> deviceIds, int scale){
        //本月数据
        List<MeterAutoRecordCount> countList = findMonthCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //上月数据
        List<MeterAutoRecordCount> lastCountList = findMonthCountList(deviceIds, DateUtil.offsetMonth(currentDate, -1));
        Map<Long, List<MeterAutoRecordCount>> lastCountMap =  CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //去年本月数据
        Date lastYearTime = DateUtil.offset(currentDate, DateField.YEAR, -1);
        List<MeterAutoRecordCount> lastYearCountList = findMonthCountList(deviceIds, lastYearTime);
        Map<Long, List<MeterAutoRecordCount>> lastYearCountMap = CollectionUtil.isNotEmpty(lastYearCountList) ? lastYearCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        for (DateTime monthDate : DateUtil.rangeToList(DateUtil.beginOfMonth(currentDate), DateUtil.endOfMonth(currentDate), DateField.DAY_OF_MONTH)) {
            MeterRecordBranchCountModel countModel = new MeterRecordBranchCountModel();
            String time = DateUtil.format(monthDate, "MM-dd");
            countModel.setTime(time);
            BigDecimal monthValue = BigDecimal.ZERO;
            BigDecimal lastMonthValue = BigDecimal.ZERO;
            BigDecimal lastYearMonthValue = BigDecimal.ZERO;
            for(Long deviceId : deviceIds){
                Map<String, List<MeterAutoRecordCount> > countGroupMap = countMap.containsKey(deviceId) ? countMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.format(p.getCountTime(), "MM-dd"))) : new HashMap<>();
                Map<String, List<MeterAutoRecordCount> > lastCountGroupMap = lastCountMap.containsKey(deviceId) ? lastCountMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.format(p.getCountTime(), "MM-dd"))) : new HashMap<>();
                Map<String, List<MeterAutoRecordCount> > lastYearCountGroupMap = lastYearCountMap.containsKey(deviceId) ? lastYearCountMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.format(p.getCountTime(), "MM-dd"))) : new HashMap<>();
                //本月的能耗
                BigDecimal value = countGroupMap.containsKey(time) ? countGroupMap.get(time).get(0).getEnergyConsumption() : null;
                //上个月的能耗
                BigDecimal lastValue = lastCountGroupMap.containsKey(time) ? lastCountGroupMap.get(time).get(0).getEnergyConsumption() : null;
                //去年本月的能耗
                BigDecimal yearValue = lastYearCountGroupMap.containsKey(time) ? lastYearCountGroupMap.get(time).get(0).getReadingValue() : null;
                if(value != null){
                    monthValue = monthValue.add(value);
                }
                if(lastValue != null){
                    lastMonthValue = lastMonthValue.add(lastValue);
                }
                if(yearValue != null){
                    lastYearMonthValue = lastYearMonthValue.add(yearValue);
                }
            }
            if(monthValue.compareTo(BigDecimal.ZERO) != 0){
                countModel.setValue(monthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            if(lastMonthValue.compareTo(BigDecimal.ZERO) != 0){
                countModel.setLastValue(lastMonthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            if(lastYearMonthValue.compareTo(BigDecimal.ZERO) != 0){
                countModel.setLastYearValue(lastMonthValue.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            branchCountModels.add(countModel);
        }
    }

    private void handleYearBranch(List<MeterRecordBranchCountModel> branchCountModels, Date currentDate, List<Long> deviceIds, int scale){
        //今年数据
        List<MeterAutoRecordCount> countList = findYearCountList(deviceIds, currentDate);
        Map<Long, List<MeterAutoRecordCount>> countMap = CollectionUtil.isNotEmpty(countList) ? countList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        //去年数据
        List<MeterAutoRecordCount> lastCountList = findYearCountList(deviceIds, DateUtil.offset(currentDate, DateField.YEAR, -1));
        Map<Long, List<MeterAutoRecordCount>> lastYearCountMap = CollectionUtil.isNotEmpty(lastCountList) ? lastCountList.stream().collect(Collectors.groupingBy(MeterAutoRecordCount::getDeviceId)) : new HashMap<>();
        for (DateTime monthDate : DateUtil.rangeToList(DateUtil.beginOfYear(DateUtil.date()), DateUtil.endOfYear(DateUtil.date()), DateField.MONTH)) {
            int month = DateUtil.month(monthDate) + 1;
            MeterRecordBranchCountModel countModel = new MeterRecordBranchCountModel();
            countModel.setTime(DateUtil.format(monthDate, "M月"));
            BigDecimal monthValue = BigDecimal.ZERO;
            BigDecimal lastMonthValue = BigDecimal.ZERO;
            for(Long deviceId : deviceIds){
                Map<Integer, List<MeterAutoRecordCount> > countGroupMap = countMap.containsKey(deviceId) ? countMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.month(p.getCountTime()) + 1)) : new HashMap<>();
                Map<Integer, List<MeterAutoRecordCount> > lastYearCountGroupMap = lastYearCountMap.containsKey(deviceId) ? lastYearCountMap.get(deviceId).stream().collect(Collectors.groupingBy(p-> DateUtil.month(p.getCountTime()) + 1)) : new HashMap<>();
                //今年的抄表值
                BigDecimal valueEnd = countGroupMap.containsKey(month) ? countGroupMap.get(month).get(0).getReadingValue() : null;
                BigDecimal valueStart = countGroupMap.containsKey(month) && countGroupMap.get(month).size() > 1 ? countGroupMap.get(month).get(countGroupMap.get(month).size() - 1).getReadingValue() : null;

                //去年的抄表值
                BigDecimal yearValueEnd = lastYearCountGroupMap.containsKey(month) ? lastYearCountGroupMap.get(month).get(0).getReadingValue() : null;
                BigDecimal yearValueStart = lastYearCountGroupMap.containsKey(month) && lastYearCountGroupMap.get(month).size() > 1 ? lastYearCountGroupMap.get(month).get(lastYearCountGroupMap.get(month).size() - 1).getReadingValue() : null;
                if(valueEnd != null && valueStart != null){
                    monthValue = monthValue.add(valueEnd.subtract(valueStart));
                }
                if(yearValueEnd != null && yearValueStart != null){
                    lastMonthValue = lastMonthValue.add(yearValueEnd.subtract(yearValueStart));
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

    private void handleAppMonthSonBranch(List<MeterRecordSonBranchCountModel> sonBranchCountModels, List<EnergyBranchParam> sonBranchParams, Map<Long, List<BranchDeviceModel>> branchDeviceMap, Date currentDate, int scale){
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

}
