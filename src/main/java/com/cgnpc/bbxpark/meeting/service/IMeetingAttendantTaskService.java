
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingAttendantTask;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskPageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveAppSaveParam;

import java.util.List;

/***
 * @Description 会服人员任务服务接口
 * @author huangyongtao
 * @date 2024/12/23 16:05
 */
public interface IMeetingAttendantTaskService extends IService<MeetingAttendantTask> {

	/**
	 * 根据会服人员任务标识获得会服人员任务详情信息.
	 * @Param [id] 会服人员任务标识
	 * @Return 会服人员任务详情信息
	 */
	MeetingAttendantTaskModel detail(MeetingAttendantTaskParam param);

	/**
	 * 会服人员任务详情
	 * @param id 任务id
	 * @return 会服人员任务详情
	 */
	MeetingAttendantTaskModel simpleDetail(Long id);

	/**
	 * PC端-获取会服人员任务列表(分页).
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表（分页）
	 */
	IPage<MeetingAttendantTaskModel> page(MeetingAttendantTaskPageParam param);

	/**
	 * 获取会服人员任务列表(分页).
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表（分页）
	 */
	IPage<MeetingAttendantTaskPageModel> pageApp(MeetingAttendantTaskPageParam param);

	/**
	 * 获取会服人员任务列表.
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表
	 */
	List<MeetingAttendantTaskModel> list(MeetingAttendantTaskListParam param);

	/**
	 * 新增会服人员任务.
	 * @Param param 会服人员任务信息
	 * @Return 新增会服人员任务是否成功
	 */
	Boolean add(Long reserveId, MeetingReserveAppSaveParam param);

	/**
	 * 批量新增会服人员任务.
	 * @Param params 会服人员任务信息列表
	 * @Return 批量新增会服人员任务是否成功
	 */
	Boolean addBatch(List<MeetingAttendantTaskParam> params);

	/**
	 * 编辑会服人员任务信息.
	 * @Param param 会服人员任务信息
	 * @Return 编辑会服人员任务是否成功
	 */
	Boolean edit(MeetingAttendantTaskParam param);

	/**
	 * 批量编辑会服人员任务信息.
	 * @Param params 会服人员任务信息列表
	 * @Return 批量编辑会服人员任务是否成功
	 */
	Boolean editBatch(List<MeetingAttendantTaskParam> params);

	/**
	 * 根据会服人员任务标识获得会服人员任务详情信息.
	 * @Param [id] 会服人员任务标识
	 * @Return 会服人员任务详情信息
	 */
	List<MeetingServiceModel> serviceDetail(MeetingAttendantTaskParam param);

	/**
	 * 根据会议id删除
	 * @param reserveId
	 * @return 结果
	 */
	Boolean removeByReserveId(Long reserveId);

	/***
	 * @Description 校验会服是否可用
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	Boolean checkService(MeetingReserveAppSaveParam param);

	/***
	 * @Description 编辑会前布置的录音服务
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	Boolean editServiceBeforeRecording(MeetingReserveAppSaveParam param);

	/***
	 * @Description 编辑会前布置的普通服务
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	Boolean editServiceBeforeOrdinary(MeetingReserveAppSaveParam param);

	/***
	 * @Description 编辑会前布置的排座服务
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	Boolean editServiceBeforeSeat(MeetingReserveAppSaveParam param);

	/***
	 * @Description 编辑会前布置的打印服务
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	Boolean editServiceBeforePrint(MeetingReserveAppSaveParam param);

	/***
	 * @Description 编辑会前布置的会议名称
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	Boolean editServiceBeforeReserveName(MeetingReserveAppSaveParam param);


	/***
	 * @Description 处理会议打印服务
	 * @author huangyongtao
	 * @date 2024/12/24 10:09
	 */
	void handleServiceBeforePrint(Long reserveId, Long fileId);

	/***
	 * @Description 处理取消的会议
	 * @author huangyongtao
	 * @date 2024/12/27 16:06
	 * @param reserveId
	 */
	void handleTaskCancelReserve(Long reserveId);

	/***
	 * @Description 根据会议id查询排座信息
	 * @author huangyongtao
	 * @date 2024/12/25 15:40
	 * @param reserveId
	 */
	List<MeetingAttendantTaskModel> findByReserveId(Long reserveId);

	/***
	 * @Description 处理结束会议
	 * @author huangyongtao
	 * @date 2024/12/27 17:30
	 * @param reserve
	 */
	void handleTaskEndReserve(MeetingReserve reserve);

	/***
	 * @Description 处理呼叫会服
	 * @author huangyongtao
	 * @date 2024/12/27 17:30
	 * @param reserve
	 */
	void handleTaskCallReserve(MeetingReserve reserve);

	/**
	 * 根据会服人员任务标识获得会服人员任务详情信息.
	 * @Param [id] 会服人员任务标识
	 * @Return 会服人员任务详情信息
	 */
	List<MeetingAttendantTaskDetailModel> serviceDetailByTaskId(Long taskId);

	/***
	 * @Description 会服确认
	 * @author huangyongtao
	 * @date 2024/12/28 17:20
	 * @param param
	 */
	Boolean confirm(MeetingAttendantTaskParam param);

	/***
	 * @Description 会服确认
	 * @author huangyongtao
	 * @date 2024/12/28 17:20
	 * @param param
	 */
	Boolean complete(MeetingAttendantTaskParam param);

	/***
	 * @Description 处理进行中的会议
	 * @author huangyongtao
	 * @date 2024/12/30 17:30
	 * @param reserveId
	 */
	void handleTaskStartReserve(Long reserveId, Long roomId);

	/**
	 * 根据会议室id批量查询会议室存在的未完成的会服任务
	 * @param roomIdList
	 * @return
	 */
	List<MeetingAttendantTask> findUnCompleteTask(List<Long> roomIdList);

	/**
	 * 获取会服人员会中呼叫任务列表
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表
	 */
	List<MeetingAttendantTaskPageModel> callingList(MeetingAttendantTaskPageParam param);

	/**
	 * PC端-获获取会服人员完成的会服列表（分页）
	 * @Param param 会服人员任务查询条件
	 * @Return 会服人员任务信息列表（分页）
	 */
	IPage<MeetingAttendantTaskPageModel> pageHistory(MeetingAttendantTaskPageParam param);

	/***
	 * @Description 会服人员完成会服统计
	 * @author huangyongtao
	 * @date 2025/1/9 17:01
	 */
	MeetingAttendantTaskCountModel taskCompleteCount();
}
