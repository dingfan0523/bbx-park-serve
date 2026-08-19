package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.RestaurantLinePageParam;
import com.cgnpc.bbxpark.ioc.service.IRestaurantScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 大屏餐厅统计
 */
@RestController
@RequestMapping("/api/dtwin/restaurant")
@Api(tags= "大屏-餐厅统计")
@Slf4j
public class ApiRestaurantScreenController {

    @Autowired
    private IRestaurantScreenService restaurantScreenService;

    @GetMapping(value = "/line/detail")
    @ApiOperation(value = "模型餐线详情")
    public CudResult<RestaurantLineModel> getLineDetail(@ApiParam(value = "所属楼层物模型编码") @RequestParam(required = true) String sslcCode) {
//  模型为餐厅楼层模型时，广告牌展示各个餐线档口名称、营业状态（通过标签UI体现营业和休息）以及档口排队状态（档口关闭时不展示排队状态）。鼠标移入广告牌，窗口展示餐线当前营业状态、人流量、预计等待时间。
        //营业状态：营业中、休息中（数据来源于餐线营业时间，PC-餐线管理已增加此功能）。
        //档口排队状态：根据排队摄像头判断当前排队情况，判断状态取值0~3，分别表示(很稀疏，稀疏，密集，很密集)，大屏相应替换为为：空闲、正常、忙碌、爆满。
        //预计等待时间根据状态估算，如空闲状态5分钟，正常状态10分钟，忙碌状态20分钟、爆满状态30分钟。
        try {
            RestaurantLineModel data = restaurantScreenService.getLineDetail(sslcCode);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/overview")
    @ApiOperation(value = "餐厅实况-餐厅实况")
    public CudResult<RestaurantOverviewModel> getOverview() {
//        (1)餐厅实况：实时数据，页面载入时刷新，每间隔10s刷新。
//        就餐人次：数据来源于核服一卡通刷卡次数
//        摄像机视频：轮播每天所有餐线关联的摄像头实时视频，30S轮播

        try {
            RestaurantOverviewModel data = restaurantScreenService.getOverview();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/dining-line/status")
    @ApiOperation(value = "餐厅实况-供餐状态")
    public CudResult<List<RestaurantLineStatusModel>> getDiningLineStatus() {
//        (2)供餐状态：实时数据，页面载入时刷新，每间隔10s刷新。
//        展示各餐线当前的情况。字段包含：餐线名、所属餐厅、供餐状态、排队情况、菜谱数量。
        //餐线名：全量启用状态的餐线。
        //所属餐厅：餐线对应的餐厅名称。
        //供餐状态：餐线营业状态，取值为营业中、休息中（数据来源于餐线营业时间，PC-餐线管理已增加此功能）。
        //排队情况：根据排队摄像头判断。
        //菜品数量：餐线关联的菜品数量。

        // 餐厅名称如下：BBX餐厅、BJ餐厅、科技园餐厅、BXH餐厅、金色蓝湾餐厅
        // 餐线：餐厅下维护的餐线，数量和类别不一，类别名称例如：特色餐线、传统餐线1、面档、煎趴线、智能餐线 …………
        try {
            List<RestaurantLineStatusModel> data = restaurantScreenService.getDiningLineStatus();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/compartment/overview")
    @ApiOperation(value = "餐厅实况-包间实况")
    public CudResult<RestaurantCompartmentOverviewModel> getCompartmentOverview() {
//        (3)包间实况：实时数据，页面载入时刷新，包间预定情况每间隔1H刷新。
//        展示包间预订情况，包间来源于包间管理数据，按当前时间判断，
//        若今日后续包间还有预订，包间图片高亮展示（能区分有预定和无预定即可），鼠标移入展示包间名，移出隐藏。
        try {
            RestaurantCompartmentOverviewModel data = restaurantScreenService.getCompartmentOverview();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/compartment/reservedTrend")
    @ApiOperation(value = "餐厅实况-餐厅包间预定趋势(近30天)")
    public CudResult<RestaurantCompartmentTrendModel> getCompartmentReservedTrend() {
//        (3)包间实况：实时数据，页面载入时刷新，包间预定情况每间隔1H刷新。
//        图表展示近30天包间当日的预定次数情况，即某包间今日有3条预定信息，计为3次。
        try {
            RestaurantCompartmentTrendModel data = restaurantScreenService.getCompartmentReservedTrend();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "餐厅实况-实时菜品-获取餐厅餐线营业时间信息")
    public CudResult<List<RestaurantModel>> getList() {
//        (4)实时菜品-餐线数据
//        餐线数据来源于餐线管理维护的餐线数据，取值为级联，一级为餐厅，二级为对应餐线，仅可选择二级，默认选择BBX餐厅的第一条餐线；
//        根据选择餐线展示对应用餐时间

        try {
            List<RestaurantModel> data = restaurantScreenService.getList();
            return CudResult.success(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/line/dishes/list")
    @ApiOperation(value = "餐厅实况-实时菜品-获取餐线的菜品信息")
    public CudResult<List<RestaurantDishesScheduleModel>> getLineDishes(@ApiParam(value = "餐线id") @RequestParam Long lineId, @ApiParam(value = "餐线营业时间类型") @RequestParam String timeType ){
//        (4)实时菜品：实时数据，页面载入时刷新，每间隔1H刷新。
//        菜品信息来源于菜品周排；
//        餐线数据来源于餐线管理维护的餐线数据，取值为级联，一级为餐厅，二级为对应餐线，仅可选择二级，默认选择BBX餐厅的第一条餐线；
//        根据选择餐线展示对应用餐时间，对比当前时间，自动切换，也可手动切换，刷新时恢复自动。若所选餐线在该时间无菜品，提示：餐线暂未排布菜品
//        点击详细菜品，默认筛选当前餐厅餐线、当前时段的菜品。
        try {
            List<RestaurantDishesScheduleModel> data = restaurantScreenService.getLineDishes(lineId, timeType);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "餐厅实况-实时菜品-获取餐线的菜品信息(分页)")
    @PostMapping(value = "/Line/Dishes/page")
    public CudResult<IPage<RestaurantDishesScheduleModel>> pageLineDishes(@RequestBody RestaurantLinePageParam param) {
        try {
            IPage<RestaurantDishesScheduleModel> data = restaurantScreenService.pageLineDishes(param);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/compartment/evaluate")
    @ApiOperation(value = "餐厅实况-实时评价")
    public CudResult<List<RestaurantDishesEvaluateModel>> getCompartmentEvaluate() {
//        (5)实时评价：实时数据，页面载入时刷新，每间隔10s刷新。
//        员工在移动端对菜品的评价，展示近20条，滚动播放。
//        3星及以下为不满意，满意与不满意需要有区分。
        try {
            List<RestaurantDishesEvaluateModel> data = restaurantScreenService.getCompartmentEvaluate();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/complaintSuggestion")
    @ApiOperation(value = "餐厅实况-员工诉求")
    public CudResult<List<RestaurantComplaintSuggestionModel>> getComplaintSuggestion() {
//        (6)员工诉求：实时数据，页面载入时刷新，每间隔10s刷新。
//        员工对餐厅的投诉建议及当前处理状态，展示近20条，滚动播放。小勤蜂分配的处理人员为餐厅部门的人员，即为餐厅的投诉建议。
//        处理回复完成且审核通过的为已解决。
        try {
            List<RestaurantComplaintSuggestionModel> data = restaurantScreenService.getComplaintSuggestion();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/supplier")
    @ApiOperation(value = "后厨管理-供应商画像")
    public CudResult<List<RestaurantSupplierModel>> getSupplier() {
    //  2、后厨管理
    //(1)供应商画像：页面载入时刷新，每日0点刷新。（入库餐料验收汇总表）
    //供应商：入库的供应商，供货次数最多的即为核心供应商。
    //供货次数：入库一条数据即算供货一次。
    //供货品类：入库数据中的各品类数量。
    //供货时长：查找系统中已记录最早的一条入库数据日期，计算与今日的间隔，按年计，保留一位小数，向下取整。
        try {
            List<RestaurantSupplierModel> data = restaurantScreenService.getSupplier();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/inventory/analysis")
    @ApiOperation(value = "后厨管理-餐料库存分析")
    public CudResult<List<RestaurantInventoryCategoryModel>> getInventoryAnalysis() {
//        (2)餐料库存分析：页面载入时刷新，每日0点刷新。数据来源于核服餐厅系统报表（即时库存表）。
//        统计各品类（物料类型）对应的商品名称的库存数量。
//        物料类型：冻品、副食品、基地特色、酒水饮料/茶/烟、粮油干调、禽蛋、蔬菜、水果、鲜豆/面/粉制品、鲜肉、禽鸟、水产、预制菜
        try {
            List<RestaurantInventoryCategoryModel> data = restaurantScreenService.getInventoryAnalysis();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/inventory/inboundTrend")
    @ApiOperation(value = "后厨管理-入库数量分析")
    public CudResult<List<RestaurantInventoryInboundTrendModel>> getInventoryInboundTrend(
            @ApiParam(value = "时间，格式：yyyy-MM-dd",example = "2024-01-15", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date yearDate) {
//        (3)入库数量分析：页面载入时刷新，每日0点刷新。数据来源于核服餐厅系统报表（入库餐料验收汇总表）。
//        统计一年各月入库各品类餐料的商品名称数量。默认当年。
        //        物料类型：冻品、副食品、基地特色、酒水饮料/茶/烟、粮油干调、禽蛋、蔬菜、水果、鲜豆/面/粉制品、鲜肉、禽鸟、水产、预制菜
        try {
            List<RestaurantInventoryInboundTrendModel> data = restaurantScreenService.getInventoryInboundTrend(yearDate);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/kitchen/staff/situation")
    @ApiOperation(value = "后厨管理-后厨人员情况")
    public CudResult<RestaurantKitchenStaffModel> getKitchenStaffSituation() {
//        后厨人员情况：取页面配置数据
//        环形图中，男女比例数据根据根据填入的人员数据进行计算
//        有效体检率=体检人数/后厨人员数*100%
//        培训率=培训人数/后厨人员数数*100%
//        柱状图警戒线根据填入的各年龄段人数总数的20%进行设置
        try {
            RestaurantKitchenStaffModel data = restaurantScreenService.getKitchenStaffSituation();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/waste/statistics")
    @ApiOperation(value = "后厨管理-餐厅废弃物")
    public CudResult<List<RestaurantWasteStatisticsModel>> getWasteStatistics(@ApiParam(value = "时间类型(week:周，month：月，year：年)") @RequestParam String timeType) {
//        (5)餐厅废弃物：页面载入时刷新，每日0点刷新。数据来源于核服餐厅系统报表（餐厅垃圾处理记录清单）。
//        按周月年维度统计后厨餐余垃圾和厨余垃圾重量。默认周。
        try {
            List<RestaurantWasteStatisticsModel> data = restaurantScreenService.getWasteStatistics(timeType);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/notice/smart-exposure")
    @ApiOperation(value = "后厨管理-智能曝光台")
    public CudResult<List<RestaurantDeviceAlarmModel>> getNoticeSmartExposure() {
//        (6)智能曝光台：实时推送。
//        展示最近几条餐厅中产生的告警数据，展示数量前端根据空间大小控制。告警信息包含字段：告警时间、告警名称、上报设备名称、设备位置、抓拍图片。
//        抓拍图片：根据设备情况是否支持抓拍告警时刻的监控画面，如没有不展示。
//        餐厅设备来源：通过设备分组增确定餐厅设备，增加智慧餐厅分组，分组底下为各设备大类，如：后厨摄像头、餐厅门禁...
        try {
            List<RestaurantDeviceAlarmModel> data = restaurantScreenService.getNoticeSmartExposure();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/revenue/overview")
    @ApiOperation(value = "消费分析-营收总览")
    public CudResult<RestaurantRevenueOverview> getConsumptionOverview() {
//        4、消费分析
//消费相关数据，涉及金额，保留两位小数，且整数部分大于3位时，用英文逗号分隔，其他数据均保留一位小数。
//消费信息均来源于PC端数据表每日手动导入，统计范围为BBX和科技园餐厅数据。无实时数据，数据按导入情况每日更新。
//        (1)营收总览
//        总消费人次：一卡通消费数据，一条数据计为一次。
//        总营收：总消费金额。
        try {
            RestaurantRevenueOverview data = restaurantScreenService.getRevenueOverview();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }
    @GetMapping(value = "/count/analysis")
    @ApiOperation(value = "消费分析-消费次数分析")
    public CudResult<List<RestaurantConsumptionCountItem>> getCountAnalysis(
            @ApiParam(value = "时间，格式：yyyy-MM-dd",example = "2024-01-15", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date yearDate,
            @ApiParam(value = "餐厅id") @RequestParam(required = false) Long id) {
//        (2)消费次数分析
//        按年统计餐厅及各餐线的消费次数情况。日期支持选择之前年份。
//        数据钻取为两级，一级为餐厅管理中维护的餐厅，二级为餐厅对应餐线。

        // 餐厅名称如下：BBX餐厅、BJ餐厅、科技园餐厅、BXH餐厅、金色蓝湾餐厅
        // 餐线：餐厅下维护的餐线，数量和类别不一，类别名称例如：特色餐线、传统餐线1、面档、煎趴线、智能餐线 …………
        try {
            List<RestaurantConsumptionCountItem> data = restaurantScreenService.getCountAnalysis(yearDate, id);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/ability/trend")
    @ApiOperation(value = "消费分析-消费能力分析")
    public CudResult<List<RestaurantConsumptionAbilityTrend>> getConsumptionAbilityTrend(
            @ApiParam(value = "时间，格式：yyyy-MM-dd",example = "2024-01-15", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date monthDate) {
//        (3)消费能力分析
//        按月查询、统计所有餐厅每日消费金额，及每日人均消费金额。时间默认当前年月，支持选择之前年月。
//        日均消费金额=当日消费总额/当日刷卡人数（去重）
        try {
            List<RestaurantConsumptionAbilityTrend> data = restaurantScreenService.getConsumptionAbilityTrend(monthDate);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/behavior/overview")
    @ApiOperation(value = "消费分析-用餐行为分析")
    public CudResult<RestaurantConsumptionBehavior> getConsumptionBehavior(
            @ApiParam(value = "时间，格式：yyyy-MM-dd",example = "2024-01-15", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date monthDate) {
//        (4)用餐行为分析
//        需要记录每位员工每日刷卡的次数和总金额。一位员工只有一张一卡通。
//        平均每日消费次数：=总刷卡次数/统计天数，统计天数为当前日期-库中最早一条数据日期
//        平均每日消费金额：=消费总额/统计天数，统计天数为当前日期-库中最早一条数据日期
//        一卡通余额均值：=当前余额总数/一卡通账号数
//        在用一卡通：发生交易的卡数量
        try {
            RestaurantConsumptionBehavior data = restaurantScreenService.getConsumptionBehavior(monthDate);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/employee/list")
    @ApiOperation(value = "消费分析-员工关怀")
    public CudResult<List<RestaurantEmployeeConsumption>> getEmployeeConsumptionList() {
//        (5)员工关怀
//        日期默认为本年，数据从本年开始计算，支持选择往年。
//        滚动展示前20条，按提醒产生时间倒序。
//        姓名：一卡通对应员工，姓名脱敏处理
//        日均刷卡次数、日均消费金额：计算同用餐行为分析
//        关怀项：策略名称
    //        关怀策略1：卡余额不足
    //        员工有效一卡通余额低于50元。实时查询，一旦余额高于50元，停止提醒，数据隐藏。
    //        关怀策略3：日均消费金额低
    //        30天内日均消费金额小于10元。实时查询，一旦日均消费金额≥10元，停止提醒，数据隐藏
        try {
            List<RestaurantEmployeeConsumption> data = restaurantScreenService.getEmployeeConsumptionList();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/share/analysis")
    @ApiOperation(value = "消费分析-消费占比分析")
    public CudResult<List<RestaurantConsumptionShareItem>> getConsumptionShareAnalysis(
            @ApiParam(value = "时间，格式：yyyy-MM-dd",example = "2024-01-15", required = true)
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam Date yearDate,
            @ApiParam(value = "餐厅id") @RequestParam(required = false) Long id) {
        try {
//            (6)消费占比分析
//            按年统计餐厅消费总额及占比，日期默认为本年，支持选择往年。
//            数据钻取为两级，一级为餐厅管理中维护的餐厅，二级为餐厅对应餐线。

            // 餐厅名称如下：BBX餐厅、BJ餐厅、科技园餐厅、BXH餐厅、金色蓝湾餐厅
            // 餐线：餐厅下维护的餐线，数量和类别不一，类别名称例如：特色餐线、传统餐线1、面档、煎趴线、智能餐线 …………
            List<RestaurantConsumptionShareItem> data = restaurantScreenService.getConsumptionShareAnalysis(yearDate, id);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

}