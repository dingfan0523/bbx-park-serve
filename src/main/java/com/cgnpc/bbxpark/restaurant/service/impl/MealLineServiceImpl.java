
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.restaurant.domain.*;
import com.cgnpc.bbxpark.restaurant.dto.model.AppCompartmentTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.model.AppMealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.model.MealLineModel;
import com.cgnpc.bbxpark.restaurant.dto.model.MealLineTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.mapper.DishesScheduleRepository;
import com.cgnpc.bbxpark.restaurant.mapper.MealLineRepository;
import com.cgnpc.bbxpark.restaurant.mapper.RestaurantRepository;
import com.cgnpc.bbxpark.restaurant.service.IMealLinePosService;
import com.cgnpc.bbxpark.restaurant.service.IMealLineService;
import com.cgnpc.bbxpark.restaurant.service.IMealLineTimeService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 餐线服务实现
 * @author dingfan
 * @date 2024/7/18
 */
@Service
public class MealLineServiceImpl extends ServiceImpl<MealLineRepository, MealLine> implements IMealLineService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private DishesScheduleRepository dishesScheduleRepository;
    @Autowired
    private IMealLinePosService mealLinePosService;
    @Autowired
    private IMealLineTimeService mealLineTimeService;
    @Autowired
    private IIocDeviceService iocDeviceService;

    @Override
    public IPage<MealLineModel> page(MealLinePageParam param) {
        IPage<MealLine> page = baseMapper.selectPage(new Page<>(param.getCurrent(),param.getSize()),buildSearchWrapper(param.getRestaurantId(),param.getName(),param.getType(),param.getStatus()));
        //批量查询菜品库信息
        Map<Long,String> restaurantMap = getRestaurantMap(page.getRecords().stream().map(MealLine::getRestaurantId).collect(Collectors.toList()));
        //批量查询设备信息
        Map<Long,String> deviceMap = getDeviceMap(page.getRecords().stream().map(MealLine::getDeviceId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList()));
        List<Long> ids = page.getRecords().stream().map(MealLine::getId).collect(Collectors.toList());
        //营业时间
        List<MealLineTime> timeList = CollectionUtil.isEmpty(ids) ? Collections.emptyList() : mealLineTimeService.list(new LambdaQueryWrapper<MealLineTime>().in(MealLineTime::getMealLineId, ids));
        Map<Long, List<MealLineTime>> timeMap = CollectionUtil.isEmpty(timeList) ? Collections.emptyMap() : timeList.stream().collect(Collectors.groupingBy(MealLineTime::getMealLineId));
        //pos号
        List<MealLinePos> posList = CollectionUtil.isEmpty(ids) ? Collections.emptyList() : mealLinePosService.list(new LambdaQueryWrapper<MealLinePos>().in(MealLinePos::getMealLineId, ids));
        Map<Long, List<MealLinePos>> posMap = CollectionUtil.isEmpty(posList) ? Collections.emptyMap() : posList.stream().collect(Collectors.groupingBy(MealLinePos::getMealLineId));
        //返回结果组装
        List<MealLineModel> list = page.getRecords().stream().map(mealLine -> {
            MealLineModel model = new MealLineModel();
            BeanUtils.copyProperties(mealLine,model);
            //餐厅名称
            model.setRestaurantName(restaurantMap.get(model.getRestaurantId()));
            if(ObjectUtil.isNotEmpty(model.getDeviceId())){
                //设备名称
                model.setDeviceName(deviceMap.get(model.getDeviceId()));
            }
            if(CollectionUtil.isNotEmpty(timeMap.get(model.getId()))){
                model.setTimeModels(BeanUtils.convertListTo(timeMap.get(model.getId()), MealLineTimeModel::new));
            }
            if(CollectionUtil.isNotEmpty(posMap.get(model.getId()))){
                model.setPosList(posMap.get(model.getId()).stream().map(MealLinePos::getPos).collect(Collectors.toList()));
            }
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(),list);
    }

    @Override
    public List<MealLineModel> list(MealLineListParam param) {
        List<MealLine> list = list(buildSearchWrapper(param.getRestaurantId(),param.getName(),param.getType(),param.getStatus()));
        //批量查询菜品库信息
        Map<Long,String> restaurantMap = getRestaurantMap(list.stream().map(MealLine::getRestaurantId).collect(Collectors.toList()));
        //批量查询设备信息
        Map<Long,String> deviceMap = getDeviceMap(list.stream().map(MealLine::getDeviceId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList()));
        //返回结果组装
        return list.stream().map(mealLine -> {
            MealLineModel model = new MealLineModel();
            BeanUtils.copyProperties(mealLine,model);
            //餐厅名称
            model.setRestaurantName(restaurantMap.get(model.getRestaurantId()));
            if(ObjectUtil.isNotEmpty(model.getDeviceId())){
                //设备名称
                model.setDeviceName(deviceMap.get(model.getDeviceId()));
            }
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AppMealLineModel> listApp(AppMealLineListParam param) {
        LambdaQueryWrapper<MealLine> wrapper = buildSearchWrapper(param.getRestaurantId(),null,null, Status.enabled.getKey());
        //返回结果组装
        return list(wrapper).stream().map(mealLine -> BeanUtils.convertTo(mealLine,AppMealLineModel::new)).collect(Collectors.toList());
    }

    @Override
    public MealLineModel detail(Long id) {
        MealLine mealLine = getById(id);
        AssertUtils.notNull(mealLine, SystemResultCode.RESULT_DATA_NONE.message());
        MealLineModel model = BeanUtils.convertTo(mealLine,MealLineModel::new);
        //餐厅名称
        model.setRestaurantName(getRestaurant(mealLine.getRestaurantId()).getName());
        //营业时间
        List<MealLineTime> timeList = mealLineTimeService.list(new LambdaQueryWrapper<MealLineTime>().eq(MealLineTime::getMealLineId, id));
        if(CollectionUtil.isNotEmpty(timeList)){
            model.setTimeModels(BeanUtils.convertListTo(timeList, MealLineTimeModel::new));
        }
        //pos号
        List<MealLinePos> posList = mealLinePosService.list(new LambdaQueryWrapper<MealLinePos>().eq(MealLinePos::getMealLineId, id));
        if(CollectionUtil.isNotEmpty(timeList)){
            model.setPosList(posList.stream().map(MealLinePos::getPos).collect(Collectors.toList()));
        }
        return model;
    }

    @Override
    public Boolean add(MealLineParam param) {
        //餐线名称唯一性校验
        AssertUtils.isFalse(verifyName(param.getId(), param.getName()), "餐线名称重复");
        List<MealLineTimeParam> timeParams = param.getMealLineTimeParams().stream()
                .filter(f -> StrUtil.isNotEmpty(f.getStartTime()) && StrUtil.isNotEmpty(f.getEndTime())).collect(Collectors.toList());
        //校验数据
        validate(timeParams);
        MealLine mealLine = BeanUtils.convertTo(param,MealLine::new);
        //默认字段
        mealLine.setId(null);
        mealLine.setStatus(Status.enabled.getKey());
        save(mealLine);
        Long id = mealLine.getId();
        //添加餐线营业时间表
        if (CollUtil.isNotEmpty(timeParams)) {
            List<MealLineTime> mealLineTimes = BeanUtils.convertListTo(timeParams, MealLineTime::new);
            mealLineTimes.forEach(f -> {
                f.setMealLineId(id);
            });
            mealLineTimeService.saveBatch(mealLineTimes);
        }
        //添加餐线pos号
        if (CollUtil.isNotEmpty(param.getPosList())) {
            List<MealLinePos> posList = new ArrayList<>();
            param.getPosList().forEach(f -> {
                MealLinePos pos = new MealLinePos();
                pos.setMealLineId(id);
                pos.setPos(f);
                posList.add(pos);
            });
            mealLinePosService.saveBatch(posList);
        }
        return true;
    }



    @Override
    public Boolean edit(MealLineParam param) {
        //餐线名称唯一性校验
        AssertUtils.isFalse(verifyName(param.getId(), param.getName()), "餐线名称重复");
        MealLine mealLine = getById(param.getId());
        AssertUtils.notNull(mealLine, SystemResultCode.RESULT_DATA_NONE.message());
        //餐线菜品校验(餐线下存在菜品周排数据时,无法为餐线更改餐厅)
        AssertUtils.isFalse(verifyRestaurantId(mealLine.getRestaurantId(),param.getRestaurantId(),mealLine.getId()),"该餐线下已有菜品，不可更改");
        List<MealLineTimeParam> timeParams = param.getMealLineTimeParams().stream()
                .filter(f -> StrUtil.isNotEmpty(f.getStartTime()) && StrUtil.isNotEmpty(f.getEndTime())).collect(Collectors.toList());
        //校验数据
        validate(timeParams);
        param.setId(mealLine.getId());
        BeanUtils.copyProperties(param,mealLine);
        updateById(mealLine);
        //删除关联表
        mealLinePosService.remove(new LambdaQueryWrapper<MealLinePos>().eq(MealLinePos::getMealLineId, param.getId()));
        mealLineTimeService.remove(new LambdaQueryWrapper<MealLineTime>().eq(MealLineTime::getMealLineId, param.getId()));
        //添加餐线营业时间表
        if (CollUtil.isNotEmpty(timeParams)) {
            List<MealLineTime> mealLineTimes = BeanUtils.convertListTo(timeParams, MealLineTime::new);
            mealLineTimes.forEach(f -> {
                f.setMealLineId(mealLine.getId());
            });
            mealLineTimeService.saveBatch(mealLineTimes);
        }
        //添加餐线pos号
        if (CollUtil.isNotEmpty(param.getPosList())) {
            List<MealLinePos> posList = new ArrayList<>();
            param.getPosList().forEach(f -> {
                MealLinePos pos = new MealLinePos();
                pos.setMealLineId(mealLine.getId());
                pos.setPos(f);
                posList.add(pos);
            });
            mealLinePosService.saveBatch(posList);
        }
        return true;
    }

    @Override
    public Boolean enable(Long id) {
        MealLine mealLine = getById(id);
        AssertUtils.notNull(mealLine, SystemResultCode.RESULT_DATA_NONE.message());
        mealLine.setStatus(Status.enabled.getKey());
        return updateById(mealLine);
    }

    @Override
    public Boolean disable(Long id) {
        MealLine mealLine = getById(id);
        AssertUtils.notNull(mealLine, SystemResultCode.RESULT_DATA_NONE.message());
        mealLine.setStatus(Status.disabled.getKey());
        return updateById(mealLine);
    }

    @Override
    public Boolean remove(Long id) {
        MealLine mealLine = getById(id);
        AssertUtils.isFalse(Objects.equals(Status.enabled.getKey(), mealLine.getStatus()),"请将餐线禁用后再删除");
        AssertUtils.notNull(mealLine, SystemResultCode.RESULT_DATA_NONE.message());
        mealLine.setDeleted(Delete.DELETED.getKey());
        return updateById(mealLine);
    }

    @Override
    public Boolean removeByRestaurantId(Long id){
        AssertUtils.notNull(id,"餐厅id不能为空");
        List<MealLine> list = list(Wrappers.<MealLine>lambdaQuery().eq(MealLine::getRestaurantId,id));
        if(CollectionUtils.isEmpty(list)){
            return true;
        }
        list.forEach(mealLine -> {
            mealLine.setStatus(Status.disabled.getKey());
            mealLine.setDeleted(Delete.DELETED.getKey());
        });
        return updateBatchById(list);
    }

    /**
     * 构建搜索条件
     * @param restaurantId 餐厅id
     * @param name 名称
     * @param type 类型
     * @param status 状态
     * @return 条件
     */
    private LambdaQueryWrapper<MealLine> buildSearchWrapper(Long restaurantId,String name,String type,Integer status){
        LambdaQueryWrapper<MealLine> wrapper = Wrappers.lambdaQuery();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        wrapper.eq(restaurantId != null,MealLine::getRestaurantId,restaurantId)
                .like(StringUtils.isNotEmpty(name),MealLine::getName,name)
                .eq(StringUtils.isNotEmpty(type),MealLine::getType,type)
                .eq(status != null,MealLine::getStatus,status)
                .eq(tenantId != null,MealLine::getTenantId,tenantId)
                .eq(MealLine::getDeleted, Delete.NORMAL.getKey())
                .orderByDesc(MealLine::getCreateTime);
        return wrapper;
    }

    /**
     * 餐线名称唯一性校验
     * @param id id
     * @param name 餐线名称
     * @return 校验结果
     */
    private boolean verifyName(Long id,String name){
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return count(Wrappers.<MealLine>lambdaQuery().eq(MealLine::getName, name)
                .eq(MealLine::getDeleted, Delete.NORMAL.getKey())
                .eq(tenantId != null,MealLine::getTenantId,tenantId)
                //如果id不为空,那么说明是编辑,需排除自身
                .ne(id != null, MealLine::getId, id)) > 0;
    }

    /**
     * 餐线修改绑定餐厅校验
     * 餐线下存在菜品后就无法更改绑定餐厅
     * @param restaurantId 原绑定餐厅id
     * @param newRestaurantId 新绑定餐厅id
     * @param mealLineId 餐线id
     * @return 校验结果
     */
    private boolean verifyRestaurantId(Long restaurantId,Long newRestaurantId,Long mealLineId){
        if(!restaurantId.equals(newRestaurantId)){
            return dishesScheduleRepository.selectCount(Wrappers.<DishesSchedule>lambdaQuery().eq(DishesSchedule::getMealLineId,mealLineId).eq(DishesSchedule::getDeleted,Delete.NORMAL.getKey())) > 0;
        }
        return false;
    }

    /**
     * 根据id集合查询餐厅map
     * @param idList id集合
     * @return 餐厅id-名称 map
     */
    private Map<Long,String> getRestaurantMap(List<Long> idList){
        if(!CollectionUtils.isNotEmpty(idList)){
            return new HashMap<>(4);
        }
        List<Restaurant> list = restaurantRepository.selectList(Wrappers.<Restaurant>lambdaQuery().in(Restaurant::getId,idList).eq(Restaurant::getDeleted,Delete.NORMAL.getKey()));
        return list.stream().collect(Collectors.toMap(Restaurant::getId, Restaurant::getName, (k1, k2) -> k2));
    }

    /**
     * 根据id集合查询设备map
     * @param idList id集合
     * @return 设备id-名称 map
     */
    private Map<Long,String> getDeviceMap(List<Long> idList){
        if(!CollectionUtils.isNotEmpty(idList)){
            return new HashMap<>(4);
        }
        List<IocDevice> list = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery().in(IocDevice::getId,idList).eq(IocDevice::getDeleted,Delete.NORMAL.getKey()));
        return list.stream().collect(Collectors.toMap(IocDevice::getId, IocDevice::getDeviceName, (k1, k2) -> k2));
    }

    /**
     * 获取餐厅数据
     * @param id 餐厅id
     * @return 餐厅数据
     */
    private Restaurant getRestaurant(Long id){
        Restaurant restaurant = restaurantRepository.selectById(id);
        AssertUtils.notNull(restaurant,SystemResultCode.RESULT_DATA_NONE.message());
        return restaurant;
    }

    private void validate(List<MealLineTimeParam> timeList) {
        Set<String> timeSets = timeList.stream().map(MealLineTimeParam::getType).collect(Collectors.toSet());
        AssertUtils.isEquals(timeList.size(), timeSets.size(), "营业时间类型重复，请确认");
        AssertUtils.isTrue(!timeOverlapChecker(timeList), "营业时间重复，请确认");
        timeContinuitylapChecker(timeList);
    }

    private void timeContinuitylapChecker(List<MealLineTimeParam> timeList) {
        for (int i = 0; i < timeList.size(); i++) {
            if (i == (timeList.size() - 1) || (timeList.size() - 1) == 0) {
                continue;
            }
            MealLineTimeParam timeParam = timeList.get(i);
            Date startTime = DateUtils.parse("2024-07-22 " + timeParam.getEndTime(), "yyyy-MM-dd HH:mm");
            MealLineTimeParam nextTime = timeList.get(i + 1);
            Date endTime = DateUtils.parse("2024-07-22 " + nextTime.getStartTime(), "yyyy-MM-dd HH:mm");
            AssertUtils.isTrue((DateUtil.between(startTime, endTime, DateUnit.MINUTE) > 29), "连续营业时间段需要前后间隔半小时");
        }

    }
    private boolean timeOverlapChecker(List<MealLineTimeParam> timeList) {
        ArrayList<MealLineServiceImpl.TimeRange> timeRanges = new ArrayList<MealLineServiceImpl.TimeRange>();
        // 添加时间段到列表
        for (MealLineTimeParam e : timeList) {
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
            timeRanges.add(new MealLineServiceImpl.TimeRange(startTime, endTime));
        }
        // 2. 对时间段列表按照开始时间进行排序
        timeRanges.sort((tr1, tr2) -> tr1.getStartTime().compareTo(tr2.getStartTime()));
        // 3. 遍历时间段列表，判断相邻时间段是否有交叉
        boolean hasOverlap = false;
        Date prevEndTime = null;
        for (MealLineServiceImpl.TimeRange tr : timeRanges) {
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
}
