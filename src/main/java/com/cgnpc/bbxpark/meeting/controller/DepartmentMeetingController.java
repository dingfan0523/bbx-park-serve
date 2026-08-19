package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingDetail;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingStats;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingSum;
import com.cgnpc.bbxpark.meeting.dto.param.DepartmentMeetingCountParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;
import com.cgnpc.bbxpark.meeting.service.IDepartmentMeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @create zhaoshuo
 * @time 2025/2/6
 * @desc 部门会议考核服务接口
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/departmentMeeting")
@Api(tags = "部门会议考核服务接口")
public class DepartmentMeetingController {


    @Autowired
    private IDepartmentMeetingService iDepartmentMeetingService;

    /**
     * 各部门会议时长统计
     */
    @ApiOperation(value = "各部门会议时长统计")
    @PostMapping(value = "/departmentMeetingTime")
    public CudResult<List<Map<String, Object>>> departmentMeetingTime(@RequestBody MeetingAttendantTaskCountParam param) {
        return CudResult.success(iDepartmentMeetingService.getDepartmentMeetingTime(param));
    }

    /**
     * 各部门会议场次统计
     */
    @ApiOperation(value = "各部门会议场次统计")
    @PostMapping(value = "/departmentMeetingCount")
    public CudResult<List<Map<String, Object>>> departmentMeetingCount(@RequestBody MeetingAttendantTaskCountParam param) {
        return CudResult.success(iDepartmentMeetingService.getDepartmentMeetingCount(param));
    }

    /**
     * 获取时间段内的部门会议信息统计
     */
    @ApiOperation(value = "获取时间段内的部门会议信息统计")
    @PostMapping(value = "/departmentMeetingSta")
    public CudResult<DepartmentMeetingSum> departmentMeetingSta(@RequestBody MeetingAttendantTaskCountParam param) {
        return CudResult.success(iDepartmentMeetingService.getDepartmentMeetingSta(param));
    }

    /**
     * 分页查询所有部门会议统计
     */
    @ApiOperation(value = "分页查询所有部门会议统计")
    @PostMapping(value = "/queryDepartmentMeetingPage")
    public CudResult<IPage<DepartmentMeetingStats>> queryDepartmentMeetingPage(@RequestBody DepartmentMeetingCountParam param) {
        return CudResult.success(iDepartmentMeetingService.queryDepartmentMeetingPage(param));
    }
    /**
     * 所有部门会议统计导出
     */
    @ApiOperation(value = "所有部门会议统计导出")
    @GetMapping(value = "/departmentMeetingExport")
    public void  departmentMeetingExport(HttpServletResponse response, @ModelAttribute DepartmentMeetingCountParam param) {
        iDepartmentMeetingService.departmentMeetingExport(param,response);
    }

    /**
     * 分页查询部门会议详情
     */
    @ApiOperation(value = "分页查询部门会议详情")
    @PostMapping(value = "/queryDepartmentMeetingDetail")
    public CudResult<IPage<DepartmentMeetingDetail>> queryDepartmentMeetingDetail(@RequestBody DepartmentMeetingCountParam param) {
        return CudResult.success(iDepartmentMeetingService.queryDepartmentMeetingDetail(param));
    }

    /**
     * 部门会议详情导出
     */
    @ApiOperation(value = "部门会议详情导出")
    @GetMapping(value = "/deptMeetingDetailExport")
    public void  deptMeetingDetailExport(HttpServletResponse response, @ModelAttribute DepartmentMeetingCountParam param) {
        iDepartmentMeetingService.deptMeetingDetailExport(param,response);
    }
}
