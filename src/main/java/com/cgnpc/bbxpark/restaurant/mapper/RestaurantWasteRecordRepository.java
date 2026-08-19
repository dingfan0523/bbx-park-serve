
package com.cgnpc.bbxpark.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantWasteTypeModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantWasteRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface RestaurantWasteRecordRepository extends BaseMapper<RestaurantWasteRecord> {

    /**
     * 获取餐料垃圾分析数据
     * @param tenantId
     * @param minDay
     * @param maxDay
     * @return
     */
    List<RestaurantWasteTypeModel> getWasteStatistics(@Param("tenantId") Long tenantId, @Param("timeType") String timeType, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

}
