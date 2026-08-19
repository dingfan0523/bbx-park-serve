package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.CollectionUtils;
import com.cgnpc.bbxpark.common.utils.DateUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.energy.domain.BranchDevice;
import com.cgnpc.bbxpark.energy.domain.EnergyBranch;
import com.cgnpc.bbxpark.energy.mapper.BranchDeviceRepository;
import com.cgnpc.bbxpark.energy.mapper.EnergyBranchRepository;
import com.cgnpc.bbxpark.energy.mapper.MeterAutoRecordCountRepository;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.service.IScreenWaterService;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.domain.SpaceBasicInfo;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import com.cgnpc.bbxpark.space.mapper.SpaceBasicInfoRepository;
import com.cgnpc.cud.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScreenWaterServiceImpl implements IScreenWaterService {

    @Autowired
    private MeterAutoRecordCountRepository meterAutoRecordCountRepository;

    @Autowired
    private EnergyBranchRepository energyBranchRepository;
    @Autowired
    private BranchDeviceRepository branchDeviceRepository;

    @Autowired
    private SpaceBasicInfoRepository spaceBasicInfoRepository;
    @Autowired
    private IocDeviceRepository iocDeviceRepository;
    @Autowired
    private ParkSpaceRepository parkSpaceRepository;

    //冷水支路集合
    private final static List<Long> COLDBRANCHIDS = Arrays.asList(83L,93L);

    //热水支路集合
    private final static List<Long> HOTDBRANCHIDS = Arrays.asList(84L,94L);

    //科技园空间id
    private final static Long KJYSPACEID = 2338L;

    @Override
    public WaterOverviewModel getWaterOverview(Integer year, Integer month) {
        List<WaterTrendModel> list = getWaterTrend(year,month);
        //遍历获取总用水量
        BigDecimal total = list.stream().map(d->BigDecimal.valueOf((d.getColdWater() == null ? 0.0 : d.getColdWater()) + (d.getHotWater() == null ? 0.0 : d.getHotWater())).setScale(2,RoundingMode.HALF_DOWN)).reduce(BigDecimal.ZERO,BigDecimal::add);
        //查询苍南基地面积
        BigDecimal area = getTotalArea(WebFrameworkUtils.getHeaderTenantId());
        //时间范围内天数
        Date end = DateUtil.getLastTimeBy(year, month);
        end = end.compareTo(new Date()) > 0 ? new Date() : end;
        long days = DateUtil.between(DateUtil.getFirstTimeBy(year, month), end, DateUnit.DAY) + 1;
        return WaterOverviewModel.builder().totalWater(total)
                .dailyWaterIntensity(total.divide(area.multiply(new BigDecimal(days)),2,RoundingMode.HALF_UP)).build();
    }

    @Override
    public List<WaterTrendModel> getWaterTrend(Integer year, Integer month) {
        AssertUtils.isNotEmpty(year, "年份不能为空");
        String timeType  = "year";
        if(ObjectUtil.isNotEmpty(month)){
            timeType = "month";
        }
        switch (timeType) {
            case "month":
                return handleWaterTrend("MM-dd",  DateUtil.beginOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateUtil.endOfMonth(DateUtil.parse(String.format("%04d-%02d-01", year, month))), DateField.DAY_OF_MONTH);
            case "year":
                return handleWaterTrend("MM",  DateUtil.beginOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateUtil.endOfYear(DateUtil.parse(String.format("%04d-01-01", year))), DateField.MONTH);
            default:
                throw new BaseException("参数错误");
        }
    }

    @Override
    public List<WaterAreaCompareModel> getAreaCompare(Long id, Integer year, Integer month) {
        return meterAutoRecordCountRepository.findAreaCompare(id, DeviceReadingTypeEnum.WATER.getCode(),DateUtil.getFirstTimeBy(year,month),DateUtil.getLastTimeBy(year,month),WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<WaterLossRateModel> getLossRate(Long branchId, Integer year, Integer month) {
        //获取当前支路的能耗数据(科技园-冷水/热水、BBX-冷水/热水)
        List<Long> deviceIds = getDeviceIds(Collections.singletonList(branchId));
        if(CollectionUtil.isEmpty(deviceIds)){
            return Collections.emptyList();
        }
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Date start = DateUtil.getFirstTimeBy(year,month);
        Date end = DateUtil.getLastTimeBy(year,month);
        //能耗数据
        List<WaterStatisticsModel> models = meterAutoRecordCountRepository.getMeterAutoRecordStatistics(tenantId, month == null?"MM":"MM-dd", start, end, deviceIds);
        BigDecimal total = models.stream().map(WaterStatisticsModel::getWater).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add);
        if(total.compareTo(BigDecimal.ZERO) <= 0){
            return Collections.emptyList();
        }
        //获取下一层级的支路能耗
        List<WaterAreaCompareModel> children = meterAutoRecordCountRepository.findAreaCompare(branchId, DeviceReadingTypeEnum.WATER.getCode(),DateUtil.getFirstTimeBy(year,month),DateUtil.getLastTimeBy(year,month),WebFrameworkUtils.getHeaderTenantId());
        if(CollectionUtil.isEmpty(children)){
            return Collections.emptyList();
        }
        //流失电量及流失占比
        BigDecimal[] loss = {total,new BigDecimal(100)};
        List<WaterLossRateModel> list = children.stream().map(model->{
            BigDecimal ratio = model.getWater().multiply(new BigDecimal(100)).divide(total,2,RoundingMode.HALF_UP);
            loss[0] = loss[0].subtract(model.getWater());
            loss[1] = loss[1].subtract(ratio);
            return new WaterLossRateModel(model.getBranchName(),model.getWater(),ratio);
        }).collect(Collectors.toList());

        if(loss[0].compareTo(BigDecimal.ZERO) > 0){
            //存在流失水量
            list.add(new WaterLossRateModel("流失水量",loss[0],loss[1]));
        }else if(loss[0].compareTo(BigDecimal.ZERO) == 0){
            //刚好不存在流失水量，那么子支路可能因为除法四舍五入导致百分比不为100%，此时补上剩余百分比
            WaterLossRateModel m =  list.get(list.size() - 1);
            m.setRatio(m.getRatio().add(loss[1]));
        }
        return list;
    }

    @Override
    public List<WaterPerCapitaTrendModel> getPerCapitaTrend(Integer year, Integer month) {
        List<WaterTrendModel> waterTrendModels = getWaterTrend(year, month);
        if(CollectionUtil.isEmpty(waterTrendModels)){
            return Collections.emptyList();
        }
        List<SpaceBasicInfo> spaceBasicInfos = this.getSpace(Collections.singletonList(KJYSPACEID));
        int personNum = CollectionUtil.isEmpty(spaceBasicInfos) ? 1 : (ObjectUtil.isEmpty(spaceBasicInfos.get(0).getCapacity()) ? 1 : spaceBasicInfos.get(0).getCapacity());
        Date now = new Date();
        int now_year = DateUtil.year(now);
        int now_month = DateUtil.month(now) + 1;
        int now_dayOfMonth = DateUtil.dayOfMonth(now);
        return waterTrendModels.stream().map(item->{
            int days = 1;
            if(ObjectUtil.isEmpty(month)){
                int monthInt = Integer.parseInt(item.getDate());
                days = YearMonth.of(year, monthInt).lengthOfMonth();
                if(now_year == year && now_month == monthInt){
                    days = now_dayOfMonth;
                }
            }
            WaterPerCapitaTrendModel model = new WaterPerCapitaTrendModel();
            model.setDate(item.getDate());
            model.setPersonColdWater(item.getColdWater() > 0 ? Math.round(item.getColdWater() / personNum / days * 100) / 100.0 : 0d);
            model.setPersonHotWater(item.getHotWater() > 0 ? Math.round(item.getHotWater() / personNum / days * 100) / 100.0 : 0d);
            return model;
        }).collect(Collectors.toList());

    }

    @Override
    public List<WaterRealtimeFlowRankModel> getRealtimeFlowRank(String type, Integer year, Integer month) {
        //查出冷热水流量计设备的id集合
        List<Long> parentBranchIds = "cold".equals(type) ? COLDBRANCHIDS : HOTDBRANCHIDS;
        Set<Long> branchIds = new HashSet<>();
        parentBranchIds.forEach(id->{
            List<EnergyBranch> branches = energyBranchRepository.listWithChildren(id,WebFrameworkUtils.getHeaderTenantId());
            branchIds.addAll(branches.stream().map(EnergyBranch::getId).collect(Collectors.toSet()));
        });
        List<Long> deviceIds = getDeviceIds(new ArrayList<>(branchIds));
        if(CollectionUtil.isEmpty(deviceIds)){
            return Collections.emptyList();
        }
        List<IocDevice> devices = iocDeviceRepository.selectList(Wrappers.<IocDevice>lambdaQuery()
                .in(IocDevice::getId,deviceIds).ne(IocDevice::getIotDevicePlatform,0));
        if(CollectionUtil.isEmpty(devices)){
            return Collections.emptyList();
        }
        deviceIds = devices.stream().map(IocDevice::getId).collect(Collectors.toList());
        Map<Long,String> deviceNameMap = devices.stream().collect(Collectors.toMap(IocDevice::getId,IocDevice::getDeviceName,(v1,v2)->v2));
        //查询设备能耗排序
        List<WaterRealtimeFlowRankModel> list = meterAutoRecordCountRepository.getRealtimeFlowRank(DateUtil.getFirstTimeBy(year,month),DateUtil.getLastTimeBy(year,month),deviceIds,WebFrameworkUtils.getHeaderTenantId());
        list.forEach(l->l.setDeviceName(deviceNameMap.get(l.getDeviceId())));
        return list;
    }

    private List<WaterTrendModel> handleWaterTrend(String timeType, Date minDay, Date maxDay, DateField dateField){
        List<WaterTrendModel> models = new ArrayList<>();
        List<Long> coldDeviceIds = getDeviceIds(COLDBRANCHIDS);
        List<Long> hotDeviceIds = getDeviceIds(HOTDBRANCHIDS);
        //冷水
        List<WaterStatisticsModel> coldModels = CollectionUtil.isEmpty(coldDeviceIds) ? Collections.emptyList() : meterAutoRecordCountRepository.getMeterAutoRecordStatistics(WebFrameworkUtils.getHeaderTenantId(), timeType, minDay, maxDay, coldDeviceIds);
        //热水
        List<WaterStatisticsModel> hotModels = CollectionUtil.isEmpty(hotDeviceIds) ? Collections.emptyList() : meterAutoRecordCountRepository.getMeterAutoRecordStatistics(WebFrameworkUtils.getHeaderTenantId(), timeType, minDay, maxDay, hotDeviceIds);
        Map<String, BigDecimal> coldModelMap = CollectionUtil.isEmpty(coldModels) ? Collections.emptyMap(): coldModels.stream().collect(Collectors.toMap(WaterStatisticsModel::getDate, WaterStatisticsModel::getWater, (k1, k2)->k1));
        Map<String, BigDecimal> hotModelMap = CollectionUtil.isEmpty(hotModels) ? Collections.emptyMap(): hotModels.stream().collect(Collectors.toMap(WaterStatisticsModel::getDate, WaterStatisticsModel::getWater, (k1,k2)->k1));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            WaterTrendModel model = new WaterTrendModel();
            model.setDate(dayStr);
            model.setColdWater(coldModelMap.getOrDefault(dayStr, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setHotWater(hotModelMap.getOrDefault(dayStr, BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP).doubleValue());
            models.add(model);
        }
        return models;
    }

    private List<Long> getDeviceIds(List<Long> branchIds){
        List<BranchDevice> devices = branchDeviceRepository.selectList(Wrappers.<BranchDevice>lambdaQuery().in(BranchDevice::getBranchId, branchIds)
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), BranchDevice::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(BranchDevice::getDeleted, Status.enabled.getKey()));
        return CollectionUtil.isEmpty(devices) ? Collections.emptyList() : devices.stream().map(BranchDevice::getDeviceId).collect(Collectors.toList());
    }

    private List<SpaceBasicInfo> getSpace(List<Long> spaceIds){
        return spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery().in(SpaceBasicInfo::getSpaceId, spaceIds)
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), SpaceBasicInfo::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(SpaceBasicInfo::getDeleted, Status.enabled.getKey()));
    }

    /**
     * 获取最上级空间的总面积（㎡）
     */
    private BigDecimal getTotalArea(Long tenantId) {
        List<ParkSpace> parkSpaces = parkSpaceRepository.selectList(Wrappers.<ParkSpace>lambdaQuery()
                .eq(ParkSpace::getParentSpaceId, 0L).eq(ParkSpace::getDeleted, Delete.NORMAL.getKey())
                .eq(ParkSpace::getTenantId, tenantId).select(ParkSpace::getId));
        List<Long> spaceIds = parkSpaces.stream().map(ParkSpace::getId).collect(Collectors.toList());
        if (spaceIds.isEmpty()) {
            return BigDecimal.ZERO;
        }
        List<SpaceBasicInfo> spaces = spaceBasicInfoRepository.selectList(Wrappers.<SpaceBasicInfo>lambdaQuery().in(SpaceBasicInfo::getSpaceId, spaceIds));
        return spaces.stream().map(SpaceBasicInfo::getArea).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
