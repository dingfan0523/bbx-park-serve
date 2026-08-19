package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.property.dto.model.MaintainPlanModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.service.IMaintainPlanService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 维保计划管理服务控制类
 * @author huangyongtao
 * @date 2025/10/16 17:21
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/maintain/plan")
@Api(tags = "BBX-维保计划管理")
public class MaintainPlanController {

    /**
     * 维保计划管理服务接口.
     */
    @Autowired
    private IMaintainPlanService maintainPlanService;

    /**
     * 获取维保计划管理信息.
     */
    @ApiOperation(value = "获取维保计划管理信息")
    @GetMapping(value = "/{id}")
    public CudResult<MaintainPlanModel> detail(@PathVariable Long id) {
        return CudResult.success(maintainPlanService.detail(id));
    }

    /**
     * 获取维保计划管理列表(分页).
     */
    @ApiOperation(value = "获取维保计划管理列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<MaintainPlanModel>> page(@RequestBody MaintainPlanPageParam param) {
        return CudResult.success(maintainPlanService.page(param));
    }

    /**
     * 获取维保计划管理列表.
     */
    @ApiOperation(value = "获取维保计划管理列表")
    @PostMapping(value = "/list")
    public CudResult<List<MaintainPlanModel>> list(@RequestBody MaintainPlanListParam param) {
        return CudResult.success(maintainPlanService.list(param));
    }

    /**
     * 新增维保计划管理.
     */
    @ApiOperation(value = "新增维保计划管理")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class}) 
                                      @RequestBody MaintainPlanParam param) {
        return CudResult.success(maintainPlanService.add(param));
    }

    /**
     * 删除维保计划管理.
     */
    @ApiOperation(value = "删除维保计划管理")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(maintainPlanService.remove(id));
    }

    /**
     * 编辑维保计划管理.
     */
    @ApiOperation(value = "编辑维保计划管理")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@Validated//({Default.class, UpdateGroup.class})
                                       @RequestBody MaintainPlanParam param) {
        return CudResult.success(maintainPlanService.edit(param));
    }
    /**
     * 编辑启用状态维保计划管理.
     */
    @ApiOperation(value = "编辑启用状态维保计划管理")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody MaintainPlanStatusParam param) {
        return CudResult.success(maintainPlanService.statusEdit(param));
    }
    /**
     * 查询维保设备列表.
     */
    @ApiOperation(value = "查询维保设备列表")
    @PostMapping(value = "/findDeviceList")
    public CudResult<List<IocDeviceModel>> findDeviceList(@RequestBody MaintainPlanDeviceParam param) {
        return CudResult.success(maintainPlanService.findDeviceList(param));
    }

    /**
     * 获取维保计划工单列表(分页).
     */
    @ApiOperation(value = "获取维保计划工单列表(分页)")
    @PostMapping(value = "/pageWorkOrder")
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(maintainPlanService.pageWorkOrder(param));
    }

    /**
     * 执行维保计划
     */
    @ApiOperation(value = "执行维保计划")
    @PostMapping(value = "/executePlan")
    public CudResult<Boolean> executePlan() {
        return CudResult.success(maintainPlanService.executePlan(null));
    }
    
}
