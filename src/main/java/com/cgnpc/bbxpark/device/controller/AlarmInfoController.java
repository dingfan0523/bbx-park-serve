
package com.cgnpc.bbxpark.device.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.AlarmDeviceModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmIgnoreConfigModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmHandleRecordParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmIgnoreConfigParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 告警业务压缩服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/alarm")
@Api(tags = "BBX-告警业务压缩")
public class AlarmInfoController {

    /**
     * Logger.
     */

    /**
     * 告警业务压缩服务接口.
     */
    @Autowired
    private IAlarmInfoService alarmInfoService;

    /**
     * 历史告警(分页).
     */
    @ApiOperation(value = "历史告警列表(分页)")
    @PostMapping(value = "/his/page")
    public CudResult<IPage<AlarmInfoModel>> hisPage(@RequestBody AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(alarmInfoService.pageHisAlarmInfoModel(param));
    }

    /**
     * 实时告警列表(分页).
     */
    @ApiOperation(value = "实时告警列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<AlarmInfoModel>> page(@RequestBody AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(alarmInfoService.pageAlarmInfoModel(param));
    }

    /**
     * 实时告警详情.
     */
    @ApiOperation(value = "实时告警详情")
    @GetMapping(value = "/detail/{id}" )
    public CudResult<AlarmInfoModel> detail(@PathVariable @NotNull(message = "实时告警标识") Long id) {
        return CudResult.success(alarmInfoService.detail(id));
    }

    /**
     * 告警忽略
     */
    @ApiOperation(value = "告警忽略")
    @PostMapping(value = "/ignore")
    public CudResult<Boolean> alarmIgnore(@Validated @RequestBody AlarmIgnoreConfigParam param) {
        return CudResult.success(alarmInfoService.alarmIgnore(param));
    }

    /**
     * 告警忽略
     */
    @ApiOperation(value = "获取设备最近一条忽略告警信息")
    @PostMapping(value = "/getLastIgnore")
    public CudResult<AlarmIgnoreConfigModel> getLastIgnore(@Validated @RequestBody AlarmIgnoreConfigParam param) {
        return CudResult.success(alarmInfoService.getLastIgnore(param));
    }

    /**
     * 告警确认
     */
    @ApiOperation(value = "告警确认")
    @PostMapping(value = "/confirm")
    public CudResult<Boolean> alarmConfirm(@Validated @RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.alarmConfirm(param));
    }

    /**
     * 告警确认
     */
    @ApiOperation(value = "设备停用")
    @PostMapping(value = "/stopDevice")
    public CudResult<Boolean> stopDevice(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.stopDevice(param));
    }

    /**
     * 告警级别调整
     */
    @ApiOperation(value = "告警级别调整")
    @PostMapping(value = "/adjustAlarmLevel")
    public CudResult<Boolean> adjustAlarmLevel(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.adjustAlarmLevel(param));
    }

    @ApiOperation(value = "手动结束告警")
    @PostMapping(value = "/endAlarm")
    public CudResult<Boolean> endAlarm(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.endAlarm(param));
    }

    @ApiOperation(value = "批量忽略告警(仅忽略本次)")
    @PostMapping(value = "/ignoreAlarmS")
    public CudResult<Boolean> ignoreAlarmS(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.ignoreAlarmS(param));
    }

    @ApiOperation(value = "批量设备下线")
    @PostMapping(value = "/offlineDevices")
    public CudResult<Boolean> offlineDevices(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.offlineDevices(param));
    }

    @ApiOperation(value = "获取所有告警设备")
    @PostMapping(value = "/getAlarmDevices")
    public CudResult<List<AlarmDeviceModel>> getAlarmDevices(@RequestBody AlarmHandleRecordParam param) {
        return  CudResult.success(alarmInfoService.getAlarmDevices(param));
    }


    @ApiOperation(value = "获取空间下所有人员")
    @PostMapping(value = "/getSpaceUsers")
    public CudResult<List<UserInfoModel>> getSpaceUsers(@RequestBody AlarmHandleRecordParam param) {
        return  CudResult.success(alarmInfoService.getSpaceUsers(param));
    }

    /**
     * 监控设备历史告警(分页).
     */
    @ApiOperation(value = "监控设备历史告警(分页)")
    @PostMapping(value = "/his/video/page")
    public CudResult<IPage<AlarmInfoModel>> hisVideoPage(@RequestBody AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(alarmInfoService.pageVideoHisAlarmInfoModel(param));
    }

    /**
     * 监控设备实时告警列表(分页).
     */
    @ApiOperation(value = "监控设备实时告警列表(分页)")
    @PostMapping(value = "/video/page")
    public CudResult<IPage<AlarmInfoModel>> videoPage(@RequestBody AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(alarmInfoService.pageVideoAlarmInfoModel(param));
    }

}
