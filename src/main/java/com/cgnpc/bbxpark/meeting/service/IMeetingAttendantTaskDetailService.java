
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTaskDetail;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingAttendantTaskDetailModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskDetailListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskDetailParam;

import java.util.List;

/***
 * @Description 会服任务详情服务接口
 * @author huangyongtao
 * @date 2024/12/23 16:07
 */
public interface IMeetingAttendantTaskDetailService extends IService<MeetingAttendantTaskDetail> {

	/**
	 * 根据会服任务详情标识获得会服任务详情详情信息.
	 * @Param [id] 会服任务详情标识
	 * @Return 会服任务详情详情信息
	 */
	MeetingAttendantTaskDetailModel detail(MeetingAttendantTaskDetailParam id);


	/**
	 * 获取会服任务详情列表.
	 * @Param param 会服任务详情查询条件
	 * @Return 会服任务详情信息列表
	 */
	List<MeetingAttendantTaskDetailModel> list(MeetingAttendantTaskDetailListParam param);

	/**
	 * 新增会服任务详情.
	 * @Param param 会服任务详情信息
	 * @Return 新增会服任务详情是否成功
	 */
	Boolean add(MeetingAttendantTaskDetail param);

	/**
	 * 批量新增会服任务详情.
	 * @Param params 会服任务详情信息列表
	 * @Return 批量新增会服任务详情是否成功
	 */
	Boolean addBatch(List<MeetingAttendantTaskDetail> params);

	/**
	 * 根据任务id集合删除
	 * @param taskIds
	 * @return 结果
	 */
	Boolean removeByTaskIds(List<Long> taskIds);

	/**
	 * 根据任务id查询集合
	 * @param taskId
	 * @return 结果
	 */
	List<MeetingAttendantTaskDetail> findByTaskId(Long taskId);

}
