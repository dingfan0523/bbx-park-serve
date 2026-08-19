package com.cgnpc.bbxpark.ioc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestion;
import com.cgnpc.bbxpark.complaint.mapper.ComplaintSuggestionRepository;
import com.cgnpc.bbxpark.device.domain.AlarmInfo;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupRelModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import com.cgnpc.bbxpark.device.mapper.AlarmInfoRepository;
import com.cgnpc.bbxpark.device.mapper.DeviceGroupRelRepository;
import com.cgnpc.bbxpark.device.mapper.IocDeviceRepository;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.RestaurantLinePageParam;
import com.cgnpc.bbxpark.ioc.service.IRestaurantScreenService;
import com.cgnpc.bbxpark.restaurant.domain.*;
import com.cgnpc.bbxpark.restaurant.mapper.*;
import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.settings.domain.ScreenOverview;
import com.cgnpc.bbxpark.settings.mapper.FileRepository;
import com.cgnpc.bbxpark.settings.mapper.ScreenOverviewRepository;
import com.cgnpc.cud.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 餐厅大屏数据服务实现类
 */
@Service
public class RestaurantScreenServiceImpl implements IRestaurantScreenService {

    @Autowired
    private MealLineRepository mealLineRepository;

    @Autowired
    private MealLineTimeRepository mealLineTimeRepository;

    @Autowired
    private MealLinePosRepository mealLinePosRepository;

    @Autowired
    private IocDeviceRepository iocDeviceRepository;

    @Autowired
    private DishesScheduleRepository dishesScheduleRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CompartmentRepository compartmentRepository;

    @Autowired
    private CompartmentReserveRepository compartmentReserveRepository;

    @Autowired
    private DishesEvaluateRepository dishesEvaluateRepository;

    @Autowired
    private ComplaintSuggestionRepository complaintSuggestionRepository;

    @Autowired
    private  RestaurantCardRecordRepository restaurantCardRecordRepository;

    @Autowired
    private RestaurantInboundRecordRepository restaurantInboundRecordRepository;

    @Autowired
    private RestaurantInventoryRecordRepository restaurantInventoryRecordRepository;

    @Autowired
    private RestaurantWasteRecordRepository restaurantWasteRecordRepository;

    @Autowired
    private ScreenOverviewRepository screenOverviewRepository;

    @Autowired
    private AlarmInfoRepository alarmInfoRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DeviceGroupRelRepository deviceGroupRelRepository;

    /**
     * 餐饮业务模块组织部门id
     */
    @Value("${hrcenter.department.canting:50707364}")
    private String cantingDepartmentId;

    @Override
    public RestaurantLineModel getLineDetail(String sslcCode) {
        AssertUtils.isNotEmpty(sslcCode, "楼层编码不能为空");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        RestaurantLineModel model = new RestaurantLineModel();
        List<MealLine> mealLineList = getMealLines(sslcCode, tenantId);
        if(CollectionUtil.isEmpty(mealLineList)){
            return model;
        }
        MealLine line = mealLineList.get(0);
        List<MealLineTime> mealLineTimes = getMealLineTimes(Collections.singletonList(line.getId()), tenantId);
        if(CollectionUtil.isNotEmpty(mealLineTimes)){
            model.setStatus(isCurrentTimeInTimeRange(mealLineTimes, new ArrayList<>()) ? "营业中" : "休息中");
        }
        model.setId(line.getId());
        model.setName(line.getName());
        // 排队状态：空闲、正常、忙碌、爆满
        String[] queueStatus = {"空闲", "正常", "忙碌", "爆满"};
        model.setWorkStatus(queueStatus[1]);
        // 预计等待时间根据状态
        String workStatus = model.getWorkStatus();
        int waitingMinutes;
        switch (workStatus) {
            case "空闲": waitingMinutes = 5; break;
            case "忙碌": waitingMinutes = 20; break;
            case "爆满": waitingMinutes = 30; break;
            case "正常":
            default: waitingMinutes = 10;
        }
        model.setWaitingTime(waitingMinutes + "分钟");
        model.setSslcCode(sslcCode);
        return model;
    }



    @Override
    public RestaurantOverviewModel getOverview() {
        RestaurantOverviewModel model = new RestaurantOverviewModel();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Integer count = restaurantCardRecordRepository.selectCount(Wrappers.<RestaurantCardRecord>lambdaQuery().eq(ObjectUtil.isNotEmpty(tenantId), RestaurantCardRecord::getTenantId, tenantId));
        model.setDiningTotal(count);
        List<RestaurantOverviewModel.videoDevice> videoDevices = new ArrayList<>();
        List<MealLine> mealLineList = getMealLines(null, tenantId);
        if(CollectionUtil.isNotEmpty(mealLineList)){
            List<Long> deviceIds = mealLineList.stream().map(MealLine::getDeviceId).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList());
            List<IocDevice> iocDevices = CollectionUtil.isEmpty(deviceIds) ? Collections.emptyList() : iocDeviceRepository.selectBatchIds(deviceIds);
            for (IocDevice iocDevice : iocDevices) {
                RestaurantOverviewModel.videoDevice device = new RestaurantOverviewModel.videoDevice();
                device.setDeviceName(iocDevice.getDeviceName());
                device.setId(iocDevice.getId());
                device.setIotDeviceDn(iocDevice.getIotDeviceDn());
                videoDevices.add(device);
            }
            model.setVideoDevices(videoDevices);
        }
        return model;
    }

    @Override
    public List<RestaurantLineStatusModel> getDiningLineStatus() {
        List<RestaurantLineStatusModel> modelList = new ArrayList<>();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<MealLine> mealLineList = getMealLines(null, tenantId);
        if(CollectionUtil.isEmpty(mealLineList)){
            return Collections.emptyList();
        }
        List<Long> lineIds = mealLineList.stream().map(MealLine::getId).collect(Collectors.toList());
        List<Long> restaurantIds =  mealLineList.stream().map(MealLine::getRestaurantId).filter(ObjectUtil::isNotEmpty).distinct().collect(Collectors.toList());
        List<Restaurant> restaurants = getRestaurantList(restaurantIds, tenantId);
        Map<Long, String> restaurantMap = CollectionUtil.isEmpty(restaurants) ? Collections.emptyMap() : restaurants.stream().collect(Collectors.toMap(Restaurant::getId, Restaurant::getName));
        List<DishesSchedule> dishesSchedules = getDishesScheduleList(lineIds,  null, tenantId);
        // 根据餐线ID分组并计算每组数量
        Map<Long, Long> mealLineCountMap = CollectionUtil.isEmpty(dishesSchedules) ? Collections.emptyMap() : dishesSchedules.stream()
                .collect(Collectors.groupingBy(DishesSchedule::getMealLineId, Collectors.counting()));
        List<MealLineTime> mealLineTimes = getMealLineTimes(lineIds, tenantId);
        Map<Long, List<MealLineTime>> mealLineTimeMap = CollectionUtil.isEmpty(mealLineTimes) ? Collections.emptyMap() :  mealLineTimes.stream().collect(Collectors.groupingBy(MealLineTime::getMealLineId));
        for (MealLine mealLine : mealLineList) {
            RestaurantLineStatusModel model = new RestaurantLineStatusModel();
            model.setLineName(mealLine.getName());
            model.setRestaurantName(restaurantMap.get(mealLine.getRestaurantId()));
            model.setStatus(isCurrentTimeInTimeRange(mealLineTimeMap.get(mealLine.getId()), new ArrayList<>()) ? "营业中" : "休息中");
            model.setDishCount(Math.toIntExact(mealLineCountMap.getOrDefault(mealLine.getId(), 0L)));
            model.setQueueStatus("正常");
            modelList.add(model);
        }
        return modelList;
    }



    @Override
    public RestaurantCompartmentOverviewModel getCompartmentOverview() {
        RestaurantCompartmentOverviewModel model = new RestaurantCompartmentOverviewModel();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Compartment> compartments = compartmentRepository.selectList(Wrappers.<Compartment>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(tenantId), Compartment::getTenantId, tenantId)
                .eq(Compartment::getDeleted, Status.enabled.getKey())
                .eq(Compartment::getStatus, Status.enabled.getKey())
                .orderByDesc(Compartment::getCreateTime));
        if(CollectionUtil.isEmpty(compartments)){
            return model;
        }
        List<Long> ids = compartments.stream().map(Compartment::getId).collect(Collectors.toList());
        List<CompartmentReserve> compartmentReserveList = compartmentReserveRepository.selectList(Wrappers.<CompartmentReserve>lambdaQuery()
                        .in(CompartmentReserve::getCompartmentId, ids)
                        .ne(CompartmentReserve::getReserveStatus, CompartmentReserveStatusEnum.CANCELLED.getCode())
                        .eq(ObjectUtil.isNotEmpty(tenantId), CompartmentReserve::getTenantId, tenantId)
                        .ge(CompartmentReserve::getReserveStartTime, DateUtil.beginOfDay(new Date()))
                        .le(CompartmentReserve::getReserveStartTime, DateUtil.endOfDay(new Date()))
                        .eq(CompartmentReserve::getDeleted, Status.enabled.getKey())
        );
        List<Long> reserveCompartmentIds = compartmentReserveList.stream().map(CompartmentReserve::getCompartmentId).distinct().collect(Collectors.toList());
        model.setTotalNum(compartments.size());
        model.setReservedNum(CollectionUtil.isEmpty(compartmentReserveList) ? 0 : compartmentReserveList.size());
        List<RestaurantCompartmentOverviewModel.Compartment> compartmentList = new ArrayList<>();
        for (Compartment compartment : compartments) {
            RestaurantCompartmentOverviewModel.Compartment com = new RestaurantCompartmentOverviewModel.Compartment();
            com.setName(compartment.getName());
            com.setImageUrl(compartment.getImageUrl());
            com.setReserved(reserveCompartmentIds.contains(compartment.getId()));
            compartmentList.add(com);
        }
        model.setCompartments(compartmentList);
        return model;
    }

    @Override
    public RestaurantCompartmentTrendModel getCompartmentReservedTrend() {
        RestaurantCompartmentTrendModel models = new RestaurantCompartmentTrendModel();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<RestaurantCompartmentReserveModel> modelList = new ArrayList<>();
        Integer compartmentReserveNum = compartmentReserveRepository.selectCount(Wrappers.<CompartmentReserve>lambdaQuery()
                .ne(CompartmentReserve::getReserveStatus, CompartmentReserveStatusEnum.CANCELLED.getCode())
                .eq(ObjectUtil.isNotEmpty(tenantId), CompartmentReserve::getTenantId, tenantId)
                .eq(CompartmentReserve::getDeleted, Status.enabled.getKey())
        );
        models.setTotalNum(compartmentReserveNum);
        List<RestaurantCompartmentReserveModel> reserveList = compartmentReserveRepository.getCompartmentReserveTrend(tenantId);
        Map<String, RestaurantCompartmentReserveModel> reserveMap = CollectionUtil.isEmpty(reserveList) ? new HashMap<>() : reserveList.stream().collect(Collectors.toMap(RestaurantCompartmentReserveModel::getTime, p->p));
        Date currentDate  = DateUtil.date();
        // 循环生成近30天的日期（从当前日-29天 到 当前日，共30天）
        for (int i = 29; i >= 0; i--) {
            // 偏移日期：currentDate - i天
            Date offsetDate = DateUtil.offsetDay(currentDate, -i);
            // 格式化为MM-dd（比如01-28）
            String formatDate = DateUtil.format(offsetDate, "MM-dd");
            RestaurantCompartmentReserveModel model = new RestaurantCompartmentReserveModel();
            model.setTime(formatDate);
            model.setTotalNum(ObjectUtil.isEmpty(reserveMap.get(formatDate)) ? 0 : reserveMap.get(formatDate).getTotalNum());
            modelList.add(model);
        }
        models.setCompartmentTrends(modelList);
        return models;
    }

    @Override
    public List<RestaurantModel> getList() {
        List<RestaurantModel> models = new ArrayList<>();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Restaurant> restaurants = getRestaurantList(null, tenantId);
        if (CollectionUtil.isEmpty(restaurants)){
            return Collections.emptyList();
        }
        List<MealLine> mealLines = getMealLines(null,  tenantId);
        Map<Long, List<MealLine>> mealLinesMap = CollectionUtil.isEmpty(mealLines) ? Collections.emptyMap() :  mealLines.stream().collect(Collectors.groupingBy(MealLine::getRestaurantId));
        List<MealLineTime> mealLineTimes = getMealLineTimes(null, tenantId);
        Map<Long, List<MealLineTime>> mealLineTimeMap = CollectionUtil.isEmpty(mealLineTimes) ? Collections.emptyMap() :  mealLineTimes.stream().collect(Collectors.groupingBy(MealLineTime::getMealLineId));
        for (Restaurant restaurant : restaurants) {
            RestaurantModel model = new RestaurantModel();
            model.setId(restaurant.getId());
            model.setName(restaurant.getName());
            List<RestaurantLineModel> lines = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(mealLinesMap.get(restaurant.getId()))){
                for (MealLine mealLine : mealLinesMap.get(restaurant.getId())) {
                    List<RestaurantLineTimeModel> times = new ArrayList<>();
                    RestaurantLineModel lineModel = new RestaurantLineModel();
                    lineModel.setId(mealLine.getId());
                    lineModel.setName(mealLine.getName());
                    lineModel.setStatus(isCurrentTimeInTimeRange(mealLineTimeMap.get(mealLine.getId()),times) ? "营业中" : "休息中");
                    lineModel.setTimes(times);
                    lines.add(lineModel);
                }
            }
            model.setLines(lines);
            models.add(model);
        }
        return models;
    }

    @Override
    public List<RestaurantDishesScheduleModel> getLineDishes(Long lineId, String timeType) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<DishesSchedule> dishesSchedules = getDishesScheduleList(Collections.singletonList(lineId), timeType, tenantId);
        DecimalFormat df = new DecimalFormat("0.00");
        return dishesSchedules.stream().map(dishesSchedule -> {
            RestaurantDishesScheduleModel model = new RestaurantDishesScheduleModel();
            BeanUtils.copyProperties(dishesSchedule, model);
            //价格格式化
            if(ObjectUtil.isNotEmpty(dishesSchedule.getPrice())){
                model.setPrice(df.format(dishesSchedule.getPrice()));
            }
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<RestaurantDishesScheduleModel> pageLineDishes(RestaurantLinePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        IPage<DishesSchedule> page = dishesScheduleRepository.selectPage(new Page<>(param.getCurrent(), param.getSize()), Wrappers.<DishesSchedule>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(param.getLineId()),DishesSchedule::getMealLineId, param.getLineId())
                .eq(ObjectUtil.isNotEmpty(tenantId), DishesSchedule::getTenantId, tenantId)
                .eq(ObjectUtil.isNotEmpty(param.getTimeType()), DishesSchedule::getMealTime, param.getTimeType())
                .eq(DishesSchedule::getProductionDate, DateUtils.format(DateUtils.formatYMD(new Date())))
                .eq(DishesSchedule::getDeleted, Status.enabled.getKey())
                .eq(DishesSchedule::getStatus, Status.enabled.getKey())
                .orderByDesc(DishesSchedule::getCreateTime));
        //批量查询餐线信息
        List<MealLine> mealLines = getMealLines(null,  tenantId);
        Map<Long,String> mealLineMap = CollectionUtil.isEmpty(mealLines) ? Collections.emptyMap() : mealLines.stream().collect(Collectors.toMap(MealLine::getId, MealLine::getName));
        //返回结果组装
        DecimalFormat df = new DecimalFormat("0.00");
        List<RestaurantDishesScheduleModel> list = page.getRecords().stream().map(dishesSchedule -> {
            RestaurantDishesScheduleModel model = new RestaurantDishesScheduleModel();
            BeanUtils.copyProperties(dishesSchedule, model);
            //餐线名称
            model.setMealLineName(mealLineMap.get(model.getMealLineId()));
            //价格格式化
            if(ObjectUtil.isNotEmpty(dishesSchedule.getPrice())){
                model.setPrice(df.format(dishesSchedule.getPrice()));
            }
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), list);
    }

    @Override
    public List<RestaurantDishesEvaluateModel> getCompartmentEvaluate() {
        List<RestaurantDishesEvaluateModel> models = new ArrayList<>();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<DishesEvaluate> dishesEvaluates = dishesEvaluateRepository.selectList(Wrappers.<DishesEvaluate>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(tenantId), DishesEvaluate::getTenantId, tenantId)
                .eq(DishesEvaluate::getDeleted, Status.enabled.getKey())
                .orderByDesc(DishesEvaluate::getCreateTime)
                .last("limit 20"));
        if(CollectionUtil.isEmpty(dishesEvaluates)){
            return models;
        }
        for (DishesEvaluate dishesEvaluate : dishesEvaluates) {
            RestaurantDishesEvaluateModel model = new RestaurantDishesEvaluateModel();
            BeanUtils.copyProperties(dishesEvaluate, model);
            model.setStatus(dishesEvaluate.getSatisfaction() > 3);
            models.add(model);
        }
        return models;
    }

    @Override
    public List<RestaurantComplaintSuggestionModel> getComplaintSuggestion() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<ComplaintSuggestion> complaintSuggestions = complaintSuggestionRepository.selectList(Wrappers.<ComplaintSuggestion>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(tenantId), ComplaintSuggestion::getTenantId, tenantId)
                .ne(ComplaintSuggestion::getStatus, ComplaintSuggestionStatusEnum.REPLY.getCode())
                .eq(ComplaintSuggestion::getDeleted, Status.enabled.getKey())
                .eq(ComplaintSuggestion::getDepartmentId, cantingDepartmentId)
                .orderByDesc(ComplaintSuggestion::getCreateTime)
                .last("limit 20"));
        if(CollectionUtil.isEmpty(complaintSuggestions)){
            return Collections.emptyList();
        }
        return BeanUtils.convertListTo(complaintSuggestions, RestaurantComplaintSuggestionModel::new);
    }

    @Override
    public List<RestaurantSupplierModel> getSupplier() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<RestaurantSupplierModel> models = new ArrayList<>();
        RestaurantSupplierModel model = restaurantInboundRecordRepository.getCoreSupplier(tenantId);
        if(ObjectUtil.isEmpty(model)){
            return models;
        }
        List<RestaurantInboundRecord> records = restaurantInboundRecordRepository.selectList(Wrappers.<RestaurantInboundRecord>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(tenantId), RestaurantInboundRecord::getTenantId, tenantId)
                .eq(RestaurantInboundRecord::getDeleted, Status.enabled.getKey())
                .orderByAsc(RestaurantInboundRecord::getInboundTime)
                .last("limit 1"));

        long millisDiff = Math.abs(DateUtil.betweenMs(records.get(0).getInboundTime(), new Date()));
        // 换算为年：1年 = 365天 × 24小时 × 60分钟 × 60秒 × 1000毫秒 = 31536000000毫秒
        BigDecimal yearDiff = new BigDecimal(millisDiff)
                .divide(new BigDecimal(31536000000L), 1, RoundingMode.HALF_UP);
        model.setYears(yearDiff.doubleValue());
        models.add(model);
        return models;
    }

    @Override
    public List<RestaurantInventoryCategoryModel> getInventoryAnalysis() {
        return restaurantInventoryRecordRepository.getInventoryAnalysis(WebFrameworkUtils.getHeaderTenantId());
    }

    @Override
    public List<RestaurantInventoryInboundTrendModel> getInventoryInboundTrend(Date yearDate) {
        AssertUtils.isNotEmpty(yearDate, "时间不能为空");
        List<RestaurantInventoryInboundTrendModel> models = new ArrayList<>();
        Date yearMinTime = DateUtil.beginOfYear(yearDate);
        Date yearMaxTime = DateUtil.endOfYear(yearDate);
        List<RestaurantInventoryInboundModel> trends = restaurantInboundRecordRepository.getInventoryInboundTrend(WebFrameworkUtils.getHeaderTenantId(), yearMinTime, yearMaxTime);
        Set<String> categorys = CollectionUtil.isEmpty(trends) ? Collections.emptySet() : trends.stream().map(RestaurantInventoryInboundModel::getCategory).collect(Collectors.toSet());
        Map<String, List<RestaurantInventoryInboundModel>> trendDayMap = trends.stream().collect(Collectors.groupingBy(RestaurantInventoryInboundModel::getTime));
        for (DateTime day : DateUtil.rangeToList(yearMinTime, yearMaxTime, DateField.MONTH)) {
            String dayStr = DateUtil.format(day, "MM");
            Map<String, Double> trendMap = CollectionUtil.isEmpty(trendDayMap.get(dayStr)) ? Collections.emptyMap() : trendDayMap.get(dayStr).stream().collect(Collectors.toMap(RestaurantInventoryInboundModel::getCategory, RestaurantInventoryInboundModel::getInboundQty, (k1,k2)->k1));
            RestaurantInventoryInboundTrendModel model = new RestaurantInventoryInboundTrendModel();
            model.setTime(formatTime(dayStr, "月"));
            List<RestaurantInventoryCategoryModel> categoryModels = new ArrayList<>();
            for (String category : categorys) {
                RestaurantInventoryCategoryModel categoryModel = new RestaurantInventoryCategoryModel();
                categoryModel.setCategory(category);
                categoryModel.setInboundQty(Math.round(trendMap.getOrDefault(category, 0.0d)  * 100) / 100.0);
                categoryModels.add(categoryModel);
            }
            model.setCategoryModels(categoryModels);
            models.add(model);
        }
        return models;
    }

    @Override
    public RestaurantKitchenStaffModel getKitchenStaffSituation() {
        RestaurantKitchenStaffModel model = new RestaurantKitchenStaffModel();
        List<ScreenOverview> screenOverviews = screenOverviewRepository.selectList(Wrappers.<ScreenOverview>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), ScreenOverview::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .eq(ScreenOverview::getModelType, ModelTypeEnum.CTHC.getCode())
                .eq(ScreenOverview::getDeleted, Status.enabled.getKey()));
        if(ObjectUtil.isEmpty(screenOverviews) || ObjectUtil.isEmpty(screenOverviews.get(0).getModelData())){
           return model;
        }
        RestaurantKitchenStaffTempModel tempModel = JSON.parseObject(screenOverviews.get(0).getModelData(), RestaurantKitchenStaffTempModel.class);

        model.setMaleNum(StringToInteger(tempModel.getMaleNum()));
        model.setFemaleNum(StringToInteger(tempModel.getFemaleNum()));
        model.setHealthCheckupNum(StringToInteger(tempModel.getHealthCheckupNum()));
        model.setTrainingNum(StringToInteger(tempModel.getTrainingNum()));
        model.setTotalStaff(StringToInteger(tempModel.getTotalStaff()));
        model.setExpiringCertNum(StringToInteger(tempModel.getExpiringCertNum()));
        model.setAgeUnder30(StringToInteger(tempModel.getAgeUnder30()));
        model.setAge30To40(StringToInteger(tempModel.getAge30To40()));
        model.setAge40To50(StringToInteger(tempModel.getAge40To50()));
        model.setAgeOver50(StringToInteger(tempModel.getAgeOver50()));
        if(ObjectUtil.isEmpty(model.getTotalStaff()) || model.getTotalStaff() == 0){
            return model;
        }
        model.setMaleRate(ObjectUtil.isEmpty(model.getMaleNum()) ? 0.0d : Math.round((double) model.getMaleNum() / model.getTotalStaff() * 10000) / 100.0);
        model.setFemaleRate(ObjectUtil.isEmpty(model.getFemaleNum()) ? 0.0d : Math.round((double) model.getFemaleNum() / model.getTotalStaff() * 10000) / 100.0);
        model.setHealthCheckupRate(ObjectUtil.isEmpty(model.getHealthCheckupNum()) ? 0.0d : Math.round((double) model.getHealthCheckupNum() / model.getTotalStaff() * 10000) / 100.0);
        model.setTrainingRate(ObjectUtil.isEmpty(model.getTrainingNum()) ? 0.0d : Math.round((double) model.getTrainingNum() / model.getTotalStaff() * 10000) / 100.0);
        model.setAlarmLine(NumberUtil.round(model.getTotalStaff() * 0.2,0).intValue());
        return model;

    }

    @Override
    public List<RestaurantWasteStatisticsModel> getWasteStatistics(String timeType) {
        AssertUtils.isNotEmpty(timeType, "时间类型不能为空");
        Date nowDay = DateUtil.date();
        //获取今年年份
        switch (timeType) {
            case "week":
                return handleWasteStatistics("dd", DateUtil.beginOfWeek(nowDay), DateUtil.endOfWeek(nowDay), DateField.DAY_OF_MONTH, null);
            case "month":
                return handleWasteStatistics("dd",  DateUtil.beginOfMonth(nowDay), DateUtil.endOfMonth(nowDay), DateField.DAY_OF_MONTH, "日");
            case "year":
                return handleWasteStatistics("MM",  DateUtil.beginOfYear(nowDay), DateUtil.endOfYear(nowDay), DateField.MONTH, "月");
            default:
                throw new BaseException("timeType参数错误");
        }
    }

    private List<RestaurantWasteStatisticsModel> handleWasteStatistics(String timeType, Date minDay, Date maxDay, DateField dateField, String unit){
        List<RestaurantWasteStatisticsModel> models = new ArrayList<>();
        List<RestaurantWasteTypeModel> typeModels = restaurantWasteRecordRepository.getWasteStatistics(WebFrameworkUtils.getHeaderTenantId(), timeType, minDay, maxDay);
        Map<String, List<RestaurantWasteTypeModel>> typeModelMap = CollectionUtil.isEmpty(typeModels) ? Collections.emptyMap(): typeModels.stream().collect(Collectors.groupingBy(RestaurantWasteTypeModel::getTime));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, dateField)) {
            String dayStr = DateUtil.format(day, timeType);
            Map<String, Double> typeMap = CollectionUtil.isEmpty(typeModelMap.get(dayStr)) ? Collections.emptyMap() : typeModelMap.get(dayStr).stream().collect(Collectors.toMap(RestaurantWasteTypeModel::getType, RestaurantWasteTypeModel::getQuantity, (k1,k2)->k1));
            RestaurantWasteStatisticsModel model = new RestaurantWasteStatisticsModel();
            model.setTime(ObjectUtil.isEmpty(unit) ? DateUtil.dayOfWeekEnum(day).toChinese() : formatTime(dayStr, unit));
            for (Map.Entry<String, Double> entity : typeMap.entrySet()) {
                if(entity.getKey().contains("餐")){
                    model.setKitchenWaste(entity.getValue());
                }else if(entity.getKey().contains("厨")){
                    model.setFoodWaste(entity.getValue());
                }

            }
            models.add(model);
        }
        return models;
    }

    @Override
    public List<RestaurantDeviceAlarmModel> getNoticeSmartExposure() {
        // 查询与这些设备相关的未结束告警
//        List<Long> deviceIds = Arrays.asList(1L,2L,3L);
        DeviceGroupRelParam relParam = new DeviceGroupRelParam();
        relParam.setGroupCode("ZHCT");
        relParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<DeviceGroupRelModel> relModels = deviceGroupRelRepository.findDevices(relParam);
        if(CollectionUtil.isEmpty(relModels)){
            return Collections.emptyList();
        }
        List<Long> deviceIds = relModels.stream().map(DeviceGroupRelModel::getDeviceId).collect(Collectors.toList());
        List<AlarmInfo> unfinishedAlarms = alarmInfoRepository.selectUnfinishedAlarmsByDeviceIds(deviceIds);
        if(ObjectUtil.isEmpty(unfinishedAlarms)){
            return Collections.emptyList();
        }
        List<AlarmInfo> sortAlarms = unfinishedAlarms.stream()
                .sorted(Comparator.comparing(AlarmInfo::getCreateTime,
                        Comparator.nullsLast(Date::compareTo)).reversed())
                .collect(Collectors.toList());
        return sortAlarms.stream().map(item->{
            RestaurantDeviceAlarmModel model = new RestaurantDeviceAlarmModel();
            model.setDeviceName(item.getAlarmDevice());
            model.setAlarmName(item.getAlarmName());
            model.setCreateTime(item.getCreateTime());
            model.setSpaceName(item.getSpaceAddr());
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public RestaurantRevenueOverview getRevenueOverview() {
        RestaurantRevenueOverview model = restaurantCardRecordRepository.getRevenueOverview(WebFrameworkUtils.getHeaderTenantId());
        model.setTotalRevenue(model.getTotalRevenue() > 0 ? Math.round(model.getTotalRevenue() / 10000 * 100) / 100.0 : 0d);
        model.setTotalConsumerCount(model.getTotalConsumerCount() > 0 ? Math.round(model.getTotalConsumerCount() / 10000 * 10) / 10.0 : 0d);
        return model;
    }

    @Override
    public List<RestaurantConsumptionCountItem> getCountAnalysis(Date yearDate, Long id) {
        AssertUtils.isNotEmpty(yearDate, "时间不能为空");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Date yearMinTime = DateUtil.beginOfYear(yearDate);
        Date yearMaxTime = DateUtil.endOfYear(yearDate);
        List<RestaurantConsumptionCountItem> countList = restaurantCardRecordRepository.getCountAnalysis(tenantId, yearMinTime, yearMaxTime);
        if(CollectionUtil.isEmpty(countList)){
            return Collections.emptyList();
        }
        Map<String, Integer> posCountMap = countList.stream()
                .collect(Collectors.toMap(
                        RestaurantConsumptionCountItem::getName,
                        RestaurantConsumptionCountItem::getCount,
                        (v1, v2) -> v1 // 重复POS取第一个值，根据业务调整
                ));

        Set<String> posList = posCountMap.keySet();
        Map<Long, List<String>> mealLinePosMap = buildMealLinePosMap(posList, tenantId);
        if (CollectionUtil.isEmpty(mealLinePosMap)) {
            return Collections.emptyList();
        }

        // 获取餐线数据，并构建餐厅ID->餐线列表的映射
        Map<Long, List<MealLine>> restaurantMealLineMap = buildRestaurantMealLineMap(tenantId);
        if (CollectionUtil.isEmpty(restaurantMealLineMap)) {
            return Collections.emptyList();
        }

        // 分场景处理：id为空查餐厅，id不为空查餐线
        if (ObjectUtil.isEmpty(id)) {
            return buildRestaurantCountModel(restaurantMealLineMap, mealLinePosMap, posCountMap);
        } else {
            return buildMealLineCountModel(id, restaurantMealLineMap, mealLinePosMap, posCountMap);
        }
    }

    @Override
    public List<RestaurantConsumptionAbilityTrend> getConsumptionAbilityTrend(Date monthDate) {
        AssertUtils.isNotEmpty(monthDate, "时间不能为空");
        Date minDay = DateUtil.beginOfMonth(monthDate);
        Date maxDay = DateUtil.endOfMonth(monthDate);
        List<RestaurantConsumptionAbilityTrend> models = new ArrayList<>();
        List<RestaurantConsumptionAbilityModel> abilityModels = restaurantCardRecordRepository.getConsumptionAbility(WebFrameworkUtils.getHeaderTenantId(), minDay, maxDay);
        Map<String, List<RestaurantConsumptionAbilityModel>> abilityModelMap = CollectionUtil.isEmpty(abilityModels) ? Collections.emptyMap(): abilityModels.stream().collect(Collectors.groupingBy(RestaurantConsumptionAbilityModel::getTime));
        for (DateTime day : DateUtil.rangeToList(minDay, maxDay, DateField.DAY_OF_MONTH)) {
            String dayStr = DateUtil.format(day, "dd");
            List<RestaurantConsumptionAbilityModel> abilityList = CollectionUtil.isEmpty(abilityModelMap.get(dayStr)) ? Collections.emptyList(): abilityModelMap.get(dayStr);
            RestaurantConsumptionAbilityTrend model = new RestaurantConsumptionAbilityTrend();
            model.setTime(formatTime(dayStr, "日"));
            double totalAmount = abilityList.stream()
                    .mapToDouble(item -> item.getAmount() == null ? 0.0 : item.getAmount())
                    // 流式累加求和
                    .sum();

            double avgAmount = 0.0;
            if (!abilityList.isEmpty()) {
                avgAmount = totalAmount / abilityList.size();
                // 保留2位小数（四舍五入，解决浮点数精度问题）
                avgAmount = Math.round(avgAmount * 100) / 100.0;
            }
            model.setTotalAmount(Math.round(totalAmount * 100) / 100.0);
            model.setDailyAvgAmount(avgAmount);
            models.add(model);
        }
        return models;
    }

    @Override
    public RestaurantConsumptionBehavior getConsumptionBehavior(Date monthDate) {
        RestaurantConsumptionBehavior model = new RestaurantConsumptionBehavior();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        RestaurantConsumptionBehaviorModel consumptionModel = restaurantCardRecordRepository.getConsumptionBehavior(tenantId);
        if(ObjectUtil.isEmpty(consumptionModel)){
            return model;
        }
        Double diffDay = restaurantCardRecordRepository.getDiffDay(tenantId, null) + 1;
        Double totalAccountBalance = restaurantCardRecordRepository.getTotalAccountBalance(tenantId);
        model.setActiveCardCount(consumptionModel.getDistinctCardCount());
        model.setDailyAmount(consumptionModel.getTotalTradeAmount() > 0 ? Math.round(consumptionModel.getTotalTradeAmount() / diffDay * 100) / 100.0 : 0d);
        model.setActiveCardCount(consumptionModel.getDistinctCardCount());
        model.setAvgCardBalance(totalAccountBalance != null && totalAccountBalance > 0 ? Math.round(totalAccountBalance / consumptionModel.getDistinctCardCount() * 100) / 100.0 : 0d);
        model.setDailyConsumerCount(consumptionModel.getTotalConsumeCount() > 0 ? Math.round(consumptionModel.getTotalConsumeCount() / diffDay * 10) / 10.0 : 0d);
        return model;
    }

    @Override
    public List<RestaurantEmployeeConsumption> getEmployeeConsumptionList() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<File> files = fileRepository.selectList(Wrappers.<File>lambdaQuery()
                .eq(File::getType, FileTypeEnum.RESTAURANTCARD.getValue())
                .eq(ObjectUtil.isNotEmpty(tenantId), File::getTenantId, tenantId)
                .eq(File::getDeleted, Status.enabled.getKey())
                .orderByDesc(File::getCreateTime)
                .last("limit 1"));
        if(CollectionUtil.isEmpty(files)){
            return Collections.emptyList();
        }
        Long fileId = files.get(0).getId();
        // 余额小于10的卡号
        List<String> lowBalanceCardNos = restaurantCardRecordRepository.selectLowBalanceCardNos(tenantId, fileId);
        // 查询消费行为数据并构建映射（卡号->消费行为）
        List<RestaurantConsumptionBehaviorModel> consumptionBehaviorList = restaurantCardRecordRepository.getConsumptionBehaviorList(tenantId, fileId);
        Map<String, RestaurantConsumptionBehaviorModel> cardConsumptionMap = CollectionUtil.isEmpty(consumptionBehaviorList) ? Collections.emptyMap() : consumptionBehaviorList.stream().collect(Collectors.toMap(RestaurantConsumptionBehaviorModel::getCardNo, Function.identity(),(k1,k2)->k1));
        // 计算消费天数（默认0，避免除零异常）
        Double consumeDays = restaurantCardRecordRepository.getDiffDay(tenantId, fileId) + 1;
        // 日均消费低于1元的卡号
        List<String> lowConsumptionCardNos = CollectionUtil.isEmpty(consumptionBehaviorList) ? Collections.emptyList() : consumptionBehaviorList.stream().filter(p->p.getTotalTradeAmount() < consumeDays).map(RestaurantConsumptionBehaviorModel::getCardNo).collect(Collectors.toList());
        // 合并异常卡号并去重
        Set<String> abnormalCardNos = new HashSet<>();
        abnormalCardNos.addAll(lowBalanceCardNos);
        abnormalCardNos.addAll(lowConsumptionCardNos);
        if (abnormalCardNos.isEmpty()) {
            return Collections.emptyList();
        }
        // 查询卡号对应的员工姓名
        Map<String, String> cardNameMap = getCardNameMap(tenantId, fileId, abnormalCardNos);
        List<RestaurantEmployeeConsumption> models = new ArrayList<>();
        // 处理余额不足的卡片
        buildConsumptionModels(lowBalanceCardNos, "卡余额不足", cardConsumptionMap, cardNameMap, consumeDays, models);
        // 处理日均消费过低的卡片
        buildConsumptionModels(lowConsumptionCardNos, "日均消费金额低", cardConsumptionMap, cardNameMap, consumeDays, models);
        return models;
    }

    /**
     * 查询卡号对应的员工姓名映射
     */
    private Map<String, String> getCardNameMap(Long tenantId, Long fileId, Set<String> cardNos) {
        List<RestaurantCardRecord> cardRecords = restaurantCardRecordRepository.selectList(Wrappers.<RestaurantCardRecord>lambdaQuery()
                .select(RestaurantCardRecord::getCardNo, RestaurantCardRecord::getName)
                .in(RestaurantCardRecord::getCardNo, cardNos)
                .eq(ObjectUtil.isNotEmpty(tenantId), RestaurantCardRecord::getTenantId, tenantId)
                .eq(RestaurantCardRecord::getDeleted, Status.enabled.getKey())
                .eq(RestaurantCardRecord::getFileId, fileId));

        if (CollectionUtil.isEmpty(cardRecords)) {
            return Collections.emptyMap();
        }
        return cardRecords.stream()
                .collect(Collectors.toMap(
                        RestaurantCardRecord::getCardNo,
                        RestaurantCardRecord::getName,
                        (k1, k2) -> k1 // 重复卡号保留第一个姓名
                ));
    }

    /**
     * 构建员工消费异常模型列表
     */
    private void buildConsumptionModels(
            List<String> cardNos,
            String careItem,
            Map<String, RestaurantConsumptionBehaviorModel> consumptionMap,
            Map<String, String> cardNameMap,
            Double diffDay,
            List<RestaurantEmployeeConsumption> resultList) {

        if (CollectionUtil.isEmpty(cardNos)) {
            return;
        }
        int num = 1;
        for (String cardNo : cardNos) {
            // 获取消费行为数据（空值防护）
            RestaurantConsumptionBehaviorModel consumption = consumptionMap.getOrDefault(cardNo, new RestaurantConsumptionBehaviorModel());
            // 构建异常模型
            RestaurantEmployeeConsumption model = new RestaurantEmployeeConsumption();
            model.setCareItem(careItem);
            // 姓名脱敏（空值防护）
            model.setStaffName(desensitizeName(cardNameMap.getOrDefault(cardNo, "")));
            // 计算日均消费金额（保留2位小数，处理除零异常）
            model.setDailyAmount(consumption.getTotalTradeAmount() > 0 ? Math.round(consumption.getTotalTradeAmount() / diffDay * 100) / 100.0 : 0d);
            // 计算日均刷卡次数（保留1位小数，处理除零异常）
            model.setDailySwipeCount(consumption.getTotalConsumeCount() > 0 ? Math.round(consumption.getTotalConsumeCount() / diffDay * 10) / 10.0 : 0d);
            if(num > 10){
                break;
            }
            resultList.add(model);
            num++;
        }
    }

    @Override
    public List<RestaurantConsumptionShareItem> getConsumptionShareAnalysis(Date yearDate, Long id) {
        AssertUtils.isNotEmpty(yearDate, "时间不能为空");
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Date yearMinTime = DateUtil.beginOfYear(yearDate);
        Date yearMaxTime = DateUtil.endOfYear(yearDate);
        List<RestaurantConsumptionShareItem> countList = restaurantCardRecordRepository.getConsumptionShareAnalysis(tenantId, yearMinTime, yearMaxTime);
        if(CollectionUtil.isEmpty(countList)){
            return Collections.emptyList();
        }
        Map<String, Double> posAmountMap = countList.stream()
                .collect(Collectors.toMap(
                        RestaurantConsumptionShareItem::getName,
                        RestaurantConsumptionShareItem::getAmount,
                        (v1, v2) -> v1 // 重复POS取第一个值，根据业务调整
                ));

        Set<String> posList = posAmountMap.keySet();
        Map<Long, List<String>> mealLinePosMap = buildMealLinePosMap(posList, tenantId);
        if (CollectionUtil.isEmpty(mealLinePosMap)) {
            return Collections.emptyList();
        }

        // 获取餐线数据，并构建餐厅ID->餐线列表的映射
        Map<Long, List<MealLine>> restaurantMealLineMap = buildRestaurantMealLineMap(tenantId);
        if (CollectionUtil.isEmpty(restaurantMealLineMap)) {
            return Collections.emptyList();
        }
        List<RestaurantConsumptionShareItem> modles;
        // 分场景处理：id为空查餐厅，id不为空查餐线
        if (ObjectUtil.isEmpty(id)) {
            modles = buildRestaurantAmountModel(restaurantMealLineMap, mealLinePosMap, posAmountMap);
        } else {
            modles = buildMealLineAmountModel(id, restaurantMealLineMap, mealLinePosMap, posAmountMap);
        }
        fillPercent(modles);
        return modles;
    }

    private List<MealLineTime> getMealLineTimes(List<Long> lineIds, Long tenantId){
        return  mealLineTimeRepository.selectList(Wrappers.<MealLineTime>lambdaQuery()
                .in(CollectionUtil.isNotEmpty(lineIds), MealLineTime::getMealLineId, lineIds)
                .eq(ObjectUtil.isNotEmpty(tenantId), MealLineTime::getTenantId, tenantId)
                .eq(MealLineTime::getDeleted, Status.enabled.getKey()));
    }
    private List<MealLine> getMealLines(String sslcCode, Long tenantId){
        return  mealLineRepository.selectList(Wrappers.<MealLine>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(sslcCode),MealLine::getSslcCode, sslcCode)
                .eq(ObjectUtil.isNotEmpty(tenantId), MealLine::getTenantId, tenantId)
                .eq(MealLine::getDeleted, Status.enabled.getKey())
                .eq(MealLine::getStatus, Status.enabled.getKey())
                .orderByDesc(MealLine::getCreateTime));
    }

    private List<Restaurant> getRestaurantList(List<Long> restaurantIds, Long tenantId){
        return restaurantRepository.selectList(Wrappers.<Restaurant>lambdaQuery()
                .in(CollectionUtil.isNotEmpty(restaurantIds), Restaurant::getId, restaurantIds)
                .eq(ObjectUtil.isNotEmpty(tenantId), Restaurant::getTenantId, tenantId)
                .eq(Restaurant::getStatus, Status.enabled.getKey())
                .eq(Restaurant::getDeleted, Status.enabled.getKey())
                .orderByDesc(Restaurant::getCreateTime));
    }

    private List<DishesSchedule> getDishesScheduleList(List<Long> lineIds, String type, Long tenantId){
        return dishesScheduleRepository.selectList(Wrappers.<DishesSchedule>lambdaQuery()
                .in(CollectionUtil.isNotEmpty(lineIds),DishesSchedule::getMealLineId, lineIds)
                .eq(ObjectUtil.isNotEmpty(tenantId), DishesSchedule::getTenantId, tenantId)
                .eq(ObjectUtil.isNotEmpty(type), DishesSchedule::getMealTime, type)
                .eq(DishesSchedule::getDeleted, Status.enabled.getKey())
                .eq(DishesSchedule::getStatus, Status.enabled.getKey())
                .orderByDesc(DishesSchedule::getCreateTime));
    }

    /**
     * 构建餐线ID -> POS列表的映射（缓存POS与餐线的关联关系）
     * @param posList POS集合
     * @param tenantId 租户ID
     * @return 餐线ID->POS列表
     */
    private Map<Long, List<String>> buildMealLinePosMap(Set<String> posList, Long tenantId) {
        if (CollectionUtil.isEmpty(posList)) {
            return Collections.emptyMap();
        }

        List<MealLinePos> linePosList = mealLinePosRepository.selectList(
                Wrappers.<MealLinePos>lambdaQuery()
                        .in(MealLinePos::getPos, posList)
                        .eq(ObjectUtil.isNotEmpty(tenantId), MealLinePos::getTenantId, tenantId)
                        .eq(MealLinePos::getDeleted, Status.enabled.getKey())
        );

        // 转换为：餐线ID -> POS列表
        return linePosList.stream()
                .filter(pos -> ObjectUtil.isNotEmpty(pos.getMealLineId()) && ObjectUtil.isNotEmpty(pos.getPos()))
                .collect(Collectors.groupingBy(
                        MealLinePos::getMealLineId,
                        Collectors.mapping(MealLinePos::getPos, Collectors.toList())
                ));
    }
    /**
     * 构建餐厅ID -> 餐线列表的映射
     * @param tenantId 租户ID
     * @return 餐厅ID->餐线列表
     */
    private Map<Long, List<MealLine>> buildRestaurantMealLineMap(Long tenantId) {
        List<MealLine> mealLines = mealLineRepository.selectList(
                Wrappers.<MealLine>lambdaQuery()
                        .eq(ObjectUtil.isNotEmpty(tenantId), MealLine::getTenantId, tenantId)
                        .eq(MealLine::getDeleted, Status.enabled.getKey())
        );

        return mealLines.stream()
                .filter(line -> ObjectUtil.isNotEmpty(line.getRestaurantId()))
                .collect(Collectors.groupingBy(MealLine::getRestaurantId));
    }

    /**
     * 构建餐厅维度的统计模型（id为空时）
     * @param restaurantMealLineMap 餐厅->餐线映射
     * @param mealLinePosMap 餐线->POS映射
     * @param posCountMap POS->消费次数映射
     * @return 餐厅统计列表
     */
    private List<RestaurantConsumptionCountItem> buildRestaurantCountModel(
            Map<Long, List<MealLine>> restaurantMealLineMap,
            Map<Long, List<String>> mealLinePosMap,
            Map<String, Integer> posCountMap) {

        // 获取所有餐厅列表（根据实际业务调整，此处假设从餐线映射中提取）
        List<Restaurant> restaurants = getRestaurantList(null, WebFrameworkUtils.getHeaderTenantId());
        if (CollectionUtil.isEmpty(restaurants)) {
            return Collections.emptyList();
        }

        return restaurants.stream()
                .map(restaurant -> {
                    RestaurantConsumptionCountItem model = new RestaurantConsumptionCountItem();
                    model.setName(restaurant.getName());
                    model.setId(restaurant.getId());
                    model.setType("restaurant");

                    // 计算该餐厅下所有餐线的总消费次数
                    int totalCount = calculateTotalCount(
                            restaurantMealLineMap.getOrDefault(restaurant.getId(), Collections.emptyList()),
                            mealLinePosMap,
                            posCountMap
                    );
                    model.setCount(totalCount);
                    return model;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建餐厅维度的统计模型（id为空时）
     * @param restaurantMealLineMap 餐厅->餐线映射
     * @param mealLinePosMap 餐线->POS映射
     * @param posAmountMap POS->消费金额映射
     * @return 餐厅统计列表
     */
    private List<RestaurantConsumptionShareItem> buildRestaurantAmountModel(
            Map<Long, List<MealLine>> restaurantMealLineMap,
            Map<Long, List<String>> mealLinePosMap,
            Map<String, Double> posAmountMap) {

        // 获取所有餐厅列表（根据实际业务调整，此处假设从餐线映射中提取）
        List<Restaurant> restaurants = getRestaurantList(null, WebFrameworkUtils.getHeaderTenantId());
        if (CollectionUtil.isEmpty(restaurants)) {
            return Collections.emptyList();
        }

        return restaurants.stream()
                .map(restaurant -> {
                    RestaurantConsumptionShareItem model = new RestaurantConsumptionShareItem();
                    model.setName(restaurant.getName());
                    model.setId(restaurant.getId());
                    model.setType("restaurant");

                    // 计算该餐厅下所有餐线的总消费金额
                    Double totalAmount = calculateTotalAmount(
                            restaurantMealLineMap.getOrDefault(restaurant.getId(), Collections.emptyList()),
                            mealLinePosMap,
                            posAmountMap
                    );
                    model.setAmount(totalAmount);
                    return model;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建餐线维度的统计模型（id不为空时）
     * @param restaurantId 餐厅ID
     * @param restaurantMealLineMap 餐厅->餐线映射
     * @param mealLinePosMap 餐线->POS映射
     * @param posCountMap POS->消费次数映射
     * @return 餐线统计列表
     */
    private List<RestaurantConsumptionCountItem> buildMealLineCountModel(
            Long restaurantId,
            Map<Long, List<MealLine>> restaurantMealLineMap,
            Map<Long, List<String>> mealLinePosMap,
            Map<String, Integer> posCountMap) {

        // 获取该餐厅下的所有餐线
        List<MealLine> mealLines = restaurantMealLineMap.getOrDefault(restaurantId, Collections.emptyList());
        if (CollectionUtil.isEmpty(mealLines)) {
            return Collections.emptyList();
        }

        return mealLines.stream()
                .map(line -> {
                    RestaurantConsumptionCountItem model = new RestaurantConsumptionCountItem();
                    model.setName(line.getName());
                    model.setId(line.getId());
                    model.setType("mealLine");

                    // 计算该餐线下所有POS的总消费次数
                    int totalCount = calculateTotalCount(
                            Collections.singletonList(line),
                            mealLinePosMap,
                            posCountMap
                    );
                    model.setCount(totalCount);
                    return model;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建餐线维度的统计模型（id不为空时）
     * @param restaurantId 餐厅ID
     * @param restaurantMealLineMap 餐厅->餐线映射
     * @param mealLinePosMap 餐线->POS映射
     * @param posAmountMap POS->消费金额映射
     * @return 餐线统计列表
     */
    private List<RestaurantConsumptionShareItem> buildMealLineAmountModel(
            Long restaurantId,
            Map<Long, List<MealLine>> restaurantMealLineMap,
            Map<Long, List<String>> mealLinePosMap,
            Map<String, Double> posAmountMap) {

        // 获取该餐厅下的所有餐线
        List<MealLine> mealLines = restaurantMealLineMap.getOrDefault(restaurantId, Collections.emptyList());
        if (CollectionUtil.isEmpty(mealLines)) {
            return Collections.emptyList();
        }

        return mealLines.stream()
                .map(line -> {
                    RestaurantConsumptionShareItem model = new RestaurantConsumptionShareItem();
                    model.setName(line.getName());
                    model.setId(line.getId());
                    model.setType("mealLine");

                    // 计算该餐线下所有POS的总消费次数
                    Double totalCount = calculateTotalAmount(
                            Collections.singletonList(line),
                            mealLinePosMap,
                            posAmountMap
                    );
                    model.setAmount(totalCount);
                    return model;
                })
                .collect(Collectors.toList());
    }

    /**
     * 通用方法：计算餐线列表对应的总消费次数（提取重复逻辑）
     * @param mealLines 餐线列表
     * @param mealLinePosMap 餐线->POS映射
     * @param posCountMap POS->消费次数映射
     * @return 总消费次数
     */
    private int calculateTotalCount(List<MealLine> mealLines,
                                    Map<Long, List<String>> mealLinePosMap,
                                    Map<String, Integer> posCountMap) {
        return mealLines.stream()
                // 遍历餐线，获取对应的POS列表
                .map(line -> mealLinePosMap.getOrDefault(line.getId(), Collections.emptyList()))
                // 扁平化POS列表
                .flatMap(Collection::stream)
                // 累加每个POS的消费次数（无则为0）
                .mapToInt(pos -> posCountMap.getOrDefault(pos, 0))
                .sum();
    }

    /**
     * 通用方法：计算餐线列表对应的总消费次数（提取重复逻辑）
     * @param mealLines 餐线列表
     * @param mealLinePosMap 餐线->POS映射
     * @param posAmountMap POS->消费次数映射
     * @return 总消费次数
     */
    private Double calculateTotalAmount(List<MealLine> mealLines,
                                    Map<Long, List<String>> mealLinePosMap,
                                    Map<String, Double> posAmountMap) {
        return mealLines.stream()
                // 遍历餐线，获取对应的POS列表
                .map(line -> mealLinePosMap.getOrDefault(line.getId(), Collections.emptyList()))
                // 扁平化POS列表
                .flatMap(Collection::stream)
                // 累加每个POS的消费次数（无则为0）
                .mapToDouble(pos -> posAmountMap.getOrDefault(pos, 0d))
                .sum();
    }
    /**
     * 计算总金额& 填充百分比
     */
    private void fillPercent(List<RestaurantConsumptionShareItem> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        // 总金额
        double totalAmount = list.stream()
                .mapToDouble(RestaurantConsumptionShareItem::getAmount)
                .sum();

        // 计算百分比，保留2位小数
        list.forEach(item -> {
            double percent = 0.0;
            if (totalAmount > 0) {
                // 四舍五入保留2位
                percent = Math.round(item.getAmount() / totalAmount * 10000) / 100.0;
            }
            item.setSharePercent(percent);
        });
    }

    /**
     * 判断当前时间是否在时间段列表内
     * @param timeList 时间段列表
     * @return true-在营业时间内，false-不在营业时间内
     */
    private Boolean isCurrentTimeInTimeRange(List<MealLineTime> timeList, List<RestaurantLineTimeModel> times) {
        if (CollectionUtil.isEmpty(timeList)) {
            return false;
        }
        // 获取当前时间的 HH:mm 格式
        String currentTimeStr = DateUtil.format(new Date(), "HH:mm");
        Date currentTime = DateUtils.parse("2024-07-22 " + currentTimeStr, "yyyy-MM-dd HH:mm");
        boolean flag = false;
        for (MealLineTime timeParam : timeList) {
            RestaurantLineTimeModel timeModel = new RestaurantLineTimeModel();
            timeModel.setEndTime(timeParam.getEndTime());
            timeModel.setStartTime(timeParam.getStartTime());
            timeModel.setId(timeParam.getId());
            timeModel.setType(timeParam.getType());
            timeModel.setLineId(timeParam.getMealLineId());
            timeModel.setSelected(false);
            Date startTime = DateUtils.parse("2024-07-22 " + timeParam.getStartTime(), "yyyy-MM-dd HH:mm");
            Date endTime = DateUtils.parse("2024-07-22 " + timeParam.getEndTime(), "yyyy-MM-dd HH:mm");
            // 判断当前时间是否在该时间段内（包含边界）
            if (DateUtil.compare(currentTime, startTime) >= 0 && DateUtil.compare(currentTime, endTime) <= 0) {
                timeModel.setSelected(true);
                flag = true;
            }
            times.add(timeModel);
        }
        return flag;
    }

    /**
     * 去掉前置0
     * @param numStr 输入字符串（如"01"/"09"/"12"）
     * @return 处理后的字符串
     */
    private String formatTime(String numStr, String unit) {
        // 校验空值/非数字
        if (numStr == null || numStr.trim().isEmpty()) {
            return "";
        }
        // 去掉前置0（转为数字再转回字符串）
        String cleanStr = String.valueOf(Integer.parseInt(numStr.trim()));
        return cleanStr + unit;
    }

    /**
     * 字符串转数字
     * @param numberStr
     * @return
     */
    private Integer StringToInteger(String numberStr) {
        numberStr = numberStr.trim();
        if (numberStr.isEmpty() || "-".equals(numberStr) || "无".equals(numberStr) || "0.0".equals(numberStr)) {
            return 0;
        }
        try {
            // 移除千分位逗号、非数字字符（如次数后的"次"）
            numberStr = numberStr.replaceAll(",", "") // 去掉千分位：1,234 → 1234
                    .replaceAll("[^0-9.]", ""); // 只保留数字和小数点
            // 处理小数（如5.9 → 5，或四舍五入为6，按需调整）
            if (numberStr.contains(".")) {
                // 方案1：直接取整（舍弃小数部分）
                numberStr = numberStr.split("\\.")[0];
                // 方案2：四舍五入（如需保留小数后四舍五入，打开下面注释）
                // double doubleValue = Double.parseDouble(cleanValue);
                // return (int) Math.round(doubleValue);
            }
            // 转换为Integer
            return Integer.parseInt(numberStr);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 员工姓名脱敏核心方法
     * @param fullName 原始姓名（可能为null/空/单字/多字）
     * @return 脱敏后的姓名
     */
    private static String desensitizeName(String fullName) {
        // 1. 空值处理（原生Java）
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        // 2. 去除首尾空格
        String name = fullName.trim();
        int nameLength = name.length();

        // 3. 分支处理
        if (nameLength == 1) {
            return name;
        } else if (nameLength == 2) {
            return name.substring(0, 1) + "*";
        } else {
            String firstChar = name.substring(0, 1);
            String lastChar = name.substring(nameLength - 1);
            // 原生Java生成指定数量的*
            StringBuilder middleStars = new StringBuilder();
            for (int i = 0; i < nameLength - 2; i++) {
                middleStars.append("*");
            }
            return firstChar + middleStars + lastChar;
        }
    }
    public static void main(String[] args) {
        // 1. 定义输入时间字符串
        String inputTimeStr = "2026-03-10";
        // 2. 转换为Date类型（Hutool自动识别常见格式）
        Date inputDate = DateUtil.parse(inputTimeStr);

        // 3. 计算该年份的最小时间（2025-01-01 00:00:00）
        Date yearMinTime = DateUtil.beginOfYear(inputDate);
        // 4. 计算该年份的最大时间（2025-12-31 23:59:59）
        Date yearMaxTime = DateUtil.endOfYear(inputDate);

        // 3. 计算该月份的最小时间（2025-01-01 00:00:00）
        Date monthMinTime = DateUtil.beginOfMonth(inputDate);
        // 4. 计算该月份的最大时间（2025-12-31 23:59:59）
        Date montMaxTime = DateUtil.endOfMonth(inputDate);

        // 3. 计算该周的最小时间（2025-01-01 00:00:00）
        Date weekMinTime = DateUtil.beginOfWeek(inputDate);
        // 4. 计算该月份的最大时间（2025-12-31 23:59:59）
        Date weekMaxTime = DateUtil.endOfWeek(inputDate);

        // 5. 格式化输出（便于查看）
        System.out.println("输入时间：" + DateUtil.format(inputDate, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("当年最小时间：" + DateUtil.format(yearMinTime, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("当年最大时间：" + DateUtil.format(yearMaxTime, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("当月最小时间：" + DateUtil.format(monthMinTime, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("当月最大时间：" + DateUtil.format(montMaxTime, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("当周最小时间：" + DateUtil.format(weekMinTime, "yyyy-MM-dd HH:mm:ss"));
        System.out.println("当周最大时间：" + DateUtil.format(weekMaxTime, "yyyy-MM-dd HH:mm:ss"));
        for (DateTime day : DateUtil.rangeToList(yearMinTime, yearMaxTime, DateField.MONTH)) {
            String dayStr = DateUtil.format(day, "mm");
            System.out.println(dayStr);
            System.out.println("时间月：" + DateUtil.format(day, "yyyy-MM-dd HH:mm:ss"));
        }
        for (DateTime day : DateUtil.rangeToList(monthMinTime, montMaxTime, DateField.DAY_OF_MONTH)) {
            String dayStr = DateUtil.format(day, "dd");
            System.out.println("时间日：" + DateUtil.format(day, "yyyy-MM-dd HH:mm:ss"));
            System.out.println(dayStr);
        }
        for (DateTime day : DateUtil.rangeToList(weekMinTime, weekMaxTime, DateField.DAY_OF_WEEK)) {
            int weekdayNum = DateUtil.dayOfWeek(day);
            String weekdayCn = DateUtil.dayOfWeekEnum(day).toChinese();
            System.out.println("输入时间：" + DateUtil.format(day, "yyyy-MM-dd"));
            System.out.println("周几（数字）：" + weekdayNum); // 输出 2
            System.out.println("周几（中文）：" + weekdayCn);   // 输出 周二
        }
    }

}
