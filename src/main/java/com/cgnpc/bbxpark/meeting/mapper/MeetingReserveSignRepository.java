
package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.DailySignAnalysis;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveSign;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 会议预约签到数据操作接口
 * @author huangyongtao
 * @date 2024/8/23 15:38
 */
@Repository
public interface MeetingReserveSignRepository extends BaseMapper<MeetingReserveSign> {
    /**
     * 员工参会行为分析
     * @param tenantId 租户id
     * @return 数据
     */
    List<DailySignAnalysis> getEmployeeAttendance(@Param("tenantId") Long tenantId);
}
