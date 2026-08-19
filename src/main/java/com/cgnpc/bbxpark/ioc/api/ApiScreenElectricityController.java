package com.cgnpc.bbxpark.ioc.api;

import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.BranchEnergyFlowParam;
import com.cgnpc.bbxpark.ioc.service.IScreenElectricityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping({"/api/dtwin/electricity"})
@Api(tags = {"大屏-用电统计接口"})
public class ApiScreenElectricityController {
    private static final long ROOT_PARENT_ID = 0L;

    @Autowired
    private IScreenElectricityService screenElectricityService;

    @GetMapping({"/getElectricityOverview"})
    @ApiOperation("用电宏观成效")
    public CudResult<ElectricityOverviewModel> getElectricityOverview(@ApiParam("年") @RequestParam Integer year, @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getElectricityOverview(year,month));
    }

    @GetMapping({"/getElectricityTrend"})
    @ApiOperation("用电趋势分析")
    public CudResult<List<ElectricityTrendModel>> getElectricityTrend(@ApiParam("年") @RequestParam Integer year, @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getElectricityTrend(year,month));
    }

    @GetMapping({"/getAreaCompare"})
    @ApiOperation("区域能耗对比")
    public CudResult<List<ElectricityAreaCompareModel>> getAreaCompare(@ApiParam("支路id,为空时查询最上级支路") @RequestParam(required = false) Long id,
                                                                       @ApiParam("年") @RequestParam Integer year, @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getAreaCompare(id,year,month));
    }

    @GetMapping({"/getManageAnalysis"})
    @ApiOperation("日常管理分析")
    public CudResult<ElectricityManageAnalysisModel> getManageAnalysis() {
        return CudResult.success(screenElectricityService.getManageAnalysis(DeviceReadingTypeEnum.ELECTRICITY.getCode()));
    }

    @GetMapping({"/getRealtimeConfig"})
    @ApiOperation("实时组态")
    public CudResult<List<ElectricityRealtimeConfigModel>> getRealtimeConfig(@ApiParam("支路id") @RequestParam(required = false) Long id) {
        return CudResult.success(screenElectricityService.getRealtimeConfig(id,DeviceReadingTypeEnum.ELECTRICITY.getCode()));
    }

    @GetMapping({"/getLossRate"})
    @ApiOperation("能源流失率分析")
    public CudResult<List<ElectricityLossRateModel>> getLossRate(@ApiParam("年") @RequestParam(required = false) Integer year,
                                                                 @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getLossRate(year,month));
    }

    @ApiOperation(value = "能流数据图")
    @PostMapping(value = "/getFlowData")
    public CudResult<List<EnergyBranchModel>> getFlowData(@RequestBody BranchEnergyFlowParam param){
        param.setBranchType(DeviceReadingTypeEnum.ELECTRICITY.getCode());
        return CudResult.success(screenElectricityService.getFlowData(param));
    }

    @GetMapping({"/getPerCapitaTrend"})
    @ApiOperation("人均用电趋势")
    public CudResult<List<ElectricityPerCapitaTrendModel>> getPerCapitaTrend(@ApiParam("年") @RequestParam(required = false) Integer year,
                                                                             @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getPerCapitaTrend(year,month));
    }

    @GetMapping({"/safety/list"})
    @ApiOperation("用电安全提醒列表")
    public CudResult<List<ElectricitySafetyReminderModel>> getSafetyReminderList(@ApiParam("年") @RequestParam(required = false) Integer year,
                                                                                 @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenElectricityService.getSafetyReminderList(year,month,DeviceReadingTypeEnum.ELECTRICITY.getCode()));
    }

    private List<EnergyBranchModel> generateFlowData(BranchEnergyFlowParam param) {
        Integer yearVal = param == null ? null : param.getYear();
        Integer monthVal = param == null ? null : param.getMonth();
        long seed = 20260413L;
        if (yearVal != null) {
            seed += yearVal.hashCode();
        }
        if (monthVal != null) {
            seed += monthVal.hashCode();
        }
        Random random = new Random(seed);

        List<EnergyBranchModel> roots = new ArrayList<>();
        roots.add(buildEnergyBranch(1L, null, "BBX总水表", "WATER_BBX_TOTAL", "water", random, 3));
        roots.add(buildEnergyBranch(2L, null, "科技园总水表", "WATER_PARK_TOTAL", "water", random, 2));
        return roots;
    }

    private EnergyBranchModel buildEnergyBranch(Long id,
                                                Long parentId,
                                                String branchName,
                                                String branchCode,
                                                String branchType,
                                                Random random,
                                                int depth) {
        EnergyBranchModel model = new EnergyBranchModel();
        model.setId(id);
        model.setParentId(parentId);
        model.setBranchName(branchName);
        model.setBranchCode(branchCode);
        model.setBranchType(branchType);
        model.setSortOrder((int) (id % 100));
        model.setDescription(branchName);
        model.setStatus(1);
        model.setDeleted(1);
        model.setTotalPower(new BigDecimal(String.valueOf(round(120D + random.nextDouble() * 260D, 2))));
        model.setTenantId(1L);

        if (depth <= 0) {
            model.setChildren(Collections.emptyList());
            return model;
        }

        int childCount = 2 + random.nextInt(2);
        List<EnergyBranchModel> children = new ArrayList<>();
        for (int i = 1; i <= childCount; i++) {
            long childId = id * 10 + i;
            children.add(buildEnergyBranch(childId, id, branchName + "-" + i, branchCode + "_" + i, branchType, random, depth - 1));
        }
        model.setChildren(children);
        return model;
    }

    private List<ElectricitySafetyReminderModel> generateSafetyReminderData(Integer year, Integer month) {
        String[] contents = {
                "1栋4层配电箱温升偏高，请安排巡检复核。",
                "2栋强电井回路负载连续超阈值，请关注削峰。",
                "3栋地下车库照明支路夜间未按计划关闭，请核查策略。",
                "4栋西侧办公区空调支路用电异常波动，请排查末端设备。",
                "5栋配电室电表通信中断，请检查采集链路。",
                "公共区域景观照明回路连续高负荷运行，请及时处理。",
                "裙楼餐饮区动力支路存在尖峰负载，请关注设备启停。",
                "北区充电桩支路出现瞬时过流告警，请复核现场状态。"
        };
        List<ElectricitySafetyReminderModel> result = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime baseTime = LocalDateTime.of(year, month, yearMonth.lengthOfMonth(), 18, 0, 0);

        for (int i = 0; i < contents.length; i++) {
            ElectricitySafetyReminderModel model = new ElectricitySafetyReminderModel();
            LocalDateTime remindTime = baseTime.minusHours(i * 7L).minusDays(i / 2);
            model.setRemindTime(Date.from(remindTime.atZone(ZoneId.systemDefault()).toInstant()));
            model.setContent(contents[i]);
            result.add(model);
        }
        return result;
    }


    private Double round(double value, int scale) {
        double factor = Math.pow(10D, scale);
        return Math.round(value * factor) / factor;
    }

    private String validateYearMonth(Integer year, Integer month) {
        //年份和月份不能同时为空
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
}
