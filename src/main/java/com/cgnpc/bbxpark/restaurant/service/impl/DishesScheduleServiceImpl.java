
package com.cgnpc.bbxpark.restaurant.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.restaurant.domain.DishesEvaluate;
import com.cgnpc.bbxpark.restaurant.domain.DishesGallery;
import com.cgnpc.bbxpark.restaurant.domain.DishesSchedule;
import com.cgnpc.bbxpark.restaurant.domain.MealLine;
import com.cgnpc.bbxpark.restaurant.dto.model.AppDishesScheduleModel;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesScheduleModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import com.cgnpc.bbxpark.restaurant.mapper.DishesScheduleRepository;
import com.cgnpc.bbxpark.restaurant.service.IDishesEvaluateService;
import com.cgnpc.bbxpark.restaurant.service.IDishesGalleryService;
import com.cgnpc.bbxpark.restaurant.service.IDishesScheduleService;
import com.cgnpc.bbxpark.restaurant.service.IMealLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜品排班服务实现
 * @author dingfan
 * @date 2024/7/22
 */
@Service
public class DishesScheduleServiceImpl extends ServiceImpl<DishesScheduleRepository, DishesSchedule> implements IDishesScheduleService {
    @Autowired
    private IDishesEvaluateService dishesEvaluateService;
    @Autowired
    private IMealLineService mealLineService;
    @Autowired
    private IDishesGalleryService dishesGalleryService;

    @Override
    public IPage<DishesScheduleModel> page(DishesSchedulePageParam param) {
        IPage<DishesSchedule> page = baseMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()), buildSearchWrapper(BeanUtils.convertTo(param,DishesScheduleListParam::new)));
        //批量查询餐线信息
        Map<Long,String> mealLineMap = getMealLineMap(page.getRecords().stream().map(DishesSchedule::getMealLineId).collect(Collectors.toList()));
        //批量查询菜品库信息
        Map<String,DishesGallery> dishesGalleryMap = getDishesMap(page.getRecords().stream().map(DishesSchedule::getName).collect(Collectors.toList()));
        //返回结果组装
        DecimalFormat df = new DecimalFormat("0.00");
        List<DishesScheduleModel> list = page.getRecords().stream().map(dishesSchedule -> {
            DishesScheduleModel model = new DishesScheduleModel();
            BeanUtils.copyProperties(dishesSchedule, model);
            //餐线名称
            model.setMealLineName(mealLineMap.get(model.getMealLineId()));
            //菜品图片
            Optional.ofNullable(dishesGalleryMap.get(model.getName())).ifPresent(g->model.setImageUrl(g.getImageUrl()));
            //价格格式化
            model.setPrice(df.format(dishesSchedule.getPrice()));
            return model;
        }).collect(Collectors.toList());
        return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), list);
    }

    @Override
    public List<DishesScheduleModel> list(DishesScheduleListParam param) {
        //查询条件组装
        LambdaQueryWrapper<DishesSchedule> wrapper = buildSearchWrapper(param);
        List<DishesSchedule> list = list(wrapper);
        //批量查询餐线信息
        Map<Long,String> mealLineMap = getMealLineMap(list.stream().map(DishesSchedule::getMealLineId).collect(Collectors.toList()));
        //批量查询菜品库信息
        Map<String,DishesGallery> dishesGalleryMap = getDishesMap(list.stream().map(DishesSchedule::getName).collect(Collectors.toList()));
        DecimalFormat df = new DecimalFormat("0.00");
        return list.stream().map(dishesSchedule -> {
            DishesScheduleModel model = new DishesScheduleModel();
            BeanUtils.copyProperties(dishesSchedule, model);
            //餐线名称
            model.setMealLineName(mealLineMap.get(model.getMealLineId()));
            //菜品图片
            Optional.ofNullable(dishesGalleryMap.get(model.getName())).ifPresent(g->model.setImageUrl(g.getImageUrl()));
            //价格格式化
            model.setPrice(df.format(dishesSchedule.getPrice()));
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AppDishesScheduleModel> listApp(AppDishesScheduleListParam param) {
        DishesScheduleListParam listParam = BeanUtils.convertTo(param,DishesScheduleListParam::new);
        listParam.setStatus(Status.enabled.getKey());
        //仅能查出启用的餐线菜品
        List<MealLine> mealLineList = mealLineService.list(Wrappers.<MealLine>lambdaQuery().eq(MealLine::getRestaurantId,param.getRestaurantId()).eq(MealLine::getStatus,Status.enabled.getKey()));
        List<Long> mealLineIdList = mealLineList.stream().map(MealLine::getId).collect(Collectors.toList());
        //如果没有有用的餐线，为避免条件不执行，设置一条不存在的餐线id
        listParam.setMealLineIds(CollectionUtils.isNotEmpty(mealLineIdList)?mealLineIdList:new ArrayList<>(Collections.singletonList(-1L)));
        //查询条件组装
        LambdaQueryWrapper<DishesSchedule> wrapper = buildSearchWrapper(listParam);
        List<DishesSchedule> list = list(wrapper);

        //批量查询菜品库信息
        Map<String,DishesGallery> dishesGalleryMap = getDishesMap(list.stream().map(DishesSchedule::getName).collect(Collectors.toList()));
        DecimalFormat df = new DecimalFormat("0.00");
        return list.stream().map(dishesSchedule -> {
            AppDishesScheduleModel model = BeanUtils.convertTo(dishesSchedule, AppDishesScheduleModel::new);
            //菜品图片
            if(dishesGalleryMap.containsKey(model.getName())){
                model.setImageUrl(dishesGalleryMap.get(model.getName()).getImageUrl());
                model.setSatisfaction(dishesGalleryMap.get(model.getName()).getSatisfaction());
            }
            if(model.getSatisfaction() == null){
                model.setSatisfaction(getSatisfaction(model.getName(),dishesGalleryMap.containsKey(model.getName())));
            }
            //价格格式化
            model.setPrice(df.format(dishesSchedule.getPrice()));
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> listType(DishesTypeListParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        //查询本周菜品排班集合
        List<DishesSchedule> scheduleList = list(Wrappers.<DishesSchedule>lambdaQuery().eq(DishesSchedule::getRestaurantId,param.getRestaurantId())
                .between(DishesSchedule::getProductionDate, com.cgnpc.bbxpark.common.utils.DateUtil.getFirstTimeOfWeek(), com.cgnpc.bbxpark.common.utils.DateUtil.getLastTimeOfWeek())
                .eq(DishesSchedule::getStatus,Status.enabled.getKey()).eq(DishesSchedule::getDeleted, Delete.NORMAL.getKey()).orderByDesc(DishesSchedule::getCreateTime)
                .eq(tenantId != null,DishesSchedule::getTenantId,tenantId));
        //筛选本周菜品类型集合
        return scheduleList.stream().map(DishesSchedule::getType).distinct().collect(Collectors.toList());
    }

    @Override
    public DishesScheduleModel detail(Long id) {
        DishesSchedule schedule = getById(id);
        AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
        DishesScheduleModel model = BeanUtils.convertTo(schedule, DishesScheduleModel::new);
        //餐线名称
        model.setMealLineName(getMealLine(schedule.getMealLineId()).getName());
        //菜品图片
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        DishesGallery gallery = dishesGalleryService.getOne(Wrappers.<DishesGallery>lambdaQuery().eq(DishesGallery::getName,schedule.getName()).eq(tenantId != null,DishesGallery::getTenantId,tenantId));
        Optional.ofNullable(gallery).ifPresent(g->model.setImageUrl(g.getImageUrl()));
        //价格格式化
        DecimalFormat df = new DecimalFormat("0.00");
        model.setPrice(df.format(schedule.getPrice()));
        return model;
    }

    @Override
    public AppDishesScheduleModel detailApp(Long id) {
        DishesSchedule schedule = getById(id);
        AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
        AppDishesScheduleModel model = BeanUtils.convertTo(schedule, AppDishesScheduleModel::new);
        //价格格式化
        DecimalFormat df = new DecimalFormat("0.00");
        model.setPrice(df.format(schedule.getPrice()));
        //餐线名称
        model.setMealLineName(getMealLine(schedule.getMealLineId()).getName());
        //菜品图片
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        DishesGallery gallery = dishesGalleryService.getOne(Wrappers.<DishesGallery>lambdaQuery().eq(DishesGallery::getName,schedule.getName()).eq(tenantId != null,DishesGallery::getTenantId,tenantId));
        if(gallery != null){
            model.setImageUrl(gallery.getImageUrl());
            model.setSatisfaction(gallery.getSatisfaction());
        }
        if(model.getSatisfaction() == null){
            //如果菜品库平均分未更新,则进行更新
            model.setSatisfaction(getSatisfaction(schedule.getName(),gallery != null));
        }
        //评价总数
        model.setEvaluateTotal(dishesEvaluateService.count(Wrappers.<DishesEvaluate>lambdaQuery().eq(DishesEvaluate::getName,schedule.getName()).eq(tenantId != null,DishesEvaluate::getTenantId,tenantId)));
        return model;
    }

    @Override
    public Boolean add(DishesScheduleParam param) {
        param.setProductionDate(DateUtils.format(DateUtils.formatYMD(param.getProductionDate())));
        //出品日期+菜品名称唯一性校验
        AssertUtils.isFalse(verifyNameAndDate(param), "新增菜品与日期重复，请重试");
        AssertUtils.isTrue(verifyProductionDate(param.getProductionDate()),"出品日期仅支持选择本周及下周时间");

        DishesSchedule schedule = BeanUtils.convertTo(param, DishesSchedule::new);
        //星期几
        schedule.setWeek(getWeek(param.getProductionDate()).toString());
        //默认字段
        schedule.setId(null);
        schedule.setStatus( Status.enabled.getKey());
        schedule.setDeleted( Delete.NORMAL.getKey());
        return save(schedule);
    }


    /**
     * 批量新增菜品排班
     *
     * @param dishesScheduleParams 参数
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean adds(List<DishesScheduleParam> dishesScheduleParams) {

        DishesScheduleListParam param = new DishesScheduleListParam();
        param.setMealLineIds(dishesScheduleParams.stream().map(DishesScheduleParam::getMealLineId).distinct().collect(Collectors.toList()));
        List<DishesScheduleModel> dishesScheduleModels = list(param);
        Map<String, List<DishesScheduleModel>> dishesMap = dishesScheduleModels.stream()
                .collect(Collectors.groupingBy(a -> a.getMealLineId() + "-" + DateUtil.format(a.getProductionDate(),"yyyy-MM-dd") + "-" + a.getMealTime()+ "-" + a.getName()));
        List<DishesScheduleParam> addParams = new ArrayList<>();
        List<DishesScheduleParam> updataParams = new ArrayList<>();
        //校验数据
        for (DishesScheduleParam f : dishesScheduleParams) {
            String key = f.getMealLineId() + "-" + DateUtil.format(f.getProductionDate(),"yyyy-MM-dd") + "-" + f.getMealTime()+ "-" + f.getName();
            if (dishesMap.containsKey(key)) {
                f.setId(dishesMap.get(key).get(0).getId());
                updataParams.add(f);
            } else {
                addParams.add(f);
            }
        }
        //批量保存数据
        if (CollUtil.isNotEmpty(addParams)){
            batchSaveDishesSchedule(addParams);
        }
        //批量编辑数据
        if (CollUtil.isNotEmpty(updataParams)){
            batchUpdataDishesSchedule(updataParams);
        }
        return Boolean.TRUE;
    }

    /**
     * 批量编辑菜品排班
     *
     * @param updataParams 参数
     * @return 批量编辑菜品排班
     */
    private void batchUpdataDishesSchedule(List<DishesScheduleParam> updataParams) {
        List<DishesSchedule> dishesSchedules = BeanUtils.convertListTo(updataParams, DishesSchedule::new);
        updateBatchById(dishesSchedules);
    }


    /**
     * 批量新增菜品排班
     *
     * @param addParams 参数
     * @return 新增结果
     */
    private void batchSaveDishesSchedule(List<DishesScheduleParam> addParams) {
        List<DishesSchedule> dishesSchedules = BeanUtils.convertListTo(addParams, DishesSchedule::new);
        dishesSchedules.forEach(f-> {
            //星期几
            f.setWeek(getWeek(f.getProductionDate()).toString());
            //默认字段
            f.setId(null);
            f.setStatus( Status.enabled.getKey());
            f.setDeleted( Delete.NORMAL.getKey());
        });
        saveBatch(dishesSchedules);
    }

    @Override
    public Boolean edit(DishesScheduleParam param) {
        param.setProductionDate(DateUtils.format(DateUtils.formatYMD(param.getProductionDate())));
        //出品日期+菜品名称唯一性校验
        AssertUtils.isFalse(verifyNameAndDate(param), "菜品与日期重复，请重试");
        AssertUtils.isTrue(verifyProductionDate(param.getProductionDate()),"出品日期仅支持选择本周及下周时间");

        DishesSchedule schedule = getById(param.getId());
        AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
        //历史数据无法编辑校验
        boolean historyFlag = schedule.getProductionDate().before(com.cgnpc.bbxpark.common.utils.DateUtil.getFirstTimeOfCurrent());
        AssertUtils.isFalse(historyFlag,"历史菜品无法编辑");
        param.setId(schedule.getId());
        BeanUtils.copyProperties(param, schedule);
        //星期几
        schedule.setWeek(getWeek(param.getProductionDate()).toString());
        return updateById(schedule);
    }

    @Override
    public Boolean enable(Long id) {
        DishesSchedule schedule = getById(id);
        AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
        schedule.setStatus( Status.enabled.getKey());
        return updateById(schedule);
    }

    @Override
    public Boolean disable(Long id) {
        DishesSchedule schedule = getById(id);
        AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
        schedule.setStatus( Status.disabled.getKey());
        return updateById(schedule);
    }

    @Override
    public Boolean remove(Long id) {
        DishesSchedule schedule = getById(id);
        AssertUtils.isFalse(Objects.equals(Status.enabled.getKey(), schedule.getStatus()), "请将菜品下架后再删除");
        AssertUtils.notNull(schedule, SystemResultCode.RESULT_DATA_NONE.message());
        schedule.setDeleted( Delete.DELETED.getKey());
        return updateById(schedule);
    }

    /**
     * 构建搜索条件
     * @param param 参数
     * @return 条件
     */
    private LambdaQueryWrapper<DishesSchedule> buildSearchWrapper(DishesScheduleListParam param){
        //查询条件组装
        LambdaQueryWrapper<DishesSchedule> wrapper = Wrappers.lambdaQuery();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        wrapper.eq(param.getRestaurantId() != null, DishesSchedule::getRestaurantId, param.getRestaurantId())
                .eq(param.getMealLineId() != null, DishesSchedule::getMealLineId, param.getMealLineId())
                .like(StringUtils.isNotEmpty(param.getName()), DishesSchedule::getName, param.getName())
                .eq(StringUtils.isNotEmpty(param.getType()), DishesSchedule::getType, param.getType())
                .eq(StringUtils.isNotEmpty(param.getMealTime()), DishesSchedule::getMealTime, param.getMealTime())
                .in(CollUtil.isNotEmpty(param.getMealLineIds()), DishesSchedule::getMealLineId, param.getMealLineIds())
                .eq(param.getStatus() != null, DishesSchedule::getStatus, param.getStatus())
                .eq(param.getProductionDate() != null, DishesSchedule::getProductionDate, DateUtils.format(DateUtils.formatYMD(param.getProductionDate())))
                .ge(param.getStartDate() != null, DishesSchedule::getProductionDate, DateUtils.format(DateUtils.formatYMD(param.getStartDate())))
                .le(param.getEndDate() != null, DishesSchedule::getProductionDate, DateUtils.format(DateUtils.formatYMD(param.getEndDate())))
                .eq(DishesSchedule::getDeleted, Delete.NORMAL.getKey())
                .eq(tenantId != null,DishesSchedule::getTenantId,tenantId)
                .orderByDesc(DishesSchedule::getCreateTime);
        return wrapper;
    }

    /**
     * 餐线+菜品类型+菜品名称+出品日期唯一性校验
     *
     * @param param 菜品排班信息
     * @return 结果
     */
    private boolean verifyNameAndDate(DishesScheduleParam param) {
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        return count(Wrappers.<DishesSchedule>lambdaQuery().eq(DishesSchedule::getName, param.getName())
                .eq(DishesSchedule::getProductionDate, param.getProductionDate())
                .eq(DishesSchedule::getMealLineId,param.getMealLineId())
                .eq(DishesSchedule::getMealTime,param.getMealTime())
                .eq(DishesSchedule::getDeleted, Delete.NORMAL.getKey())
                .eq(tenantId != null,DishesSchedule::getTenantId,tenantId)
                //如果id不为空,那么说明是编辑,需排除自身
                .ne(param.getId() != null, DishesSchedule::getId, param.getId())) > 0;
    }

    /**
     * 获取星期几
     * 1-7为周一到周日
     */
    private static Integer getWeek(Date date){
        int weekday = DateUtil.dayOfWeek(date);
        weekday--;
        return weekday ==0 ? 7 : weekday;
    }

    /**
     * 出品日期时间范围校验
     * @param date 时间
     * @return 校验结果
     */
    public static Boolean verifyProductionDate(Date date) {
        DateTime productionDate = DateUtil.parse(DateUtil.format(date,"yyyy-MM-dd"));
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int daysRemaining = Calendar.SATURDAY - dayOfWeek + 1;
        DateTime nowTime = DateUtil.parse(DateUtil.date().toString("yyyy-MM-dd"), "yyyy-MM-dd");
        if (!productionDate.isBefore(nowTime) && !productionDate.isAfter(DateUtil.offsetDay(nowTime, daysRemaining + 7))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 根据id集合查询餐线map
     * @param idList id集合
     * @return 餐线id-名称 map
     */
    private Map<Long,String> getMealLineMap(List<Long> idList){
        if(!CollectionUtils.isNotEmpty(idList)){
            return new HashMap<>(4);
        }
        List<MealLine> list = mealLineService.list(Wrappers.<MealLine>lambdaQuery().in(MealLine::getId,idList).eq(MealLine::getDeleted,Status.enabled.getKey()));
        return list.stream().collect(Collectors.toMap(MealLine::getId, MealLine::getName, (k1, k2) -> k2));
    }

    /**
     * 根据id获取餐线信息
     * @param id id
     * @return 餐线信息
     */
    private MealLine getMealLine(Long id){
        MealLine mealLine = mealLineService.getById(id);
        AssertUtils.notNull(mealLine,SystemResultCode.RESULT_DATA_NONE.message());
        return mealLine;
    }

    /**
     * 根据菜品名称集合查询菜品库信息
     * @param nameList 菜品名称集合
     * @return 菜品名称-菜品信息 map
     */
    private Map<String,DishesGallery> getDishesMap(List<String> nameList){
        if(!CollectionUtils.isNotEmpty(nameList)){
            return new HashMap<>(4);
        }
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        List<DishesGallery> list = dishesGalleryService.list(Wrappers.<DishesGallery>lambdaQuery().in(DishesGallery::getName,nameList).eq(tenantId != null,DishesGallery::getTenantId,tenantId));
        return list.stream().collect(Collectors.toMap(DishesGallery::getName, dishesGallery -> dishesGallery, (k1, k2) -> k2));
    }

    /**
     * 获取菜品平均分
     * @param name 菜品名称
     * @param existDishes 菜品是否存在
     * @return 平均分
     */
    private Double getSatisfaction(String name,boolean existDishes){
        //如果菜品不存在,则计算平均分,如果菜品存在,则计算平均分并更新菜品库,避免下次重复计算
        return existDishes ? dishesEvaluateService.calculateAverageAndUpdate(name,null):dishesEvaluateService.calculateAverage(name,null);
    }
}
