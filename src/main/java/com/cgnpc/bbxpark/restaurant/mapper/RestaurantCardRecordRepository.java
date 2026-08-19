
package com.cgnpc.bbxpark.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.restaurant.domain.RestaurantCardRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface RestaurantCardRecordRepository extends BaseMapper<RestaurantCardRecord> {

    /**
     * 获取一卡通营收总览
     * @param tenantId
     * @return
     */
    RestaurantRevenueOverview getRevenueOverview(@Param("tenantId") Long tenantId);

    /**
     * 获取一卡通消费次数分析
     * @param tenantId
     * @return
     */
    List<RestaurantConsumptionCountItem> getCountAnalysis(@Param("tenantId") Long tenantId, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取一卡通消费金额分析
     * @param tenantId
     * @return
     */
    List<RestaurantConsumptionShareItem> getConsumptionShareAnalysis(@Param("tenantId") Long tenantId, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取一卡通某月的消费金额分析
     * @param tenantId
     * @return
     */
    List<RestaurantConsumptionAbilityModel> getConsumptionAbility(@Param("tenantId") Long tenantId, @Param("minDay") Date minDay, @Param("maxDay") Date maxDay);

    /**
     * 获取一卡通的用餐行为分析
     * @param tenantId
     * @return
     */
    RestaurantConsumptionBehaviorModel getConsumptionBehavior(@Param("tenantId") Long tenantId);

    /**
     * 获取一卡通的用餐的天数
     * @param tenantId
     * @return
     */
    Double getDiffDay(@Param("tenantId") Long tenantId, @Param("fileId") Long fileId);

    /**
     * 获取一卡通卡号的消费金额
     * @param tenantId
     * @return
     */
    List<RestaurantConsumptionBehaviorModel> getConsumptionBehaviorList(@Param("tenantId") Long tenantId, @Param("fileId") Long fileId);

    /**
     * 获取余额小于10的一卡通卡号
     * @param tenantId
     * @return
     */
    List<String> selectLowBalanceCardNos(@Param("tenantId") Long tenantId, @Param("fileId") Long fileId);

    /**
     * 获取余额总数
     * @param tenantId
     * @return
     */
    Double getTotalAccountBalance(@Param("tenantId") Long tenantId);
}
