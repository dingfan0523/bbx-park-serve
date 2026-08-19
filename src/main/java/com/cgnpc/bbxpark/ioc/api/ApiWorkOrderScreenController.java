package com.cgnpc.bbxpark.ioc.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.device.service.IIocDeviceCountService;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.WorkOrderPageSimpleParam;
import com.cgnpc.bbxpark.ioc.dto.param.WorkUnsatisfiedPageParam;
import com.cgnpc.bbxpark.ioc.service.IWorkOrderScreenService;
import com.cgnpc.bbxpark.workorder.dto.model.WorkEvaluateModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderCountModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 大屏工单统计
 */
@RestController
@RequestMapping("/api/dtwin/workOrder")
@Api(tags= "大屏-工单统计")
public class ApiWorkOrderScreenController {
    @Autowired
    private IWorkOrderScreenService workOrderScreenService;
    @Autowired
    private IIocDeviceCountService iocDeviceCountService;

    /**
     * 工单主服务接口.
     */
    @Autowired
    private IWorkOrderService workOrderService;

    @GetMapping(value = "/getKeyMetrics")
    @ApiOperation(value = "核心指标")
    public CudResult<WorkOrderKeyMetricsModel> getWorkOrderKeyMetrics() {
        return CudResult.success(workOrderScreenService.getWorkOrderKeyMetrics());
    }

    @GetMapping(value = "/getSourceAnalysis")
    @ApiOperation(value = "工单来源分析")
    public CudResult<List<WorkOrderSourceAnalysisModel>> getWorkOrderSourceAnalysis() {
        return CudResult.success(workOrderScreenService.getWorkOrderSourceAnalysis());
    }

    @GetMapping(value = "/getDepartmentAnalysis")
    @ApiOperation(value = "责任部门分析")
    public CudResult<List<WorkOrderDepartmentAnalysisModel>> getWorkOrderDepartmentAnalysis(@ApiParam(value = "排行类型（1：高； 2：低）") @RequestParam(required = true) Long type) {
        return CudResult.success(workOrderScreenService.getWorkOrderDepartmentAnalysis(type));
    }

    @GetMapping(value = "/getSpaceAnalysis")
    @ApiOperation(value = "计划外工单分布区域分析")
    public CudResult<List<WorkOrderScreenSpaceAnalysisModel>> getWorkOrderSpaceAnalysis(@ApiParam(value = "空间id") @RequestParam(required = false) Long id) {
        return CudResult.success(workOrderScreenService.getWorkOrderSpaceAnalysis(id));
    }

    @GetMapping(value = "/getHandleDateTrend")
    @ApiOperation(value = "工单处理趋势(近30天)")
    public CudResult<List<WorkOrderHandleDateTrendModel>> getWorkOrderHandleDateTrend() {
        return CudResult.success(workOrderScreenService.getWorkOrderHandleDateTrend());
    }

    @GetMapping(value = "/getUnsatisfiedTrace")
    @ApiOperation(value = "不满意工单溯源")
    public CudResult<WorkOrderScreenUnsatisfiedTraceModel> getWorkOrderUnsatisfiedTrace() {
        return CudResult.success(workOrderScreenService.getWorkOrderUnsatisfiedTrace());
    }

    @GetMapping(value = "/getContractEffect")
    @ApiOperation(value = "合同计划工单成效分析")
    public CudResult<List<WorkOrderContractEffectModel>> getWorkOrderContractEffect() {
        return CudResult.success(workOrderScreenService.getWorkOrderContractEffect());
    }

    @GetMapping(value = "/getSpaceCount")
    @ApiOperation(value = "工单空间统计")
    public CudResult<List<WorkOrderSpaceCountModel>> getWorkOrderSpaceCount(@ApiParam(value = "所属楼层物模型编码") @RequestParam(required = true) String sslcCode) {
        return CudResult.success(workOrderScreenService.getWorkOrderSpaceCount(sslcCode));
    }

    @GetMapping(value = "/getSpaceView")
    @ApiOperation(value = "工单空间楼层高亮展示列表")
    public CudResult<List<SpaceViewModel>> getWorkOrderSpaceView(@ApiParam(value = "所属楼层物模型编码") @RequestParam(required = false) String sslcCode) {
        return CudResult.success(workOrderScreenService.getWorkOrderSpaceView(sslcCode));
    }

    /**
     * 获取工单主列表(分页).
     */
    @ApiOperation(value = "获取工单主列表(分页)")
    @PostMapping(value = "/page")
    public CudResult<IPage<WorkOrderSimpleModel>> page(@RequestBody WorkOrderPageSimpleParam param) {
        return CudResult.success(workOrderScreenService.page(param));
    }

    /**
     * 获取不满意工单列表(分页).
     */
    @ApiOperation(value = "获取不满意工单列表(分页)")
    @PostMapping(value = "/pageUnsatisfied")
    public CudResult<IPage<WorkEvaluateModel>> pageUnsatisfied(@RequestBody WorkUnsatisfiedPageParam param) {
        return CudResult.success(workOrderScreenService.pageUnsatisfied(param));
    }

    @ApiOperation(value = "获取工单详情")
    @GetMapping(value = "/detail")
    public CudResult<WorkOrderModel> detail(@RequestParam Long id) {
        return CudResult.success(workOrderService.detail(id));
    }

    /**
     * 工单信息统计
     */
    @ApiOperation(value = "工单信息统计")
    @GetMapping(value = "/count")
    public CudResult<WorkOrderCountModel> countWorkOrder(@ApiParam("设备id") @RequestParam("deviceId") Long deviceId) {
        WorkOrderPageParam param = new WorkOrderPageParam();
        param.setDeviceId(deviceId);
        return CudResult.success(iocDeviceCountService.countWorkOrder(param));
    }
}
