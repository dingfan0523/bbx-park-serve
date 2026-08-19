package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.AlarmInfo;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoCountModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.ioc.dto.model.AlarmLevelDistributionModel;
import com.cgnpc.bbxpark.ioc.dto.model.AlarmTypeDistributionModel;
import com.cgnpc.bbxpark.ioc.dto.model.WeekRoomHealth;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;

@Mapper
public interface AlarmInfoRepository extends BaseMapper<AlarmInfo> {

    /**
     * 通过设备ID查询相关的未结束告警信息
     *
     * @param deviceIds 设备ID集合
     * @return 未结束告警信息列表
     */
    List<AlarmInfo> selectUnfinishedAlarmsByDeviceIds(@Param("deviceIds")List<Long> deviceIds);


    AlarmInfoCountModel countAlarmInfo(@Param("condition") AlarmInfoParam param);

    /**
     * 查询会议室每日健康度-告警数量
     * @return 数据
     */
    List<WeekRoomHealth> getMeetingRoomHealth(@Param("tenantId")Long tenantId,@Param("deviceIds")List<Long> deviceIds);

    /**
     * 大屏-告警类型分布统计
     */
    List<AlarmTypeDistributionModel> getAlarmTypeDistribution(@Param("groupCode")String groupCode,@Param("tenantId")Long tenantId);

    /**
     * 大屏-告警等级分布统计
     */
    List<AlarmLevelDistributionModel> getAlarmLevelDistribution(@Param("groupCode")String groupCode,@Param("tenantId")Long tenantId);
}
