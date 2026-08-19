
package com.cgnpc.bbxpark.meeting.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserve;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;

import java.util.Date;
import java.util.List;

/***
 * @Description 会议预约服务接口
 * @author huangyongtao
 * @date 2024/8/23 15:44
 */
public interface IMeetingReserveService extends IService<MeetingReserve> {
    /**
     * 获取会议列表(分页).
     *
     * @Param param 会议查询条件
     * @Return 会议信息列表（分页）
     */
    IPage<MeetingReserveModel> page(MeetingReservePageParam param);

    /**
     * 获取我的会议列表(分页).
     *
     * @Param param 会议查询条件
     * @Return 会议信息列表（分页）
     */
    IPage<MeetingReserveModel> myPage(MeetingReservePageParam param);

    /**
     * 获取会议附件列表(分页)
     *
     * @param param 会议查询条件
     * @return 会议附件列表
     */
    IPage<MeetingReserveFileModel> pageFile(MeetingReserveFilePageParam param);

    /***
     * @Description 编辑会议实际结束时间
     * @author huangyongtao
     * @date 2024/8/26 17:15
     */
    Boolean updateRealEndTime(MeetingReserveTimeParam param);

    /***
     * @Description 会议设为有效
     * @author huangyongtao
     * @date 2024/8/26 17:15
     */
    Boolean valid(MeetingReserveValidParam param);

    /***
     * @Description 会议设为有效
     * @author huangyongtao
     * @date 2024/8/26 17:15
     */
    Boolean inValid(MeetingReserveInValidParam param);

    /**
     * 根据会议预约标识获得会议预约详情信息.
     *
     * @Param [id] 会议预约标识
     * @Return 会议预约详情信息
     */
    MeetingReserveDetailModel detail(Long id);

    /**
     * PC端-保存会议附件
     * @return 结果
     */
    Boolean saveFile(AppMeetingFileParam param);

    /**
     * PC端-删除会议附件
     * @param id 文件id
     * @return 结果
     */
    Boolean removeFile(Long id);

    /**
     * 移动端-会议数量查询
     *
     * @return 会议数量
     */
    AppMeetingReserveCountModel pageCount();

    /**
     * 移动端-会议分页列表
     *
     * @Param param 会议预约查询条件
     * @Return 会议预约信息列表（分页）
     */
    IPage<AppMeetingReserveModel> pageApp(AppMeetingReservePageParam param);

    /**
     * 移动端-会服会议分页列表
     *
     * @Param param 会议预约查询条件
     * @Return 会议预约信息列表（分页）
     */
    IPage<AppMeetingReserveModel> attendantPageApp(AppMeetingReservePageParam param);

    /**
     * 移动端-会议详情
     *
     * @Param [id] 会议预约标识
     * @Return 会议预约详情信息
     */
    AppMeetingReserveDetailModel detailApp(Long id);

    /**
     * 移动端-获取会议
     *
     * @Param [id] 会议预约标识
     * @Return 会议预约详情信息
     */
    AppMeetingReserveSimpleModel getSimple(Long id);

    /**
     * 移动端-更改签到规则
     */
    Boolean editRule(AppMeetingSignRuleParam param);

    /**
     * 移动端-保存会议附件
     * @return 结果
     */
    Boolean appSaveFile(AppMeetingFileParam param);

    /**
     * 移动端-删除会议附件
     * @param id 文件id
     * @return 结果
     */
    Boolean appRemoveFile(Long id);

    /**
     * 移动端-取消会议
     * @param id 会议id
     * @return 结果
     */
    Boolean cancel(Long id);

    /**
     * 移动端-结束会议
     * @param param 参数
     * @return 结果
     */
    Boolean finish(AppMeetingFinishParam param);

    /**
     * 移动端-重置会议结束时间
     * @param id 会议id
     * @return 结果
     */
    Boolean resetRealEndTime(Long id, Date endTime, Integer endType);

    /**
     * 移动端-获取最近的一条预约信息
     *
     * @return 预约信息
     */
    AppSimpleReserveModel getNearest();








    /**
     * 新增会议预约.
     *
     * @Param param 会议预约信息
     * @Return 新增会议预约是否成功
     */
    Boolean add(MeetingSaveParam param);


    /**
     * 删除会议预约.
     *
     * @Param id 会议预约标识
     * @Return 删除会议预约是否成功
     */
    Boolean remove(Long id);


    /***
     * @Description 查询会议预约信息
     * @author huangyongtao
     * @date 2024/8/27 17:58
     * @param param
     */
    List<MeetingReserveModel> findReserve(MeetingReserveListParam param);


    /***
     * @Description 会议预约无效任务
     * @author huangyongtao
     * @date 2024/8/29 17:13
     */
    Boolean invalidTask();

    /***
     * @Description 更新会议预约实际结束时间任务
     * @author huangyongtao
     * @date 2024/8/29 17:12
     */
    Boolean realEndTimeTask();

    /***
     * @Description 更新会议预约实际开始时间任务
     * @author huangyongtao
     * @date 2024/9/24 11:07
     */
    Boolean realStartTimeTask();

    /***
     * @Description 会议预约人签到通知任务
     * @author huangyongtao
     * @date 2024/8/29 19:12
     */
    Boolean signNoticeTask();

    /**
     * 会服待确认通知任务
     */
    Boolean waitingConfirmNoticeTask();

    /**
     * 会议设备告警任务
     */
    Boolean meetingDeviceWarnTask();

    /**
     * 会议延时提醒任务
     */
    Boolean meetingDelayTask();

   /***
    * @Description 校验会议室是否被占用
    * @author huangyongtao
    * @date 2024/12/24 10:09
    */
    Boolean checkTime(MeetingReserveParam param);

    /**
     * 移动端-新增会议预约.
     *
     * @Param param 会议预约信息
     * @Return 新增会议预约是否成功
     */
    AppMeetingReserveDraftModel appAdd(MeetingReserveAppSaveParam param);

    /**
     * 移动端-会议编辑详情
     *
     * @Param [id] 会议预约标识
     * @Return 会议预约详情信息
     */
    AppMeetingReserveDetailModel detailEditApp(Long id);

    /**
     * 移动端-编辑会议预约.
     *
     * @Param param 会议预约信息
     * @Return 新增会议预约是否成功
     */
    AppMeetingReserveDraftModel appEdit(MeetingReserveAppSaveParam param);

    /**
     * 移动端-删除会议草稿.
     *
     * @Param param 会议预约信息
     * @Return 新增会议预约是否成功
     */
    Boolean appDelete(MeetingReserveAppSaveParam param);

    /**
     * 移动端-会议草稿分页查询
     *
     * @Param param 会议预约查询条件
     * @Return 会议预约信息列表（分页）
     */
    IPage<AppMeetingReserveModel> draftPage(AppMeetingReservePageParam param);

    /***
     * @Description 会议设为有效
     * @author huangyongtao
     * @date 2024/12/28 11:15
     */
    Boolean delay(MeetingReserveDelayParam param);

    /**
     * 移动端-呼叫会服
     * @param id 会议id
     * @return 结果
     */
    Boolean call(Long id);

    /**
     * 移动端-会前布置页面的详情
     *
     * @Param [id] 会服标识
     * @Return 会议预约详情信息
     */
    AppMeetingReserveDetailModel serviceDetail(Long id, Integer ServiceType);

    /***
     * @Description 获取用户部门列表信息
     * @author huangyongtao
     * @date 2025/1/2 14:28
     * @param userId
     */
    List<OrgDepartmentNode> getUserDepartment(String userId);
}
