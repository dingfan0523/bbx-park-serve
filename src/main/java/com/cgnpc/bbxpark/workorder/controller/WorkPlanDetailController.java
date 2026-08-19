
package com.cgnpc.bbxpark.workorder.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkPlanDetailModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPlanHandleParam;
import com.cgnpc.bbxpark.workorder.dto.param.WorkPlanDetailParam;
import com.cgnpc.bbxpark.workorder.service.IWorkPlanDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/***
 * @Description 工单计划详细信息服务控制类
 * @author huangyongtao
 * @date 2025/3/26 14:23
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/workOrder/plan")
@Api(tags= "智慧物业-PC端-工单抄表计划")
public class WorkPlanDetailController {

    /**
     * 工单计划详细信息服务接口.
     */
    @Autowired
    private IWorkPlanDetailService workPlanDetailService;

    /**
     * 获取工单抄表计划详情
     */
    @ApiOperation(value = "获取工单抄表计划详情")
    @GetMapping(value = "/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<WorkPlanDetailModel> detail(@PathVariable WorkPlanDetailParam param) {
        return CudResult.success(workPlanDetailService.detail(param));
    }

    /**
     * 查询工单抄表设备列表
     */
    @ApiOperation(value = "查询工单抄表设备列表")
    @PostMapping(value = "/findDeviceList", produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<WorkOrderModel> findWorkOrderDeviceList(@RequestBody WorkOrderPlanHandleParam param) {
        return CudResult.success(workPlanDetailService.findWorkOrderDeviceList(param));
    }

    /**
     * 保存工单抄表计划
     */
    @ApiOperation(value = "保存工单抄表计划")
    @PostMapping(value = "/save",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> saveWorkPlan(@RequestBody WorkOrderPlanHandleParam param) {
        return CudResult.success(workPlanDetailService.saveWorkPlan(param));
    }

    /**
     * 提交工单抄表计划
     */
    @ApiOperation(value = "提交工单抄表计划")
    @PostMapping(value = "/submit",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> submitWorkPlan(@RequestBody WorkOrderPlanHandleParam param) {
        return CudResult.success(workPlanDetailService.submitWorkPlan(param));
    }
}
