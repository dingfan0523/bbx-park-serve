package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.TaskItemModel;
import com.cgnpc.bbxpark.property.dto.model.TaskPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.TaskPlanModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.TaskPlanListParam;
import com.cgnpc.bbxpark.property.dto.param.TaskPlanPageParam;
import com.cgnpc.bbxpark.property.dto.param.TaskPlanParam;
import com.cgnpc.bbxpark.property.service.ITaskPlanService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务计划管理服务控制类
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/task/plan")
@Api(tags = "BBX-任务计划管理")
public class TaskPlanController {

    /**
     * 任务计划管理服务接口.
     */
    @Autowired
    private ITaskPlanService taskPlanService;

    /**
     * 获取任务计划管理信息.
     */
    @ApiOperation(value = "获取任务计划管理信息")
    @GetMapping(value = "/{id}")
    public CudResult<TaskPlanModel> detail(@PathVariable Long id) {
        return CudResult.success(taskPlanService.detail(id));
    }

    /**
     * 获取任务计划管理列表(分页).
     */
    @ApiOperation(value = "获取任务计划管理列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<TaskPlanListModel>> page(@RequestBody TaskPlanPageParam param) {
        return CudResult.success(taskPlanService.page(param));
    }

    /**
     * 获取任务计划管理列表.
     */
    @ApiOperation(value = "获取任务计划管理列表")
    @PostMapping(value = "/list")
    public CudResult<List<TaskPlanListModel>> list(@RequestBody TaskPlanListParam param) {
        return CudResult.success(taskPlanService.list(param));
    }

    /**
     * 新增任务计划管理.
     */
    @ApiOperation(value = "新增任务计划管理")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class})
                                      @RequestBody TaskPlanParam param) {
        return CudResult.success(taskPlanService.add(param));
    }

    /**
     * 编辑任务计划管理.
     */
    @ApiOperation(value = "编辑任务计划管理")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                       TaskPlanParam param) {
        return CudResult.success(taskPlanService.edit(param));
    }

    /**
     * 删除任务计划管理.
     */
    @ApiOperation(value = "删除任务计划管理")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(taskPlanService.remove(id));
    }

    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                         InspectionPointStatusParam param) {
        return CudResult.success(taskPlanService.statusEdit(param));
    }

    /**
     * 查询任务计划下任务列表
     */
    @ApiOperation(value = "查询任务计划下任务列表")
    @GetMapping(value = "/findItemList")
    public CudResult<List<TaskItemModel>> findItemList(@RequestParam Long id) {
        return CudResult.success(taskPlanService.findItemList(id));
    }

    /**
     * 获取任务计划工单列表(分页).
     */
    @ApiOperation(value = "获取任务计划工单列表(分页)")
    @PostMapping(value = "/pageWorkOrder")
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(taskPlanService.pageWorkOrder(param));
    }

    /**
     * 执行任务计划
     */
    @ApiOperation(value = "执行任务计划")
    @PostMapping(value = "/executePlan")
    public CudResult<Boolean> executePlan() {
        return CudResult.success(taskPlanService.executePlan(null));
    }
}