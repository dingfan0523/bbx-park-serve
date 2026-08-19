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
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficMileagePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficRepairPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficVehiclePageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenTrafficOverviewService;
import com.cgnpc.bbxpark.traffic.domain.*;
import com.cgnpc.bbxpark.traffic.mapper.*;
import com.cgnpc.cud.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScreenTrafficOverviewServiceImpl implements IScreenTrafficOverviewService {

    @Autowired
    private DwdVehicleInfoRepository dwdVehicleInfoRepository;

    @Autowired
    private DwdVehicleRepairRepository dwdVehicleRepairRepository;

    @Autowired
    private DwdVehicleCarTaskRecordRepository dwdVehicleCarTaskRecordRepository;

    @Autowired
    private DwdVehicleDriverInfoRepository dwdVehicleDriverInfoRepository;

    @Autowired
    private DwdVehicleMaintainRepository dwdVehicleMaintainRepository;

    @Autowired
    private DwdVehiclePartReplaceRepository dwdVehiclePartReplaceRepository;

    @Autowired
    private DwdVehicleMonthlySettlementRepository dwdVehicleMonthlySettlementRepository;


    @Override
    public TrafficMacroIndexModel getMacroIndex(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, month);
        Date end = getEndDate(year, month);
        TrafficMacroIndexModel model = new TrafficMacroIndexModel();
        long totalVehicles = dwdVehicleInfoRepository.selectCount(Wrappers.emptyWrapper());
        long repair = dwdVehicleRepairRepository.selectCount(Wrappers.<DwdVehicleRepair>lambdaQuery().between(DwdVehicleRepair::getApplyDate, start, end));
        long maintain = dwdVehicleMaintainRepository.selectCount(Wrappers.<DwdVehicleMaintain>lambdaQuery().between(DwdVehicleMaintain::getMaintainDate, start, end));
        long partReplace = dwdVehiclePartReplaceRepository.selectCount(Wrappers.<DwdVehiclePartReplace>lambdaQuery().between(DwdVehiclePartReplace::getReplaceDate, start, end));
        model.setTotalVehicles(totalVehicles);
        model.setTotalMaintenanceCount(repair + maintain + partReplace);
        //车辆总里程后续计算
        model.setTotalMileage(dwdVehicleMonthlySettlementRepository.getMileageCount(start, end));
        return model;
    }

    @Override
    public List<TrafficVehicleTypeModel> getVehicleTypeDistribution() {
        return dwdVehicleInfoRepository.getVehicleTypeDistribution();
    }

    @Override
    public List<TrafficMaintenanceModel> getMaintenanceAnalysis(Integer year) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, null);
        Date end = getEndDate(year, null);
        return handleMaintenanceTrend("MM",  start, end, DateField.MONTH);
    }

    @Override
    public List<TrafficVehicleDetailModel> getVehicleDetailList(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, null);
        Date end = getEndDate(year, null);
        List<DwdVehicleInfo> dwdVehicleInfos = dwdVehicleInfoRepository.selectList(Wrappers.emptyWrapper());
        if(CollectionUtil.isEmpty(dwdVehicleInfos)){
            return Collections.emptyList();
        }
        List<TrafficMaintenanceModel> maintenanceModels = dwdVehicleMaintainRepository.getMaintenanceCountByPlateNum(start, end);
        //运营成本后续计算
        List<TrafficMaintenanceModel> directOperatingCostModels = dwdVehicleMonthlySettlementRepository.getCostCountByCarPlate(start, end);
        Map<String, Double> maintenanceMap = CollectionUtil.isEmpty(maintenanceModels) ? Collections.emptyMap(): maintenanceModels.stream().collect(Collectors.toMap(TrafficMaintenanceModel::getMonth, TrafficMaintenanceModel::getMaintenanceCost, (k1, k2)->k1));
        Map<String, Double> directOperatingCostMap = CollectionUtil.isEmpty(directOperatingCostModels) ? Collections.emptyMap(): directOperatingCostModels.stream().collect(Collectors.toMap(TrafficMaintenanceModel::getMonth, TrafficMaintenanceModel::getDirectOperatingCost, (k1,k2)->k1));
        return dwdVehicleInfos.stream().map(item ->{
            TrafficVehicleDetailModel model = new TrafficVehicleDetailModel();
            model.setId(item.getId());
            model.setVehicleType(item.getVehicleType());
            model.setPlateNumber(item.getLicensePlate());
            model.setTotalCost(directOperatingCostMap.getOrDefault(item.getLicensePlate(), 0d) + maintenanceMap.getOrDefault(item.getLicensePlate(), 0d));
            model.setMaintenanceCost(maintenanceMap.getOrDefault(item.getLicensePlate(), 0d));
            model.setMaintenanceCostRate(model.getTotalCost() > 0 ? Math.round(model.getMaintenanceCost() / model.getTotalCost() * 10000) / 100.0 : 0d);
            String remind = "--";
            if(model.getMaintenanceCostRate() >= 15){
                remind = "高耗低效";
            }else if(model.getMaintenanceCostRate() >= 10){
                remind = "特别关注";
            }
            model.setDiagnosisReminder(remind);
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public TrafficVehicleDetailInfoModel getVehicleDetailInfo(String plateNumber) {
        AssertUtils.isNotEmpty(plateNumber, "车牌号不能为空");
        TrafficVehicleDetailInfoModel model = new TrafficVehicleDetailInfoModel();
        List<DwdVehicleInfo> dwdVehicleInfos = dwdVehicleInfoRepository.selectList(Wrappers.<DwdVehicleInfo>lambdaQuery().eq(DwdVehicleInfo::getLicensePlate, plateNumber));
        if(CollectionUtil.isEmpty(dwdVehicleInfos)){
            return model;
        }
        DwdVehicleInfo info = dwdVehicleInfos.get(0);
        model.setVehicleType(info.getVehicleType());
        model.setPlateNumber(info.getLicensePlate());
        model.setVehicleModel(info.getVehicleModel());
        model.setLastUpdateTime(info.getImportTime());
        //基础状态
        model.setMileage(info.getMileage());
        model.setTeam(info.getDepartment());
        //运行成本-后续
        List<DwdVehicleMonthlySettlement> dwdVehicleMonthlySettlements = dwdVehicleMonthlySettlementRepository.selectList(Wrappers.<DwdVehicleMonthlySettlement>lambdaQuery()
                .eq(DwdVehicleMonthlySettlement::getCarPlate, plateNumber)
                .orderByDesc(DwdVehicleMonthlySettlement::getSettlementDate));
        if(CollectionUtil.isNotEmpty(dwdVehicleMonthlySettlements)){
            DwdVehicleMonthlySettlement settlement = dwdVehicleMonthlySettlements.get(0);
            model.setMonthlyRent(settlement.getMonthlyRent());
            model.setFuelCost(settlement.getFuelConsumption());
            model.setFuelSubsidy(settlement.getFuelSubsidy());
            model.setParkingFee(settlement.getParkingFee());
            model.setRoadBridgeFee(settlement.getTollFee());
            model.setTotalCost(sumDoubles(model.getMonthlyRent(),model.getFuelCost(),model.getFuelSubsidy(),model.getParkingFee(), model.getRoadBridgeFee()));
        }
        //运行安全
        model.setNextInspectDate(info.getNextInspectDate());
        return model;
    }

    @Override
    public IPage<TrafficVehiclePageModel> getVehiclePage(TrafficVehiclePageParam param) {
        IPage<DwdVehicleInfo> page = dwdVehicleInfoRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DwdVehicleInfo>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(param.getLicensePlate()),DwdVehicleInfo::getLicensePlate, param.getLicensePlate())
                .eq(ObjectUtil.isNotEmpty(param.getVehicleType()), DwdVehicleInfo::getVehicleType, param.getVehicleType())
                .eq(ObjectUtil.isNotEmpty(param.getUseStatus()), DwdVehicleInfo::getUseStatus, param.getUseStatus())
                .eq(ObjectUtil.isNotEmpty(param.getRunStatus()), DwdVehicleInfo::getRunStatus, param.getRunStatus())
                .orderByDesc(DwdVehicleInfo::getImportTime));
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), TrafficVehiclePageModel::new));
    }

    @Override
    public IPage<TrafficMileagePageModel> getMileagePage(TrafficMileagePageParam param) {
        AssertUtils.isNotEmpty(param.getYear(), "年份不能为空");
        Date start = getStartDate(param.getYear(), param.getMonth());
        Date end = getEndDate(param.getYear(), param.getMonth());
        IPage<TrafficVehiclePageModel> vehiclePage = getVehiclePage(BeanUtils.convertTo(param, TrafficVehiclePageParam::new));
        List<TrafficVehiclePageModel> vehicleResults = vehiclePage.getRecords();
        List<TrafficMileagePageModel> models = new ArrayList<>();
        //车辆费用里程信息
        List<TrafficSettlementMileageModel> mileageModels = dwdVehicleMonthlySettlementRepository.getMileageByCarPlate(start, end);
        Map<String, Double> mileageMap = CollectionUtil.isEmpty(mileageModels) ? Collections.emptyMap() : mileageModels.stream().collect(Collectors.toMap(TrafficSettlementMileageModel::getCarPlate, TrafficSettlementMileageModel::getCurrentMileage));
        if(CollectionUtil.isNotEmpty(vehicleResults)){
            models = vehicleResults.stream().map(item->{
                TrafficMileagePageModel model = new TrafficMileagePageModel();
                model.setId(item.getId());
                model.setVehicleType(item.getVehicleType());
                model.setLicensePlate(item.getLicensePlate());
                //当前里程后续开发
                model.setCurrentMileage(mileageMap.getOrDefault(item.getLicensePlate(), 0d));
                return model;
            }).collect(Collectors.toList());
        }
        return ConvertUtil.pageConvert(vehiclePage.getCurrent(), vehiclePage.getTotal(), vehiclePage.getSize(), models);

    }

    @Override
    public IPage<TrafficRepairPageModel> getRepairPage(TrafficRepairPageParam param) {
        AssertUtils.isNotEmpty(param.getYear(), "年份不能为空");
        Date start = getStartDate(param.getYear(), param.getMonth());
        Date end = getEndDate(param.getYear(), param.getMonth());
        IPage<DwdVehicleRepair> page = dwdVehicleRepairRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DwdVehicleRepair>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(param.getPlateNum()),DwdVehicleRepair::getPlateNum, param.getPlateNum())
                // 添加车辆类型查询条件
                 .inSql(ObjectUtil.isNotEmpty(param.getVehicleType()), DwdVehicleRepair::getPlateNum, "SELECT distinct license_plate FROM dwd_vehicle_info WHERE vehicle_type = " + param.getVehicleType())
                .between(DwdVehicleRepair::getApplyDate, start, end)
                .orderByDesc(DwdVehicleRepair::getApplyDate));
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), TrafficRepairPageModel::new));
    }

    @Override
    public IPage<TrafficMaintainPageModel> getMaintainPage(TrafficRepairPageParam param) {
        AssertUtils.isNotEmpty(param.getYear(), "年份不能为空");
        Date start = getStartDate(param.getYear(), param.getMonth());
        Date end = getEndDate(param.getYear(), param.getMonth());
        IPage<DwdVehicleMaintain> page = dwdVehicleMaintainRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DwdVehicleMaintain>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(param.getPlateNum()),DwdVehicleMaintain::getPlateNum, param.getPlateNum())
                .eq(ObjectUtil.isNotEmpty(param.getVehicleType()), DwdVehicleMaintain::getVehicleType, param.getVehicleType())
                .between(DwdVehicleMaintain::getMaintainDate, start, end)
                .orderByDesc(DwdVehicleMaintain::getMaintainDate));
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), TrafficMaintainPageModel::new));
    }

    @Override
    public IPage<TrafficPartReplacePageModel> getPartReplacePage(TrafficRepairPageParam param) {
        AssertUtils.isNotEmpty(param.getYear(), "年份不能为空");
        Date start = getStartDate(param.getYear(), param.getMonth());
        Date end = getEndDate(param.getYear(), param.getMonth());
        IPage<DwdVehiclePartReplace> page = dwdVehiclePartReplaceRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DwdVehiclePartReplace>lambdaQuery()
                .like(ObjectUtil.isNotEmpty(param.getPlateNum()),DwdVehiclePartReplace::getPlateNum, param.getPlateNum())
                .eq(ObjectUtil.isNotEmpty(param.getVehicleType()), DwdVehiclePartReplace::getVehicleType, param.getVehicleType())
                .between(DwdVehiclePartReplace::getReplaceDate, start, end)
                .orderByDesc(DwdVehiclePartReplace::getReplaceDate));
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), BeanUtils.convertListTo(page.getRecords(), TrafficPartReplacePageModel::new));
    }

    @Override
    public TrafficDriverAnalysisModel getDriverAnalysis() {
        TrafficDriverAnalysisModel model = new TrafficDriverAnalysisModel();
        List<TrafficDriverCountModel> countModels = dwdVehicleDriverInfoRepository.getDriverAnalysis();
        if(CollectionUtil.isEmpty(countModels)){
            return model;
        }
        Map<String, Integer> countMap = countModels.stream().collect(Collectors.toMap(TrafficDriverCountModel::getGender, TrafficDriverCountModel::getCount));
        model.setFemaleCount(countMap.getOrDefault("女", 0));
        model.setMaleCount(countMap.getOrDefault("男", 0));
        model.setTotalDrivers(model.getFemaleCount() + model.getMaleCount());
        model.setMalePercentage(model.getTotalDrivers() > 0 ? Math.round((float) model.getMaleCount() / model.getTotalDrivers() * 10000) / 100.0 : 0d);
        model.setFemalePercentage(model.getTotalDrivers() > 0 ? Math.round((float) model.getFemaleCount() / model.getTotalDrivers() * 10000) / 100.0 : 0d);
        return model;
    }

    @Override
    public List<TrafficDriverEntryCountModel> getDriverEntryCount(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        String timeType = getTimeType(year, month);
        switch (timeType) {
            case "month":
                return handleDriverTrend("MM-dd",  DateUtil.beginOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateUtil.endOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateField.DAY_OF_MONTH);
            case "year":
                return handleDriverTrend("MM",  DateUtil.beginOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateUtil.endOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateField.MONTH);
            default:
                throw new BaseException("参数错误");
        }
    }

    @Override
    public List<TrafficDriverWorkloadModel> getDriverWorkload(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, month);
        Date end = getEndDate(year, month);
        return dwdVehicleCarTaskRecordRepository.getDriverWorkload(start, end);
    }

    @Override
    public List<TrafficMileageUtilizationModel> getMileageUtilization(Integer year) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        Date start = getStartDate(year, null);
        Date end = getEndDate(year, null);
        //后续开发
        return handleMileageUtilizationTrend("MM",  start, end, DateField.MONTH);
    }

    private List<TrafficMaintenanceModel> handleMaintenanceTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<TrafficMaintenanceModel> models = new ArrayList<>();
        List<TrafficMaintenanceModel> maintenanceModels = dwdVehicleMaintainRepository.getMaintenanceAnalysis(minDay, maxDay);
        //运营成本后续计算
        List<TrafficMaintenanceModel> directOperatingCostModels = dwdVehicleMonthlySettlementRepository.getCostCountByDate(minDay, maxDay);
        Map<String, Double> maintenanceMap = CollectionUtil.isEmpty(maintenanceModels) ? Collections.emptyMap(): maintenanceModels.stream().collect(Collectors.toMap(TrafficMaintenanceModel::getMonth, TrafficMaintenanceModel::getMaintenanceCost, (k1, k2)->k1));
        Map<String, Double> directOperatingCostMap = CollectionUtil.isEmpty(directOperatingCostModels) ? Collections.emptyMap(): directOperatingCostModels.stream().collect(Collectors.toMap(TrafficMaintenanceModel::getMonth, TrafficMaintenanceModel::getDirectOperatingCost, (k1,k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            TrafficMaintenanceModel model = new TrafficMaintenanceModel();
            model.setMonth(dayStr);
            model.setDirectOperatingCost(directOperatingCostMap.getOrDefault(dayStr, 0d));
            model.setMaintenanceCost(maintenanceMap.getOrDefault(dayStr, 0d));
            models.add(model);
        }
        return models;
    }

    private List<TrafficDriverEntryCountModel> handleDriverTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<TrafficDriverEntryCountModel> models = new ArrayList<>();
        List<TrafficDriverEntryCountModel> driverModels = dwdVehicleDriverInfoRepository.getDriverEntryCount(timeType, minDay, maxDay);
        Map<String, Integer> driverMap = CollectionUtil.isEmpty(driverModels) ? Collections.emptyMap(): driverModels.stream().collect(Collectors.toMap(TrafficDriverEntryCountModel::getDate, TrafficDriverEntryCountModel::getCount, (k1, k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            TrafficDriverEntryCountModel model = new TrafficDriverEntryCountModel();
            model.setDate(dayStr);
            model.setCount(driverMap.getOrDefault(dayStr, 0));
            models.add(model);
        }
        return models;
    }

    private List<TrafficMileageUtilizationModel> handleMileageUtilizationTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<TrafficMileageUtilizationModel> models = new ArrayList<>();
        //后续计算
        List<TrafficMileageUtilizationModel> mileageUtilizationModels = dwdVehicleMonthlySettlementRepository.getMileageUtilization(minDay, maxDay);
        Map<String, TrafficMileageUtilizationModel> mileageUtilizationMap = CollectionUtil.isEmpty(mileageUtilizationModels) ? Collections.emptyMap(): mileageUtilizationModels.stream().collect(Collectors.toMap(TrafficMileageUtilizationModel::getMonth, Function.identity(), (k1, k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            TrafficMileageUtilizationModel model = new TrafficMileageUtilizationModel();
            model.setMonth(dayStr);
            model.setMonthlyMileage(ObjectUtil.isEmpty(mileageUtilizationMap.get(dayStr)) ? 0d : mileageUtilizationMap.get(dayStr).getMonthlyMileage());
            model.setTotalPackageMileage(ObjectUtil.isEmpty(mileageUtilizationMap.get(dayStr)) ? 0d : mileageUtilizationMap.get(dayStr).getTotalPackageMileage());
            model.setRemainingMileage(ObjectUtil.isEmpty(mileageUtilizationMap.get(dayStr)) ? 0d : mileageUtilizationMap.get(dayStr).getRemainingMileage());
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

    /**
     * 计算任意多个 Double 类型的和
     * 自动把 null 当作 0.0，不会空指针
     */
    public static Double sumDoubles(Double... values) {
        double sum = 0.0D;
        if (values == null || values.length == 0) {
            return sum;
        }
        for (Double val : values) {
            sum += Objects.isNull(val) ? 0.0D : val;
        }
        return sum;
    }
}
