
package com.cgnpc.bbxpark.energy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecordCount;
import com.cgnpc.bbxpark.ioc.dto.model.WaterAreaCompareModel;
import com.cgnpc.bbxpark.ioc.dto.model.WaterRealtimeFlowRankModel;
import com.cgnpc.bbxpark.ioc.dto.model.WaterStatisticsModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/***
 * @Description 抄表自动上报记录统计数据操作接口
 * @author huangyongtao
 * @date 2025/4/21 9:23
 */
@Repository
public interface MeterAutoRecordCountRepository extends BaseMapper<MeterAutoRecordCount> {

    /**
     * 获取自动抄表分析数据
     * @param tenantId
     * @param minDay
     * @param maxDay
     * @return
     */
    List<WaterStatisticsModel> getMeterAutoRecordStatistics(@Param("tenantId") Long tenantId, @Param("timeType") String timeType,
                                                            @Param("minDay") Date minDay, @Param("maxDay") Date maxDay, @Param("deviceIds") List<Long> deviceIds);

    /**
     * 获取区域能耗对比
     */
    List<WaterAreaCompareModel> findAreaCompare(@Param("branchId")Long branchId, @Param("branchType")String branchType,
                                                @Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("tenantId")Long tenantId);

    List<WaterRealtimeFlowRankModel> getRealtimeFlowRank( @Param("startTime") Date startTime, @Param("endTime")Date endTime,
                                                          @Param("deviceIds")List<Long> deviceIds,@Param("tenantId")Long tenantId);
}
