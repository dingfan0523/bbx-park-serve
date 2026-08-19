package com.cgnpc.bbxpark.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.*;
import com.cgnpc.bbxpark.device.dto.param.*;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.space.dto.model.DepartmentInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
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
@RequestMapping(Constant.BASE_PATH + "/ioc/device")
@Api(tags = "BBX-智慧物业-pc端-ioc设备管理")
public class IocDeviceController {

    @Autowired
    private IIocDeviceService iocDeviceService;

    /**
     * 获取设备详情
     */
    @ApiOperation(value = "获取设备详情")
    @GetMapping(value = "/detail/{id}")
    public CudResult<IocDeviceModel> detail(@PathVariable Long id) {
        return CudResult.success(iocDeviceService.detail(id));
    }

    /**
     * 设备列表(分页).
     */
    @ApiOperation(value = "设备列表(分页).")
    @PostMapping(value = "/page")
    public CudResult<IPage<IocDeviceModel>> page(@RequestBody IocDevicePageParam param) {
        return CudResult.success(iocDeviceService.pageDevice(param));
    }

    /**
     * 获取设备列表.
     */
    @ApiOperation(value = "获取设备列表")
    @PostMapping(value = "/list")
    public CudResult<List<IocDeviceModel>> list(@RequestBody IocDeviceListParam param) {
        return CudResult.success(iocDeviceService.findList(param));
    }

    /**
     * 编辑设备信息
     */
    @ApiOperation(value = "编辑设备信息")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody IocDeviceParam param){
        return CudResult.success(iocDeviceService.edit(param));
    }

    /**
     * 新增设备信息
     */
    @ApiOperation(value = "新增设备信息")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@RequestBody IocDeviceParam param){
        return CudResult.success(iocDeviceService.add(param));
    }

    /**
     * 删除设备信息
     */
    @ApiOperation(value = "删除设备信息")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id){
        return CudResult.success(iocDeviceService.remove(id));
    }

    /**
     * 绑定设备空间位置.
     */
    @ApiOperation(value = "绑定设备空间位置")
    @PostMapping(value = "/bindDeviceSpace")
    public CudResult<Boolean> bindDeviceSpace(@RequestBody IocDeviceBindingParam param){
        return CudResult.success(iocDeviceService.bindDeviceSpace(param));
    }
    /**
     * 绑定设备部门信息.
     */
    @ApiOperation(value = "绑定设备部门信息")
    @PostMapping(value = "/bindDeviceDepartment")
    public CudResult<Boolean> bindDeviceDepartment(@RequestBody IocDeviceBindingParam param){
        return CudResult.success(iocDeviceService.bindDeviceDepartment(param));
    }

    /**
     * 绑定设备产品信息.
     */
    @ApiOperation(value = "绑定设备产品信息")
    @PostMapping(value = "/bindDeviceProduct")
    public CudResult<Boolean> bindDeviceProduct(@RequestBody IocDeviceBindingParam param){
        return CudResult.success(iocDeviceService.bindDeviceProduct(param));
    }

    /**
     * 修改设备启用状态.
     */
    @ApiOperation(value = "修改设备启用状态")
    @PostMapping(value = "/deviceEnable")
    public CudResult<Boolean> deviceEnable(@RequestBody IocDeviceStatusParam param){
        return CudResult.success(iocDeviceService.deviceEnable(param));
    }

    /**
     * 修改设备上下线状态.
     */
    @ApiOperation(value = "修改设备上下线状态")
    @PostMapping(value = "/deviceOnline")
    public CudResult<Boolean> deviceOnline(@RequestBody IocDeviceStatusParam param){
        return CudResult.success(iocDeviceService.deviceOnline(param));
    }

    /**
     * 校验母设备是否包含子设备.
     */
    @ApiOperation(value = "校验母设备是否包含子设备")
    @PostMapping(value = "/checkDeviceComplex")
    public CudResult<Boolean> checkDeviceComplex(@RequestBody IocDeviceParam param){
        return CudResult.success(iocDeviceService.checkDeviceComplex(param));
    }

    /**
     * 根据iot设备标识获取iot设备详情信息.
     */
    @ApiOperation(value = "根据iot设备标识获取iot设备详情信息.")
    @GetMapping(value = "/findIotDevice/{iotDeviceDn}")
    public CudResult<IotDeviceCheckModel> findIotDevice(@PathVariable String iotDeviceDn){
        return CudResult.success(iocDeviceService.findIotDevice(iotDeviceDn));
    }

    /**
     * 查询苍南基地下级节点部门信息
     */
    @ApiOperation(value = "查询苍南基地下级节点部门信息")
    @PostMapping(value = "/findSubDepartments")
    public CudResult<List<DepartmentInfoModel>> findSubDepartments(){
        return CudResult.success(iocDeviceService.findSubDepartments());
    }

    /**
     * 根据部门信息查询人员列表
     */
    @ApiOperation(value = "根据部门信息查询人员列表")
    @GetMapping(value = "/findUserInfoByDepartment/{departmentId}")
    public CudResult<List<UserInfoModel>> findUserInfoByDepartment(@PathVariable String departmentId){
        return  CudResult.success(iocDeviceService.findUserInfoByDepartment(departmentId));
    }

    /**
     * 获取设备物模型列表
     */
    @ApiOperation(value = "获取设备物模型列表")
    @PostMapping(value = "/thingModel/list")
    public CudResult<List<DeviceThingModel>> thingModelList(@RequestBody IocDeviceThingModelParam param){
        return CudResult.success(iocDeviceService.thingModelList(param.getDeviceId()));
    }
    /**
     * 查询关联设备列表.
     */
    @ApiOperation(value = "查询关联设备列表")
    @PostMapping(value = "/findRelationDevices")
    public CudResult<List<IotDeviceRelationModel>> findRelationDevices(@RequestBody IotDeviceRelationListParam param) {
        return CudResult.success(iocDeviceService.findRelationDevices(param));
    }

    /**
     * 保存关联设备列表.
     */
    @ApiOperation(value = "保存关联设备列表")
    @PostMapping(value = "/saveRelationDevices")
    public CudResult<Boolean> saveRelationDevices(@RequestBody IotDeviceRelationParam param) {
        return CudResult.success(iocDeviceService.saveRelationDevices(param));
    }

    /**
     * 更新物联设备状态.
     */
    @ApiOperation(value = "更新物联设备状态")
    @PostMapping(value = "/updateIotDeviceStatus")
//    //@AuthAll
    public CudResult<Boolean> updateIotDeviceStatus(){
        return CudResult.success(iocDeviceService.updateIotDeviceStatus());
    }

    /**
     * 查询空间设备树
     */
    @ApiOperation(value = "查询空间设备树")
    @PostMapping(value = "/treeSpaceDevices")
    public CudResult<DeviceVideoTreeModel> treeSpaceDevices(@RequestBody IocDeviceParam param) {
        return  CudResult.success(iocDeviceService.treeSpaceDevices(param));
    }

    /**
     * 查询重点空间设备树
     */
    @ApiOperation(value = "查询重点空间设备树")
    @PostMapping(value = "/treeKeySpaceDevices")
    public CudResult<DeviceVideoTreeModel> treeKeySpaceDevices(@RequestBody IocDeviceParam param) {
        return  CudResult.success(iocDeviceService.treeKeySpaceDevices(param));
    }

    /**
     * 标记重点设备.
     */
    @ApiOperation(value = "标记重点设备")
    @PostMapping(value = "/signKeyAreas")
    public CudResult<Boolean> signKeyAreas(@RequestBody IocDeviceParam param) {
        return CudResult.success(iocDeviceService.signKeyAreas(param));
    }
}
