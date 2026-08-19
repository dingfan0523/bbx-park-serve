
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingTempReserve;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingTempReserveModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingTempReserveListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingTempReserveParam;

import java.util.List;

/***
 * @Description 会议临时预约服务接口
 * @author huangyongtao
 * @date 2025/1/6 16:02
 */
public interface IMeetingTempReserveService extends IService<MeetingTempReserve> {

	/**
	 * 根据会议临时预约标识获得会议临时预约详情信息.
	 * @Param [id] 会议临时预约标识
	 * @Return 会议临时预约详情信息
	 */
	MeetingTempReserveModel detail(Long id);


	/**
	 * 获取会议临时预约列表.
	 * @Param param 会议临时预约查询条件
	 * @Return 会议临时预约信息列表
	 */
	List<MeetingTempReserveModel> list(MeetingTempReserveListParam param);

	/**
	 * 新增会议临时预约.
	 * @Param param 会议临时预约信息
	 * @Return 新增会议临时预约是否成功
	 */
	Boolean add(MeetingTempReserveParam param);

	/***
	 * @Description 校验是否被临时占用
	 * @author huangyongtao
	 * @date 2025/1/7 16:55
	 * @param param
	 */
	Boolean checkTempTime(MeetingTempReserveParam param);

}
