package com.cgnpc.bbxpark.ioc.api;

import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.BranchEnergyFlowParam;
import com.cgnpc.bbxpark.ioc.service.IScreenElectricityService;
import com.cgnpc.bbxpark.ioc.service.IScreenWaterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping({"/api/dtwin/water"})
@Api(tags = {"大屏-用水统计接口"})
public class ApiScreenWaterController {
    private static final long ROOT_PARENT_ID = 0L;

    @Autowired
    private IScreenWaterService screenWaterService;
    @Autowired
    private IScreenElectricityService screenElectricityService;

    @GetMapping({"/getWaterOverview"})
    @ApiOperation("用水宏观成效")
    public CudResult<WaterOverviewModel> getWaterOverview(@ApiParam("年") @RequestParam Integer year,
                                                          @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenWaterService.getWaterOverview(year,month));
    }

    @GetMapping({"/getWaterTrend"})
    @ApiOperation("用水趋势分析")
    public CudResult<List<WaterTrendModel>> getWaterTrend(@ApiParam("年") @RequestParam Integer year,
                                                          @ApiParam("月") @RequestParam(required = false) Integer month) {
        try {
            List<WaterTrendModel> data = screenWaterService.getWaterTrend(year, month);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getAreaCompare"})
    @ApiOperation("区域能耗对比(用水)")
    public CudResult<List<WaterAreaCompareModel>> getAreaCompare(@ApiParam("支路id,为空时查询最上级支路") @RequestParam(required = false) Long id,
                                                                 @ApiParam("年") @RequestParam Integer year, @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenWaterService.getAreaCompare(id,year,month));
    }

    @GetMapping({"/getManageAnalysis"})
    @ApiOperation("日常管理分析(用水)")
    public CudResult<ElectricityManageAnalysisModel> getManageAnalysis() {
        return CudResult.success(screenElectricityService.getManageAnalysis(DeviceReadingTypeEnum.WATER.getCode()));
    }

    @GetMapping({"/getRealtimeConfig"})
    @ApiOperation("实时组态(用水)")
    public CudResult<List<ElectricityRealtimeConfigModel>> getRealtimeConfig(@ApiParam("支路id") @RequestParam(required = false) Long id) {
        return CudResult.success(screenElectricityService.getRealtimeConfig(id,DeviceReadingTypeEnum.WATER.getCode()));
    }

    @GetMapping({"/getLossRate"})
    @ApiOperation("能源流失率分析(用水)")
    public CudResult<List<WaterLossRateModel>> getLossRate(@ApiParam("支路id") @RequestParam(required = false) Long branchId,
                                                           @ApiParam("年") @RequestParam Integer year, @ApiParam("月") @RequestParam(required = false) Integer month) {
       return CudResult.success(screenWaterService.getLossRate(branchId,year,month));
    }

    @ApiOperation(value = "能流数据图")
    @PostMapping(value = "/getFlowData")
    public CudResult<List<EnergyBranchModel>> getFlowData(@RequestBody BranchEnergyFlowParam param) {
        param.setBranchType(DeviceReadingTypeEnum.WATER.getCode());
        return CudResult.success(screenElectricityService.getFlowData(param));
    }

    @GetMapping({"/getPerCapitaTrend"})
    @ApiOperation("人均用水趋势")
    public CudResult<List<WaterPerCapitaTrendModel>> getPerCapitaTrend(@ApiParam("年") @RequestParam Integer year,
                                                                       @ApiParam("月") @RequestParam(required = false) Integer month) {
        try {
            List<WaterPerCapitaTrendModel> data = screenWaterService.getPerCapitaTrend(year, month);
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getRealtimeFlowRank"})
    @ApiOperation("实时流量排序")
    public CudResult<List<WaterRealtimeFlowRankModel>> getRealtimeFlowRank(@ApiParam("类型:hot->热水;cold->冷水") @RequestParam(required = false) String type,
                                                                           @ApiParam("年") @RequestParam Integer year, @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenWaterService.getRealtimeFlowRank(type,year,month));
    }

    @GetMapping({"/safety/list"})
    @ApiOperation("用水安全提醒列表")
    public CudResult<List<ElectricitySafetyReminderModel>> getSafetyReminderList(@ApiParam("年") @RequestParam(required = false) Integer year,
                                                                                 @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getSafetyReminderList(year,month,DeviceReadingTypeEnum.WATER.getCode()));
    }

    private List<ElectricitySafetyReminderModel> generateSafetyReminderData(Integer year, Integer month) {
        String[] contents = {
                "冷水支路瞬时流量异常波动，请排查阀门与末端设备。",
                "热水系统回水温度异常，建议检查循环泵运行状态。",
                "生活水总表读数长时间不变，请检查计量设备通信。",
                "补水支路夜间持续用水偏高，请关注漏损风险。",
                "消防补水表出现频繁启停，请检查压力开关与管网。",
                "东区冷水分表读数突增，请核查施工用水。",
                "西区热水分表读数偏高，请检查换热站运行策略。",
                "中区生活水分表存在间歇性离线，请检查采集链路。"
        };
        List<ElectricitySafetyReminderModel> result = new ArrayList<>();
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime baseTime = LocalDateTime.of(year, month, ym.lengthOfMonth(), 18, 0, 0);

        for (int i = 0; i < contents.length; i++) {
            ElectricitySafetyReminderModel model = new ElectricitySafetyReminderModel();
            LocalDateTime remindTime = baseTime.minusHours(i * 6L).minusDays(i / 3);
            model.setRemindTime(Date.from(remindTime.atZone(ZoneId.systemDefault()).toInstant()));
            model.setContent(contents[i]);
            result.add(model);
        }
        return result;
    }

    private List<AreaNode> buildAreaNodes() {
        List<AreaNode> nodes = new ArrayList<>();
        nodes.add(new AreaNode(1L, ROOT_PARENT_ID, "办公区", 1));
        nodes.add(new AreaNode(2L, ROOT_PARENT_ID, "公共区", 1));
        nodes.add(new AreaNode(3L, ROOT_PARENT_ID, "配套区", 1));

        nodes.add(new AreaNode(11L, 1L, "1栋", 2));
        nodes.add(new AreaNode(12L, 1L, "2栋", 2));
        nodes.add(new AreaNode(21L, 2L, "3栋", 2));
        nodes.add(new AreaNode(22L, 2L, "4栋", 2));
        nodes.add(new AreaNode(31L, 3L, "5栋", 2));
        nodes.add(new AreaNode(32L, 3L, "地库及室外", 2));

        nodes.add(new AreaNode(111L, 11L, "1栋-冷水支路", 3));
        nodes.add(new AreaNode(112L, 11L, "1栋-热水支路", 3));
        nodes.add(new AreaNode(121L, 12L, "2栋-冷水支路", 3));
        nodes.add(new AreaNode(122L, 12L, "2栋-热水支路", 3));
        nodes.add(new AreaNode(211L, 21L, "3栋-生活水支路", 3));
        nodes.add(new AreaNode(212L, 21L, "3栋-补水支路", 3));
        nodes.add(new AreaNode(221L, 22L, "4栋-冷水支路", 3));
        nodes.add(new AreaNode(222L, 22L, "4栋-生活水支路", 3));
        nodes.add(new AreaNode(311L, 31L, "5栋-热水支路", 3));
        nodes.add(new AreaNode(312L, 31L, "5栋-补水支路", 3));
        nodes.add(new AreaNode(321L, 32L, "地库生活水支路", 3));
        nodes.add(new AreaNode(322L, 32L, "室外补水支路", 3));
        return nodes;
    }

    private Double generateAreaWater(Long id, int level, Integer year, Integer month) {
        Random random = new Random(year * 5000L + month * 100L + id);
        double base = 0D;
        if (level == 1) {
            base = 10D + random.nextDouble() * 18D;
        } else if (level == 2) {
            base = 6D + random.nextDouble() * 14D;
        } else {
            base = 2D + random.nextDouble() * 9D;
        }
        return round(base, 2);
    }

    private boolean hasChildren(List<AreaNode> nodes, Long id) {
        return nodes.stream().anyMatch(item -> item.getParentId().equals(id));
    }

    private Double round(double value, int scale) {
        double factor = Math.pow(10D, scale);
        return Math.round(value * factor) / factor;
    }

    private String validateYearMonth(Integer year, Integer month) {
        if (year == null && month == null) {
            return "年份和月份不能都为空";
        }
        return null;
    }

    private YearMonth resolveYearMonth(Integer year, Integer month) {
        YearMonth now = YearMonth.now();
        int resolvedYear = year == null ? now.getYear() : year;
        int resolvedMonth = month == null ? now.getMonthValue() : month;
        return YearMonth.of(resolvedYear, resolvedMonth);
    }

    private static class AreaNode {
        private final Long id;
        private final Long parentId;
        private final String name;
        private final int level;

        private AreaNode(Long id, Long parentId, String name, int level) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
            this.level = level;
        }

        public Long getId() {
            return id;
        }

        public Long getParentId() {
            return parentId;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }
    }
}
