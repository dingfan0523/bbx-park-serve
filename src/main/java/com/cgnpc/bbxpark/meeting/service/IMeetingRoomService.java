
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.domain.MeetingRoom;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.config.eventbus.AttendantTaskEvent;
import com.cgnpc.bbxpark.config.eventbus.MeetingServeDelEvent;

import java.util.List;

/***
 * @Description 会议室服务接口
 * @author huangyongtao
 * @date 2024/8/23 15:39
 */
public interface IMeetingRoomService extends IService<MeetingRoom> {
    /**
     * PC端-会议室分页列表
     */
    IPage<MeetingRoomModel> page(MeetingRoomPageParam param);

    /**
     * PC端-会议室列表
     */
    List<MeetingRoomModel> list(MeetingRoomListParam param);

    /**
     * PC端-获取会议室-设备分页列表
     */
    IPage<MeetingRoomDeviceModel> pageDevice(MeetingRoomDevicePageParam param);

    /**
     * PC端-获取会议室-设备分页列表
     */
    List<MeetingRoomDeviceModel> listDevice(MeetingRoomDeviceListParam param);

    /**
     * 移动端-获取会议室-设备分页列表
     */
    List<AppMeetingRoomDeviceModel> listDeviceApp(MeetingRoomDeviceListParam param);


    /**
     * PC端-获取会议室-会服分页列表
     */
    IPage<MeetingRoomServiceModel> pageService(MeetingRoomServicePageParam param);

    /***
     * PC端-关联空间
     */
    Boolean addSpace(MeetingRoomSpaceParam param);

    /***
     * PC端-关联设备
     */
    Boolean addDevice(MeetingRoomDeviceParam param);

    /***
     * PC端-关联会服
     */
    Boolean addService(MeetingRoomServiceParam param);

    /***
     * PC端-编辑提醒
     */
    Boolean config(MeetingRoomConfigParam param);

    /***
     * PC端-编辑容量
     */
    Boolean editVolume(MeetingRoomVolumeParam param);

    /**
     * PC端-会议室详情
     *
     * @Param [id] 会议室标识
     * @Return 会议室详情信息
     */
    MeetingRoomDetailModel detail(Long id);









    /**
     * 移动端-获取会议室列表(分页).
     *
     * @Param param 会议室查询条件
     * @Return 会议室信息列表（分页）
     */
    IPage<MeetingRoomModel> appPage(MeetingRoomPageParam param);

    /**
     * 移动端-分页查询会议室占用情况
     *
     * @Param param 会议室查询条件
     * @Return 会议室信息列表（分页）
     */
    IPage<MeetingRoomModel> occupyPage(MeetingRoomPageParam param);

    /**
     * 移动端-会服分页查询会议室占用情况
     *
     * @Param param 会议室查询条件
     * @Return 会议室信息列表（分页）
     */
    IPage<MeetingRoomModel> attendantAppOccupyPage(MeetingRoomPageParam param);


    /**
     * 新增会议室.
     *
     * @Param param 会议室信息
     * @Return 新增会议室是否成功
     */
    Boolean add(MeetingRoomSaveParam param);


    /**
     * 删除会议室.
     *
     * @Param id 会议室标识
     * @Return 删除会议室是否成功
     */
    Boolean remove(Long id);


    /**
     * 编辑会议室信息.
     *
     * @Param param 会议室信息
     * @Return 编辑会议室是否成功
     */
    Boolean edit(MeetingRoomParam param);

   /***
    * @Description 获取简要的会议室详情信息
    * @author huangyongtao
    * @date 2024/12/25 11:31
    * @param id
    */
    MeetingRoomDetailModel simpleDetail(Long id);

    /***
     * @Description 处理会服事件
     * @author huangyongtao
     * @date 2024/12/30 19:51
     * @param event
     */
    void handleTaskEvent(AttendantTaskEvent event);

    /**
     * 处理会服删除事件
     * @param event 会服删除事件
     */
    void handleServeDel(MeetingServeDelEvent event);

    /***
     * @Description 更新会议室的使用状态
     * @author huangyongtao
     * @date 2025/1/8 11:04
     * @param
     */
    void updateUsedTask();


    /***
     * @Description PC端-会议室使用统计
     * @author huangyongtao
     * @date 2025/1/8 14:55
     */
    MeetingRoomUsedCountModel usedCount();

    /***
     * @Description 会服看板会议室的会前布置列表
     * @author huangyongtao
     * @date 2025/1/8 17:01
     * @param param
     */
    List<MeetingRoomAttendantModel> attendantList(MeetingRoomListParam param);

    /**
     * 编辑会议室图片
     *
     * @Param param 会议室信息
     * @Return 编辑会议室是否成功
     */
    Boolean editImage(MeetingRoomParam param);
}
