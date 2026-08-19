package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoCountModel;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.model.IotDeviceRelationModel;
import com.cgnpc.bbxpark.device.dto.param.AlarmInfoParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.dto.param.IotDeviceRelationListParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceCountService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderCountModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/***
 * @Description ioc设备台账服务控制类
 * @author huangyongtao
 * @date 2025/4/18 14:28
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/ioc/device/count")
@Api(tags = "BBX-智慧物业-pc端-ioc设备台账")
public class IocDeviceCountController {

    @Autowired
    private IIocDeviceCountService iocDeviceCountService;

    /**
     * 设备列表(分页).
     */
    @ApiOperation(value = "设备列表(分页).")
    @PostMapping(value = "/pageDeviceCount")
    public CudResult<IPage<IocDeviceModel>> pageDeviceCount(@RequestBody IocDevicePageParam param) {
        return CudResult.success(iocDeviceCountService.pageDeviceCount(param));
    }

    /**
     * 查询关联设备列表.
     */
    @ApiOperation(value = "查询关联设备列表")
    @PostMapping(value = "/findRelationDevices")
    public CudResult<List<IotDeviceRelationModel>> findRelationDevices(@RequestBody IotDeviceRelationListParam param) {
        return CudResult.success(iocDeviceCountService.findRelationDevices(param));
    }

    /**
     * 告警列表(分页).
     */
    @ApiOperation(value = "告警列表(分页).")
    @PostMapping(value = "/pageAlarmInfo")
    public CudResult<IPage<AlarmInfoModel>> pageAlarmInfo(@RequestBody AlarmInfoParam param) {
        return CudResult.success(iocDeviceCountService.pageAlarmInfo(param));
    }

    /**
     * 告警信息统计
     */
    @ApiOperation(value = "告警信息统计")
    @PostMapping(value = "/countAlarmInfo")
    public CudResult<AlarmInfoCountModel> detail(@RequestBody AlarmInfoParam param) {
        return CudResult.success(iocDeviceCountService.countAlarmInfo(param));
    }

    /**
     * 获取未完成的告警信息统计
     */
    @ApiOperation(value = "获取未完成的告警信息统计")
    @PostMapping(value = "/getAlarmInfoCount")
    public CudResult<AlarmInfoCountModel> getAlarmInfoCount(@RequestBody AlarmInfoParam param) {
        return CudResult.success(iocDeviceCountService.getAlarmInfoCount(param));
    }

    /**
     * 查询设备工单列表(分页).
     */
    @ApiOperation(value = "查询设备工单列表(分页).")
    @PostMapping(value = "/pageWorkOrder")
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return  CudResult.success(iocDeviceCountService.pageWorkOrder(param));
    }

    /**
     * 工单信息统计
     */
    @ApiOperation(value = "工单信息统计")
    @PostMapping(value = "/countWorkOrder")
    public CudResult<WorkOrderCountModel> countWorkOrder(@RequestBody WorkOrderPageParam param) {
        return  CudResult.success(iocDeviceCountService.countWorkOrder(param));
    }

    /***
     *设备台账详情导出
     */
    @ApiOperation(value = "设备台账详情导出")
    @GetMapping(value = "/deviceCountEasyExport")
    public void deviceCountEasyExport(HttpServletResponse response, @ModelAttribute IocDevicePageParam param) {
        iocDeviceCountService.deviceCountEasyExport(response, param);
    }

}
