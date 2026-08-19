package com.cgnpc.bbxpark.device.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceCountService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Description ioc设备台账服务控制类
 * @author huangyongtao
 * @date 2025/4/18 14:28
 */
@RestController
@RequestMapping("/api/ioc/device/count")
@Api(tags = "BBX-智慧物业-pc端-ioc设备台账")
public class ApiIocDeviceCountController {

    @Autowired
    private IIocDeviceCountService iocDeviceCountService;

    /**
     * 设备列表(分页).
     */
    @ApiOperation(value = "设备列表(分页).")
    @PostMapping(value = "/pageDeviceCount")
    @RequiredToken
    public CudResult<IPage<IocDeviceModel>> pageDeviceCount(@RequestBody IocDevicePageParam param) {
        return CudResult.success(iocDeviceCountService.pageDeviceCount(param));
    }

    /**
     * 查询设备工单列表(分页).
     */
    @ApiOperation(value = "查询设备工单列表(分页).")
    @PostMapping(value = "/pageWorkOrder")
    @RequiredToken
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return  CudResult.success(iocDeviceCountService.pageWorkOrder(param));
    }

    /**
     * 告警列表(分页).
     */
    @ApiOperation(value = "告警列表(分页).")
    @PostMapping(value = "/pageAlarmInfo")
    @RequiredToken
    public CudResult<IPage<AlarmInfoModel>> pageAlarmInfo(@RequestBody AlarmInfoParam param) {
        return CudResult.success(iocDeviceCountService.pageAlarmInfo(param));
    }
}
