
package com.cgnpc.bbxpark.meeting.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoomDeviceRel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomDeviceRelModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomDeviceRelListParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomDeviceRelRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingRoomDeviceRelService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/***
 * @Description 会议室设备关联服务实现
 * @author huangyongtao
 * @date 2024/8/23 16:37
 */
@Service("meetingRoomDeviceRelService")
public class MeetingRoomDeviceRelServiceImpl extends ServiceImpl<MeetingRoomDeviceRelRepository, MeetingRoomDeviceRel> implements IMeetingRoomDeviceRelService {

	/**
	 * 获取会议室设备关联列表.
	 * @Param param 会议室设备关联查询条件
	 * @Return 会议室设备关联信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeetingRoomDeviceRelModel> list(MeetingRoomDeviceRelListParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		List<MeetingRoomDeviceRel> meetingRoomDeviceRels = this.list(new LambdaQueryWrapper<MeetingRoomDeviceRel>()
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), MeetingRoomDeviceRel::getTenantId, param.getTenantId())
				.eq(ObjectUtil.isNotEmpty(param.getRoomId()), MeetingRoomDeviceRel::getRoomId, param.getRoomId()));
		if(CollectionUtil.isEmpty(meetingRoomDeviceRels)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(meetingRoomDeviceRels, MeetingRoomDeviceRelModel::new);
	}

	/**
	 * 新增会议室设备关联.
	 * @Param param 会议室设备关联信息
	 * @Return 新增会议室设备关联是否成功
	 */
	@Override
	public Boolean add(List<MeetingRoomDeviceRel> meetingRoomDeviceRels) {
		return this.saveBatch(meetingRoomDeviceRels);
	}

	/**
	 * 删除会议室设备关联.
	 * @Param id 会议室设备关联标识
	 * @Return 删除会议室设备关联是否成功
	 */
	@Override
	public Boolean remove(Long roomId) {
		return this.remove(new LambdaQueryWrapper<MeetingRoomDeviceRel>().eq(ObjectUtil.isNotEmpty(roomId), MeetingRoomDeviceRel::getRoomId, roomId));
	}

}
