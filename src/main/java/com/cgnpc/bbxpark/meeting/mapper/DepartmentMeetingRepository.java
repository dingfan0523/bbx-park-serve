package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeeting;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingDetail;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingStats;
import com.cgnpc.bbxpark.meeting.domain.DepartmentMeetingSum;
import com.cgnpc.bbxpark.meeting.dto.param.DepartmentMeetingCountParam;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/8
 * @desc 部门会议统计接口
 */
@Repository
public interface DepartmentMeetingRepository extends BaseMapper<DepartmentMeeting> {
    /**
     *  查询部门会议时长统计
     * @param departmentIds 部门ids
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<DepartmentMeeting> getDepartmentMeetingTime(@Param("departmentIds") List<String> departmentIds,@Param("beginTime") String beginTime,@Param("endTime") String endTime,@Param("tenantId") Long tenantId);

    /**
     *  查询部门会议时长统计
     * @param departmentIds 部门ids
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<DepartmentMeeting> getDepartmentMeetingCount(@Param("departmentIds")List<String> departmentIds, @Param("beginTime")String beginTime,@Param("endTime") String endTime,@Param("tenantId") Long tenantId);

    /**
     *  查询时间段内的会议总时长和总会议场数
     * @param departmentIds 部门ids
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据
     */
    List<DepartmentMeetingSum> getDepartmentMeetingSum(@Param("departmentIds")List<String> departmentIds, @Param("beginTime")String beginTime, @Param("endTime") String endTime,@Param("tenantId") Long tenantId) ;


    /**
     * 根据部门ids查询部门人员数量
     * @param departmentIds
     * @return 部门人员
     */
    List<HashMap<String, Object>> getMemberByDeptIds(@Param("departmentIds")List<String> departmentIds);

    /**
     * 查询所有部门会议统计
     * @param param 查询条件
     * @return
     */
    List<DepartmentMeetingStats> queryDeptMeeting(@Param("param") DepartmentMeetingCountParam param);
    /**
     * 查询所有部门会议统计
     * @param param 查询条件
     * @return
     */
    List<DepartmentMeetingDetail> queryDepartmentMeetingDetail(@Param("param")DepartmentMeetingCountParam param);

   /***
    * @Description 根据部门信息查询人员列表
    * @author huangyongtao
    * @date 2025/10/22 14:25
    * @param departmentId
    * @param tenantId
    */
    List<UserInfoModel> findUserInfoByDepartment(@Param("departmentId")String departmentId, @Param("tenantId")Long tenantId);
}
