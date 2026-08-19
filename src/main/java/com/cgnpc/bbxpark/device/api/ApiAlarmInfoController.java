
package com.cgnpc.bbxpark.device.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.AlarmDeviceModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmHandleRecordParam;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.mobile.annotation.RequiredToken;
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
@RequestMapping("/api/alarm")
@Api(tags = "BBX-告警业务压缩")
public class ApiAlarmInfoController {

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
    @RequiredToken
    public CudResult<IPage<AlarmInfoModel>> hisPage(@RequestBody AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(alarmInfoService.pageHisAlarmInfoModel(param));
    }

    /**
     * 实时告警列表(分页).
     */
    @ApiOperation(value = "实时告警列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<AlarmInfoModel>> page(@RequestBody AlarmInfoParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return CudResult.success(alarmInfoService.pageAlarmInfoModel(param));
    }

    /**
     * 实时告警详情.
     */
    @ApiOperation(value = "实时告警详情")
    @GetMapping(value = "/detail/{id}" )
    @RequiredToken
    public CudResult<AlarmInfoModel> detail(@PathVariable @NotNull(message = "实时告警标识") Long id) {
        return CudResult.success(alarmInfoService.detail(id));
    }

    /**
     * 告警确认
     */
    @ApiOperation(value = "告警确认")
    @PostMapping(value = "/confirm")
    @RequiredToken
    public CudResult<Boolean> alarmConfirm(@Validated @RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.alarmConfirm(param));
    }

    /**
     * 告警级别调整
     */
    @ApiOperation(value = "告警级别调整")
    @PostMapping(value = "/adjustAlarmLevel")
    @RequiredToken
    public CudResult<Boolean> adjustAlarmLevel(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.adjustAlarmLevel(param));
    }

    @ApiOperation(value = "手动结束告警")
    @PostMapping(value = "/endAlarm")
    @RequiredToken
    public CudResult<Boolean> endAlarm(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.endAlarm(param));
    }

    @ApiOperation(value = "批量忽略告警(仅忽略本次)")
    @PostMapping(value = "/ignoreAlarmS")
    @RequiredToken
    public CudResult<Boolean> ignoreAlarmS(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.ignoreAlarmS(param));
    }

    @ApiOperation(value = "批量设备下线")
    @PostMapping(value = "/offlineDevices")
    @RequiredToken
    public CudResult<Boolean> offlineDevices(@RequestBody AlarmHandleRecordParam param) {
        return CudResult.success(alarmInfoService.offlineDevices(param));
    }

    @ApiOperation(value = "获取所有告警设备")
    @PostMapping(value = "/getAlarmDevices")
    @RequiredToken
    public CudResult<List<AlarmDeviceModel>> getAlarmDevices(@RequestBody AlarmHandleRecordParam param) {
        return  CudResult.success(alarmInfoService.getAlarmDevices(param));
    }
}
