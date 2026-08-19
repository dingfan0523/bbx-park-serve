
package com.cgnpc.bbxpark.workorder.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkScheduleUserModel;
import com.cgnpc.bbxpark.workorder.dto.param.*;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import com.cgnpc.bbxpark.workorder.service.IWorkScheduleUserService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;


/**
 * 工单主服务控制类(移动端)
 */
@RestController
@RequestMapping("/api/workOrder")
@Api(tags= "智慧物业-移动端-工单服务接口")
public class ApiWorkOrderController {


    /**
     * 工单主服务接口.
     */
    @Autowired
    private IWorkOrderService workOrderService;


    @Autowired
    private IWorkScheduleUserService workScheduleUserService;

    /**
     * 获取工单主信息.
     */
    @ApiOperation(value = "获取工单详情")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<WorkOrderModel> detail(@PathVariable @NotNull(message = "工单标签标识不能为空") Long id) {
        return CudResult.success(workOrderService.detail(id));
    }

    /**
     * 获取工单主列表(分页).
     */
    @ApiOperation(value = "获取工单主列表(分页)")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<WorkOrderModel>> page(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(workOrderService.page(param));
    }

    /**
     * 分配
     */
    @ApiOperation(value = "分配")
    @PostMapping(value = "/allot")
    @RequiredToken
    public CudResult<Boolean> allot(@Validated({Default.class})  @RequestBody WorkOrderAllotParam param) {
        return CudResult.success(workOrderService.allot(param));
    }

    /**
     * 抢单
     */
    @ApiOperation(value = "抢单")
    @PostMapping(value = "/grab")
    @RequiredToken
    public CudResult<Boolean> grab(@Validated({Default.class})  @RequestBody WorkOrderHandleParam param) {
        return CudResult.success(workOrderService.grab(param));
    }

    /**
     * 转派处理
     */
    @ApiOperation(value = "转派处理")
    @PostMapping(value = "/transferHandle")
    @RequiredToken
    public CudResult<Boolean> transferHandle(@Validated({Default.class})  @RequestBody WorkOrderAllotParam param) {
        return CudResult.success(workOrderService.transferHandle(param));
    }

    /**
     * 转派审核
     */
    @ApiOperation(value = "转派审核")
    @PostMapping(value = "/transferAudit")
    @RequiredToken
    public CudResult<Boolean> transferAudit(@Validated({Default.class})  @RequestBody WorkOrderAllotParam param) {
        return CudResult.success(workOrderService.transferAudit(param));
    }


    /**
     * 工单处理.
     */
    @ApiOperation(value = "工单处理")
    @PostMapping(value = "/handle")
    @RequiredToken
    public CudResult<Boolean> handle(@RequestBody WorkOrderHandleParam param) {
        return CudResult.success(workOrderService.handle(param));
    }

    /**
     * 审核.
     */
    @ApiOperation(value = "审核")
    @PostMapping(value = "/audit")
    @RequiredToken
    public CudResult<Boolean> audit(@Validated({Default.class}) @RequestBody WorkOrderAuditParam param) {
        return CudResult.success(workOrderService.audit(param));
    }

    /**
     * 工单接收
     */
    @ApiOperation(value = "工单接收")
    @PostMapping(value = "/receive")
    @RequiredToken
    public CudResult<Boolean> receive(@Validated({Default.class})  @RequestBody WorkOrderHandleParam param) {
        return CudResult.success(workOrderService.receive(param));
    }

    @ApiOperation(value = "工单评价")
    @PostMapping(value = "/evaluation")
    @RequiredToken
    public CudResult<Boolean> evaluation(@RequestBody WorkOrderEvaluationParam param) {
        return CudResult.success(workOrderService.evaluation(param));
    }

    @ApiOperation(value = "通过业务id和类型查询工单id")
    @PostMapping(value = "/queryByBusinessIdAndType")
    @RequiredToken
    public CudResult<Long> queryByBusinessIdAndType(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(workOrderService.queryByBusinessIdAndType(param));
    }

    @ApiOperation(value = "获取工单分组人员列表")
    @GetMapping(value = "/findScheduleUser/{id}")
    @RequiredToken
    public CudResult<List<WorkScheduleUserModel>> findScheduleUser(@PathVariable @NotNull(message = "工单标识不能为空") Long id) {
        return CudResult.success(workScheduleUserService.list(id));
    }

    /**
     * 查询工单超时状态.
     */
    @ApiOperation(value = "查询工单超时状态")
    @GetMapping(value = "/outStatus/{id}")
    @RequiredToken
    public CudResult<Integer> findOutStatus(@PathVariable @NotNull(message = "工单标识不能为空") Long id) {
        return CudResult.success(workOrderService.findOutStatus(id));
    }

}
