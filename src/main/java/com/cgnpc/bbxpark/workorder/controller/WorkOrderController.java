
package com.cgnpc.bbxpark.workorder.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
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
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * 工单主服务控制类
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/workOrder")
@Api(tags= "智慧物业-PC端-工单服务接口")
public class WorkOrderController {

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
    public CudResult<WorkOrderModel> detail(@PathVariable @NotNull(message = "工单标签标识不能为空") Long id) {
        return CudResult.success(workOrderService.detail(id));
    }

    /**
     * 获取工单主列表(分页).
     */
    @ApiOperation(value = "获取工单主列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<WorkOrderModel>> page(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(workOrderService.page(param));
    }

    /**
     * 分配
     */
    @ApiOperation(value = "分配")
    @PostMapping(value = "/allot")
    public CudResult<Boolean> allot(@Validated({Default.class})  @RequestBody WorkOrderAllotParam param) {
        return CudResult.success(workOrderService.allot(param));
    }

    /**
     * 抢单
     */
    @ApiOperation(value = "抢单")
    @PostMapping(value = "/grab")
    public CudResult<Boolean> grab(@Validated({Default.class})  @RequestBody WorkOrderHandleParam param) {
        return CudResult.success(workOrderService.grab(param));
    }

    /**
     * 转派处理
     */
    @ApiOperation(value = "转派处理")
    @PostMapping(value = "/transferHandle")
    public CudResult<Boolean> transferHandle(@Validated({Default.class})  @RequestBody WorkOrderAllotParam param) {
        return CudResult.success(workOrderService.transferHandle(param));
    }

    /**
     * 转派审核
     */
    @ApiOperation(value = "转派审核")
    @PostMapping(value = "/transferAudit")
    public CudResult<Boolean> transferAudit(@Validated({Default.class})  @RequestBody WorkOrderAllotParam param) {
        return CudResult.success(workOrderService.transferAudit(param));
    }


//    /**
//     * 关闭.
//     */
//    @ApiOperation(value = "关闭")
//    @PostMapping(value = "/close")
//    public CudResult<Boolean> close(@RequestBody WorkOrderCloseParam param) {
//        return CudResult.success(workOrderService.close(param));
//    }


    /**
     * 工单处理.
     */
    @ApiOperation(value = "工单处理")
    @PostMapping(value = "/handle")
    public CudResult<Boolean> handle(@RequestBody WorkOrderHandleParam param) {
        return CudResult.success(workOrderService.handle(param));
    }

    /**
     * 审核.
     */
    @ApiOperation(value = "审核")
    @PostMapping(value = "/audit")
    public CudResult<Boolean> audit(@Validated({Default.class}) @RequestBody WorkOrderAuditParam param) {
        return CudResult.success(workOrderService.audit(param));
    }

    /**
     * 删除工单.
     */
    @ApiOperation(value = "删除工单")
    @GetMapping(value = "/remove/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public CudResult<Boolean> remove(@PathVariable Long id) {
        return CudResult.success(workOrderService.remove(id));
    }


//    /**
//     * 工单退回.
//     */
//    @ApiOperation(value = "工单退回")
//    @PostMapping(value = "/fallback")
//    public CudResult<Boolean> fallback(@RequestBody WorkOrderBackParam param) {
//        return CudResult.success(workOrderService.fallback(param));
//    }


    /**
     * 获取处理工单主列表(分页).
     */
//    @ApiOperation(value = "获取处理工单主列表(分页)")
// 
//    @PostMapping(value = "/handle/page")
//    public CudResult<IPage<WorkOrderModel> handlePage(@RequestBody WorkOrderPageParam param) {
//        param.setUserId(SecurityFrameworkUtils.getLoginUserId());
//        return CudResult.success(workOrderService.handlePage(param));
//    }

    /**
     * 工单接收
     */
    @ApiOperation(value = "工单接收")
    @PostMapping(value = "/receive")
    public CudResult<Boolean> receive(@Validated({Default.class})  @RequestBody WorkOrderHandleParam param) {
        return CudResult.success(workOrderService.receive(param));
    }

    @ApiOperation(value = "工单评价")
    @PostMapping(value = "/evaluation")
    public CudResult<Boolean> evaluation(@RequestBody WorkOrderEvaluationParam param) {
        return CudResult.success(workOrderService.evaluation(param));
    }

    @ApiOperation(value = "工单导出")
    @GetMapping(value = "/downloadExcel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadExcel(@ModelAttribute WorkOrderPageParam param , HttpServletResponse response) {
        workOrderService.downloadExcel(param, response);
    }

    @ApiOperation(value = "通过业务id和类型查询工单id")
    @PostMapping(value = "/queryByBusinessIdAndType")
    public CudResult<Long> queryByBusinessIdAndType(@RequestBody WorkOrderPageParam param) {
        return CudResult.success(workOrderService.queryByBusinessIdAndType(param));
    }

    @ApiOperation(value = "获取工单分组人员列表")
    @GetMapping(value = "/findScheduleUser/{id}")
    public CudResult<List<WorkScheduleUserModel>> findScheduleUser(@PathVariable @NotNull(message = "工单标识不能为空") Long id) {
        return CudResult.success(workScheduleUserService.list(id));
    }

    /**
     * 查询工单超时状态.
     */
    @ApiOperation(value = "查询工单超时状态")
    @GetMapping(value = "/outStatus/{id}")
    public CudResult<Integer> findOutStatus(@PathVariable @NotNull(message = "工单标识不能为空") Long id) {
        return CudResult.success(workOrderService.findOutStatus(id));
    }

}
