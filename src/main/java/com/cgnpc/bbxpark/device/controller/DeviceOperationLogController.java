
package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.DeviceOperationLogModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceOperationLogPageParam;
import com.cgnpc.bbxpark.device.service.IDeviceOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备控制（操作）日志服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/device/OperationLog")
@Api(tags = "设备控制（操作）日志")
public class DeviceOperationLogController {

    /**
     * Logger.
     */

    /**
     * 设备控制（操作）日志服务接口.
     */
    @Autowired
    private IDeviceOperationLogService deviceOperationLogService;


    @ApiOperation(value = "获取列表(分页)")
    @PostMapping(value = "/page" )
    public CudResult<IPage<DeviceOperationLogModel>> page(@RequestBody DeviceOperationLogPageParam param) {
        return CudResult.success(deviceOperationLogService.findPage(param));
    }


}
