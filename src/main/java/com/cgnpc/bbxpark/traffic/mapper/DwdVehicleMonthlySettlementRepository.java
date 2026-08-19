
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficMaintenanceModel;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficMileageUtilizationModel;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficSettlementMileageModel;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleMonthlySettlement;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DwdVehicleMonthlySettlementRepository extends BaseMapper<DwdVehicleMonthlySettlement> {


    /**
     * 获取车辆费用行驶总里程
     * @param minDay
     * @param maxDay
     * @return
     */
    Double getMileageCount(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取车辆费用行驶总里程按车牌号统计
     * @param minDay
     * @param maxDay
     * @return
     */
    List<TrafficSettlementMileageModel> getMileageByCarPlate(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取车辆费用按月份
     * @param minDay
     * @param maxDay
     * @return
     */
    List<TrafficMaintenanceModel> getCostCountByDate(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取车辆费用按车牌号
     * @param minDay
     * @param maxDay
     * @return
     */
    List<TrafficMaintenanceModel> getCostCountByCarPlate(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取车辆费用里程趋势
     * @param minDay
     * @param maxDay
     * @return
     */
    List<TrafficMileageUtilizationModel> getMileageUtilization(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);
}
