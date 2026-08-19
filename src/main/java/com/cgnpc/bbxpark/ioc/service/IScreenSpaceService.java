package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.ConstructionPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.StationPageParam;
import com.cgnpc.bbxpark.ioc.dto.param.StoragePageParam;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author dingfan
 * @version 1.0
 * @date 2026/2/28 14:11
 */
public interface IScreenSpaceService {
    List<SpaceTypeDistributionModel> getSpaceTypeDistribution();

    OfficeSpaceOverviewModel getOfficeSpaceOverview();

    List<DeptOfficeSpaceAreaModel> getDeptOfficeSpaceArea();

    List<DeptOfficeSpaceStationModel> getDeptOfficeSpaceStation();

    List<DeviceSpaceDistributionModel> getIntelligentDistribution(String sslcCode, Long spaceId);

    MeetingRoomUtilizationOverviewModel getMeetingRoomUtilizationOverview();

    List<MeetingRoomUtilizationAnalysisModel> getMeetingRoomUtilizationAnalysis();

    SecurityManagementOverviewModel getSecurityManagementOverview();

    List<ConstructionModel> getConstructionList();

    IPage<ConstructionModel> getConstructionPage(ConstructionPageParam param);

    List<StorageModel> getStorageList();

    IPage<StorageModel> getStoragePage(StoragePageParam param);

    ConstructionDetailModel getConstructionDetail(Long id);

    StorageDetailModel  getStorageDetail(Long id);

    List<SpaceViewModel> getSpaceView(String sslcCode,Integer type);

    List<SpaceListModel> getSpaceList(String sslcCode);

    List<ManagerModel> getManagerList(String sslcCode);

    List<StationModel> getStationList(String sslcCode,Long spaceId);

    IPage<StationModel> getStationPage(StationPageParam param);

    List<SpaceImageModel> getImageList(String sslcCode,Long spaceId);

    IPage<DeviceListModel> getDevicePage(DevicePageParam param);

    SpaceDetailModel detail(String sslcCode,Long spaceId);

    List<KeywordSearchModel> keywordSearch(String keyword);

    FloorDeviceCountModel getFloorDeviceCount(String sslcCode);
}
