
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleHotLineModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleHotStationModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.model.ShuttleRunOrgStatModel;
import com.cgnpc.bbxpark.traffic.domain.VehicleLineOrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehicleLineOrderInfoRepository extends BaseMapper<VehicleLineOrderInfo> {
    ShuttleOverviewModel getShuttleOverview(@Param("start") Date start, @Param("end") Date end);

    List<ShuttleRunOrgStatModel> getShuttleRunOrgAnalysis(@Param("start") Date start, @Param("end") Date end);

    List<ShuttleHotLineModel> getShuttleHotLine(@Param("start") Date start, @Param("end") Date end);

    List<ShuttleHotStationModel> getShuttleHotStation(@Param("start") Date start, @Param("end") Date end);
}
