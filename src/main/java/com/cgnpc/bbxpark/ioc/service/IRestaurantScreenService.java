package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.RestaurantLinePageParam;

import java.util.Date;
import java.util.List;

/**
 * 餐厅大屏数据服务接口
 */
public interface IRestaurantScreenService {

    /**
     * 获取模型餐线详情
     * @param sslcCode 所属楼层物模型编码
     * @return 餐线详情
     */
    RestaurantLineModel getLineDetail(String sslcCode);

    /**
     * 获取餐厅实况
     * @return 餐厅实况
     */
    RestaurantOverviewModel getOverview();

    /**
     * 获取供餐状态
     * @return 供餐状态列表
     */
    List<RestaurantLineStatusModel> getDiningLineStatus();

    /**
     * 获取包间实况
     *
     * @return 包间实况列表
     */
    RestaurantCompartmentOverviewModel getCompartmentOverview();

    /**
     * 获取餐厅包间预定趋势(近30天)
     * @return 包间预定趋势
     */
    RestaurantCompartmentTrendModel getCompartmentReservedTrend();

    /**
     * 获取餐厅餐线营业时间信息
     * @return 餐厅列表
     */
    List<RestaurantModel> getList();

    /**
     * 获取餐线的菜品信息
     * @param lineId 餐线id
     * @param timeType 餐线营业时间类型
     * @return 菜品列表
     */
    List<RestaurantDishesScheduleModel> getLineDishes(Long lineId, String timeType);

    /**
     * 获取餐线的菜品信息(分页)
     * @param param 分页参数
     * @return 分页菜品列表
     */
    IPage<RestaurantDishesScheduleModel> pageLineDishes(RestaurantLinePageParam param);

    /**
     * 获取实时评价
     * @return 评价列表
     */
    List<RestaurantDishesEvaluateModel> getCompartmentEvaluate();

    /**
     * 获取员工诉求
     * @return 诉求列表
     */
    List<RestaurantComplaintSuggestionModel> getComplaintSuggestion();

    /**
     * 获取供应商画像
     * @return 供应商列表
     */
    List<RestaurantSupplierModel> getSupplier();

    /**
     * 获取餐料库存分析
     * @return 库存分析列表
     */
    List<RestaurantInventoryCategoryModel> getInventoryAnalysis();

    /**
     * 获取入库数量分析
     * @param yearDate 时间
     * @return 入库趋势列表
     */
    List<RestaurantInventoryInboundTrendModel> getInventoryInboundTrend(Date yearDate);

    /**
     * 获取后厨人员情况
     * @return 人员情况列表
     */
    RestaurantKitchenStaffModel getKitchenStaffSituation();

    /**
     * 获取餐厅废弃物
     * @param timeType 时间类型(week:周，month：月，year：年)
     * @return 废弃物统计列表
     */
    List<RestaurantWasteStatisticsModel> getWasteStatistics(String timeType);

    /**
     * 获取智能曝光台
     * @return 告警列表
     */
    List<RestaurantDeviceAlarmModel> getNoticeSmartExposure();

    /**
     * 获取营收总览
     * @return 营收总览
     */
    RestaurantRevenueOverview getRevenueOverview();

    /**
     * 获取消费次数分析
     * @param yearDate 时间
     * @param id 餐厅id
     * @return 消费次数列表
     */
    List<RestaurantConsumptionCountItem> getCountAnalysis(Date yearDate, Long id);

    /**
     * 获取消费能力分析
     * @param monthDate 时间
     * @return 消费能力趋势列表
     */
    List<RestaurantConsumptionAbilityTrend> getConsumptionAbilityTrend(Date monthDate);

    /**
     * 获取用餐行为分析
     * @param monthDate 时间
     * @return 用餐行为
     */
    RestaurantConsumptionBehavior getConsumptionBehavior(Date monthDate);

    /**
     * 获取员工关怀
     *
     * @return 员工消费信息
     */
    List<RestaurantEmployeeConsumption> getEmployeeConsumptionList();

    /**
     * 获取消费占比分析
     *
     * @param yearDate 时间
     * @param id       餐厅id
     * @return 消费占比
     */
    List<RestaurantConsumptionShareItem> getConsumptionShareAnalysis(Date yearDate, Long id);
}
