package com.cgnpc.bbxpark.workorder.service;


import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderStatisticsModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderStatisticsParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @create zhaoshuo
 * @time 2025/4/15
 * @desc 物业工单统计接口
 */
public interface IWorkOrderStatisticsService {
    /**
     * 获取工单统计信息
     * @param param
     * @return
     */
    WorkOrderStatisticsModel getWorkOrderStatistics(WorkOrderStatisticsParam param);

    /**
     *  获取当前年份工单统计信息
     * @return
     */
    Map<Integer, Map<String, Integer>> currentYearStatistics();

    /**
     *  工单来源趋势导出
     * @param response
     */
    void statisticsExport(HttpServletResponse response);
}
