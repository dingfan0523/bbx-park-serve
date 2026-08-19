
package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.ServiceStaffWorkload;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantEvaluate;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantEvaluateDetailModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskAndReserveModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountDetailPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/***
 * @Description 会议服务评价数据操作接口
 * @author huangyongtao
 * @date 2024/12/23 15:52
 */
@Repository
public interface MeetingAttendantEvaluateRepository extends BaseMapper<MeetingAttendantEvaluate> {


    List<MeetingAttendantEvaluate> findScoreByReserve(@Param("condition")  MeetingAttendantTaskCountParam condition);

    List<MeetingAttendantTaskAndReserveModel> findScoreByTask(@Param("condition")  MeetingAttendantTaskCountParam condition);

    IPage<MeetingAttendantEvaluateDetailModel> pageByUserId(IPage<MeetingAttendantModel> page, @Param("condition") MeetingAttendantTaskCountDetailPageParam condition);

    /**
     * 获取整体平均评分
     * @param tenantId 租户id
     * @return 平均评分
     */
    Double getAvgScore(@Param("tenantId")Long tenantId);

    List<ServiceStaffWorkload> findAvgScoreByAttendant(@Param("tenantId")Long tenantId, @Param("startTime") Date startTime, @Param("endTime")Date endTime, @Param("userIds")List<String> userIds);
}
