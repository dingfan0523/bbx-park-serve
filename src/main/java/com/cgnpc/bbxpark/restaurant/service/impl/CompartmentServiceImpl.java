
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.Constants;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.CompartmentReserveStatusEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.restaurant.domain.*;
import com.cgnpc.bbxpark.restaurant.dto.model.*;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentRepository;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentReserveRepository;
import com.cgnpc.bbxpark.restaurant.service.*;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CompartmentServiceImpl extends ServiceImpl<CompartmentRepository, Compartment> implements ICompartmentService {
    @Autowired
    private IRestaurantService restaurantService;
    @Autowired
    private IParkSpaceService parkSpaceService;
    @Autowired
    private ICompartmentTimeService compartmentTimeService;
    @Autowired
    private ICompartmentComboService compartmentComboService;
    @Autowired
    private ICompartmentDeviceService compartmentDeviceService;
    @Autowired
    private ICompartmentEvaluateService compartmentEvaluateService;
    @Autowired
    private IComboService comboService;

    @Autowired
    private IIocDeviceService iocDeviceService;
    @Autowired
    private CompartmentReserveRepository compartmentReserveRepository;


    @Override
    public IPage<CompartmentModel> pageResult(CompartmentPageParam param) {
        // 分页参数
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        LambdaQueryWrapper<Compartment> wrapper = Wrappers.<Compartment>lambdaQuery()
                .eq(ObjectUtil.isNotNull(tenantId), Compartment::getTenantId, tenantId)
                .eq(ObjectUtil.isNotNull(param.getRestaurantId()), Compartment::getRestaurantId, param.getRestaurantId())
                .eq(ObjectUtil.isNotNull(param.getStatus()), Compartment::getStatus, param.getStatus())
                .like(StrUtil.isNotBlank(param.getName()), Compartment::getName, param.getName())
                .orderByDesc(Compartment::getCreateTime);
        // 分页查询
        IPage<Compartment> iPage = this.page(new Page<>(param.getCurrent(), param.getSize()), wrapper);
        // Model 转换
        List<CompartmentModel> compartmentModels = BeanUtils.convertListTo(iPage.getRecords(), CompartmentModel::new);

        // 实时查询空间全路径
        List<Long> spaceIdList = compartmentModels.stream().map(CompartmentModel::getSpaceId).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> spaceFullModelMap = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());
        // 实时查询餐厅名称
        Map<Long, String> restaurantMap = new HashMap<>();
        List<Long> restaurantIdList = compartmentModels.stream().map(CompartmentModel::getRestaurantId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(restaurantIdList)) {
            restaurantMap = restaurantService.listByIds(restaurantIdList).stream().collect(Collectors.toMap(Restaurant::getId, Restaurant::getName));
        }
        // 实时设施查询
        List<Long> idList = compartmentModels.stream().map(CompartmentModel::getId).collect(Collectors.toList());
        List<CompartmentDevice> compartmentDevices = compartmentDeviceService.list(new LambdaQueryWrapper<CompartmentDevice>().in(CollUtil.isNotEmpty(idList), CompartmentDevice::getCompartmentId, idList));
        Map<Long, List<CompartmentDevice>> map = compartmentDevices.stream().collect(Collectors.groupingBy(CompartmentDevice::getCompartmentId));
        List<Long> deviceIds = compartmentDevices.stream().map(CompartmentDevice::getDeviceId).collect(Collectors.toList());
        List<IocDevice> deviceInfos = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().in(CollectionUtil.isNotEmpty(deviceIds), IocDevice::getId, deviceIds));
        Map<Long, IocDevice> finalDeviceMap = deviceInfos.stream().collect(Collectors.toMap(IocDevice::getId, f2 -> f2));
        Map<Long, String> finalRestaurantMap = restaurantMap;
        compartmentModels.forEach(f -> {
            ParkSpaceFullModel fullModel = spaceFullModelMap.get(f.getSpaceId());
            if (fullModel != null) {
                f.setSpaceFullPath(fullModel.getFullPath());
            }
            if (ObjectUtil.isNotNull(finalRestaurantMap)) {
                f.setRestaurantName(finalRestaurantMap.get(f.getRestaurantId()));
            }
            if (ObjectUtil.isNotNull(map)) {
                List<CompartmentDevice> deviceList = map.get(f.getId());
                if (CollUtil.isNotEmpty(deviceList)) {
                    deviceList.forEach(f1 -> {
                        if (ObjectUtil.isNotEmpty(finalDeviceMap) && ObjectUtil.isNotEmpty(finalDeviceMap.get(f1.getDeviceId()))) {
                            IocDevice deviceInfo = finalDeviceMap.get(f1.getDeviceId());
                            f1.setDeviceName(deviceInfo.getDeviceName());
                        }
                    });
                }
                f.setCompartmentDeviceModels(BeanUtils.convertListTo(deviceList, CompartmentDeviceModel::new));
            }
        });
       return ConvertUtil.pageConvert(iPage,compartmentModels);
    }

    @Override
    public List<CompartmentModel> list(CompartmentListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<Compartment> compartments = this.list(new LambdaQueryWrapper<Compartment>().eq(StrUtil.isNotBlank(param.getName()), Compartment::getName, param.getName())
                .eq(ObjectUtil.isNotEmpty(param.getRestaurantId()), Compartment::getRestaurantId, param.getRestaurantId())
                .eq(ObjectUtil.isNotNull(tenantId), Compartment::getTenantId, tenantId)
                .eq(ObjectUtil.isNotEmpty(param.getStatus()), Compartment::getStatus, param.getStatus()));
        return BeanUtils.convertListTo(compartments, CompartmentModel::new);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long save(CompartmentParam param) {
        List<CompartmentDeviceParam> deviceParams = param.getCompartmentDeviceParams();
        List<CompartmentComboParam> comboParams = param.getCompartmentComboParams();
        List<CompartmentTimeParam> timeParams = param.getCompartmentTimeParams().stream()
                .filter(f -> StrUtil.isNotEmpty(f.getStartTime()) && StrUtil.isNotEmpty(f.getEndTime())).collect(Collectors.toList());
        //校验数据
        validate(param, timeParams);

        Compartment compartment = BeanUtils.convertTo(param, Compartment::new);
        //默认字段
        compartment.setStatus(Status.enabled.getKey());
        this.save(compartment);
        Long id = compartment.getId();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //添加包间套餐关联表
        if (CollUtil.isNotEmpty(comboParams)) {
            List<CompartmentCombo> compartmentCombos = BeanUtils.convertListTo(comboParams, CompartmentCombo::new);
            compartmentCombos.forEach(f -> {
                f.setCompartmentId(id);
                f.setTenantId(tenantId);
            });
            compartmentComboService.saveBatch(compartmentCombos);
        }
        //添加包间设施关联表
        if (CollUtil.isNotEmpty(deviceParams)) {
            List<CompartmentDevice> compartmentCombos = BeanUtils.convertListTo(deviceParams, CompartmentDevice::new);
            compartmentCombos.forEach(f -> {
                f.setCompartmentId(id);
                f.setTenantId(tenantId);
            });
            compartmentDeviceService.saveBatch(compartmentCombos);
        }
        //添加包间营业时间表
        if (CollUtil.isNotEmpty(timeParams)) {
            List<CompartmentTime> compartmentTimes = BeanUtils.convertListTo(timeParams, CompartmentTime::new);
            compartmentTimes.forEach(f -> {
                f.setCompartmentId(id);
                f.setTenantId(tenantId);
            });
            compartmentTimeService.saveBatch(compartmentTimes);
        }
        return compartment.getId();
    }


    private void validate(CompartmentParam param, List<CompartmentTimeParam> timeList) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        Set<String> timeSets = timeList.stream().map(CompartmentTimeParam::getType).collect(Collectors.toSet());
        AssertUtils.isEquals(timeList.size(), timeSets.size(), "营业时间类型重复，请确认");
        AssertUtils.isTrue(!timeOverlapChecker(timeList), "营业时间重复，请确认");
        timeContinuitylapChecker(timeList);

        int num = this.count(new LambdaQueryWrapper<Compartment>()
                .eq(Compartment::getName, param.getName())
                .eq(ObjectUtil.isNotNull(tenantId), Compartment::getTenantId, tenantId)
                .ne(param.getId() != null, Compartment::getId, param.getId()));
        AssertUtils.isTrue(num == 0, "包间名称已存在，请确认");
    }

    private void timeContinuitylapChecker(List<CompartmentTimeParam> timeList) {
        for (int i = 0; i < timeList.size(); i++) {
            if (i == (timeList.size() - 1) || (timeList.size() - 1) == 0) {
                continue;
            }
            CompartmentTimeParam timeParam = timeList.get(i);
            Date startTime = DateUtils.parse("2024-07-22 " + timeParam.getEndTime(), "yyyy-MM-dd HH:mm");
            CompartmentTimeParam nextTime = timeList.get(i + 1);
            Date endTime = DateUtils.parse("2024-07-22 " + nextTime.getStartTime(), "yyyy-MM-dd HH:mm");
            AssertUtils.isTrue((DateUtil.between(startTime, endTime, DateUnit.MINUTE) > 29), "连续营业时间段需要前后间隔半小时");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean edit(CompartmentParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<CompartmentDeviceParam> deviceParams = param.getCompartmentDeviceParams();
        List<CompartmentComboParam> comboParams = param.getCompartmentComboParams();
        List<CompartmentTimeParam> timeParams = param.getCompartmentTimeParams().stream()
                .filter(f -> StrUtil.isNotEmpty(f.getStartTime()) && StrUtil.isNotEmpty(f.getEndTime())).collect(Collectors.toList());
        //校验数据
        validate(param, timeParams);
        //校验是否更换餐厅
        validateChangeRestaurant(param);
        Compartment compartment = BeanUtils.convertTo(param, Compartment::new);
        this.updateById(compartment);
        //删除关联表
        compartmentComboService.remove(new LambdaQueryWrapper<CompartmentCombo>().eq(CompartmentCombo::getCompartmentId, param.getId()));
        compartmentDeviceService.remove(new LambdaQueryWrapper<CompartmentDevice>().eq(CompartmentDevice::getCompartmentId, param.getId()));
        compartmentTimeService.remove(new LambdaQueryWrapper<CompartmentTime>().eq(CompartmentTime::getCompartmentId, param.getId()));

        //添加包间套餐关联表
        if (CollUtil.isNotEmpty(comboParams)) {
            List<CompartmentCombo> compartmentCombos = BeanUtils.convertListTo(comboParams, CompartmentCombo::new);
            compartmentCombos.forEach(f -> {
                f.setTenantId(tenantId);
                f.setCompartmentId(param.getId());
            });
            compartmentComboService.saveBatch(compartmentCombos);
        }
        //添加包间设施关联表
        if (CollUtil.isNotEmpty(deviceParams)) {
            List<CompartmentDevice> compartmentCombos = BeanUtils.convertListTo(deviceParams, CompartmentDevice::new);
            compartmentCombos.forEach(f -> {
                f.setTenantId(tenantId);
                f.setCompartmentId(param.getId());
            });
            compartmentDeviceService.saveBatch(compartmentCombos);
        }
        //添加包间营业时间表
        if (CollUtil.isNotEmpty(timeParams)) {
            List<CompartmentTime> compartmentTimes = BeanUtils.convertListTo(timeParams, CompartmentTime::new);
            compartmentTimes.forEach(f -> {
                f.setTenantId(tenantId);
                f.setCompartmentId(param.getId());
            });
            compartmentTimeService.saveBatch(compartmentTimes);
        }
        return Boolean.TRUE;
    }

    /**
     * @Param: param
     * @Author lhy
     * @Date 2024/8/8
     * @Description: 校验是否更换餐厅
     */
    private void validateChangeRestaurant(CompartmentParam param) {
        Compartment compartment = this.getById(param.getId());
        if (ObjectUtil.isNotEmpty(compartment.getRestaurantId()) && !compartment.getRestaurantId().equals(param.getRestaurantId())) {
            CompartmentReserveParam compartmentReserveParam = new CompartmentReserveParam();
            compartmentReserveParam.setCompartmentId(param.getId());
            compartmentReserveParam.setReserveStatus(CompartmentReserveStatusEnum.RESERVED.getCode());
            List<CompartmentReserveModel> compartmentReserveModels = findCompartmentReserve(compartmentReserveParam);
            AssertUtils.isTrue(CollUtil.isEmpty(compartmentReserveModels), "此包间下存在有效预定信息不能更换餐厅！");
        }
    }

    @Override
    public AppCompartmentModel detailApp(Long id) {
        Compartment compartment = getById(id);
        AssertUtils.notNull(compartment, SystemResultCode.RESULT_DATA_NONE.message());
        AppCompartmentModel model = BeanUtils.convertTo(compartment, AppCompartmentModel::new);
        //餐厅名称
        Restaurant restaurant = restaurantService.getById(compartment.getRestaurantId());
        model.setRestaurantName(restaurant.getName());
        //空间全路径
        Map<Long, ParkSpaceFullModel> map = parkSpaceService.findFullSpaceMap(new ArrayList<>(Arrays.asList(compartment.getSpaceId())), WebFrameworkUtils.getHeaderTenantId());
        Optional.ofNullable(map.get(compartment.getSpaceId())).ifPresent(g -> model.setSpaceName(g.getFullPath()));
        //营业时间
        List<CompartmentTime> timeList = compartmentTimeService.list(new LambdaQueryWrapper<CompartmentTime>().eq(CompartmentTime::getCompartmentId, id));
        model.setTimeList(BeanUtils.convertListTo(timeList, AppCompartmentTimeModel::new));
        //设施
        Map<Long, List<AppCompartmentDeviceModel>> deviceMap = getDeviceMap(new ArrayList<>(Arrays.asList(id)));
        Optional.ofNullable(deviceMap.get(id)).ifPresent(model::setDeviceList);
        //满意度
        model.setSatisfaction(compartment.getSatisfaction());
        //评价总数
        model.setEvaluateTotal(compartmentEvaluateService.count(Wrappers.<CompartmentEvaluate>lambdaQuery().eq(CompartmentEvaluate::getCompartmentId, id)));
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        Compartment compartment = this.getById(id);
        AssertUtils.state(Constants.STATUS_ENABLED != compartment.getStatus(), "包间已启用，不能删除");
        //删除关联表
        compartmentComboService.remove(new LambdaQueryWrapper<CompartmentCombo>().eq(CompartmentCombo::getCompartmentId, id));
        compartmentDeviceService.remove(new LambdaQueryWrapper<CompartmentDevice>().eq(CompartmentDevice::getCompartmentId, id));
        compartmentTimeService.remove(new LambdaQueryWrapper<CompartmentTime>().eq(CompartmentTime::getCompartmentId, id));
        this.getBaseMapper().deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public CompartmentModel get(Long id) {
        Compartment compartment = getById(id);
        AssertUtils.notNull(compartment, SystemResultCode.RESULT_DATA_NONE.message());
        CompartmentModel model = BeanUtils.convertTo(compartment, CompartmentModel::new);
        //餐厅名称
        Restaurant restaurant = restaurantService.getById(compartment.getRestaurantId());
        model.setRestaurantName(ObjectUtil.isEmpty(restaurant) ? null : restaurant.getName());
        //空间全路径
        Map<Long, ParkSpaceFullModel> map = parkSpaceService.findFullSpaceMap(new ArrayList<>(Arrays.asList(compartment.getSpaceId())), WebFrameworkUtils.getHeaderTenantId());
        Optional.ofNullable(map.get(compartment.getSpaceId())).ifPresent(g -> model.setSpaceName(g.getFullPath()));
        //营业时间
        List<CompartmentTime> timeList = compartmentTimeService.list(new LambdaQueryWrapper<CompartmentTime>().eq(CompartmentTime::getCompartmentId, id));
        model.setCompartmentTimeModels(BeanUtils.convertListTo(timeList, CompartmentTimeModel::new));
        //设施
        List<CompartmentDevice> compartmentDevices = compartmentDeviceService.list(new LambdaQueryWrapper<CompartmentDevice>().eq(CompartmentDevice::getCompartmentId, id));
        List<Long> deviceIds = compartmentDevices.stream().map(e -> e.getDeviceId()).collect(Collectors.toList());
        List<IocDevice> deviceInfos = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().in(CollectionUtil.isNotEmpty(deviceIds), IocDevice::getId, deviceIds));
        Map<Long, IocDevice> deviceMap = deviceInfos.stream().collect(Collectors.toMap(IocDevice::getId, f -> f));
        compartmentDevices.forEach(f -> {
            if (ObjectUtil.isNotEmpty(deviceMap) && ObjectUtil.isNotEmpty(deviceMap.get(f.getDeviceId()))) {
                IocDevice deviceInfo = deviceMap.get(f.getDeviceId());
                f.setDeviceName(deviceInfo.getDeviceName());
            }
        });
        model.setCompartmentDeviceModels(BeanUtils.convertListTo(compartmentDevices, CompartmentDeviceModel::new));
        //关联套餐
        List<CompartmentCombo> compartmentCombos = compartmentComboService.list(new LambdaQueryWrapper<CompartmentCombo>().eq(CompartmentCombo::getCompartmentId, id));
        List<CompartmentComboModel> compartmentComboModels = BeanUtils.convertListTo(compartmentCombos, CompartmentComboModel::new);
        List<Long> comboIds = compartmentComboModels.stream().map(e -> e.getComboId()).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(comboIds)) {
            Map<Long, String> comboMap = comboService.listByIds(comboIds).stream().collect(Collectors.toMap(Combo::getId, Combo::getName));
            compartmentComboModels.forEach(f -> f.setComboName(comboMap.get(f.getComboId())));
        }
        model.setCompartmentComboModels(compartmentComboModels);
        return model;
    }

    @Override
    public List<CompartmentComboModel> getCombolistByIds(List<Long> compartmentIds) {
        List<CompartmentComboModel> newCompartmentCombos = new ArrayList<>();
        List<CompartmentCombo> compartmentCombos = compartmentComboService.list(new LambdaQueryWrapper<CompartmentCombo>().in(CompartmentCombo::getCompartmentId, compartmentIds));
        List<CompartmentComboModel> compartmentComboModels = BeanUtils.convertListTo(compartmentCombos, CompartmentComboModel::new);
        List<Long> comboIds = compartmentCombos.stream().map(e -> e.getComboId()).collect(Collectors.toList());
        if (CollUtil.isEmpty(comboIds)) {
            return new ArrayList<>();
        }
        Map<Long, Combo> map = comboService.listByIds(comboIds).stream().collect(Collectors.toMap(Combo::getId, combo -> combo));
        for (CompartmentComboModel f : compartmentComboModels) {
            if (ObjectUtil.isEmpty(map.get(f.getComboId()))) {
                continue;
            }
            if (map.get(f.getComboId()).getStatus().equals(Status.disabled.getKey())) {
                continue;
            }
            f.setComboName(map.get(f.getComboId()).getName());
            f.setComboDescription(map.get(f.getComboId()).getDescription());
            newCompartmentCombos.add(BeanUtil.toBean(f, CompartmentComboModel.class));
        }
        return newCompartmentCombos;
    }

    @Override
    public List<AppComboModel> findComboListById(AppComboListParam param) {
        List<CompartmentCombo> compartmentComboList = compartmentComboService.list(Wrappers.<CompartmentCombo>lambdaQuery().eq(CompartmentCombo::getCompartmentId, param.getCompartmentId()));
        List<Long> comboIdList = compartmentComboList.stream().map(CompartmentCombo::getComboId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(comboIdList)) {
            return Collections.emptyList();
        }
        List<Combo> comboList = comboService.list(Wrappers.<Combo>lambdaQuery().in(Combo::getId, comboIdList).eq(Combo::getStatus, Status.enabled.getKey()));
        return comboList.stream().map(combo -> {
            AppComboModel model = new AppComboModel();
            model.setId(combo.getId());
            model.setComboName(combo.getName());
            model.setComboDescription(combo.getDescription());
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<AppCompartmentDeviceModel>> getDeviceMap(List<Long> compartmentIdList) {
        if (CollUtil.isEmpty(compartmentIdList)) {
            return new HashMap<>(4);
        }
        //批量查询包间设备
        List<CompartmentDevice> compartmentDeviceList = compartmentDeviceService.list(new LambdaQueryWrapper<CompartmentDevice>().in(CompartmentDevice::getCompartmentId, compartmentIdList));
        Map<Long, List<CompartmentDevice>> compartmentDeviceMap = compartmentDeviceList.stream().collect(Collectors.groupingBy(CompartmentDevice::getCompartmentId));
        List<Long> deviceIdList = compartmentDeviceList.stream().map(CompartmentDevice::getDeviceId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(deviceIdList)){
            return new HashMap<>(4);
        }
        List<IocDevice> deviceInfoList = iocDeviceService.list(new LambdaQueryWrapper<IocDevice>().in(CollectionUtil.isNotEmpty(deviceIdList), IocDevice::getId, deviceIdList));
        Map<Long, String> deviceMap = new HashMap<>(4);
        for (IocDevice deviceInfo:deviceInfoList){
            deviceMap.put(deviceInfo.getId(),deviceInfo.getDeviceName());
        }

        Map<Long, List<AppCompartmentDeviceModel>> map = new HashMap<>(8);
        compartmentIdList.forEach(id -> {
            if (compartmentDeviceMap.containsKey(id)) {
                List<CompartmentDevice> compartmentDevices = compartmentDeviceMap.get(id);
                List<AppCompartmentDeviceModel> deviceModelList = compartmentDevices.stream().map(compartmentDevice -> {
                    AppCompartmentDeviceModel model = new AppCompartmentDeviceModel();
                    model.setDeviceId(compartmentDevice.getDeviceId());
                    model.setDeviceName(deviceMap.get(compartmentDevice.getDeviceId()));
                    return model;
                }).collect(Collectors.toList());
                map.put(id, deviceModelList);
            } else {
                map.put(id, Collections.emptyList());
            }
        });
        return map;
    }

    @Override
    public Boolean disable(Long id) {
        CompartmentReserveParam compartmentReserveParam = new CompartmentReserveParam();
        compartmentReserveParam.setCompartmentId(id);
        compartmentReserveParam.setReserveStatus(CompartmentReserveStatusEnum.RESERVED.getCode());
        List<CompartmentReserveModel> compartmentReserveModels = findCompartmentReserve(compartmentReserveParam);
        AssertUtils.isTrue(CollUtil.isEmpty(compartmentReserveModels), "此包间下存在有效预定信息不能禁用！");
        return this.update(Wrappers.<Compartment>lambdaUpdate().eq(Compartment::getId, id).set(Compartment::getStatus, Status.disabled.getKey()));
    }


    private boolean timeOverlapChecker(List<CompartmentTimeParam> timeList) {
        ArrayList<TimeRange> timeRanges = new ArrayList<TimeRange>();
        // 添加时间段到列表
        for (CompartmentTimeParam e : timeList) {
            Date startTime = DateUtils.parse("2024-07-22 " + e.getStartTime(), "yyyy-MM-dd HH:mm");
            Date endTime = DateUtils.parse("2024-07-22 " + e.getEndTime(), "yyyy-MM-dd HH:mm");
            if (ObjectUtil.isEmpty(startTime) || ObjectUtil.isEmpty(endTime)) {
                continue;
            }
            // 检查开始时间是否大于结束时间
            if (startTime.after(endTime)) {
                throw GenericException.fail("营业开始时间不能大于结束时间");
            }
            if (startTime.compareTo(endTime) == 0) {
                throw GenericException.fail("营业开始时间不能等于结束时间");
            }
            timeRanges.add(new TimeRange(startTime, endTime));
        }

        // 2. 对时间段列表按照开始时间进行排序
        Collections.sort(timeRanges, new Comparator<TimeRange>() {
            public int compare(TimeRange tr1, TimeRange tr2) {
                return tr1.getStartTime().compareTo(tr2.getStartTime());
            }
        });
        // 3. 遍历时间段列表，判断相邻时间段是否有交叉
        boolean hasOverlap = false;
        Date prevEndTime = null;
        for (TimeRange tr : timeRanges) {
            if (prevEndTime != null && tr.getStartTime().before(prevEndTime)) {
                hasOverlap = true;
                break;
            }
            prevEndTime = tr.getEndTime();
        }
        return hasOverlap;
    }

    @Data
    class TimeRange {

        private Date startTime;

        private Date endTime;

        public TimeRange(Date parse, Date parse1) {
            this.startTime = parse;
            this.endTime = parse1;
        }
    }


    /***
     * @Description 查询包间预约信息
     * @author huangyongtao
     * @date 2024/8/7 17:13
     * @param param
     */
    public List<CompartmentReserveModel> findCompartmentReserve(CompartmentReserveParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<CompartmentReserve> compartmentReserveList = compartmentReserveRepository.selectList(new LambdaQueryWrapper<CompartmentReserve>().eq(ObjectUtil.isNotEmpty(param.getCompartmentId()), CompartmentReserve::getCompartmentId, param.getCompartmentId())
                .eq(ObjectUtil.isNotEmpty(param.getReserveStatus()), CompartmentReserve::getReserveStatus, param.getReserveStatus())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), CompartmentReserve::getTenantId, param.getTenantId()));
        if (CollectionUtil.isEmpty(compartmentReserveList)) {
            return new ArrayList<>();
        }
        return BeanUtils.convertListTo(compartmentReserveList, CompartmentReserveModel::new);
    }

}
