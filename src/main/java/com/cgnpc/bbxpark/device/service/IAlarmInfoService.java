package com.cgnpc.bbxpark.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.AlarmInfo;
import com.cgnpc.bbxpark.device.dto.model.AlarmDeviceModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmIgnoreConfigModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmHandleRecordParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmIgnoreConfigParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;


public interface IAlarmInfoService extends IBaseService<AlarmInfo> {


    List<String> getDeviceIdList(String deviceName);

    int saveAlarmInfo(String msg, Long offset);
    int restoreAlarmInfo(String msg, Long offset);

    IPage<AlarmInfoModel> pageAlarmInfoModel(AlarmInfoParam param);

    IPage<AlarmInfoModel> pageHisAlarmInfoModel(AlarmInfoParam param);

    IPage<AlarmInfoModel> pageVideoAlarmInfoModel(AlarmInfoParam param);

    IPage<AlarmInfoModel> pageVideoHisAlarmInfoModel(AlarmInfoParam param);

    AlarmInfoModel detail(Long id);

    Boolean alarmIgnore(AlarmIgnoreConfigParam param);

    Boolean alarmConfirm(AlarmHandleRecordParam param);

    Boolean stopDevice(AlarmHandleRecordParam param);

    AlarmIgnoreConfigModel getLastIgnore(AlarmIgnoreConfigParam param);

    Boolean adjustAlarmLevel(AlarmHandleRecordParam param);

    Boolean endAlarm(AlarmHandleRecordParam param);

    Boolean ignoreAlarmS(AlarmHandleRecordParam param);

    Boolean offlineDevices(AlarmHandleRecordParam param);

    List<AlarmDeviceModel> getAlarmDevices(AlarmHandleRecordParam param);

    List<UserInfoModel> getSpaceUsers(AlarmHandleRecordParam param);

    /***
     * @Description 根据设备id集合查询告警列表
     * @author huangyongtao
     * @date 2025/3/11 17:41
     * @param deviceIds
     */
    List<AlarmInfo> findAlarmInfoList(List<Long> deviceIds);

    /**
     * 根据设备ID列表处理未结束的告警
     *
     * @param deviceIds 设备ID列表
     * @param userInfoModel 用户信息模型，用于记录操作者信息
     * @param operateDesc 操作备注，用于记录操作备注
     * @param alarmEndType 告警结束类型，表示告警结束的原因
     * @return 处理结果，true表示成功处理
     */
    Boolean handAlarmsByDevices(List<Long> deviceIds , UserInfoModel userInfoModel , String operateDesc , Integer alarmEndType);
}
