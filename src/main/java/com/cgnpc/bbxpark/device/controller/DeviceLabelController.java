
package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.DeviceLabelModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelListParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelPageParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceLabelParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.service.IDeviceLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;


/**
 * 设备标签应用控制类
 */
@Validated
@RestController
@RequestMapping(Constant.BASE_PATH + "/device/label")
@Api(tags = "设备标签管理")
public class DeviceLabelController{

    /**
     * 设备标签服务.
     */
    @Autowired
    private IDeviceLabelService deviceLabelService;

    /**
     * 获取设备标签详情.
     */
    @ApiOperation(value = "获取设备标签详情")
    @GetMapping(value = "/detail" )
    public CudResult<DeviceLabelModel> detail(@RequestParam @NotNull(message = "设备标签标识不能为空") Long id) {
        return CudResult.success(deviceLabelService.get(id));
    }

    /**
     * 获取设备标签列表(分页).
     */
    @ApiOperation(value = "获取设备标签列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<DeviceLabelModel>> page(@RequestBody @Validated DeviceLabelPageParam param) {
        return CudResult.success(deviceLabelService.pageResult(param));
    }


    /**
     * 新增设备标签.
     */
    @ApiOperation(value = "新增设备标签")
    @PostMapping(value = "/add" )
    public CudResult<Boolean> add(@RequestBody @Validated({Default.class}) DeviceLabelParam param) {
        return CudResult.success(deviceLabelService.add(param));
    }


    /**
     * 删除设备标签.
     */
    @ApiOperation(value = "删除设备标签")
    @GetMapping(value = "/remove" )
    public CudResult<Boolean> remove(@RequestParam @NotNull(message = "设备标签标识不能为空") Long id) {
        return CudResult.success(deviceLabelService.removeId(id));
    }


    /**
     * 编辑设备标签.
     */
    @ApiOperation(value = "编辑设备标签")
    @PostMapping(value = "/edit" )
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class}) DeviceLabelParam param) {
        return CudResult.success(deviceLabelService.edit(param));
    }


    /**
     * 新增设备到标签.
     */
    @ApiOperation(value = "新增设备到标签")
    @PostMapping(value = "/addDeviceToLabel" )
    public CudResult<Boolean>  addDeviceToLabel(@RequestBody DeviceLabelParam param) {
        return CudResult.success(deviceLabelService.addDeviceToLabel(param));
    }


    /**
     * 标签下的设备分组分页列表(分页)
     */
    @ApiOperation(value = "标签下的设备分组分页列表(分页)")
    @PostMapping(value = "/pageDeviceToLabel")
    public CudResult<IPage<IocDeviceModel>> pageDeviceToLabel(@RequestBody @Validated IocDevicePageParam param) {
        return CudResult.success(deviceLabelService.pageDeviceToLabel(param));
    }


    /**
     * 移除标签下的设备.
     */
    @ApiOperation(value = "移除标签下的设备")
    @PostMapping(value = "removeDeviceToLabel" )
    public CudResult<Boolean> removeDeviceToLabel(@RequestBody DeviceLabelListParam param) {
        return CudResult.success(deviceLabelService.removeDeviceToLabel(param));
    }


    /**
     * 根据设备id查询设备下的标签信息.
     */
    @ApiOperation(value = "根据设备id查询设备下的标签信息")
    @PostMapping(value = "/list" )
    public CudResult<List<DeviceLabelModel>> list(@RequestBody DeviceLabelListParam param) {
        return CudResult.success(deviceLabelService.list(param));
    }

}
