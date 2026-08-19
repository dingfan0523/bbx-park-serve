
package com.cgnpc.bbxpark.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.RestaurantInventoryCategoryModel;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantInventoryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface RestaurantInventoryRecordRepository extends BaseMapper<RestaurantInventoryRecord> {

    /**
     * 获取餐料品类的库存数量
     * @param tenantId
     * @return
     */
    List<RestaurantInventoryCategoryModel> getInventoryAnalysis(@Param("tenantId") Long tenantId);
}
