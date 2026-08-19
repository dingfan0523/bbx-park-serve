
package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.WeekRoomHealth;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTask;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskAndReserveModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskPageModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskPageParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 会服人员任务数据操作接口
 * @author huangyongtao
 * @date 2024/12/23 15:51
 */
@Repository
public interface MeetingAttendantTaskRepository extends BaseMapper<MeetingAttendantTask> {

    /**
     * 移动端-分页查询
     */
    IPage<MeetingAttendantTaskPageModel> pageApp(IPage<MeetingAttendantTaskPageModel> page, @Param("condition") MeetingAttendantTaskPageParam condition);

    /**
     * pc端-分页查询
     */
    IPage<MeetingAttendantTaskPageModel> pageHistory(IPage<MeetingAttendantTaskPageModel> page, @Param("condition") MeetingAttendantTaskPageParam condition);

    /**
     * 根据条件查询会服列表
     */
    List<MeetingAttendantTaskModel> findByCondition(@Param("condition") MeetingAttendantTaskListParam condition);

    /**
     * 根据条件查询会服和会议信息
     */
    List<MeetingAttendantTaskAndReserveModel> findTaskAndReserve(@Param("condition") MeetingAttendantTaskCountParam condition);

    /**
     * 根据条件查询会服列表
     */
    List<MeetingAttendantTaskModel> findTask(@Param("condition") MeetingAttendantTaskCountParam condition);

    /**
     * 查询会议室每日健康度-服务次数
     * @return 数据
     */
    List<WeekRoomHealth> getMeetingRoomHealth(@Param("tenantId")Long tenantId, @Param("serviceStatus")Integer serviceStatus);

}
