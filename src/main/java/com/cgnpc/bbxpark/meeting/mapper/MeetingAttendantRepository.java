
package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendant;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantPageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 会服人员数据操作接口
 */
@Repository
public interface MeetingAttendantRepository extends BaseMapper<MeetingAttendant> {

    IPage<MeetingAttendantModel> page(IPage<MeetingAttendantModel> page, @Param("tenantId")Long tenantId,@Param("code")String code, @Param("condition") MeetingAttendantPageParam condition);
}
