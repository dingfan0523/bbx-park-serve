
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantConsumptionCountItem;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficMaintenanceModel;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMaintain;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DwdVehicleMaintainRepository extends BaseMapper<DwdVehicleMaintain> {

    /**
     * 车辆维保费用按月统计
     * @return
     */
    List<TrafficMaintenanceModel> getMaintenanceAnalysis(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 车辆维保费用按车牌号统计
     * @return
     */
    List<TrafficMaintenanceModel> getMaintenanceCountByPlateNum(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);
}
