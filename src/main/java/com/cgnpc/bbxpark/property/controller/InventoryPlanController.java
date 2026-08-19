package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.DeviceItemModel;
import com.cgnpc.bbxpark.property.dto.model.InventoryPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.InventoryPlanModel;
import com.cgnpc.bbxpark.property.dto.model.MaterialItemModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.service.IInventoryPlanService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 盘点计划管理服务控制类
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/inventory/plan")
@Api(tags = "BBX-盘点计划管理")
public class InventoryPlanController {

    /**
     * 盘点计划管理服务接口.
     */
    @Autowired
    private IInventoryPlanService inventoryPlanService;

    /**
     * 获取盘点计划管理信息.
     */
    @ApiOperation(value = "获取盘点计划管理信息")
    @GetMapping(value = "/{id}")
    public CudResult<InventoryPlanModel> detail(@PathVariable Long id) {
        return CudResult.success(inventoryPlanService.detail(id));
    }

    /**
     * 获取盘点计划管理列表(分页).
     */
    @ApiOperation(value = "获取盘点计划管理列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<InventoryPlanListModel>> page(@RequestBody InventoryPlanPageParam param) {
        return CudResult.success(inventoryPlanService.page(param));
    }

    /**
     * 获取盘点计划管理列表.
     */
    @ApiOperation(value = "获取盘点计划管理列表")
    @PostMapping(value = "/list")
    public CudResult<List<InventoryPlanListModel>> list(@RequestBody InventoryPlanListParam param) {
        return CudResult.success(inventoryPlanService.list(param));
    }

    /**
     * 新增盘点计划管理.
     */
    @ApiOperation(value = "新增盘点计划管理")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class})
                                      @RequestBody InventoryPlanParam param) {
        return CudResult.success(inventoryPlanService.add(param));
    }

    /**
     * 编辑盘点计划管理.
     */
    @ApiOperation(value = "编辑盘点计划管理")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                       InventoryPlanParam param) {
        return CudResult.success(inventoryPlanService.edit(param.getId(), param));
    }

    /**
     * 删除盘点计划管理.
     */
    @ApiOperation(value = "删除盘点计划管理")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(inventoryPlanService.remove(id));
    }

    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                         InspectionPointStatusParam param) {
        return CudResult.success(inventoryPlanService.statusEdit(param));
    }

    /**
     * 获取盘点计划工单列表(分页).
     */
    @ApiOperation(value = "获取盘点计划工单列表(分页)")
    @PostMapping(value = "/pageWorkOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(inventoryPlanService.pageWorkOrder(param));
    }

    /**
     * 执行盘点计划
     */
    @ApiOperation(value = "执行盘点计划")
    @PostMapping(value = "/executePlan")
    public CudResult<Boolean> executePlan() {
        return CudResult.success(inventoryPlanService.executePlan(null));
    }

    /**
     * 查询盘点计划下材料列表
     */
    @ApiOperation(value = "查询盘点计划下材料列表")
    @PostMapping(value = "/findMaterialList")
    public CudResult<List<MaterialItemModel>> findMaterialList(@RequestBody InventoryPlanItemParam param) {
        return CudResult.success(inventoryPlanService.findMaterialList(param));
    }

    /**
     * 查询盘点计划下设备列表
     */
    @ApiOperation(value = "查询盘点计划下设备列表")
    @PostMapping(value = "/findDeviceList")
    public CudResult<List<DeviceItemModel>> findDeviceList(@RequestBody InventoryPlanItemParam param) {
        return CudResult.success(inventoryPlanService.findDeviceList(param));
    }
}