package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.InspectionPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPlanModel;
import com.cgnpc.bbxpark.property.dto.model.InspectionPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPlanListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPlanPageParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPlanParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.service.IInspectionPlanService;
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
 * 巡检计划服务接口.
 * @author 54766
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/inspection/plan")
@Api(tags = "BBX-物业管理-PC端-巡检计划")
public class InspectionPlanController {

    /**
     * 巡检计划服务接口.
     */
    @Autowired
    private IInspectionPlanService inspectionPlanService;

    /**
     * 获取巡检计划信息.
     */
    @ApiOperation(value = "获取巡检计划详细信息")
    @GetMapping(value = "/{id}")
    public CudResult<InspectionPlanModel> detail(@PathVariable Long id) {
        return CudResult.success(inspectionPlanService.detail(id));
    }

    /**
     * 获取巡检计划列表(分页).
     */
    @ApiOperation(value = "获取巡检计划列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<InspectionPlanListModel>> page(@RequestBody InspectionPlanPageParam param) {
        return CudResult.success(inspectionPlanService.page(param));
    }

    /**
     * 获取巡检计划列表.
     */
    @ApiOperation(value = "获取巡检计划列表")
    @PostMapping(value = "/list")
    public CudResult<List<InspectionPlanListModel>> list(@RequestBody InspectionPlanListParam param) {
        return CudResult.success(inspectionPlanService.list(param));
    }

    /**
     * 新增巡检计划.
     */
    @ApiOperation(value = "新增巡检计划")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class}) 
                                      @RequestBody InspectionPlanParam param) {
        return CudResult.success(inspectionPlanService.add(param));
    }

    /**
     * 编辑巡检计划.
     */
    @ApiOperation(value = "编辑巡检计划")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class}) 
                                       InspectionPlanParam param) {
        return CudResult.success(inspectionPlanService.edit(param));
    }

    /**
     * 删除巡检计划.
     */
    @ApiOperation(value = "删除巡检计划")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(inspectionPlanService.remove(id));
    }

    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class}) 
                                         InspectionPointStatusParam param) {
        return CudResult.success(inspectionPlanService.statusEdit(param));
    }

    /**
     * 根据id获取巡检计划下的巡检点列表.
     */
    @ApiOperation(value = "根据id获取巡检计划下的巡检点列表")
    @GetMapping(value = "/findPointList")
    public CudResult<List<InspectionPointModel>> findPointList(@RequestParam Long id) {
        return CudResult.success(inspectionPlanService.findPointList(id));
    }

    /**
     * 获取巡检计划工单列表(分页).
     */
    @ApiOperation(value = "获取巡检计划工单列表(分页)")
    @PostMapping(value = "/pageWorkOrder")
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(inspectionPlanService.pageWorkOrder(param));
    }

    /**
     * 执行巡检计划
     */
    @ApiOperation(value = "执行巡检计划")
    @PostMapping(value = "/executePlan")
    public CudResult<Boolean> executePlan() {
        return CudResult.success(inspectionPlanService.executePlan(null));
    }
}
