package com.cgnpc.bbxpark.ioc.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.WorkOrderPageSimpleParam;
import com.cgnpc.bbxpark.ioc.dto.param.WorkUnsatisfiedPageParam;
import com.cgnpc.bbxpark.workorder.dto.model.WorkEvaluateModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;

import java.util.List;

/**
 * 大屏工单统计接口
 */
public interface IWorkOrderScreenService {

    /**
     * 核心指标
     * @return
     */
    WorkOrderKeyMetricsModel getWorkOrderKeyMetrics();

    /**
     * 工单来源分析
     * @return
     */
    List<WorkOrderSourceAnalysisModel> getWorkOrderSourceAnalysis();

    /**
     * 责任部门分析
     * @return
     */
    List<WorkOrderDepartmentAnalysisModel> getWorkOrderDepartmentAnalysis(Long type);

    /**
     * 计划外工单分布区域分析
     * @return
     */
    List<WorkOrderScreenSpaceAnalysisModel> getWorkOrderSpaceAnalysis(Long id);

    /**
     * 工单处理趋势
     * @return
     */
    List<WorkOrderHandleDateTrendModel> getWorkOrderHandleDateTrend();


    /**
     * 不满意工单溯源
     * @return
     */
    WorkOrderScreenUnsatisfiedTraceModel getWorkOrderUnsatisfiedTrace();


    /**
     * 合同计划工单成效分析
     * @return
     */
    List<WorkOrderContractEffectModel> getWorkOrderContractEffect();

    /**
     * 合同计划工单成效分析
     * @return
     */
    List<WorkOrderSpaceCountModel> getWorkOrderSpaceCount(String sslcCode);

    /**
     * 工单空间楼层高亮展示列表
     * @return
     */
    List<SpaceViewModel> getWorkOrderSpaceView(String sslcCode);

    /**
     * 获取工单主列表(分页).
     * @Param param 工单主查询条件
     * @Return 工单主信息列表（分页）
     */
    IPage<WorkOrderSimpleModel> page(WorkOrderPageSimpleParam param);

    /**
     * 获取不满意工单列表(分页).
     * @Return 工单主信息列表（分页）
     */
    IPage<WorkEvaluateModel> pageUnsatisfied(WorkUnsatisfiedPageParam param);


    /**
     * 大屏-工单的完成率
     * @return
     */
    Double getWorkCompleteRate();

    /**
     * 大屏-获取报事报修工单的设备集合
     * @return
     */
    List<Long> getProblemDeviceIds();
}
