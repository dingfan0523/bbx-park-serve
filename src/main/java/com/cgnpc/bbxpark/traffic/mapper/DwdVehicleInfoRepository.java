
package com.cgnpc.bbxpark.traffic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantWasteTypeModel;
import com.cgnpc.bbxpark.ioc.dto.model.TrafficVehicleTypeModel;
import com.cgnpc.bbxpark.traffic.domain.DwdVehicleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DwdVehicleInfoRepository extends BaseMapper<DwdVehicleInfo> {

    /**
     * 车辆类型数量统计
     * @return
     */
    List<TrafficVehicleTypeModel> getVehicleTypeDistribution();

}
