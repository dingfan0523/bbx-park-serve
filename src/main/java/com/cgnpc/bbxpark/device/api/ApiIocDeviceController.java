package com.cgnpc.bbxpark.device.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.model.IotDeviceRelationModel;
import com.cgnpc.bbxpark.device.dto.param.IocDeviceListParam;
import com.cgnpc.bbxpark.device.dto.param.IotDeviceRelationListParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description ioc设备服务控制类
 * @author huangyongtao
 * @date 2025/2/26 14:28
 */
@RestController
@RequestMapping("/api/ioc/device")
@Api(tags = "BBX-智慧物业-pc端-ioc设备管理")
public class ApiIocDeviceController {

    @Autowired
    private IIocDeviceService iocDeviceService;

    /**
     * 获取设备详情
     */
    @ApiOperation(value = "获取设备详情")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<IocDeviceModel> detail(@PathVariable Long id) {
        return CudResult.success(iocDeviceService.detail(id));
    }

    /**
     * 获取设备列表.
     */
    @ApiOperation(value = "获取设备列表")
    @PostMapping(value = "/list")
    @RequiredToken
    public CudResult<List<IocDeviceModel>> list(@RequestBody IocDeviceListParam param) {
        return CudResult.success(iocDeviceService.findList(param));
    }

    /**
     * 查询关联设备列表.
     */
    @ApiOperation(value = "查询关联设备列表")
    @PostMapping(value = "/findRelationDevices")
    @RequiredToken
    public CudResult<List<IotDeviceRelationModel>> findRelationDevices(@RequestBody IotDeviceRelationListParam param) {
        return CudResult.success(iocDeviceService.findRelationDevices(param));
    }
}
