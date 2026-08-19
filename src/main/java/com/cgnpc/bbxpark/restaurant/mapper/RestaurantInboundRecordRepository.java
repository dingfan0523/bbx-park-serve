
package com.cgnpc.bbxpark.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantInventoryInboundModel;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantSupplierModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInboundRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface RestaurantInboundRecordRepository extends BaseMapper<RestaurantInboundRecord> {

    /**
     * 获取核心供应商信息
     * @param tenantId
     * @return
     */
    RestaurantSupplierModel getCoreSupplier(@Param("tenantId") Long tenantId);

    /**
     * 获取餐料入库分析数据
     * @param tenantId
     * @param minDay
     * @param maxDay
     * @return
     */
    List<RestaurantInventoryInboundModel> getInventoryInboundTrend(@Param("tenantId") Long tenantId, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);
}
