
package com.cgnpc.bbxpark.workorder.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanDetailModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPlanHandleParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkPlanDetailParam;
import com.cgnpc.bbxpark.workorder.service.IWorkTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * @Description 工单任务服务控制类
 * @author huangyongtao
 * @date 2025/11/10 17:11
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/workOrder/task")
@Api(tags= "智慧物业-PC端-工单任务")
public class WorkTaskController {

    /**
     * 工单任务服务接口.
     */
    @Autowired
    private IWorkTaskService workTaskService;

    /**
     * 获取工单抄表计划详情
     */
    @ApiOperation(value = "获取工单抄表计划详情")
    @PostMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<WorkPlanDetailModel> detail(@RequestBody WorkPlanDetailParam param) {
        return CudResult.success(workTaskService.detail(param));
    }

    /**
     * 查询工单计划
     */
    @ApiOperation(value = "查询工单计划")
    @PostMapping(value = "/findWorkPlan", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<WorkOrderModel> findWorkOrderDeviceList(@RequestBody WorkOrderPlanHandleParam param) {
        return CudResult.success(workTaskService.findWorkPlan(param));
    }

    /**
     * 保存工单计划
     */
    @ApiOperation(value = "保存工单计划")
    @PostMapping(value = "/save",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> saveWorkPlan(@RequestBody WorkOrderPlanHandleParam param) {
        return CudResult.success(workTaskService.saveWorkPlan(param));
    }

    /**
     * 提交工单计划
     */
    @ApiOperation(value = "提交工单计划")
    @PostMapping(value = "/submit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> submitWorkPlan(@RequestBody WorkOrderPlanHandleParam param) {
        return CudResult.success(workTaskService.submitWorkPlan(param));
    }
}
