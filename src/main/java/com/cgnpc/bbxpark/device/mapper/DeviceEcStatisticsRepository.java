package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.DeviceEcStatistics;
import com.cgnpc.bbxpark.ioc.dto.model.DailyEnergyTrend;
import com.cgnpc.bbxpark.ioc.dto.model.EnergySavingOverview;
import com.cgnpc.bbxpark.ioc.dto.model.RoomEnergyRank;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceEcStatisticsRepository extends BaseMapper<DeviceEcStatistics> {
    /**
     * 节能概览统计
     * @return 数据
     */
    EnergySavingOverview getEnergySavingOverview(@Param("tenantId")Long tenantId);

    /**
     * 30天内日均节能趋势
     * @param tenantId 租户id
     * @return 数据
     */
    List<DailyEnergyTrend> getDailyEnergyTrend(@Param("tenantId")Long tenantId);

    List<RoomEnergyRank> getRoomEnergyRanking(@Param("tenantId")Long tenantId,@Param("sortOrder")String sortOrder);
}
