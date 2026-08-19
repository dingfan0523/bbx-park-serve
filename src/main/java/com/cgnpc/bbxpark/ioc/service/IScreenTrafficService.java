package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.CallTaxiRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.CarRentRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.CarTravelRecordPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.ShuttleOrderPageParam;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IScreenTrafficService {
    List<CallTaxiReasonStatModel> getCallTaxiReasonAnalysis(Integer year,Integer month);

    List<CallTaxiDeptStatModel> getCallTaxiDeptAnalysis(Integer year,Integer month);

    ShuttleOverviewModel getShuttleOverview(Integer year,Integer month);

    List<ShuttleRunOrgStatModel> getShuttleRunOrgAnalysis(Integer year,Integer month);

    List<ShuttleHotLineModel> getShuttleHotLine(Integer year,Integer month);

    List<ShuttleHotStationModel> getShuttleHotStation(Integer year,Integer month);

    IPage<CallTaxiRecordModel> pageCallTaxiRecord(CallTaxiRecordPageParam param);

    IPage<ShuttleOrderModel> pageShuttleOrder(ShuttleOrderPageParam param);

    DispatchOverviewModel getDispatchOverview(Integer year, Integer month);

    List<DispatchCountTrendModel> getDispatchCountTrend(Integer year, Integer month);

    List<DispatchMileageTrendModel> getDispatchMileageTrend(Integer year, Integer month);

    IPage<CarTravelRecordModel> pageCarTravelRecord(CarTravelRecordPageParam param);

    IPage<CarRentRecordModel> pageCarRentRecord(CarRentRecordPageParam param);

    CarRentOverviewModel getCarRentOverview(Integer year, Integer month);

    List<CarRentTypeStatModel> getCarRentTypeAnalysis(Integer year, Integer month);

    List<CarRentTrendModel> getCarRentTrend(Integer year, Integer month);
}