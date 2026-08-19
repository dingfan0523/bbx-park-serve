
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleCarTaskRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DwdVehicleCarTaskRecordRepository extends BaseMapper<DwdVehicleCarTaskRecord> {

    /**
     * 前10驾驶员行驶里程
     * @return
     */
    List<TrafficDriverWorkloadModel> getDriverWorkload(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 驾驶员行驶里程概览
     * @return
     */
    DispatchOverviewModel getDispatchOverview(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 驾驶员行驶数量统计
     * @return
     */
    List<DispatchCountTrendModel> getDispatchCountTrend(@Param("timeType") String timeType, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 驾驶员行驶里程统计
     * @return
     */
    List<DispatchMileageTrendModel> getDispatchMileageTrend(@Param("timeType") String timeType, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);
}
