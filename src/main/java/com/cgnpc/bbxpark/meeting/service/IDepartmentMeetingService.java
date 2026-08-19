package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingDetail;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingStats;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingSum;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.dto.param.DepartmentMeetingCountParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @create zhaoshuo
 * @time 2025/2/6
 * @desc 部门会议考核服务接口
 */
public interface IDepartmentMeetingService extends IService<MeetingReserve> {
    /**
     * 获取部门会议时长
     * @return 部门会议时长
     */
    List<Map<String, Object>> getDepartmentMeetingTime(MeetingAttendantTaskCountParam param);

    /**
     * 统计获取部门会议数量
     * @return
     */
    List<Map<String, Object>> getDepartmentMeetingCount(MeetingAttendantTaskCountParam param);

    /**
     * 获取时间段内的部门会议信息统计
     * @return
     */
    DepartmentMeetingSum getDepartmentMeetingSta(MeetingAttendantTaskCountParam param);
    /**
     * 查询所有部门会议统计(分页)
     * @return
     */
    IPage<DepartmentMeetingStats> queryDepartmentMeetingPage(DepartmentMeetingCountParam param);

    /**
     * 查询部门会议详情(分页)
     * @param param
     * @return
     */
    IPage<DepartmentMeetingDetail> queryDepartmentMeetingDetail(DepartmentMeetingCountParam param);

    /**
     * 所有部门会议统计导出
     */
    void departmentMeetingExport(DepartmentMeetingCountParam param, HttpServletResponse response);

    /**
     * 部门会议详情导出
     */
    void deptMeetingDetailExport(DepartmentMeetingCountParam param, HttpServletResponse response);
}
