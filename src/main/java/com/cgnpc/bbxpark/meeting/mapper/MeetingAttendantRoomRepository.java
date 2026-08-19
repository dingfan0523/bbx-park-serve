
package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantRoom;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantRoomModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会服人员-会议室数据操作接口
 */
@Repository
public interface MeetingAttendantRoomRepository extends BaseMapper<MeetingAttendantRoom> {
    /**
     * PC端-会服人员-会议室查询
     */
    List<MeetingAttendantRoomModel> listByUserId(@Param("tenantId")Long tenantId,@Param("status")Integer status, @Param("userIdList") List<String> userIdList);
}
