package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficMileagePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficRepairPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.TrafficVehiclePageParam;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IScreenTrafficOverviewService {

    TrafficMacroIndexModel getMacroIndex(Integer year, Integer month);

    List<TrafficVehicleTypeModel> getVehicleTypeDistribution();

    List<TrafficMaintenanceModel> getMaintenanceAnalysis(Integer year);

    List<TrafficVehicleDetailModel> getVehicleDetailList(Integer year, Integer month);

    TrafficVehicleDetailInfoModel getVehicleDetailInfo(String plateNumber);

    IPage<TrafficVehiclePageModel> getVehiclePage(TrafficVehiclePageParam param);

    IPage<TrafficMileagePageModel> getMileagePage(TrafficMileagePageParam param);

    IPage<TrafficRepairPageModel> getRepairPage(TrafficRepairPageParam param);

    IPage<TrafficMaintainPageModel> getMaintainPage(TrafficRepairPageParam param);

    IPage<TrafficPartReplacePageModel> getPartReplacePage(TrafficRepairPageParam param);

    TrafficDriverAnalysisModel getDriverAnalysis();

    List<TrafficDriverEntryCountModel> getDriverEntryCount(Integer year, Integer month);

    List<TrafficDriverWorkloadModel> getDriverWorkload(Integer year, Integer month);

    List<TrafficMileageUtilizationModel> getMileageUtilization(Integer year);
}
