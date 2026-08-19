package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoCountModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.service.IAlarmInfoService;
import com.cgnpc.bbxpark.device.service.IIocDeviceCountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 大屏工单统计
 */
@RestController
@RequestMapping("/api/dtwin/alarm")
@Api(tags= "大屏-告警模块接口")
@Slf4j
public class ApiAlarmScreenController {
    @Autowired
    private IIocDeviceCountService iocDeviceCountService;
    @Autowired
    private IAlarmInfoService alarmInfoService;

    /**
     * 告警列表(分页).
     */
    @ApiOperation(value = "告警列表(分页).")
    @PostMapping(value = "/page")
    public CudResult<IPage<AlarmInfoModel>> pageAlarmInfo(@RequestBody AlarmInfoParam param) {
        return CudResult.success(iocDeviceCountService.pageAlarmInfo(param));
    }

    /**
     * 告警信息统计
     */
    @ApiOperation(value = "告警信息统计")
    @GetMapping(value = "/count")
    public CudResult<AlarmInfoCountModel> count(@ApiParam("设备id") @RequestParam("deviceId") Long deviceId) {
        AlarmInfoParam param = new AlarmInfoParam();
        param.setDeviceId(deviceId);
        return CudResult.success(iocDeviceCountService.countAlarmInfo(param));
    }

    /**
     * 实时告警详情.
     */
    @ApiOperation(value = "告警详情")
    @GetMapping(value = "/detail" )
    public CudResult<AlarmInfoModel> detail(@RequestParam Long id) {
        return CudResult.success(alarmInfoService.detail(id));
    }
}