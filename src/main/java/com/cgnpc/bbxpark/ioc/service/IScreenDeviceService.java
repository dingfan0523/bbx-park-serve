package com.cgnpc.bbxpark.ioc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsModel;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.AlarmAnalysisParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePageParam;
import com.cgnpc.bbxpark.ioc.dto.param.DevicePointParam;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IScreenDeviceService {
    DeviceOverviewModel getDeviceOverview(String sslcCode);

    List<DeviceDeptDistributionModel> getDeptDeviceDistribution(String sslcCode);

    DeviceLifeDistributionModel getLifeDistribution(String sslcCode);

    DeviceDepreciationAnalysisModel getDepreciationAnalysis(String sslcCode);

    StrategicMetricsModel getStrategicMetrics(String sslcCode);

    IntelligentDeviceOverview getIntelligentOverview(String sslcCode);

    List<DeviceSpaceDistributionModel> getIntelligentDistribution(String sslcCode,Long spaceId);

    List<DeviceHealthTrendModel> getHealthTrend(String sslcCode);

    List<AlarmAnalysisModel> getAlarmAnalysis(AlarmAnalysisParam param);

    List<DevicePointModel> pointList(DevicePointParam param);

    IPage<DeviceModel> list(DevicePageParam param);

    DeviceDetailModel detail(Long id);

    DeviceSummaryModel summaryDetail(Long id);

    List<ProductListModel> productList();

    List<KeywordSearchModel> search(String keyword);

    PlayBackURLsModel getHKCameraPreviewUrl(Long id, Integer streamType);
}
