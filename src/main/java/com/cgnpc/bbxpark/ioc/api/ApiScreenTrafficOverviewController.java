package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficMileagePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficRepairPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficVehiclePageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenTrafficOverviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/dtwin/traffic"})
@Api(tags = {"大屏-智慧交通概览接口"})
public class ApiScreenTrafficOverviewController {

    @Autowired
    private IScreenTrafficOverviewService screenTrafficOverviewService;

    @GetMapping({"/getMacroIndex"})
    @ApiOperation("交通宏观指标")
    public CudResult<TrafficMacroIndexModel> getMacroIndex(
            @ApiParam("年") @RequestParam(required = false) Integer year,
            @ApiParam("月") @RequestParam(required = false) Integer month) {
        try {
//            TrafficMacroIndexModel data = generateMacroIndexData(year, month);
            return CudResult.success(screenTrafficOverviewService.getMacroIndex(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getVehicleTypeDistribution"})
    @ApiOperation("车辆类型分布")
    public CudResult<List<TrafficVehicleTypeModel>> getVehicleTypeDistribution() {
        try {
//            List<TrafficVehicleTypeModel> data = generateVehicleTypeDistributionData();
            return CudResult.success(screenTrafficOverviewService.getVehicleTypeDistribution());
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getMaintenanceAnalysis"})
    @ApiOperation("车辆维护情况")
    public CudResult<List<TrafficMaintenanceModel>> getMaintenanceAnalysis(
            @ApiParam("年") @RequestParam(required = false) Integer year) {
        try {
//            int targetYear = year != null ? year : java.time.Year.now().getValue();
//            List<TrafficMaintenanceModel> data = generateMaintenanceAnalysisData(targetYear);
            return CudResult.success(screenTrafficOverviewService.getMaintenanceAnalysis(year));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getVehicleDetailList"})
    @ApiOperation("车辆明细列表")
    public CudResult<List<TrafficVehicleDetailModel>> getVehicleDetailList(
            @ApiParam("年") @RequestParam(required = false) Integer year,
            @ApiParam("月") @RequestParam(required = false) Integer month) {
        try {
//            List<TrafficVehicleDetailModel> data = generateVehicleDetailListData(year, month);
            return CudResult.success(screenTrafficOverviewService.getVehicleDetailList(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getVehicleDetailInfo"})
    @ApiOperation("车辆详情信息")
    public CudResult<TrafficVehicleDetailInfoModel> getVehicleDetailInfo(
            @ApiParam("车牌号") @RequestParam String plateNumber) {
        try {
//            TrafficVehicleDetailInfoModel data = generateVehicleDetailInfoData(plateNumber);
            return CudResult.success(screenTrafficOverviewService.getVehicleDetailInfo(plateNumber));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @PostMapping({"/vehicle/page"})
    @ApiOperation("车辆信息分页查询")
    public CudResult<IPage<TrafficVehiclePageModel>> getVehiclePage(@RequestBody TrafficVehiclePageParam param) {
        try {
//            IPage<TrafficVehiclePageModel> data = generateVehiclePageData(param);
            return CudResult.success(screenTrafficOverviewService.getVehiclePage(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @PostMapping({"/mileage/page"})
    @ApiOperation("车辆里程信息分页查询")
    public CudResult<IPage<TrafficMileagePageModel>> getMileagePage(@RequestBody TrafficMileagePageParam param) {
        try {
//            IPage<TrafficMileagePageModel> data = generateMileagePageData(param);
            return CudResult.success(screenTrafficOverviewService.getMileagePage(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @PostMapping({"/repair/page"})
    @ApiOperation("车辆维修记录分页查询")
    public CudResult<IPage<TrafficRepairPageModel>> getRepairPage(@RequestBody TrafficRepairPageParam param) {
        try {
//            IPage<TrafficRepairPageModel> data = generateRepairPageData(param);
            return CudResult.success(screenTrafficOverviewService.getRepairPage(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @PostMapping({"/maintain/page"})
    @ApiOperation("车辆保养记录分页查询")
    public CudResult<IPage<TrafficMaintainPageModel>> getMaintainPage(@RequestBody TrafficRepairPageParam param) {
        try {
//            IPage<TrafficMaintainPageModel> data = generateMaintainPageData(param);
            return CudResult.success(screenTrafficOverviewService.getMaintainPage(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @PostMapping({"/partReplace/page"})
    @ApiOperation("车辆轮胎更换记录分页查询")
    public CudResult<IPage<TrafficPartReplacePageModel>> getPartReplacePage(@RequestBody TrafficRepairPageParam param) {
        try {
//            IPage<TrafficPartReplacePageModel> data = generatePartReplacePageData(param);
            return CudResult.success(screenTrafficOverviewService.getPartReplacePage(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getDriverAnalysis"})
    @ApiOperation("驾驶员分析")
    public CudResult<TrafficDriverAnalysisModel> getDriverAnalysis() {
        try {
//            TrafficDriverAnalysisModel data = generateDriverAnalysisData();
            return CudResult.success(screenTrafficOverviewService.getDriverAnalysis());
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getDriverEntryCount"})
    @ApiOperation("驾驶员入职人数统计")
    public CudResult<List<TrafficDriverEntryCountModel>> getDriverEntryCount(
            @ApiParam("年") @RequestParam(required = false) Integer year,
            @ApiParam("月") @RequestParam(required = false) Integer month) {
        try {
//            List<TrafficDriverEntryCountModel> data = generateDriverEntryCountData(year, month);
            return CudResult.success(screenTrafficOverviewService.getDriverEntryCount(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getDriverWorkload"})
    @ApiOperation("驾驶员工作量分析")
    public CudResult<List<TrafficDriverWorkloadModel>> getDriverWorkload(
            @ApiParam("年") @RequestParam(required = false) Integer year,
            @ApiParam("月") @RequestParam(required = false) Integer month) {
        try {
//            List<TrafficDriverWorkloadModel> data = generateDriverWorkloadData(year, month);
            return CudResult.success(screenTrafficOverviewService.getDriverWorkload(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getMileageUtilization"})
    @ApiOperation("里程利用率趋势分析")
    public CudResult<List<TrafficMileageUtilizationModel>> getMileageUtilization(
            @ApiParam("年") @RequestParam(required = false) Integer year) {
        try {
//            int targetYear = year != null ? year : java.time.Year.now().getValue();
//            List<TrafficMileageUtilizationModel> data = generateMileageUtilizationData(targetYear);
            return CudResult.success(screenTrafficOverviewService.getMileageUtilization(year));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/getTodaySiteInfo"})
    @ApiOperation("获取今日站点信息")
    public CudResult<TrafficSiteInfoModel> getTodaySiteInfo(@ApiParam("站点名称") @RequestParam(required = false) String name) {
        try {
            TrafficSiteInfoModel data = generateTodaySiteInfoData();
            return CudResult.success(data);
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    private TrafficMacroIndexModel generateMacroIndexData(Integer year, Integer month) {
        Random random = new Random();
        TrafficMacroIndexModel model = new TrafficMacroIndexModel();
        
        model.setTotalVehicles((long) (800 + random.nextInt(100)));
        model.setTotalMileage(Math.round((3200 + random.nextDouble() * 500) * 10.0) / 10.0);
        model.setTotalMaintenanceCount((long) (500 + random.nextInt(100)));
        
        return model;
    }

    private List<TrafficVehicleTypeModel> generateVehicleTypeDistributionData() {
        List<TrafficVehicleTypeModel> result = new ArrayList<>();
        int[] counts = {20, 10, 5, 25, 30};
        String[] types = {"大巴车", "中巴车", "商务车", "轿车", "电召车"};
        int total = 90;

        for (int i = 0; i < types.length; i++) {
            TrafficVehicleTypeModel model = new TrafficVehicleTypeModel();
            model.setTypeName(types[i]);
            model.setCount(counts[i]);
            model.setPercentage(Math.round((double) counts[i] / total * 10000.0) / 100.0);
            result.add(model);
        }

        return result;
    }

    private List<TrafficMaintenanceModel> generateMaintenanceAnalysisData(Integer year) {
        List<TrafficMaintenanceModel> result = new ArrayList<>();
        Random random = new Random(year);

        for (int month = 1; month <= 12; month++) {
            TrafficMaintenanceModel model = new TrafficMaintenanceModel();
            model.setMonth(String.format("%d年%02d月", year, month));
            
            double seasonFactor = 1.0 + Math.sin((month - 2) * Math.PI / 6) * 0.2;
            double directCost = (8000 + random.nextDouble() * 4000) * seasonFactor;
            double maintenanceCost = (800 + random.nextDouble() * 1200);
            
            model.setDirectOperatingCost(BigDecimal.valueOf(directCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setMaintenanceCost(BigDecimal.valueOf(maintenanceCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setTotalCost(BigDecimal.valueOf(directCost + maintenanceCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
            
            result.add(model);
        }

        return result;
    }

    private List<TrafficVehicleDetailModel> generateVehicleDetailListData(Integer year, Integer month) {
        List<TrafficVehicleDetailModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] plateNumbers = {"浙C97078", "浙F87893", "浙P1011", "浙P1616", "浙A12345", "浙B67890"};
        String[] vehicleTypes = {"商务车", "轿车", "大巴车", "中巴车"};

        for (int i = 0; i < plateNumbers.length; i++) {
            TrafficVehicleDetailModel model = new TrafficVehicleDetailModel();
            model.setId(""+(i + 1));
            model.setPlateNumber(plateNumbers[i]);
            model.setVehicleType(vehicleTypes[i % vehicleTypes.length] + " / ~5-7年");
            
            double totalCost = 8000 + random.nextDouble() * 5000;
            double maintenanceRate = 8 + random.nextDouble() * 7;
            double maintenanceCost = totalCost * maintenanceRate / 100;
            
            model.setTotalCost(BigDecimal.valueOf(totalCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setMaintenanceCost(BigDecimal.valueOf(maintenanceCost).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setMaintenanceCostRate(BigDecimal.valueOf(maintenanceRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
            
            if (maintenanceRate >= 15) {
                model.setDiagnosisReminder("高耗低效");
            } else if (maintenanceRate >= 10) {
                model.setDiagnosisReminder("特别关注");
            } else {
                model.setDiagnosisReminder("正常");
            }
            
            result.add(model);
        }

        return result;
    }


    private IPage<TrafficVehiclePageModel> generateVehiclePageData(TrafficVehiclePageParam param) {
        long current = param.getCurrent() != 0l ? param.getCurrent() : 1L;
        long size = param.getSize() != 0l ? param.getSize() : 10L;
        
        List<TrafficVehiclePageModel> allData = generateAllVehicleData();
        
        List<TrafficVehiclePageModel> filteredData = allData.stream()
                .filter(v -> {
                    if (param.getLicensePlate() != null && !param.getLicensePlate().isEmpty()) {
                        return v.getLicensePlate().contains(param.getLicensePlate());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getVehicleType() != null && !param.getVehicleType().isEmpty() && !"全部".equals(param.getVehicleType())) {
                        return param.getVehicleType().equals(v.getVehicleType());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getUseStatus() != null && !param.getUseStatus().isEmpty() && !"全部".equals(param.getUseStatus())) {
                        return param.getUseStatus().equals(v.getUseStatus());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getRunStatus() != null && !param.getRunStatus().isEmpty() && !"全部".equals(param.getRunStatus())) {
                        return param.getRunStatus().equals(v.getRunStatus());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Page<TrafficVehiclePageModel> page = new Page<>(current, size);
        page.setTotal((long) filteredData.size());
        
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = (int) Math.min(fromIndex + size, filteredData.size());
        
        if (fromIndex < filteredData.size()) {
            page.setRecords(filteredData.subList(fromIndex, toIndex));
        } else {
            page.setRecords(new ArrayList<>());
        }
        
        return page;
    }

    private List<TrafficVehiclePageModel> generateAllVehicleData() {
        List<TrafficVehiclePageModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] platePrefixes = {"浙C", "浙F", "浙P", "浙A", "浙B", "浙D", "浙E", "浙G"};
        String[] vehicleTypes = {"大巴车", "中巴车", "商务车", "轿车", "电召车"};
        String[] vehicleModels = {"金龙KLQ6129", "宇通ZK6100", "奔驰E级", "宝马5系", "奥迪A6L", "大众迈腾", "丰田凯美瑞", "本田雅阁"};
        String[] departments = {"苍南业主班车", "综合管理部", "工程管理部", "信息文档部", "安全管理部"};
        String[] useStatuses = {"在用", "停用", "维修中"};
        String[] runStatuses = {"运行中", "空闲", "故障"};

        for (int i = 1; i <= 220; i++) {
            TrafficVehiclePageModel model = new TrafficVehiclePageModel();
            model.setId(UUID.randomUUID().toString());
            
            String prefix = platePrefixes[i % platePrefixes.length];
            model.setLicensePlate(prefix + String.format("%04d", 1234 + (i % 9000)));
            
            model.setVehicleType(vehicleTypes[i % vehicleTypes.length]);
            model.setVin("LGBF" + UUID.randomUUID().toString().substring(0, 13).toUpperCase());
            model.setVehicleModel(vehicleModels[i % vehicleModels.length]);
            model.setDepartment(departments[i % departments.length]);
            model.setUseStatus(useStatuses[random.nextInt(useStatuses.length)]);
            model.setRunStatus(runStatuses[random.nextInt(runStatuses.length)]);
            model.setMileage(BigDecimal.valueOf(5000 + random.nextDouble() * 95000).setScale(2, RoundingMode.HALF_UP));
            
            int daysToAdd = random.nextInt(365);
            model.setNextInspectDate(new Date(System.currentTimeMillis() + daysToAdd * 24 * 60 * 60 * 1000L));
            model.setImportTime(new Date());
            
            result.add(model);
        }
        
        return result;
    }

    private IPage<TrafficMileagePageModel> generateMileagePageData(TrafficMileagePageParam param) {
        long current = param.getCurrent() != 0l ? param.getCurrent() : 1L;
        long size = param.getSize() != 0l ? param.getSize() : 10L;
        
        List<TrafficMileagePageModel> allData = generateAllMileageData();
        
        List<TrafficMileagePageModel> filteredData = allData.stream()
                .filter(v -> {
                    if (param.getLicensePlate() != null && !param.getLicensePlate().isEmpty()) {
                        return v.getLicensePlate().contains(param.getLicensePlate());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getVehicleType() != null && !param.getVehicleType().isEmpty() && !"全部".equals(param.getVehicleType())) {
                        return param.getVehicleType().equals(v.getVehicleType());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Page<TrafficMileagePageModel> page = new Page<>(current, size);
        page.setTotal((long) filteredData.size());
        
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = (int) Math.min(fromIndex + size, filteredData.size());
        
        if (fromIndex < filteredData.size()) {
            page.setRecords(filteredData.subList(fromIndex, toIndex));
        } else {
            page.setRecords(new ArrayList<>());
        }
        
        return page;
    }

    private List<TrafficMileagePageModel> generateAllMileageData() {
        List<TrafficMileagePageModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] platePrefixes = {"浙C", "浙F", "浙P", "浙A", "浙B", "浙D", "浙E", "浙G"};
        String[] vehicleTypes = {"大巴车", "中巴车", "商务车", "轿车", "电召车"};

        for (int i = 1; i <= 220; i++) {
            TrafficMileagePageModel model = new TrafficMileagePageModel();
            model.setId(UUID.randomUUID().toString());
            
            String prefix = platePrefixes[i % platePrefixes.length];
            model.setLicensePlate(prefix + String.format("%04d", 1234 + (i % 9000)));
            
            model.setVehicleType(vehicleTypes[i % vehicleTypes.length]);
            
            double mileage = 100 + random.nextDouble() * 500;
            model.setCurrentMileage(BigDecimal.valueOf(mileage).setScale(2, RoundingMode.HALF_UP).doubleValue());
            
            result.add(model);
        }
        
        return result;
    }

    private IPage<TrafficRepairPageModel> generateRepairPageData(TrafficRepairPageParam param) {
        long current = param.getCurrent() != 0l ? param.getCurrent() : 1L;
        long size = param.getSize() != 0l ? param.getSize() : 10L;
        
        List<TrafficRepairPageModel> allData = generateAllRepairData();
        
        List<TrafficRepairPageModel> filteredData = allData.stream()
                .filter(v -> {
                    if (param.getPlateNum() != null && !param.getPlateNum().isEmpty()) {
                        return v.getPlateNum().contains(param.getPlateNum());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getVehicleType() != null && !param.getVehicleType().isEmpty() && !"全部".equals(param.getVehicleType())) {
                        return param.getVehicleType().equals(v.getVehicleType());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Page<TrafficRepairPageModel> page = new Page<>(current, size);
        page.setTotal((long) filteredData.size());
        
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = (int) Math.min(fromIndex + size, filteredData.size());
        
        if (fromIndex < filteredData.size()) {
            page.setRecords(filteredData.subList(fromIndex, toIndex));
        } else {
            page.setRecords(new ArrayList<>());
        }
        
        return page;
    }

    private List<TrafficRepairPageModel> generateAllRepairData() {
        List<TrafficRepairPageModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] platePrefixes = {"浙C", "浙F", "浙P", "浙A", "浙B"};
        String[] repairItems = {"更换刹车片", "更换机油、机滤、...", "更换轮胎", "更换电瓶", "更换火花塞"};
        String[] repairFactories = {"苍南汽修厂", "温州汽修厂", "杭州汽修厂", "宁波汽修厂"};
        String[] statuses = {"已完成", "维修中", "待确认"};
        String[] depts = {"综合管理部", "工程管理部", "信息文档部"};

        for (int i = 1; i <= 220; i++) {
            TrafficRepairPageModel model = new TrafficRepairPageModel();
            model.setId(UUID.randomUUID().toString());
            
            String prefix = platePrefixes[i % platePrefixes.length];
            model.setPlateNum(prefix + String.format("%04d", 1234 + (i % 9000)));
            
            model.setSettlementParty(repairFactories[i % repairFactories.length]);
            model.setRepairItemName(repairItems[i % repairItems.length]);

            int daysAgo = random.nextInt(30);
            model.setApplyDate(new Date(System.currentTimeMillis() - daysAgo * 24 * 60 * 60 * 1000L));
            
            model.setRepairStatus(statuses[random.nextInt(statuses.length)]);
            model.setVehicleDept(depts[i % depts.length]);
            model.setWorkOrderNo("WO" + String.format("%08d", i));
            
            result.add(model);
        }
        
        return result;
    }

    private IPage<TrafficMaintainPageModel> generateMaintainPageData(TrafficRepairPageParam param) {
        long current = param.getCurrent() != 0l ? param.getCurrent() : 1L;
        long size = param.getSize() != 0l ? param.getSize() : 10L;
        
        List<TrafficMaintainPageModel> allData = generateAllMaintainData();
        
        List<TrafficMaintainPageModel> filteredData = allData.stream()
                .filter(v -> {
                    if (param.getPlateNum() != null && !param.getPlateNum().isEmpty()) {
                        return v.getPlateNum().contains(param.getPlateNum());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getVehicleType() != null && !param.getVehicleType().isEmpty() && !"全部".equals(param.getVehicleType())) {
                        return param.getVehicleType().equals(v.getVehicleType());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Page<TrafficMaintainPageModel> page = new Page<>(current, size);
        page.setTotal((long) filteredData.size());
        
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = (int) Math.min(fromIndex + size, filteredData.size());
        
        if (fromIndex < filteredData.size()) {
            page.setRecords(filteredData.subList(fromIndex, toIndex));
        } else {
            page.setRecords(new ArrayList<>());
        }
        
        return page;
    }

    private List<TrafficMaintainPageModel> generateAllMaintainData() {
        List<TrafficMaintainPageModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] platePrefixes = {"浙C", "浙F", "浙P", "浙A", "浙B"};
        String[] maintainItems = {"常规保养", "首保", "大保养", "小保养"};
        String[] maintainLocations = {"苍南汽修厂", "温州汽修厂", "杭州汽修厂"};
        String[] statuses = {"已完成", "保养中", "待安排"};
        String[] depts = {"综合管理部", "工程管理部", "信息文档部"};

        for (int i = 1; i <= 220; i++) {
            TrafficMaintainPageModel model = new TrafficMaintainPageModel();
            model.setId(UUID.randomUUID().toString());
            
            String prefix = platePrefixes[i % platePrefixes.length];
            model.setPlateNum(prefix + String.format("%04d", 1234 + (i % 9000)));
            
            int daysAgo = random.nextInt(90);
            model.setMaintainDate(new Date(System.currentTimeMillis() - daysAgo * 24 * 60 * 60 * 1000L));
            
            model.setItemAmount(BigDecimal.valueOf(890 + random.nextInt(1500)).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setMaintainItemName(maintainItems[i % maintainItems.length]);
            
            int lastDaysAgo = daysAgo + random.nextInt(30);
            model.setLastMaintainDate(new Date(System.currentTimeMillis() - lastDaysAgo * 24 * 60 * 60 * 1000L));
            
            model.setInMileage(BigDecimal.valueOf(20000 + random.nextInt(50000)).setScale(0, RoundingMode.HALF_UP).doubleValue());
            model.setMaintainLocation(maintainLocations[i % maintainLocations.length]);
            model.setVehicleDept(depts[i % depts.length]);
            model.setMaintainStatus(statuses[random.nextInt(statuses.length)]);
            
            result.add(model);
        }
        
        return result;
    }

    private IPage<TrafficPartReplacePageModel> generatePartReplacePageData(TrafficRepairPageParam param) {
        long current = param.getCurrent() != 0l ? param.getCurrent() : 1L;
        long size = param.getSize() != 0l ? param.getSize() : 10L;
        
        List<TrafficPartReplacePageModel> allData = generateAllPartReplaceData();
        
        List<TrafficPartReplacePageModel> filteredData = allData.stream()
                .filter(v -> {
                    if (param.getPlateNum() != null && !param.getPlateNum().isEmpty()) {
                        return v.getPlateNum().contains(param.getPlateNum());
                    }
                    return true;
                })
                .filter(v -> {
                    if (param.getVehicleType() != null && !param.getVehicleType().isEmpty() && !"全部".equals(param.getVehicleType())) {
                        return param.getVehicleType().equals(v.getVehicleType());
                    }
                    return true;
                })
                .collect(Collectors.toList());

        Page<TrafficPartReplacePageModel> page = new Page<>(current, size);
        page.setTotal((long) filteredData.size());
        
        int fromIndex = (int) ((current - 1) * size);
        int toIndex = (int) Math.min(fromIndex + size, filteredData.size());
        
        if (fromIndex < filteredData.size()) {
            page.setRecords(filteredData.subList(fromIndex, toIndex));
        } else {
            page.setRecords(new ArrayList<>());
        }
        
        return page;
    }

    private List<TrafficPartReplacePageModel> generateAllPartReplaceData() {
        List<TrafficPartReplacePageModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] platePrefixes = {"浙C", "浙F", "浙P", "浙A", "浙B"};
        String[] replaceReasons = {"轮胎磨损", "轮胎老化", "轮胎破损", "轮胎鼓包"};
        String[] vehicleTypes = {"大巴车", "中巴车", "商务车", "轿车", "电召车"};
        String[] reporters = {"张三", "李四", "王五", "赵六"};
        String[] statuses = {"已完成", "审批中", "待更换"};

        for (int i = 1; i <= 220; i++) {
            TrafficPartReplacePageModel model = new TrafficPartReplacePageModel();
            model.setId(UUID.randomUUID().toString());
            
            String prefix = platePrefixes[i % platePrefixes.length];
            model.setPlateNum(prefix + String.format("%04d", 1234 + (i % 9000)));
            
            model.setReplaceCount(1 + random.nextInt(3));
            model.setReplaceReason(replaceReasons[i % replaceReasons.length]);
            
            int daysAgo = random.nextInt(30);
            model.setReplaceDate(new Date(System.currentTimeMillis() - daysAgo * 24 * 60 * 60 * 1000L));
            
            model.setMileage(BigDecimal.valueOf(50000 + random.nextDouble() * 100000).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setVehicleType(vehicleTypes[i % vehicleTypes.length]);
            model.setReporter(reporters[i % reporters.length]);
            model.setProcessStatus(statuses[random.nextInt(statuses.length)]);
            
            result.add(model);
        }
        
        return result;
    }

    private TrafficDriverAnalysisModel generateDriverAnalysisData() {
        Random random = new Random();
        TrafficDriverAnalysisModel model = new TrafficDriverAnalysisModel();
        
        int totalDrivers = 50 + random.nextInt(20);
        model.setTotalDrivers(totalDrivers);
        
        int maleCount = (int) (totalDrivers * 0.75 + random.nextDouble() * totalDrivers * 0.1);
        int femaleCount = totalDrivers - maleCount;
        model.setMaleCount(maleCount);
        model.setFemaleCount(femaleCount);
        
        model.setMalePercentage(Math.round((double) maleCount / totalDrivers * 10000.0) / 100.0);
        model.setFemalePercentage(Math.round((double) femaleCount / totalDrivers * 10000.0) / 100.0);
        
        return model;
    }

    private List<TrafficDriverEntryCountModel> generateDriverEntryCountData(Integer year, Integer month) {
        Random random = new Random();
        List<TrafficDriverEntryCountModel> result = new ArrayList<>();
        
        int targetYear = year != null ? year : java.time.Year.now().getValue();
        
        if (month != null && month > 0 && month <= 12) {
            TrafficDriverEntryCountModel entry = new TrafficDriverEntryCountModel();
            entry.setDate(String.format("%d年%02d月", targetYear, month));
            entry.setCount(3 + random.nextInt(15));
            result.add(entry);
        } else {
            for (int m = 1; m <= 12; m++) {
                TrafficDriverEntryCountModel entry = new TrafficDriverEntryCountModel();
                entry.setDate(String.format("%d年%02d月", targetYear, m));
                entry.setCount(3 + random.nextInt(15));
                result.add(entry);
            }
        }
        
        return result;
    }

    private List<TrafficDriverWorkloadModel> generateDriverWorkloadData(Integer year, Integer month) {
        List<TrafficDriverWorkloadModel> result = new ArrayList<>();
        Random random = new Random();
        
        String[] driverNames = {"张三", "李四", "王五", "赵六", "陈七", "周八", "吴九", "郑十"};
        String[] teams = {"一组", "二组", "三组"};

        for (int i = 0; i < driverNames.length; i++) {
            TrafficDriverWorkloadModel model = new TrafficDriverWorkloadModel();
            model.setDriverName(driverNames[i]);

            double mileage = 800 + random.nextDouble() * 1200;
            model.setTotalMileage(BigDecimal.valueOf(mileage).setScale(2, RoundingMode.HALF_UP).doubleValue());
            
            result.add(model);
        }
        
        result.sort((a, b) -> b.getTotalMileage().compareTo(a.getTotalMileage()));
        
        return result;
    }

    private List<TrafficMileageUtilizationModel> generateMileageUtilizationData(Integer year) {
        List<TrafficMileageUtilizationModel> result = new ArrayList<>();
        Random random = new Random(year);
        
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};

        for (int i = 0; i < 12; i++) {
            TrafficMileageUtilizationModel model = new TrafficMileageUtilizationModel();
            model.setMonth(months[i]);
            
            double packageMileage = 45 + random.nextDouble() * 20;
            double monthlyMileage = 35 + random.nextDouble() * packageMileage * 0.9;
            
            model.setTotalPackageMileage(BigDecimal.valueOf(packageMileage).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setMonthlyMileage(BigDecimal.valueOf(monthlyMileage).setScale(2, RoundingMode.HALF_UP).doubleValue());
            model.setRemainingMileage(BigDecimal.valueOf(packageMileage - monthlyMileage).setScale(2, RoundingMode.HALF_UP).doubleValue());
            
            double utilizationRate = monthlyMileage / packageMileage * 100;

            result.add(model);
        }
        
        return result;
    }

    private TrafficVehicleDetailInfoModel generateVehicleDetailInfoData(String plateNumber) {
        Random random = new Random(plateNumber.hashCode());
        TrafficVehicleDetailInfoModel model = new TrafficVehicleDetailInfoModel();

        model.setPlateNumber(plateNumber);

        String[] vehicleTypes = {"商务车", "轿车", "大巴车", "中巴车", "电召车"};
        String[] vehicleModels = {"GL8 2.4L舒适版", "奔驰E级", "金龙KLQ6129", "宇通ZK6100", "丰田凯美瑞"};
        String[] teams = {"苍南业主班车", "综合管理部", "工程管理部", "信息文档部", "安全管理部"};

        model.setVehicleType(vehicleTypes[random.nextInt(vehicleTypes.length)]);
        model.setVehicleModel(vehicleModels[random.nextInt(vehicleModels.length)]);
        model.setLastUpdateTime(new Date());

        model.setMileage(BigDecimal.valueOf(5000 + random.nextDouble() * 95000).setScale(2, RoundingMode.HALF_UP).doubleValue());
        model.setTeam(teams[random.nextInt(teams.length)]);

        BigDecimal monthlyRent = BigDecimal.valueOf(500 + random.nextDouble() * 500).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fuelCost = BigDecimal.valueOf(500 + random.nextDouble() * 500).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fuelSubsidy = BigDecimal.valueOf(500 + random.nextDouble() * 500).setScale(2, RoundingMode.HALF_UP);
        BigDecimal parkingFee = BigDecimal.valueOf(500 + random.nextDouble() * 500).setScale(2, RoundingMode.HALF_UP);
        BigDecimal roadBridgeFee = BigDecimal.valueOf(500 + random.nextDouble() * 500).setScale(2, RoundingMode.HALF_UP);

        model.setMonthlyRent(monthlyRent.doubleValue());
        model.setFuelCost(fuelCost.doubleValue());
        model.setFuelSubsidy(fuelSubsidy.doubleValue());
        model.setParkingFee(parkingFee.doubleValue());
        model.setRoadBridgeFee(roadBridgeFee.doubleValue());

        BigDecimal totalCost = monthlyRent.add(fuelCost).add(fuelSubsidy).add(parkingFee).add(roadBridgeFee);
        model.setTotalCost(totalCost.doubleValue());

        int yearsToAdd = 5 + random.nextInt(10);
        model.setNextInspectDate(new Date(System.currentTimeMillis() + yearsToAdd * 365L * 24 * 60 * 60 * 1000));

        return model;
    }

    private TrafficSiteInfoModel generateTodaySiteInfoData() {
        TrafficSiteInfoModel model = new TrafficSiteInfoModel();
        model.setDispatchPhone("13508671234");

        List<TrafficSiteInfoModel.SiteDetail> sites = new ArrayList<>();

        TrafficSiteInfoModel.SiteDetail site1 = new TrafficSiteInfoModel.SiteDetail();
        site1.setLineName("BBX-科技园");
        site1.setDepartureTime("15:00/16:00");
        site1.setSiteType("起始站");
        sites.add(site1);

        TrafficSiteInfoModel.SiteDetail site2 = new TrafficSiteInfoModel.SiteDetail();
        site2.setLineName("BBX-科技园");
        site2.setDepartureTime("15:00/16:00");
        site2.setSiteType("起始站");
        sites.add(site2);

        TrafficSiteInfoModel.SiteDetail site3 = new TrafficSiteInfoModel.SiteDetail();
        site3.setLineName("金色蓝湾-BBX-机场");
        site3.setDepartureTime("7:30-8:00/15:00/20:00");
        site3.setSiteType("过路站");
        sites.add(site3);

        TrafficSiteInfoModel.SiteDetail site4 = new TrafficSiteInfoModel.SiteDetail();
        site4.setLineName("BJ-北护岸下钢筋...");
        site4.setDepartureTime("15:00/16:00");
        site4.setSiteType("终点站");
        sites.add(site4);

        TrafficSiteInfoModel.SiteDetail site5 = new TrafficSiteInfoModel.SiteDetail();
        site5.setLineName("BBX-苍南南站");
        site5.setDepartureTime("12:30-13:00/15:00/17:00");
        site5.setSiteType("过路站");
        sites.add(site5);

        TrafficSiteInfoModel.SiteDetail site6 = new TrafficSiteInfoModel.SiteDetail();
        site6.setLineName("BBX-温州南站");
        site6.setDepartureTime("12:30-13:00/15:00/17:00");
        site6.setSiteType("过路站");
        sites.add(site6);

        model.setSites(sites);
        return model;
    }
}