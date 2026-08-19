
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.CarRentTrendModel;
import com.cgnpc.bbxpark.ioc.dto.model.CarRentTypeStatModel;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleCarRentApply;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DwdVehicleCarRentApplyRepository extends BaseMapper<DwdVehicleCarRentApply> {

    /**
     * 租车记录数量
     * @return
     */
    Integer getCarRentCount(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 租车记录按数量统计
     * @return
     */
    List<CarRentTypeStatModel> getCarRentTypeAnalysis(@Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 租车记录按时间统计
     * @return
     */
    List<CarRentTrendModel> getCarRentTrend(@Param("timeType") String timeType, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);
}
