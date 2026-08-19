
package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupModel;
import com.cgnpc.bbxpark.device.dto.model.DeviceGroupTreeModel;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupListParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupParam;
import com.cgnpc.bbxpark.device.dto.param.DeviceGroupRelParam;
import com.cgnpc.bbxpark.device.dto.param.IocDevicePageParam;
import com.cgnpc.bbxpark.device.service.IDeviceGroupRelService;
import com.cgnpc.bbxpark.device.service.IDeviceGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 设备分组服务控制类
 * @author huangyongtao
 * @date 2024/8/12 17:31
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/device/group")
@Api(tags = "设备分组")
public class DeviceGroupController {

    /**
     * Logger.
     */

    /**
     * 设备分组服务接口.
     */
    @Autowired
    private IDeviceGroupService deviceGroupService;

    @Autowired
    private IDeviceGroupRelService deviceGroupRelService;

    /**
     * 获取设备分组信息.
     */
    @ApiOperation(value = "获取设备分组信息")
    @PostMapping(value = "/detail")
    public CudResult<DeviceGroupModel> detail(@RequestBody DeviceGroupParam param) {
        return CudResult.success(deviceGroupService.detail(param));
    }


    /**
     * 获取设备分组列表.
     */
    @ApiOperation(value = "获取设备分组列表")
    @PostMapping(value = "/list")
    public CudResult<List<DeviceGroupModel>> list(@RequestBody DeviceGroupListParam param) {
        return CudResult.success(deviceGroupService.list(param));
    }

    /**
     *  查询设备分组树
     */
    @ApiOperation(value = "查询设备分组树")
    @PostMapping(value = "/findTree")
    public CudResult<List<DeviceGroupTreeModel>> findTree(@RequestBody DeviceGroupListParam param) {
        return CudResult.success(deviceGroupService.findTree(param));
    }

    /**
     * 新增设备分组.
     */
    @ApiOperation(value = "新增设备分组")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@RequestBody @Validated({Default.class}) DeviceGroupParam param) {
        return CudResult.success(deviceGroupService.add(param));
    }

    /**
     * 删除设备分组.
     */
    @ApiOperation(value = "删除设备分组")
    @PostMapping(value = "/remove")
    public CudResult<Boolean> remove(@RequestBody DeviceGroupParam param) {
        return CudResult.success(deviceGroupService.remove(param));
    }

    /**
     * 编辑设备分组.
     */
    @ApiOperation(value = "编辑设备分组")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class}) DeviceGroupParam param) {
        return CudResult.success(deviceGroupService.edit(param));
    }

    /**
     * 新增设备分组关系
     */
    @ApiOperation(value = "新增设备分组关系")
    @PostMapping(value = "/addDeviceToGroup")
    public CudResult<Boolean> addDeviceToGroup(@RequestBody DeviceGroupRelParam param) {
        return CudResult.success(deviceGroupRelService.addDeviceToGroup(param));
    }

    /**
     * 删除设备分组关系
     */
    @ApiOperation(value = "删除设备分组关系.")
    @PostMapping(value = "/removeDeviceToGroup")
    public CudResult<Boolean> removeDeviceToGroup(@RequestBody DeviceGroupRelParam param) {
        return CudResult.success(deviceGroupRelService.removeDeviceToGroup(param));
    }

    /***
     * @Description 分页查询设备分组下的设备列表
     * @author huangyongtao
     * @date 2024/8/13 10:30
     * @param param
     */
    @ApiOperation(value = "分页查询设备分组下的设备列表")
    @PostMapping(value = "/pageDevice")
    public CudResult<IPage<IocDeviceModel>> pageDevice(@RequestBody IocDevicePageParam param) {
        return CudResult.success(deviceGroupRelService.pageDevice(param));
    }

    /***
     * @Description 分页查询排除设备分组设备的列表
     * @author huangyongtao
     * @date 2024/8/13 10:31
     * @param param
     */
    @ApiOperation(value = "分页查询排除设备分组设备的列表")
    @PostMapping(value = "/pageNoDevice")
    public CudResult<IPage<IocDeviceModel>> pageNoDevice(@RequestBody IocDevicePageParam param) {
        return CudResult.success(deviceGroupRelService.pageNoDevice(param));
    }

    /***
     * @Description 分页查询设备分组设备列表(对外提供)
     * @author huangyongtao
     * @date 2024/8/13 10:30
     * @param param
     */
    @ApiOperation(value = "分页查询设备分组设备列表(对外提供)")
    @PostMapping(value = "/pageGroupDevice")
    public CudResult<IPage<IocDeviceModel>> pageGroupDevice(@RequestBody IocDevicePageParam param) {
        return CudResult.success(deviceGroupRelService.pageGroupDevice(param));
    }

    /***
     * @Description 查询设备分组设备列表(对外提供)
     * @author huangyongtao
     * @date 2024/8/13 10:30
     * @param param
     */
    @ApiOperation(value = "查询设备分组设备列表(对外提供)")
    @PostMapping(value = "/findGroupDevice")
    public CudResult<List<IocDeviceModel>> findGroupDevice(@RequestBody IocDevicePageParam param) {
        param.setSize(Integer.MAX_VALUE);
        return CudResult.success(deviceGroupRelService.pageGroupDevice(param).getRecords());
    }
}
