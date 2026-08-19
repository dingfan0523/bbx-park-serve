
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficDriverCountModel;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficDriverEntryCountModel;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficMaintenanceModel;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficVehicleTypeModel;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleDriverInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DwdVehicleDriverInfoRepository extends BaseMapper<DwdVehicleDriverInfo> {

    /**
     * 驾驶员性别数量统计
     * @return
     */
    List<TrafficDriverCountModel> getDriverAnalysis();

    /**
     * 驾驶员入职时间统计
     * @return
     */
    List<TrafficDriverEntryCountModel> getDriverEntryCount(@Param("timeType") String timeType, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);
}
