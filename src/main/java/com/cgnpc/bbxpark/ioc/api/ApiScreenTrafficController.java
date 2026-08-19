package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.CallTaxiDeptStatModel;
import com.cgnpc.bbxpark.ioc.dto.model.CallTaxiReasonStatModel;
import com.cgnpc.bbxpark.ioc.dto.model.CallTaxiRecordModel;
import com.cgnpc.bbxpark.ioc.dto.model.CarRentOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.model.CarRentRecordModel;
import com.cgnpc.bbxpark.ioc.dto.model.CarRentTrendModel;
import com.cgnpc.bbxpark.ioc.dto.model.CarRentTypeStatModel;
import com.cgnpc.bbxpark.ioc.dto.model.CarTravelRecordModel;
import com.cgnpc.bbxpark.ioc.dto.model.DispatchCountTrendModel;
import com.cgnpc.bbxpark.ioc.dto.model.DispatchMileageTrendModel;
import com.cgnpc.bbxpark.ioc.dto.model.DispatchOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleHotLineModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleHotStationModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleOrderModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleRunOrgStatModel;
import com.cgnpc.bbxpark.ioc.dto.param.CallTaxiRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.CarRentRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.CarTravelRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.ShuttleOrderPageParam;
import com.cgnpc.bbxpark.ioc.service.IScreenTrafficService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/dtwin/traffic"})
@Api(tags = {"大屏-智慧交通运营接口"})
public class ApiScreenTrafficController {
    private static final long MOCK_SEED = 20260525L;

    @Autowired
    private IScreenTrafficService screenTrafficService;

    @GetMapping({"/callTaxi/reason"})
    @ApiOperation("电召车使用原因分析")
    public CudResult<List<CallTaxiReasonStatModel>> getCallTaxiReasonAnalysis(@ApiParam("年") @RequestParam Integer year,
                                                                             @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenTrafficService.getCallTaxiReasonAnalysis(year,month));
    }

    @GetMapping({"/callTaxi/department"})
    @ApiOperation("电召车使用部门分析")
    public CudResult<List<CallTaxiDeptStatModel>> getCallTaxiDeptAnalysis(@ApiParam("年") @RequestParam Integer year,
                                                                         @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenTrafficService.getCallTaxiDeptAnalysis(year,month));
    }

    @GetMapping({"/shuttle/overview"})
    @ApiOperation("便民班车概览")
    public CudResult<ShuttleOverviewModel> getShuttleOverview(@ApiParam("年") @RequestParam Integer year,
                                                             @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenTrafficService.getShuttleOverview(year,month));
    }

    @GetMapping({"/shuttle/runOrg"})
    @ApiOperation("便民班车运行组织机构分析")
    public CudResult<List<ShuttleRunOrgStatModel>> getShuttleRunOrgAnalysis(@ApiParam("年") @RequestParam Integer year,
                                                                           @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenTrafficService.getShuttleRunOrgAnalysis(year,month));
    }

    @GetMapping({"/shuttle/hotLine"})
    @ApiOperation("便民班车预约热门线路TOP5")
    public CudResult<List<ShuttleHotLineModel>> getShuttleHotLine(@ApiParam("年") @RequestParam Integer year,
                                                                     @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenTrafficService.getShuttleHotLine(year,month));
    }

    @GetMapping({"/shuttle/hotStation"})
    @ApiOperation("便民班车预约热门站点")
    public CudResult<List<ShuttleHotStationModel>> getShuttleHotStation(@ApiParam("年") @RequestParam Integer year,
                                                                           @ApiParam("月") @RequestParam(required = false) Integer month) {
        return CudResult.success(screenTrafficService.getShuttleHotStation(year,month));
    }

    @GetMapping({"/rent/overview"})
    @ApiOperation("租车分析概览")
    public CudResult<CarRentOverviewModel> getCarRentOverview(@ApiParam("年") @RequestParam Integer year,
                                                             @ApiParam("月") @RequestParam(required = false) Integer month) {
//        String error = validateYearMonth(year, month);
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            YearMonth ym = resolveYearMonth(year, month);
//            CarRentOverviewModel data = generateCarRentOverviewData(ym);
            return CudResult.success(screenTrafficService.getCarRentOverview(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/rent/type"})
    @ApiOperation("租车用途分析")
    public CudResult<List<CarRentTypeStatModel>> getCarRentTypeAnalysis(@ApiParam("年") @RequestParam Integer year,
                                                                       @ApiParam("月") @RequestParam(required = false) Integer month) {
//        String error = validateYearMonth(year, month);
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            YearMonth ym = resolveYearMonth(year, month);
//            List<CarRentTypeStatModel> data = generateCarRentTypeStatData(ym);
            return CudResult.success(screenTrafficService.getCarRentTypeAnalysis(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/rent/trend"})
    @ApiOperation("租车数量趋势")
    public CudResult<List<CarRentTrendModel>> getCarRentTrend(@ApiParam("年") @RequestParam Integer year,
                                                             @ApiParam("月") @RequestParam(required = false) Integer month) {
//        String error = validateYearMonth(year, month);
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            Integer y = resolveYear(year);
//            List<CarRentTrendModel> data = generateCarRentTrendData(y, month);
            return CudResult.success(screenTrafficService.getCarRentTrend(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/dispatch/overview"})
    @ApiOperation("出车分析概览")
    public CudResult<DispatchOverviewModel> getDispatchOverview(@ApiParam("年") @RequestParam Integer year,
                                                               @ApiParam("月") @RequestParam(required = false) Integer month) {
//        String error = validateYearMonth(year, month);
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            YearMonth ym = resolveYearMonth(year, month);
//            DispatchOverviewModel data = generateDispatchOverviewData(ym);
            return CudResult.success(screenTrafficService.getDispatchOverview(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/dispatch/countTrend"})
    @ApiOperation("出车次数趋势")
    public CudResult<List<DispatchCountTrendModel>> getDispatchCountTrend(@ApiParam("年") @RequestParam Integer year,
                                                                         @ApiParam("月") @RequestParam(required = false) Integer month) {
//        String error = validateYearMonth(year, month);
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            Integer y = resolveYear(year);
//            List<DispatchCountTrendModel> data = generateDispatchCountTrendData(y, month);
            return CudResult.success(screenTrafficService.getDispatchCountTrend(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @GetMapping({"/dispatch/mileageTrend"})
    @ApiOperation("总驾驶里程趋势")
    public CudResult<List<DispatchMileageTrendModel>> getDispatchMileageTrend(@ApiParam("年") @RequestParam Integer year,
                                                                             @ApiParam("月") @RequestParam(required = false) Integer month) {
//        String error = validateYearMonth(year, month);
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            Integer y = resolveYear(year);
//            List<DispatchMileageTrendModel> data = generateDispatchMileageTrendData(y, month);
            return CudResult.success(screenTrafficService.getDispatchMileageTrend(year, month));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "电召车出车记录分页列表")
    @PostMapping(value = "/callTaxi/record/page")
    public CudResult<IPage<CallTaxiRecordModel>> pageCallTaxiRecord(@RequestBody CallTaxiRecordPageParam param) {
        return CudResult.success(screenTrafficService.pageCallTaxiRecord(param));
    }

    @ApiOperation(value = "便民班车订单分页列表")
    @PostMapping(value = "/shuttle/order/page")
    public CudResult<IPage<ShuttleOrderModel>> pageShuttleOrder(@RequestBody ShuttleOrderPageParam param) {
        return CudResult.success(screenTrafficService.pageShuttleOrder(param));
    }

    @ApiOperation(value = "租车记录分页列表")
    @PostMapping(value = "/rent/record/page")
    public CudResult<IPage<CarRentRecordModel>> pageCarRentRecord(@RequestBody CarRentRecordPageParam param) {
//        String error = validateYearMonth(param == null ? null : param.getYear(), param == null ? null : param.getMonth());
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            IPage<CarRentRecordModel> data = generateCarRentRecordPageData(param);
            return CudResult.success(screenTrafficService.pageCarRentRecord(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "车辆行驶记录分页列表")
    @PostMapping(value = "/car/travel/page")
    public CudResult<IPage<CarTravelRecordModel>> pageCarTravelRecord(@RequestBody CarTravelRecordPageParam param) {
//        String error = validateYearMonth(param == null ? null : param.getYear(), param == null ? null : param.getMonth());
//        if (error != null) {
//            return CudResult.errorMessage(error);
//        }
        try {
//            IPage<CarTravelRecordModel> data = generateCarTravelRecordPageData(param);
            return CudResult.success(screenTrafficService.pageCarTravelRecord(param));
        } catch (Exception e) {
            return CudResult.errorMessage("读取数据失败: " + e.getMessage());
        }
    }

    private List<CallTaxiReasonStatModel> generateCallTaxiReasonStatData(YearMonth ym) {
        String[] reasons = {"公务用车", "会议接驳", "出差接送", "加班返程", "客户接待", "紧急外出"};
        Random random = new Random(MOCK_SEED + ym.getYear() * 100L + ym.getMonthValue());
        List<CallTaxiReasonStatModel> list = new ArrayList<>();
        for (String reason : reasons) {
            CallTaxiReasonStatModel model = new CallTaxiReasonStatModel();
            model.setCallReason(reason);
            model.setCount(18 + random.nextInt(260));
            list.add(model);
        }
        return list.stream()
                .sorted(Comparator.comparingInt(CallTaxiReasonStatModel::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<CallTaxiDeptStatModel> generateCallTaxiDeptStatData(YearMonth ym) {
        String[] depts = {"综合管理部", "工程管理部", "安全管理部", "客户服务部", "信息文档部", "财务部"};
        Random random = new Random(MOCK_SEED + 31 + ym.getYear() * 100L + ym.getMonthValue());
        List<CallTaxiDeptStatModel> list = new ArrayList<>();
        for (String dept : depts) {
            CallTaxiDeptStatModel model = new CallTaxiDeptStatModel();
            model.setDepartment(dept);
            model.setCount(12 + random.nextInt(240));
            list.add(model);
        }
        return list.stream()
                .sorted(Comparator.comparingInt(CallTaxiDeptStatModel::getCount).reversed())
                .collect(Collectors.toList());
    }

    private ShuttleOverviewModel generateShuttleOverviewData(YearMonth ym) {
        Random random = new Random(MOCK_SEED + 61 + ym.getYear() * 100L + ym.getMonthValue());
        ShuttleOverviewModel model = new ShuttleOverviewModel();
        model.setLineTotal(6 + random.nextInt(10));
        model.setCount(600 + random.nextInt(2600));
        return model;
    }



    private List<ShuttleHotLineModel> generateShuttleHotLineTop5Data(YearMonth ym) {
        Random random = new Random(MOCK_SEED + 81 + ym.getYear() * 100L + ym.getMonthValue());
        String[] lines = {"园区环线A", "园区环线B", "BBX-科技园快线", "通勤专线1", "通勤专线2", "夜间接驳线"};

        List<ShuttleHotLineModel> list = new ArrayList<>();
        for (String line : lines) {
            ShuttleHotLineModel model = new ShuttleHotLineModel();
            model.setLineName(line);
            model.setCount(120 + random.nextInt(980));
            list.add(model);
        }

        return list.stream()
                .sorted(Comparator.comparingInt(ShuttleHotLineModel::getCount).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }


    private CarRentOverviewModel generateCarRentOverviewData(YearMonth ym) {
        Random random = new Random(MOCK_SEED + 101 + ym.getYear() * 100L + ym.getMonthValue());
        CarRentOverviewModel model = new CarRentOverviewModel();
        int rentCount = 80 + random.nextInt(1200);
        int rentDays = Math.max(rentCount, 1) * (1 + random.nextInt(3));
        model.setRentCount(rentCount);
        model.setRentDays(rentDays);
        return model;
    }

    private List<CarRentTypeStatModel> generateCarRentTypeStatData(YearMonth ym) {
        Random random = new Random(MOCK_SEED + 111 + ym.getYear() * 100L + ym.getMonthValue());
        String[] types = {"公务出行", "商务接待", "出差用车", "临时保障", "物资运输"};

        List<Integer> counts = new ArrayList<>();
        int total = 0;
        for (int i = 0; i < types.length; i++) {
            int c = 30 + random.nextInt(360);
            counts.add(c);
            total += c;
        }

        List<CarRentTypeStatModel> list = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            CarRentTypeStatModel model = new CarRentTypeStatModel();
            model.setRentType(types[i]);
            model.setCount(counts.get(i));
            model.setRatio(total == 0 ? 0D : round(counts.get(i) * 100D / total, 2));
            list.add(model);
        }
        return list.stream()
                .sorted(Comparator.comparingInt(CarRentTypeStatModel::getCount).reversed())
                .collect(Collectors.toList());
    }

    private List<CarRentTrendModel> generateCarRentTrendData(Integer year, Integer month) {
        List<CarRentTrendModel> list = new ArrayList<>();
        long seed = MOCK_SEED + 121 + year * 1000L + (month == null ? 0 : month);
        Random random = new Random(seed);

        if (month == null) {
            for (int m = 1; m <= 12; m++) {
                CarRentTrendModel model = new CarRentTrendModel();
                model.setDate(m + "月");
                model.setCount(40 + random.nextInt(260));
                list.add(model);
            }
            return list;
        }

        YearMonth ym = YearMonth.of(year, month);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            double wave = Math.sin(day * Math.PI / 6D) * 8D;
            int count = (int) Math.max(0, Math.round(18D + wave + random.nextDouble() * 20D));
            CarRentTrendModel model = new CarRentTrendModel();
            model.setDate(date.format(formatter));
            model.setCount(count);
            list.add(model);
        }
        return list;
    }

    private DispatchOverviewModel generateDispatchOverviewData(YearMonth ym) {
        Random random = new Random(MOCK_SEED + 131 + ym.getYear() * 100L + ym.getMonthValue());
        DispatchOverviewModel model = new DispatchOverviewModel();
        model.setAvgDepartCount(round(6D + random.nextDouble() * 12D, 1));
        model.setPerCapitaMileageKm(round(120D + random.nextDouble() * 320D, 1));
        return model;
    }

    private List<DispatchCountTrendModel> generateDispatchCountTrendData(Integer year, Integer month) {
        List<DispatchCountTrendModel> list = new ArrayList<>();
        long seed = MOCK_SEED + 141 + year * 1000L + (month == null ? 0 : month);
        Random random = new Random(seed);

        if (month == null) {
            for (int m = 1; m <= 12; m++) {
                DispatchCountTrendModel model = new DispatchCountTrendModel();
                model.setDate(m + "月");
                model.setCount(120 + random.nextInt(520));
                list.add(model);
            }
            return list;
        }

        YearMonth ym = YearMonth.of(year, month);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            double wave = Math.sin(day * Math.PI / 5D) * 18D;
            int count = (int) Math.max(0, Math.round(60D + wave + random.nextDouble() * 35D));
            DispatchCountTrendModel model = new DispatchCountTrendModel();
            model.setDate(date.format(formatter));
            model.setCount(count);
            list.add(model);
        }
        return list;
    }

    private List<DispatchMileageTrendModel> generateDispatchMileageTrendData(Integer year, Integer month) {
        List<DispatchMileageTrendModel> list = new ArrayList<>();
        long seed = MOCK_SEED + 151 + year * 1000L + (month == null ? 0 : month);
        Random random = new Random(seed);

        if (month == null) {
            for (int m = 1; m <= 12; m++) {
                DispatchMileageTrendModel model = new DispatchMileageTrendModel();
                model.setDate(m + "月");
                model.setTotalMileage(round(2200D + random.nextDouble() * 6800D, 1));
                list.add(model);
            }
            return list;
        }

        YearMonth ym = YearMonth.of(year, month);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            double wave = Math.sin(day * Math.PI / 5D) * 120D;
            double mileage = Math.max(0D, 260D + wave + random.nextDouble() * 180D);
            DispatchMileageTrendModel model = new DispatchMileageTrendModel();
            model.setDate(date.format(formatter));
            model.setTotalMileage(round(mileage, 1));
            list.add(model);
        }
        return list;
    }

    private IPage<CallTaxiRecordModel> generateCallTaxiRecordPageData(CallTaxiRecordPageParam param) {
        CallTaxiRecordPageParam actualParam = param == null ? new CallTaxiRecordPageParam() : param;
        IPage<CallTaxiRecordModel> page = actualParam.getPage();
        Page<CallTaxiRecordModel> result = new Page<>(page.getCurrent(), page.getSize());
        int total = 220;
        result.setTotal(total);

        Integer year = resolveYear(actualParam.getYear());
        Integer month = actualParam.getMonth();

        long seed = MOCK_SEED + 301;
        if (year != null) {
            seed += year * 1000L;
        }
        if (month != null) {
            seed += month;
        }
        if (StringUtils.isNotBlank(actualParam.getCallReason())) {
            seed += actualParam.getCallReason().hashCode();
        }
        if (StringUtils.isNotBlank(actualParam.getDepartment())) {
            seed += actualParam.getDepartment().hashCode();
        }
        Random random = new Random(seed);

        String[] passengers = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        String[] departments = {"综合管理部", "工程管理部", "安全管理部", "客户服务部", "信息文档部", "财务部"};
        String[] reasons = {"公务用车", "会议接驳", "出差接送", "加班返程", "客户接待", "紧急外出"};
        String[] points = {"BBX大楼", "科技园", "行政服务大厅", "园区北门", "园区南门", "停车场入口", "食堂"};
        String[] approveResults = {"同意", "驳回", "待审批"};

        long startIndex = (result.getCurrent() - 1) * result.getSize();
        long endExclusive = Math.min(startIndex + result.getSize(), total);
        List<CallTaxiRecordModel> records = new ArrayList<>();

        for (long idx = startIndex; idx < endExclusive; idx++) {
            CallTaxiRecordModel model = new CallTaxiRecordModel();
            model.setId(idx + 1);
            model.setPassenger(passengers[random.nextInt(passengers.length)]);

            String dept = StringUtils.isNotBlank(actualParam.getDepartment()) ? actualParam.getDepartment() : departments[random.nextInt(departments.length)];
            model.setDepartment(dept);

            model.setPassengerNum(1 + random.nextInt(5));

            String reason = StringUtils.isNotBlank(actualParam.getCallReason()) ? actualParam.getCallReason() : reasons[random.nextInt(reasons.length)];
            model.setCallReason(reason);

            String startPoint = points[random.nextInt(points.length)];
            String endPoint = points[random.nextInt(points.length)];
            if (startPoint.equals(endPoint)) {
                endPoint = points[(random.nextInt(points.length - 1) + 1) % points.length];
            }
            model.setStartPoint(startPoint);
            model.setEndPoint(endPoint);
            model.setApproveResult(approveResults[random.nextInt(approveResults.length)]);
            records.add(model);
        }

        result.setRecords(records);
        return result;
    }

    private IPage<ShuttleOrderModel> generateShuttleOrderPageData(ShuttleOrderPageParam param) {
        ShuttleOrderPageParam actualParam = param == null ? new ShuttleOrderPageParam() : param;
        IPage<ShuttleOrderModel> page = actualParam.getPage();
        Page<ShuttleOrderModel> result = new Page<>(page.getCurrent(), page.getSize());
        int total = 220;
        result.setTotal(total);

        Integer year = resolveYear(actualParam.getYear());
        Integer month = actualParam.getMonth();
        YearMonth ym = resolveYearMonth(year, month);

        long seed = MOCK_SEED + 401 + ym.getYear() * 1000L + ym.getMonthValue();
        if (StringUtils.isNotBlank(actualParam.getLineName())) {
            seed += actualParam.getLineName().hashCode();
        }
        if (StringUtils.isNotBlank(actualParam.getStartStation())) {
            seed += actualParam.getStartStation().hashCode();
        }
        if (StringUtils.isNotBlank(actualParam.getEndStation())) {
            seed += actualParam.getEndStation().hashCode();
        }
        Random random = new Random(seed);

        String[] runOrgs = {"行政服务中心", "后勤保障部", "园区运营中心"};
        String[] lineNames = {"园区环线A", "园区环线B", "BBX-科技园快线", "通勤专线1", "通勤专线2"};
        String[] directions = {"上行", "下行"};
        String[] stations = {"BBX大楼门口", "科技园北门", "停车场入口", "行政服务大厅", "食堂门口", "地铁接驳点"};

        long startIndex = (result.getCurrent() - 1) * result.getSize();
        long endExclusive = Math.min(startIndex + result.getSize(), total);
        List<ShuttleOrderModel> records = new ArrayList<>();

        for (long idx = startIndex; idx < endExclusive; idx++) {
            ShuttleOrderModel model = new ShuttleOrderModel();
            model.setId(idx + 1);
            model.setRunOrg(runOrgs[random.nextInt(runOrgs.length)]);
            model.setLineName(StringUtils.isNotBlank(actualParam.getLineName()) ? actualParam.getLineName() : lineNames[random.nextInt(lineNames.length)]);
            model.setLineDirection(directions[random.nextInt(directions.length)]);
            model.setRideNum(1 + random.nextInt(8));
            model.setStartStation(StringUtils.isNotBlank(actualParam.getStartStation()) ? actualParam.getStartStation() : stations[random.nextInt(stations.length)]);
            model.setEndStation(StringUtils.isNotBlank(actualParam.getEndStation()) ? actualParam.getEndStation() : stations[random.nextInt(stations.length)]);
            if (StringUtils.equals(model.getStartStation(), model.getEndStation())) {
                model.setEndStation(stations[(random.nextInt(stations.length - 1) + 1) % stations.length]);
            }
            records.add(model);
        }

        result.setRecords(records);
        return result;
    }

    private IPage<CarRentRecordModel> generateCarRentRecordPageData(CarRentRecordPageParam param) {
        CarRentRecordPageParam actualParam = param == null ? new CarRentRecordPageParam() : param;
        IPage<CarRentRecordModel> page = actualParam.getPage();
        Page<CarRentRecordModel> result = new Page<>(page.getCurrent(), page.getSize());
        int total = 220;
        result.setTotal(total);

        Integer year = resolveYear(actualParam.getYear());
        Integer month = actualParam.getMonth();

        long seed = MOCK_SEED + 501 + year * 1000L + (month == null ? 0 : month);
        if (StringUtils.isNotBlank(actualParam.getRentType())) {
            seed += actualParam.getRentType().hashCode();
        }
        if (StringUtils.isNotBlank(actualParam.getInstanceStatus())) {
            seed += actualParam.getInstanceStatus().hashCode();
        }
        if (actualParam.getRentTime() != null) {
            seed += actualParam.getRentTime().hashCode();
        }
        Random random = new Random(seed);

        String[] rentTypes = {"公务出行", "商务接待", "出差用车", "临时保障", "物资运输"};
        String[] statuses = {"审批中", "已通过", "已驳回", "已撤回"};
        String[] approveResults = {"同意", "驳回", "待审批"};
        String[] detailInfos = {"含司机服务", "自驾取还车", "需加装儿童座椅", "用车含高速费", "取车点：园区停车场"};

        long startIndex = (result.getCurrent() - 1) * result.getSize();
        long endExclusive = Math.min(startIndex + result.getSize(), total);
        List<CarRentRecordModel> records = new ArrayList<>();

        for (long idx = startIndex; idx < endExclusive; idx++) {
            CarRentRecordModel model = new CarRentRecordModel();
            model.setId(idx + 1);
            model.setRentType(StringUtils.isNotBlank(actualParam.getRentType()) ? actualParam.getRentType() : rentTypes[random.nextInt(rentTypes.length)]);

            Date rentTime = actualParam.getRentTime() != null
                    ? actualParam.getRentTime()
                    : java.sql.Date.valueOf(randomDateInRange(year, month, random).format(DateTimeFormatter.ISO_LOCAL_DATE));
            model.setRentStartTime(rentTime);
            model.setRentEndTime(rentTime);
            double days = 1d + random.nextInt(7);
            model.setRentDays(days);
            model.setDetailInfo(detailInfos[random.nextInt(detailInfos.length)]);
            model.setApproveResult(approveResults[random.nextInt(approveResults.length)]);
            model.setInstanceStatus(StringUtils.isNotBlank(actualParam.getInstanceStatus()) ? actualParam.getInstanceStatus() : statuses[random.nextInt(statuses.length)]);
            records.add(model);
        }

        result.setRecords(records);
        return result;
    }

    private IPage<CarTravelRecordModel> generateCarTravelRecordPageData(CarTravelRecordPageParam param) {
        CarTravelRecordPageParam actualParam = param == null ? new CarTravelRecordPageParam() : param;
        IPage<CarTravelRecordModel> page = actualParam.getPage();
        Page<CarTravelRecordModel> result = new Page<>(page.getCurrent(), page.getSize());
        int total = 220;
        result.setTotal(total);

        Integer year = resolveYear(actualParam.getYear());
        Integer month = actualParam.getMonth();

        long seed = MOCK_SEED + 601 + year * 1000L + (month == null ? 0 : month);
        if (StringUtils.isNotBlank(actualParam.getCarPlate())) {
            seed += actualParam.getCarPlate().hashCode();
        }
        if (StringUtils.isNotBlank(actualParam.getDriverName())) {
            seed += actualParam.getDriverName().hashCode();
        }
        Random random = new Random(seed);

        String[] companies = {"BBX行政服务中心", "园区运营中心", "后勤保障部"};
        String[] plates = {"浙A12345", "浙A56789", "浙A8X8X8", "浙A6Y6Y6", "浙A9Z9Z9"};
        String[] drivers = {"张三", "李四", "王五", "赵六", "钱七", "孙八"};
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        long startIndex = (result.getCurrent() - 1) * result.getSize();
        long endExclusive = Math.min(startIndex + result.getSize(), total);
        List<CarTravelRecordModel> records = new ArrayList<>();

        for (long idx = startIndex; idx < endExclusive; idx++) {
            CarTravelRecordModel model = new CarTravelRecordModel();
            model.setId(idx + 1 + "");
            model.setCarCompany(companies[random.nextInt(companies.length)]);
            model.setCarPlate(StringUtils.isNotBlank(actualParam.getCarPlate()) ? actualParam.getCarPlate() : plates[random.nextInt(plates.length)]);
            model.setDriverName(StringUtils.isNotBlank(actualParam.getDriverName()) ? actualParam.getDriverName() : drivers[random.nextInt(drivers.length)]);

            LocalDate day = randomDateInRange(year, month, random);
            LocalDateTime depart = day.atTime(8 + random.nextInt(10), random.nextInt(60), random.nextInt(60));
            LocalDateTime ret = depart.plusMinutes(20 + random.nextInt(160));

            model.setDepartTime(Date.from(depart.atZone(ZoneId.systemDefault()).toInstant()));
            model.setReturnTime(Date.from(ret.atZone(ZoneId.systemDefault()).toInstant()));
            model.setSingleMileage(round(3D + random.nextDouble() * 78D, 1));
            records.add(model);
        }

        result.setRecords(records);
        return result;
    }

    private LocalDate randomDateInRange(Integer year, Integer month, Random random) {
        int y = resolveYear(year);
        int m = month == null ? 1 + random.nextInt(12) : month;
        YearMonth ym = YearMonth.of(y, m);
        int day = 1 + random.nextInt(ym.lengthOfMonth());
        return ym.atDay(day);
    }

    private String validateYearMonth(Integer year, Integer month) {
        if (month == null) {
            return null;
        }
        if (month < 1 || month > 12) {
            return "month必须在1-12之间";
        }
        return null;
    }

    private Integer resolveYear(Integer year) {
        return year == null ? LocalDate.now().getYear() : year;
    }

    private YearMonth resolveYearMonth(Integer year, Integer month) {
        Integer y = resolveYear(year);
        Integer m = month == null ? LocalDate.now().getMonthValue() : month;
        return YearMonth.of(y, m);
    }

    private double round(double value, int scale) {
        return new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
