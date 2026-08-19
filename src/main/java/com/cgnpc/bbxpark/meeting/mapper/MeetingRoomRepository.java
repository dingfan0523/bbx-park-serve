
package com.cgnpc.bbxpark.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomDeviceModel;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingRoomServiceModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomDeviceListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomDevicePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingRoomServicePageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 * @Description 会议室数据操作接口
 * @author huangyongtao
 * @date 2024/8/23 15:37
 */
public interface MeetingRoomRepository extends BaseMapper<MeetingRoom> {
    /**
     * PC端-会议室-会服分页查询
     */
    IPage<MeetingRoomServiceModel> pageService(IPage<MeetingRoomServiceModel> page,@Param("tenantId")Long tenantId, @Param("condition") MeetingRoomServicePageParam condition);

    /**
     * PC端-会议室-设备分页查询
     */
    IPage<MeetingRoomDeviceModel> pageDevice(IPage<MeetingRoomDevicePageParam> page, @Param("tenantId")Long tenantId, @Param("condition") MeetingRoomDevicePageParam condition);
    /**
     * PC端-会议室-设备列表查询
     */
    List<MeetingRoomDeviceModel> listDevice(@Param("tenantId")Long tenantId, @Param("condition") MeetingRoomDeviceListParam condition);
}
