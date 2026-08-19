
package com.cgnpc.bbxpark.device.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.DeviceControlLogModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceControlLogPageParam;
import com.cgnpc.bbxpark.device.service.IDeviceControlLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(Constant.BASE_PATH + "/deviceControlLog")
@Api(tags = "PC端-设备控制日志")
@Slf4j
public class DeviceControlLogController {

    /**
     * 来访记录服务接口.
     */
    @Autowired
    private IDeviceControlLogService deviceControlLogService;


    /**
     * 获取访客通行记录列表(分页).
     */
    @ApiOperation(value = "获取设备控制日志列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<DeviceControlLogModel>> page(@RequestBody DeviceControlLogPageParam param) {
        return CudResult.success(deviceControlLogService.page(param));
    }

    /***
     *访客通行记录导出
     */
    @ApiOperation(value = "设备控制日志导出")
    @GetMapping(value = "/export")
    public void easyExport(HttpServletResponse response, @ModelAttribute DeviceControlLogPageParam param) {
        deviceControlLogService.export(response, param);
    }

}
