package com.cgnpc.bbxpark.property.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.property.dto.model.PatrolPlanListModel;
import com.cgnpc.bbxpark.property.dto.model.PatrolPlanModel;
import com.cgnpc.bbxpark.property.dto.model.SimplePatrolRouteModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPlanListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPlanPageParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPlanParam;
import com.cgnpc.bbxpark.property.service.IPatrolPlanService;
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
 * 巡更计划服务接口.
 * @author huangyongtao
 */
@Slf4j
@RestController
@RequestMapping(Constant.BASE_PATH + "/patrol/plan")
@Api(tags = "BBX-物业管理-PC端-巡更计划")
public class PatrolPlanController {

    /**
     * 巡更计划服务接口.
     */
    @Autowired
    private IPatrolPlanService patrolPlanService;

    /**
     * 获取巡更计划信息.
     */
    @ApiOperation(value = "获取巡更计划详细信息")
    @GetMapping(value = "/{id}")
    public CudResult<PatrolPlanModel> detail(@PathVariable Long id) {
        return CudResult.success(patrolPlanService.detail(id));
    }

    /**
     * 获取巡更计划列表(分页).
     */
    @ApiOperation(value = "获取巡更计划列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<PatrolPlanListModel>> page(@RequestBody PatrolPlanPageParam param) {
        return CudResult.success(patrolPlanService.page(param));
    }

    /**
     * 获取巡更计划列表.
     */
    @ApiOperation(value = "获取巡更计划列表")
    @PostMapping(value = "/list")
    public CudResult<List<PatrolPlanListModel>> list(@RequestBody PatrolPlanListParam param) {
        return CudResult.success(patrolPlanService.list(param));
    }

    /**
     * 新增巡更计划.
     */
    @ApiOperation(value = "新增巡更计划")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated//({Default.class, InsertGroup.class})
                                      @RequestBody PatrolPlanParam param) {
        return CudResult.success(patrolPlanService.add(param));
    }

    /**
     * 编辑巡更计划.
     */
    @ApiOperation(value = "编辑巡更计划")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                       PatrolPlanParam param) {
        return CudResult.success(patrolPlanService.edit(param.getId(), param));
    }

    /**
     * 删除巡更计划.
     */
    @ApiOperation(value = "删除巡更计划")
    @GetMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(patrolPlanService.remove(id));
    }

    /**
     * 编辑启用状态
     */
    @ApiOperation(value = "编辑启用状态")
    @PostMapping(value = "/status/edit")
    public CudResult<Boolean> statusEdit(@RequestBody @Validated//({Default.class, UpdateGroup.class})
                                         InspectionPointStatusParam param) {
        return CudResult.success(patrolPlanService.statusEdit(param));
    }

    /**
     * 根据id获取巡更计划下的巡更路线列表.
     */
    @ApiOperation(value = "根据id获取巡更计划下的巡更路线列表")
    @GetMapping(value = "/findRouteList")
    public CudResult<List<SimplePatrolRouteModel>> findRouteList(@RequestParam Long id) {
        return CudResult.success(patrolPlanService.findRouteList(id));
    }

    /**
     * 获取巡更计划工单列表(分页).
     */
    @ApiOperation(value = "获取巡更计划工单列表(分页)")
    @PostMapping(value = "/pageWorkOrder", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<IPage<WorkOrderModel>> pageWorkOrder(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(patrolPlanService.pageWorkOrder(param));
    }

    /**
     * 执行巡更计划
     */
    @ApiOperation(value = "执行巡更计划")
    @PostMapping(value = "/executePlan")
    public CudResult<Boolean> executePlan() {
        return CudResult.success(patrolPlanService.executePlan(null));
    }
}
