package com.cgnpc.bbxpark.workorder.controller;

import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderStatisticsModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderStatisticsParam;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @create zhaoshuo
 * @time 2025/4/15
 * @desc 物业工单统计
 */
@RestController
@RequestMapping(Constant.BASE_PATH + "/workOrderStatistics")
@Api(tags= "智慧物业-PC端-物业工单统计")
public class WorkOrderStatisticsController {
    @Autowired
    private IWorkOrderStatisticsService workOrderStatisticsService;


    @PostMapping(value = "/getWorkOrderStatistics")
    @ApiOperation(value = "获取时间范围内工单统计")
    public CudResult<WorkOrderStatisticsModel> getWorkOrderStatistics(@RequestBody WorkOrderStatisticsParam param) {
        return CudResult.success(workOrderStatisticsService.getWorkOrderStatistics(param));
    }

    @GetMapping(value = "/currentYearStatistics")
    @ApiOperation(value = "统计当年12月份工单数据")
    public CudResult<Map<Integer, Map<String, Integer>>> currentYearStatistics() {
        return CudResult.success(workOrderStatisticsService.currentYearStatistics());
    }

    @ApiOperation(value = "工单来源趋势导出")
    @GetMapping(value = "/statisticsExport")
    public void statisticsExport(HttpServletResponse response) {
        workOrderStatisticsService.statisticsExport(response);
    }
}
