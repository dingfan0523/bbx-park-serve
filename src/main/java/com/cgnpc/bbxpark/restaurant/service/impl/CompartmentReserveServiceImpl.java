
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.MessageConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.CompartmentReserveStatusEnum;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.service.IMessageCommonService;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.restaurant.domain.*;
import com.cgnpc.bbxpark.restaurant.dto.model.*;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.mapper.CompartmentReserveRepository;
import com.cgnpc.bbxpark.restaurant.service.*;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.service.IConfigInfoService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.IUserSpaceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 包间预定服务实现
 * @author huangyongtao
 * @date 2024/7/30 15:04
 */
@Slf4j
@Service
public class CompartmentReserveServiceImpl extends ServiceImpl<CompartmentReserveRepository, CompartmentReserve> implements ICompartmentReserveService {

    @Autowired
    private ICompartmentService compartmentService;

    @Autowired
    private ICompartmentTimeService compartmentTimeService;

    @Autowired
    private ICompartmentEvaluateService compartmentEvaluateService;

    @Autowired
    private IParkSpaceService parkSpaceService;

    @Autowired
    IUserSpaceService userSpaceService;

    @Autowired
    private IRestaurantService restaurantService;
    @Autowired
    private IComboService comboService;

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private IMessageCommonService messageCommonService;
    @Autowired
    private IRoleApiService roleApiService;
    @Autowired
    private IConfigInfoService configInfoService;



    /**
     * 根据包间预定标识获得包间预定详情信息.
     *
     * @Param [id] 包间预定标识
     * @Return 包间预定详情信息
     */
    @Override
    public CompartmentReserveModel detail(Long id) {
        CompartmentReserve compartmentReserve = this.getById(id);
        AssertUtils.notNull(compartmentReserve, SystemResultCode.RESULT_DATA_NONE.message());
        return BeanUtils.convertTo(compartmentReserve, CompartmentReserveModel::new);
    }

    @Override
    public AppCompartmentReserveDetailModel detailApp(Long id) {
        CompartmentReserve compartmentReserve = this.getById(id);
        AssertUtils.notNull(compartmentReserve, SystemResultCode.RESULT_DATA_NONE.message());
        AppCompartmentReserveDetailModel model = BeanUtils.convertTo(compartmentReserve, AppCompartmentReserveDetailModel::new);
        //餐厅名称
        Compartment compartment = compartmentService.getById(compartmentReserve.getCompartmentId());
        if (compartment != null) {
            model.setCompartmentImageUrl(compartment.getImageUrl());
            Restaurant restaurant = restaurantService.getById(compartment.getRestaurantId());
            Optional.ofNullable(restaurant).ifPresent(r -> model.setRestaurantName(r.getName()));
        }
        //评价信息
        CompartmentEvaluate evaluate = compartmentEvaluateService.getOne(Wrappers.<CompartmentEvaluate>lambdaQuery().eq(CompartmentEvaluate::getReserveId, id).last("limit 1"));
        Optional.ofNullable(evaluate).ifPresent(e -> BeanUtils.copyProperties(evaluate, model));
        model.setId(compartmentReserve.getId());
        return model;
    }

    @Override
    public AppCompartmentExModel findCompartmentEx(AppCompartmentReserveTimeListParam param) {
        Compartment compartment = compartmentService.getById(param.getCompartmentId());
        //套餐信息
        List<CompartmentComboModel> comboModelList = compartmentService.getCombolistByIds(new ArrayList<>(Collections.singletonList(param.getCompartmentId())));
        List<AppComboModel> comboList = comboModelList.stream().map(model -> BeanUtils.convertTo(model, AppComboModel::new)).collect(Collectors.toList());
        //营业时间
        CompartmentReserveParam param1 = new CompartmentReserveParam();
        param1.setCompartmentId(param.getCompartmentId());
        param1.setReserveStartTime(DateUtil.getFirstTimeOfDate(param.getReserveDate()));
        param1.setReserveEndTime(DateUtil.getLastTimeOfDate(param.getReserveDate()));
        //数据组装
        AppCompartmentExModel model = new AppCompartmentExModel();
        model.setId(param.getCompartmentId());
        model.setCompartmentName(compartment.getName());
        model.setComboList(comboList);
        model.setTimeList(findTimeApp(param));
        return model;
    }

    /**
     * 获取包间预定列表(分页).
     *
     * @Param param 包间预定查询条件
     * @Return 包间预定信息列表（分页）
     */
    @Override
    public IPage<CompartmentReserveModel> page(CompartmentReservePageParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return this.getBaseMapper().pageReserve(new Page<>(param.getCurrent(), param.getSize()), param);
    }

    @Override
    public IPage<AppCompartmentReserveModel> pageApp(AppCompartmentReservePageParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        String userId = userApiService.getCurrentStaffNo();
        LambdaQueryWrapper<CompartmentReserve> wrapper = new LambdaQueryWrapper<CompartmentReserve>().eq(tenantId != null, CompartmentReserve::getTenantId, tenantId)
                .eq(userId != null, CompartmentReserve::getSubscriberId, userId)
                .ge(param.getReserveStartTime() != null, CompartmentReserve::getReserveStartTime, param.getReserveStartTime())
                .le(param.getReserveEndTime() != null, CompartmentReserve::getReserveEndTime, param.getReserveEndTime()).orderByDesc(CompartmentReserve::getCreateTime);
        IPage<CompartmentReserve> page = baseMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()), wrapper);
        if (!CollectionUtils.isNotEmpty(page.getRecords())) {
            return ConvertUtil.pageEmptyConvert(page.getCurrent(), page.getSize());
        }
        //查询包间
        List<Long> compartmentIdList = page.getRecords().stream().map(CompartmentReserve::getCompartmentId).collect(Collectors.toList());
        List<Compartment> compartmentList = (List<Compartment>) compartmentService.listByIds(compartmentIdList);
        Map<Long, Compartment> comResMap = compartmentList.stream().collect(Collectors.toMap(Compartment::getId, c -> c, (k1, k2) -> k2));
        // 查询餐厅名称
        List<Long> restaurantIdList = compartmentList.stream().map(Compartment::getRestaurantId).collect(Collectors.toList());
        Map<Long, String> restaurantMap = restaurantService.listByIds(restaurantIdList).stream().collect(Collectors.toMap(Restaurant::getId, Restaurant::getName));
        //查询相关评价信息
        List<Long> reserveIdList = page.getRecords().stream().map(CompartmentReserve::getId).collect(Collectors.toList());
        List<CompartmentEvaluate> evaluateList = compartmentEvaluateService.list(Wrappers.<CompartmentEvaluate>lambdaQuery().in(CollectionUtils.isNotEmpty(reserveIdList), CompartmentEvaluate::getReserveId, reserveIdList));
        Map<Long, CompartmentEvaluate> evaluateMap = evaluateList.stream().collect(Collectors.toMap(CompartmentEvaluate::getReserveId, e -> e, (k1, k2) -> k2));
        //数据组装
        List<AppCompartmentReserveModel> list = page.getRecords().stream().map(compartmentReserve -> {
            AppCompartmentReserveModel model = BeanUtils.convertTo(compartmentReserve, AppCompartmentReserveModel::new);
            //包间和餐厅信息
            if (comResMap.containsKey(compartmentReserve.getCompartmentId())) {
                Compartment compartment = comResMap.get(compartmentReserve.getCompartmentId());
                model.setCompartmentImageUrl(compartment.getImageUrl());
                if (restaurantMap.containsKey(compartment.getRestaurantId())) {
                    model.setRestaurantName(restaurantMap.get(compartment.getRestaurantId()));
                }
            }
            //评价相关
            Optional.ofNullable(evaluateMap.get(compartmentReserve.getId())).ifPresent(e -> {
                model.setDishes(e.getDishes());
                model.setEnvironment(e.getEnvironment());
                model.setService(e.getService());
                model.setSatisfaction(e.getSatisfaction());
            });
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page, list);
    }

    /**
     * 获取包间预定列表.
     *
     * @Param param 包间预定查询条件
     * @Return 包间预定信息列表
     */
    @Override
    @SneakyThrows
    public List<CompartmentReserveGroupModel> list(CompartmentReserveListParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        //查询已预约，已到店，已过期的预约
        param.setReserveStatusList(Arrays.asList(CompartmentReserveStatusEnum.RESERVED.getCode(), CompartmentReserveStatusEnum.ARRIVED.getCode(), (CompartmentReserveStatusEnum.EXPIRED.getCode())));
        //查询预约信息
        List<CompartmentReserveModel> reserveModels = this.getBaseMapper().findReserve(param);
        if (reserveModels == null) {
            reserveModels = new ArrayList<>();
        }
        //查询包间信息
        List<Compartment> compartments = compartmentService.getBaseMapper().selectList(new LambdaQueryWrapper<Compartment>()
                .eq(param.getCompartmentId() != null,Compartment::getId,param.getCompartmentId())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), Compartment::getTenantId, param.getTenantId())
                .eq(Compartment::getDeleted, Delete.NORMAL.getKey())
                .eq(ObjectUtil.isNotEmpty(param.getRestaurantId()), Compartment::getRestaurantId, param.getRestaurantId())
                .in(CollectionUtil.isNotEmpty(param.getCompartmentIdList()), Compartment::getId, param.getCompartmentIdList())
                .eq(ObjectUtil.isNotEmpty(param.getSpaceId()), Compartment::getSpaceId, param.getSpaceId())
                .in(CollectionUtil.isNotEmpty(param.getSpaceIdList()), Compartment::getSpaceId, param.getSpaceIdList()));
        if (CollectionUtil.isEmpty(compartments)) {
            return new ArrayList<>();
        }
        //查询包间营业时间
        List<CompartmentTime> compartmentTimes = compartmentTimeService.getBaseMapper().selectList(new LambdaQueryWrapper<CompartmentTime>()
                .select(CompartmentTime::getCompartmentId)
                .in(CollectionUtil.isNotEmpty(param.getCompartmentIdList()), CompartmentTime::getCompartmentId, param.getCompartmentIdList())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), CompartmentTime::getTenantId, param.getTenantId()));
        List<Long> compartmentIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(compartmentTimes)) {
            compartmentIds = compartmentTimes.stream().map(CompartmentTime::getCompartmentId).collect(Collectors.toList());
        }
        //组装数据
        Map<Long, List<CompartmentReserveModel>> reserveModelMap = reserveModels.stream().collect(Collectors.groupingBy(CompartmentReserveModel::getCompartmentId));
        List<Long> finalCompartmentIds = compartmentIds;
        return compartments.stream().map(compartment -> {
            CompartmentReserveGroupModel groupModel = new CompartmentReserveGroupModel();
            BeanUtils.copyProperties(compartment, groupModel);
            if (!finalCompartmentIds.contains(groupModel.getId())) {
                groupModel.setHasTime(false);
            }
            groupModel.setCompartmentReserveModels(reserveModelMap.get(compartment.getId()));
            return groupModel;
        }).collect(Collectors.toList());
    }

    /**
     * 移动端-获取包间预定列表.
     *
     * @Param param 包间预定查询条件
     * @Return 包间预定信息列表
     */
    @Override
    @SneakyThrows
    public List<AppCompartmentReserveGroupModel> listApp(AppCompartmentReserveListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //包间相关信息
        List<Compartment> compartmentList = compartmentService.getBaseMapper().selectList(new LambdaQueryWrapper<Compartment>().eq(Compartment::getStatus, Status.enabled.getKey())
                .eq(ObjectUtil.isNotEmpty(tenantId), Compartment::getTenantId, tenantId)
                .eq(Compartment::getDeleted, Delete.NORMAL.getKey())
                .eq(ObjectUtil.isNotEmpty(param.getRestaurantId()), Compartment::getRestaurantId, param.getRestaurantId())
                .like(StringUtils.isNotEmpty(param.getCompartmentName()), Compartment::getName, param.getCompartmentName())
                .orderByDesc(Compartment::getCreateTime));
        if (!CollectionUtils.isNotEmpty(compartmentList)) {
            return Collections.emptyList();
        }
        List<Long> idList = compartmentList.stream().map(Compartment::getId).collect(Collectors.toList());
        //空间信息
        List<Long> spaceIdList = compartmentList.stream().map(Compartment::getSpaceId).collect(Collectors.toList());
        Map<Long, ParkSpaceFullModel> map = parkSpaceService.findFullSpaceMap(spaceIdList, WebFrameworkUtils.getHeaderTenantId());
        //预定信息
        List<CompartmentReserve> compartmentReserveList = list(new LambdaQueryWrapper<CompartmentReserve>().in(CompartmentReserve::getCompartmentId, idList)
                .eq(ObjectUtil.isNotEmpty(tenantId), CompartmentReserve::getTenantId, tenantId)
                .in(CompartmentReserve::getReserveStatus, Arrays.asList(CompartmentReserveStatusEnum.RESERVED.getCode(), CompartmentReserveStatusEnum.ARRIVED.getCode()))
                .ge(CompartmentReserve::getReserveStartTime, DateUtil.getFirstTimeOfDate(param.getReserveDate()))
                .le(CompartmentReserve::getReserveEndTime, DateUtil.getLastTimeOfDate(param.getReserveDate())));
        Map<Long, List<CompartmentReserve>> reserveModelMap = compartmentReserveList.stream().collect(Collectors.groupingBy(CompartmentReserve::getCompartmentId));
        //包间营业时间
        List<CompartmentTime> timeList = compartmentTimeService.list(new LambdaQueryWrapper<CompartmentTime>().in(CompartmentTime::getCompartmentId, idList).orderByAsc(CompartmentTime::getStartTime));
        Map<Long, List<CompartmentTime>> timeMap = timeList.stream().collect(Collectors.groupingBy(CompartmentTime::getCompartmentId));
        //包间设施集合
        Map<Long, List<AppCompartmentDeviceModel>> deviceMap = compartmentService.getDeviceMap(idList);
        //数据组装
        Date now = DateUtils.currentDate();
        String dayFormat = DateUtils.formatYMD(param.getReserveDate());
        return compartmentList.stream().map(compartment -> {
            AppCompartmentReserveGroupModel model = BeanUtils.convertTo(compartment, AppCompartmentReserveGroupModel::new);
            //位置全路径
            Optional.ofNullable(map.get(compartment.getSpaceId())).ifPresent(g -> model.setSpaceFullPath(g.getFullPath()));
            //营业时间
            Optional.ofNullable(timeMap.get(model.getId())).ifPresent(l -> {
                //营业时间转换
                List<AppCompartmentTimeExModel> timeExList = l.stream().map(t -> {
                    AppCompartmentTimeExModel timeModel = BeanUtils.convertTo(t, AppCompartmentTimeExModel::new);
                    timeModel.setStatus(statusHandle(t.getStartTime(), t.getEndTime(), now, dayFormat, reserveModelMap.get(compartment.getId())));
                    return timeModel;
                }).collect(Collectors.toList());
                model.setTimeList(timeExList);
                model.setRunState(isRange(l, now, dayFormat));
                //开始营业时间和结束营业时间
                model.setStartTime(l.get(0).getStartTime());
                model.setEndTime(l.get(l.size() - 1).getEndTime());
            });
            //设施
            Optional.ofNullable(deviceMap.get(compartment.getId())).ifPresent(model::setDeviceList);
            //预约时间
            Optional.ofNullable(reserveModelMap.get(compartment.getId())).ifPresent(l -> model.setReserveList(BeanUtils.convertListTo(l, ReserveTimeMode::new)));
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 新增包间预定.
     *
     * @Param param 包间预定信息
     * @Return 新增包间预定是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(CompartmentReserveParam param) {
        CompartmentReserve compartmentReserve = BeanUtils.convertTo(param, CompartmentReserve::new);
        compartmentReserve.setId(null);
        compartmentReserve.setReserveStatus(CompartmentReserveStatusEnum.RESERVED.getCode());
        synchronized (this) {
            handleReserve(param, compartmentReserve);
            this.save(compartmentReserve);
        }
        return compartmentReserve.getId();
    }

    /**
     * 编辑包间预定信息.
     *
     * @Param param 包间预定信息
     * @Return 编辑包间预定是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Boolean edit(CompartmentReserveParam param) {
        CompartmentReserve compartmentReserve = this.getById(param.getId());
        AssertUtils.notNull(compartmentReserve, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.state(CompartmentReserveStatusEnum.RESERVED.getCode().equals(compartmentReserve.getReserveStatus()), Constant.CHANGE_ERROR_MESSAGE);
        if (new Date().after(param.getReserveEndTime())) {
            throw GenericException.fail("预定时间已过，请重新选择");
        }
        CompartmentReserve editParam = BeanUtils.convertTo(param, CompartmentReserve::new);
        editParam.setReserveStatus(compartmentReserve.getReserveStatus());
        editParam.setSubscriberId(compartmentReserve.getSubscriberId());
        handleReserve(param, editParam);
        return this.updateById(editParam);
    }

    private void handleReserve(CompartmentReserveParam param, CompartmentReserve compartmentReserve) {
        //包间信息
        Compartment compartment = compartmentService.getById(compartmentReserve.getCompartmentId());
        AssertUtils.notNull(compartment, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.isFalse(compartment.getStatus().equals(Status.disabled.getKey()), "此包间已被禁用");
        compartmentReserve.setCompartmentName(compartment.getName());
        //校验包间的预定时间
        param.setReserveStatusList(Arrays.asList(CompartmentReserveStatusEnum.RESERVED.getCode(), CompartmentReserveStatusEnum.ARRIVED.getCode()));
        List<CompartmentReserveModel> models = this.getBaseMapper().findByTime(param);
        AssertUtils.isEmpty(models, "此包间已被预定");
        //用户信息
        UserInfoModel user = getUser(param.getSubscriberId());
        compartmentReserve.setSubscriberName(user.getUserName());
        compartmentReserve.setSubscriberStaffid(user.getStaffid());
        compartmentReserve.setPhone(user.getMobile());
        //套餐信息
        if (compartmentReserve.getComboId() != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            Combo combo = comboService.getById(compartmentReserve.getComboId());
            AssertUtils.notNull(combo, SystemResultCode.RESULT_DATA_NONE.message());
            compartmentReserve.setComboName(combo.getName());
            compartmentReserve.setComboDescription(combo.getDescription());
            compartmentReserve.setComboPrice(combo.getPrice());
        }
    }

    /***
     * @Description 取消包间预约
     * @author huangyongtao
     * @date 2024/7/31 17:18
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancel(CompartmentReserveParam param) {
        CompartmentReserve compartmentReserveOld = this.getById(param.getId());
        AssertUtils.notNull(compartmentReserveOld, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.state(CompartmentReserveStatusEnum.RESERVED.getCode().equals(compartmentReserveOld.getReserveStatus()), Constant.CHANGE_ERROR_MESSAGE);
        CompartmentReserve compartmentReserve = new CompartmentReserve();
        compartmentReserve.setId(param.getId());
        compartmentReserve.setReserveStatus(CompartmentReserveStatusEnum.CANCELLED.getCode());
        compartmentReserve.setCancelRemark(param.getCancelRemark());
        compartmentReserve.setCancelTime(new Date());
        compartmentReserve.setCancelReason(param.getCancelReason());
        Boolean flag = this.updateById(compartmentReserve);
        if (flag) {
            //消息通知
            Map<String, String> variables = new HashMap<>(4);
            variables.put("cancelReason", param.getCancelReason());
            messageCommonService.sendMessage(MessageConstant.PC_COMPARTMENT_RESERVE_CANCEL, WebFrameworkUtils.getHeaderTenantId(), compartmentReserve.getId(), compartmentReserveOld.getSubscriberId(), variables);
        }
        return flag;
    }

    @Override
    public Boolean cancelApp(CompartmentReserveParam param) {
        CompartmentReserve compartmentReserveOld = this.getById(param.getId());
        AssertUtils.notNull(compartmentReserveOld, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.state(CompartmentReserveStatusEnum.RESERVED.getCode().equals(compartmentReserveOld.getReserveStatus()), Constant.CHANGE_ERROR_MESSAGE);
        CompartmentReserve compartmentReserve = new CompartmentReserve();
        compartmentReserve.setId(param.getId());
        compartmentReserve.setReserveStatus(CompartmentReserveStatusEnum.CANCELLED.getCode());
        compartmentReserve.setCancelRemark(param.getCancelRemark());
        compartmentReserve.setCancelTime(new Date());
        compartmentReserve.setCancelReason(param.getCancelReason());
        Boolean flag = this.updateById(compartmentReserve);
        if (flag) {
            //消息通知
            Map<String, String> variables = new HashMap<>(4);
            variables.put("subscriberName", compartmentReserveOld.getSubscriberName());
            variables.put("reserveTime", DateUtils.format(compartmentReserveOld.getReserveStartTime(), "yyyy-MM-dd HH:mm") + "-" + DateUtils.format(compartmentReserveOld.getReserveEndTime(), "HH:mm"));
            variables.put("compartmentName", compartmentReserveOld.getCompartmentName());
            variables.put("cancelReason", param.getCancelReason());
            List<String> userIdList = getRoleUserId();
            messageCommonService.sendMessage(MessageConstant.APP_COMPARTMENT_RESERVE_CANCEL, WebFrameworkUtils.getHeaderTenantId(), compartmentReserveOld.getId(), new HashSet<>(userIdList), variables);
        }
        return flag;
    }

    private List<String> getRoleUserId(){
        ConfigInfoModel configInfo = configInfoService.getByCodeDetail(Constant.COMPARTMENT_ROLE_CONFIG);
        return ObjectUtil.isNotEmpty(configInfo) ? roleApiService.findStaffNoByRoleCode(configInfo.getValue()) : Collections.emptyList();
    }

    /***
     * @Description 到店
     * @author huangyongtao
     * @date 2024/7/31 17:18
     * @param param
     */
    @Override
    public Boolean arrive(CompartmentReserveParam param) {
        CompartmentReserve compartmentReserveOld = this.getById(param.getId());
        AssertUtils.notNull(compartmentReserveOld, SystemResultCode.RESULT_DATA_NONE.message());
        AssertUtils.state(CompartmentReserveStatusEnum.RESERVED.getCode().equals(compartmentReserveOld.getReserveStatus()), Constant.CHANGE_ERROR_MESSAGE);
        CompartmentReserve compartmentReserve = new CompartmentReserve();
        compartmentReserve.setId(param.getId());
        compartmentReserve.setReserveStatus(CompartmentReserveStatusEnum.ARRIVED.getCode());
        compartmentReserve.setUseTime(new Date());
        return this.updateById(compartmentReserve);
    }

    /***
     * @Description 查询包间可预订的时间
     * @author huangyongtao
     * @date 2024/8/2 10:03
     * @param param
     */
    @Override
    public List<CompartmentTimeModel> findTime(CompartmentReserveParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<CompartmentTime> compartmentTimeList = compartmentTimeService.getBaseMapper().selectList(new LambdaQueryWrapper<CompartmentTime>().eq(ObjectUtil.isNotEmpty(param.getCompartmentId()), CompartmentTime::getCompartmentId, param.getCompartmentId())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), CompartmentTime::getTenantId, param.getTenantId()));
        if (CollectionUtil.isEmpty(compartmentTimeList)) {
            return new ArrayList<>();
        }
        param.setReserveStatusList(Arrays.asList(CompartmentReserveStatusEnum.RESERVED.getCode(), CompartmentReserveStatusEnum.ARRIVED.getCode()));
        List<CompartmentReserve> compartmentReserveList = this.getBaseMapper().selectList(new LambdaQueryWrapper<CompartmentReserve>().eq(ObjectUtil.isNotEmpty(param.getCompartmentId()), CompartmentReserve::getCompartmentId, param.getCompartmentId())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), CompartmentReserve::getTenantId, param.getTenantId())
                .in(CollectionUtil.isNotEmpty(param.getReserveStatusList()), CompartmentReserve::getReserveStatus, param.getReserveStatusList())
                .ne(ObjectUtil.isNotEmpty(param.getId()), CompartmentReserve::getId, param.getId())
                .ge(ObjectUtil.isNotEmpty(param.getReserveStartTime()), CompartmentReserve::getReserveStartTime, param.getReserveStartTime())
                .le(ObjectUtil.isNotEmpty(param.getReserveEndTime()), CompartmentReserve::getReserveEndTime, param.getReserveEndTime()));
        List<CompartmentTimeModel> compartmentTimeModelList = new ArrayList<>();
        String dayFormat = DateUtils.formatYMD(ObjectUtil.isEmpty(param.getReserveStartTime()) ? new Date() : param.getReserveStartTime());
        for (CompartmentTime compartmentTime : compartmentTimeList) {
            Date startTime = DateUtils.format(dayFormat + " " + compartmentTime.getStartTime(), "yyyy-MM-dd HH:mm");
            Date endTime = DateUtils.format(dayFormat + " " + compartmentTime.getEndTime(), "yyyy-MM-dd HH:mm");
            if (endTime.before(new Date())) {
                continue;
            }
            CompartmentTimeModel model = BeanUtils.convertTo(compartmentTime, CompartmentTimeModel::new);
            boolean occupyFlag = false;
            if (CollectionUtil.isNotEmpty(compartmentReserveList)) {
                for (CompartmentReserve reserve : compartmentReserveList) {
                    if (startTime.before(reserve.getReserveEndTime()) && endTime.after(reserve.getReserveStartTime())) {
                        occupyFlag = true;
                        break;
                    }
                }
            }
            model.setOccupyFlag(occupyFlag);
            compartmentTimeModelList.add(model);
        }
        return compartmentTimeModelList;
    }

    @Override
    public List<AppCompartmentTimeExModel> findTimeApp(AppCompartmentReserveTimeListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //查询包间营业时间
        List<CompartmentTime> compartmentTimeList = compartmentTimeService.list(new LambdaQueryWrapper<CompartmentTime>().eq(CompartmentTime::getCompartmentId, param.getCompartmentId()).eq(ObjectUtil.isNotEmpty(tenantId), CompartmentTime::getTenantId, tenantId));
        if (CollectionUtil.isEmpty(compartmentTimeList)) {
            //包间不存在营业时间直接返回空
            return Collections.emptyList();
        }
        //查询包间预约时间(已到店、已预约状态，其他状态的预约信息无用)
        List<CompartmentReserve> compartmentReserveList = list(new LambdaQueryWrapper<CompartmentReserve>().eq(CompartmentReserve::getCompartmentId, param.getCompartmentId())
                .eq(ObjectUtil.isNotEmpty(tenantId), CompartmentReserve::getTenantId, tenantId)
                .in(CompartmentReserve::getReserveStatus, Arrays.asList(CompartmentReserveStatusEnum.RESERVED.getCode(), CompartmentReserveStatusEnum.ARRIVED.getCode()))
                .ge(CompartmentReserve::getReserveStartTime, DateUtil.getFirstTimeOfDate(param.getReserveDate()))
                .le(CompartmentReserve::getReserveEndTime, DateUtil.getLastTimeOfDate(param.getReserveDate())));

        Date now = DateUtils.currentDate();
        String dayFormat = DateUtils.formatYMD(param.getReserveDate());
        return compartmentTimeList.stream().map(compartmentTime -> {
            AppCompartmentTimeExModel model = BeanUtils.convertTo(compartmentTime, AppCompartmentTimeExModel::new);
            //营业开始和结束时间处理
            model.setStatus(statusHandle(compartmentTime.getStartTime(), compartmentTime.getEndTime(), now, dayFormat, compartmentReserveList));
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 营业时间状态处理
     *
     * @param startTime              营业开始时间
     * @param endTime                营业结束时间
     * @param now                    当前时间
     * @param dayFormat              当天日期
     * @param compartmentReserveList 预约时间集合
     * @return 状态:0->可预订;1->时段已过;2->已被预定
     */
    private int statusHandle(String startTime, String endTime, Date now, String dayFormat, List<CompartmentReserve> compartmentReserveList) {
        Date start = DateUtils.parse(dayFormat + " " + startTime, "yyyy-MM-dd HH:mm");
        Date end = DateUtils.parse(dayFormat + " " + endTime, "yyyy-MM-dd HH:mm");
        if (end.compareTo(now) <= 0) {
            //时段已过
            return 1;
        }
        if (!CollectionUtils.isNotEmpty(compartmentReserveList)) {
            return 0;
        }
        for (CompartmentReserve reserve : compartmentReserveList) {
            if (DateUtil.isOverlap(start, end, reserve.getReserveStartTime(), reserve.getReserveEndTime())) {
                //如果营业时间范围和预约时间范围有重合，则说明已经被预约
                return 2;
            }
        }
        return 0;
    }

    /***
     * @Description 获取用户信息
     * @author huangyongtao
     * @date 2024/8/2 10:34
     * @param userId
     */
    private UserInfoModel getUser(String userId) {
        return Objects.requireNonNull(userApiService.detail(userId));
    }

    /**
     * 判断是否在营业中
     */
    private boolean isRange(List<CompartmentTime> timeEntityList, Date now, String dayFormat) {
        if (CollectionUtils.isNotEmpty(timeEntityList)) {
            boolean isWithinTimeRange = timeEntityList.stream().anyMatch(time -> {
                Date startTime = DateUtils.parse(dayFormat + " " + time.getStartTime(), "yyyy-MM-dd HH:mm");
                Date endTime = DateUtils.parse(dayFormat + " " + time.getEndTime(), "yyyy-MM-dd HH:mm");

                return now.compareTo(startTime) >= 0 && now.compareTo(endTime) < 0;
            });
            return isWithinTimeRange;
        }
        return false;
    }

    /***
     * @Description 更新过期状态
     * @author huangyongtao
     * @date 2024/8/2 14:27
     * @param
     */
    public Boolean updateStatus() {
        return this.lambdaUpdate().eq(CompartmentReserve::getReserveStatus, CompartmentReserveStatusEnum.RESERVED.getCode())
        .le(CompartmentReserve::getReserveEndTime, new Date())
        .set(CompartmentReserve::getReserveStatus, CompartmentReserveStatusEnum.EXPIRED.getCode())
                .update();
    }

    /***
     * @Description 查询包间预约信息
     * @author huangyongtao
     * @date 2024/8/7 17:13
     * @param param
     */
    @Override
    public List<CompartmentReserveModel> findCompartmentReserve(CompartmentReserveParam param) {
        param.setReserveStatusList(Arrays.asList(CompartmentReserveStatusEnum.RESERVED.getCode(), CompartmentReserveStatusEnum.ARRIVED.getCode()));
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        List<CompartmentReserve> compartmentReserveList = this.getBaseMapper().selectList(new LambdaQueryWrapper<CompartmentReserve>().eq(ObjectUtil.isNotEmpty(param.getCompartmentId()), CompartmentReserve::getCompartmentId, param.getCompartmentId())
                .eq(ObjectUtil.isNotEmpty(param.getReserveStatus()), CompartmentReserve::getReserveStatus, param.getReserveStatus())
                .in(CollectionUtil.isNotEmpty(param.getReserveStatusList()), CompartmentReserve::getReserveStatus, param.getReserveStatusList())
                .eq(ObjectUtil.isNotEmpty(param.getTenantId()), CompartmentReserve::getTenantId, param.getTenantId()));
        if (CollectionUtil.isEmpty(compartmentReserveList)) {
            return new ArrayList<>();
        }
        return BeanUtils.convertListTo(compartmentReserveList, CompartmentReserveModel::new);
    }

    /***
     * @Description 查询包间营业时间范围
     * @author huangyongtao
     * @date 2024/8/8 14:46
     * @param param
     */
    @Override
    public CompartmentTimeModel findTimeRange(CompartmentReserveParam param) {
        CompartmentTimeModel timeModel = new CompartmentTimeModel();
        List<CompartmentTimeModel> timeModels = compartmentTimeService.list(param.getRestaurantId());
        if (CollectionUtil.isEmpty(timeModels)) {
            return timeModel;
        }
        List<String> dates = new ArrayList<>();
        timeModels.forEach(p -> {
            dates.add(p.getEndTime());
            dates.add(p.getStartTime());
        });
        List<String> sortDates = dates.stream().sorted().collect(Collectors.toList());
        timeModel.setStartTime(sortDates.get(0));
        timeModel.setEndTime(sortDates.get(sortDates.size() - 1));
        return timeModel;
    }

    @Override
    public AppSimpleReserveModel getNearest() {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        String userId = WebFrameworkUtils.getHeaderUserId();

        CompartmentReserve reserve = getOne(Wrappers.<CompartmentReserve>lambdaQuery().eq(tenantId != null, CompartmentReserve::getTenantId, tenantId)
                .eq(CompartmentReserve::getSubscriberId, userId).eq(CompartmentReserve::getReserveStatus, CompartmentReserveStatusEnum.RESERVED.getCode())
                .ge(CompartmentReserve::getReserveStartTime, new Date()).orderByAsc(CompartmentReserve::getReserveStartTime).last("limit 1"));
        if (reserve == null || reserve.getId() == null) {
            return new AppSimpleReserveModel();
        }
        AppSimpleReserveModel model = BeanUtils.convertTo(reserve, AppSimpleReserveModel::new);
        model.setStartTime(reserve.getReserveStartTime());
        model.setEndTime(reserve.getReserveEndTime());
        //餐厅名称
        Compartment compartment = compartmentService.getById(reserve.getCompartmentId());
        if (compartment != null) {
            Restaurant restaurant = restaurantService.getById(compartment.getRestaurantId());
            Optional.ofNullable(restaurant).ifPresent(r -> model.setRestaurantName(r.getName()));
        }
        return model;
    }
}
