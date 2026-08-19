package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.CallTaxiRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.CarRentRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.CarTravelRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.ShuttleOrderPageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenTrafficService;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleCarRentApply;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleCarTaskRecord;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleCarRentApplyRepository;
import com.cgnpc.bbxpark.traffic.mapper.DwdVehicleCarTaskRecordRepository;
import com.cgnpc.bbxpark.traffic.domain.VehicleApply;
import com.cgnpc.bbxpark.traffic.domain.VehicleLineOrderInfo;
import com.cgnpc.bbxpark.traffic.mapper.VehicleApplyRepository;
import com.cgnpc.bbxpark.traffic.mapper.VehicleLineOrderInfoRepository;
import com.cgnpc.cud.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;

@Service
public class ScreenTrafficServiceImpl implements IScreenTrafficService {
    @Autowired
    private VehicleApplyRepository vehicleApplyRepository;
    @Autowired
    private VehicleLineOrderInfoRepository vehicleLineOrderInfoRepository;
    @Autowired
    private DwdVehicleCarTaskRecordRepository dwdVehicleCarTaskRecordRepository;
    @Autowired
    private DwdVehicleCarRentApplyRepository dwdVehicleCarRentApplyRepository;



    @Override
    public List<CallTaxiReasonStatModel> getCallTaxiReasonAnalysis(Integer year, Integer month) {
        return vehicleApplyRepository.getCallTaxiReasonAnalysis(DateUtil.getFirstTimeBy(year, month),DateUtil.getLastTimeBy(year,month));
    }

    @Override
    public List<CallTaxiDeptStatModel> getCallTaxiDeptAnalysis(Integer year, Integer month) {
        return vehicleApplyRepository.getCallTaxiDeptAnalysis(DateUtil.getFirstTimeBy(year, month),DateUtil.getLastTimeBy(year,month));
    }

    @Override
    public ShuttleOverviewModel getShuttleOverview(Integer year, Integer month) {
        return vehicleLineOrderInfoRepository.getShuttleOverview(DateUtil.getFirstTimeBy(year, month),DateUtil.getLastTimeBy(year,month));
    }

    @Override
    public List<ShuttleRunOrgStatModel> getShuttleRunOrgAnalysis(Integer year, Integer month) {
        return vehicleLineOrderInfoRepository.getShuttleRunOrgAnalysis(DateUtil.getFirstTimeBy(year, month),DateUtil.getLastTimeBy(year,month));
    }

    @Override
    public List<ShuttleHotLineModel> getShuttleHotLine(Integer year, Integer month) {
        return vehicleLineOrderInfoRepository.getShuttleHotLine(DateUtil.getFirstTimeBy(year, month),DateUtil.getLastTimeBy(year,month));
    }

    @Override
    public List<ShuttleHotStationModel> getShuttleHotStation(Integer year, Integer month) {
        return vehicleLineOrderInfoRepository.getShuttleHotStation(DateUtil.getFirstTimeBy(year, month),DateUtil.getLastTimeBy(year,month));
    }

    @Override
    public IPage<CallTaxiRecordModel> pageCallTaxiRecord(CallTaxiRecordPageParam param) {
        IPage<VehicleApply> page = vehicleApplyRepository.selectPage(param.getPage(),Wrappers.<VehicleApply>lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCallReason()),VehicleApply::getCallReason,param.getCallReason())
                .like(StringUtils.isNotEmpty(param.getDepartment()),VehicleApply::getDepartment,param.getDepartment())
                .between(VehicleApply::getCreateTime,DateUtil.getFirstTimeBy(param.getYear(),param.getMonth()),DateUtil.getLastTimeBy(param.getYear(),param.getMonth()))
                .orderByDesc(VehicleApply::getCreateTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        return ConvertUtil.pageConvert(page, BeanUtils.convertListTo(page.getRecords(),CallTaxiRecordModel::new));
    }

    @Override
    public IPage<ShuttleOrderModel> pageShuttleOrder(ShuttleOrderPageParam param) {
        IPage<VehicleLineOrderInfo> page = vehicleLineOrderInfoRepository.selectPage(param.getPage(),Wrappers.<VehicleLineOrderInfo>lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getLineName()),VehicleLineOrderInfo::getLineName,param.getLineName())
                .eq(StringUtils.isNotEmpty(param.getStartStation()),VehicleLineOrderInfo::getStartStation,param.getStartStation())
                .eq(StringUtils.isNotEmpty(param.getEndStation()),VehicleLineOrderInfo::getEndStation,param.getEndStation())
                .between(VehicleLineOrderInfo::getActualPayTime,DateUtil.getFirstTimeBy(param.getYear(),param.getMonth()),DateUtil.getLastTimeBy(param.getYear(),param.getMonth()))
                .orderByDesc(VehicleLineOrderInfo::getActualPayTime));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
        }
        return ConvertUtil.pageConvert(page, BeanUtils.convertListTo(page.getRecords(),ShuttleOrderModel::new));
    }

    @Override
    public DispatchOverviewModel getDispatchOverview(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, month);
        Date end = getEndDate(year, month);
        return dwdVehicleCarTaskRecordRepository.getDispatchOverview(start, end);
    }

    @Override
    public List<DispatchCountTrendModel> getDispatchCountTrend(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        String timeType = getTimeType(year, month);
        switch (timeType) {
            case "month":
                return handleDispatchCountTrend("MM-dd",  DateUtil.beginOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateUtil.endOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateField.DAY_OF_MONTH);
            case "year":
                return handleDispatchCountTrend("MM",  DateUtil.beginOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateUtil.endOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateField.MONTH);
            default:
                throw new BaseException("参数错误");
        }
    }

    @Override
    public List<DispatchMileageTrendModel> getDispatchMileageTrend(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        String timeType = getTimeType(year, month);
        switch (timeType) {
            case "month":
                return handleDispatchMileageTrend("MM-dd",  DateUtil.beginOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateUtil.endOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateField.DAY_OF_MONTH);
            case "year":
                return handleDispatchMileageTrend("MM",  DateUtil.beginOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateUtil.endOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateField.MONTH);
            default:
                throw new BaseException("参数错误");
        }
    }

    @Override
    public IPage<CarTravelRecordModel> pageCarTravelRecord(CarTravelRecordPageParam param) {
        AssertUtils.isNotEmpty(param.getYear(), "年份不能为空");
        Date start = getStartDate(param.getYear(), param.getMonth());
        Date end = getEndDate(param.getYear(), param.getMonth());
        IPage<DwdVehicleCarTaskRecord> page = dwdVehicleCarTaskRecordRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DwdVehicleCarTaskRecord>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(param.getCarPlate()), DwdVehicleCarTaskRecord::getCarPlate, param.getCarPlate())
                .like(ObjectUtil.isNotEmpty(param.getDriverName()), DwdVehicleCarTaskRecord::getDriverName, param.getDriverName())
                .between(DwdVehicleCarTaskRecord::getDepartTime, start, end)
                .orderByDesc(DwdVehicleCarTaskRecord::getDepartTime));
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), CarTravelRecordModel::new));
    }

    @Override
    public IPage<CarRentRecordModel> pageCarRentRecord(CarRentRecordPageParam param) {
        AssertUtils.isNotEmpty(param.getYear(), "年份不能为空");
        Date start = getStartDate(param.getYear(), param.getMonth());
        Date end = getEndDate(param.getYear(), param.getMonth());
        IPage<DwdVehicleCarRentApply> page = dwdVehicleCarRentApplyRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DwdVehicleCarRentApply>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(param.getRentType()), DwdVehicleCarRentApply::getRentType, param.getRentType())
                .le(DwdVehicleCarRentApply::getRentStartTime, end)
                .ge(DwdVehicleCarRentApply::getRentEndTime, start)
                .le(ObjectUtil.isNotEmpty(param.getRentTime()),DwdVehicleCarRentApply::getRentStartTime, param.getRentTime())
                .ge(ObjectUtil.isNotEmpty(param.getRentTime()), DwdVehicleCarRentApply::getRentEndTime, param.getRentTime())
                .eq(DwdVehicleCarRentApply::getInstanceStatus, "已完成")
                .eq(DwdVehicleCarRentApply::getApproveResult, "同意")
                .orderByDesc(DwdVehicleCarRentApply::getApplyDate));
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), CarRentRecordModel::new));
    }

    @Override
    public CarRentOverviewModel getCarRentOverview(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, month);
        Date end = getEndDate(year, month);
        Integer rentCount = dwdVehicleCarRentApplyRepository.selectCount(Wrappers.<DwdVehicleCarRentApply>lambdaQuery().between(DwdVehicleCarRentApply::getApplyDate, start, end).eq(DwdVehicleCarRentApply::getInstanceStatus, "已完成"));
        Integer count =dwdVehicleCarRentApplyRepository.getCarRentCount(start, end);
        CarRentOverviewModel model = new CarRentOverviewModel();
        model.setRentDays(count);
        model.setRentCount(rentCount);
        return model;
    }

    @Override
    public List<CarRentTypeStatModel> getCarRentTypeAnalysis(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, month);
        Date end = getEndDate(year, month);
        List<CarRentTypeStatModel> models = dwdVehicleCarRentApplyRepository.getCarRentTypeAnalysis(start, end);
        if(CollectionUtil.isEmpty(models)){
            return Collections.emptyList();
        }
        int total = models.stream().mapToInt(CarRentTypeStatModel::getCount).sum();
        models.forEach(model->{
            model.setRatio(total == 0 ? 0.0d : Math.round((double) model.getCount() / total * 10000) / 100.0);
        });
        return models;
    }

    @Override
    public List<CarRentTrendModel> getCarRentTrend(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        String timeType = getTimeType(year, month);
        switch (timeType) {
            case "month":
                return handleRentTrend("MM-dd",  DateUtil.beginOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateUtil.endOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateField.DAY_OF_MONTH);
            case "year":
                return handleRentTrend("MM",  DateUtil.beginOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateUtil.endOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateField.MONTH);
            default:
                throw new BaseException("参数错误");
        }
    }

    private List<DispatchCountTrendModel> handleDispatchCountTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<DispatchCountTrendModel> models = new ArrayList<>();
        List<DispatchCountTrendModel> countModels = dwdVehicleCarTaskRecordRepository.getDispatchCountTrend(timeType, minDay, maxDay);
        Map<String, Integer> countMap = CollectionUtil.isEmpty(countModels) ? Collections.emptyMap(): countModels.stream().collect(Collectors.toMap(DispatchCountTrendModel::getDate, DispatchCountTrendModel::getCount, (k1, k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            DispatchCountTrendModel model = new DispatchCountTrendModel();
            model.setDate(dayStr);
            model.setCount(countMap.getOrDefault(dayStr, 0));
            models.add(model);
        }
        return models;
    }

    private List<DispatchMileageTrendModel> handleDispatchMileageTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<DispatchMileageTrendModel> models = new ArrayList<>();
        List<DispatchMileageTrendModel> mileageModels = dwdVehicleCarTaskRecordRepository.getDispatchMileageTrend(timeType, minDay, maxDay);
        Map<String, Double> mileageMap = CollectionUtil.isEmpty(mileageModels) ? Collections.emptyMap(): mileageModels.stream().collect(Collectors.toMap(DispatchMileageTrendModel::getDate, DispatchMileageTrendModel::getTotalMileage, (k1, k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            DispatchMileageTrendModel model = new DispatchMileageTrendModel();
            model.setDate(dayStr);
            model.setTotalMileage(mileageMap.getOrDefault(dayStr, 0d));
            models.add(model);
        }
        return models;
    }

    private List<CarRentTrendModel> handleRentTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<CarRentTrendModel> models = new ArrayList<>();
        List<CarRentTrendModel> rentList = dwdVehicleCarRentApplyRepository.getCarRentTrend(timeType, minDay, maxDay);
        Map<String, Integer> rentMap = CollectionUtil.isEmpty(rentList) ? Collections.emptyMap(): rentList.stream().collect(Collectors.toMap(CarRentTrendModel::getDate, CarRentTrendModel::getCount, (k1, k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            CarRentTrendModel model = new CarRentTrendModel();
            model.setDate(dayStr);
            model.setCount(rentMap.getOrDefault(dayStr, 0));
            models.add(model);
        }
        return models;
    }

    private String getTimeType(Integer year, Integer month){
        return ObjectUtil.isEmpty(month) ? "year" : "month";
    }

    private Date getStartDate(Integer year, Integer month){
        return ObjectUtil.isEmpty(month) ? DateUtil.beginOfYear(DateUtil.parse(String.format("%04d-01-01", year))) : DateUtil.beginOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month)));
    }

    private Date getEndDate(Integer year, Integer month){
        return ObjectUtil.isEmpty(month) ? DateUtil.endOfYear(DateUtil.parse(String.format("%04d-01-01", year))) : DateUtil.endOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month)));
    }
}