
package com.cgnpc.bbxpark.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.WorkUnsatisfiedPageParam;
import com.cgnpc.bbxpark.workorder.domain.WorkOrder;
import com.cgnpc.bbxpark.workorder.dto.model.WorkEvaluateModel;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderCountModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkOrderPageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单主数据操作接口
 */
@Mapper
public interface WorkOrderRepository extends BaseMapper<WorkOrder> {

    IPage<WorkOrder> selectPageByParam(@Param("page")IPage<WorkOrder> page,@Param("param") WorkOrderPageParam param);

    //获取指定处理人平均耗时
    Double getAvgExpendTimeByProcessedPersonId(@Param("processedPersonId") String processedPersonId, @Param("completedStatus") Integer completedStatus);

    WorkOrderCountModel countWorkOrder(@Param("condition") WorkOrderPageParam param);

    /**
     * 工单大屏-核心指标
     * @param tenantId
     * @return
     */
    WorkOrderKeyMetricsModel getWorkOrderKeyMetrics(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-工单来源分析
     * @param tenantId
     * @return
     */
    List<WorkOrderSourceAnalysisModel> getWorkOrderSourceAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-责任部门分析
     * @param tenantId
     * @return
     */
    List<WorkOrderDepartmentAnalysisModel> getWorkOrderDepartmentAnalysis(@Param("tenantId") Long tenantId, @Param("type") Long type);

    /**
     * 工单大屏-合同计划工单成效分析
     * @param tenantId
     * @return
     */
    List<WorkOrderContractEffectModel> getWorkOrderContractEffect(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-工单处理趋势30天（工单完成趋势）
     * @param tenantId
     * @return
     */
    List<WorkOrderHandleDateTrendModel> getCompleteTrend(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-工单处理趋势30天（工单评价趋势）
     * @param tenantId
     * @return
     */
    List<WorkOrderHandleDateTrendModel> getEvaluateTrend(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-不满意工单溯源
     * @param tenantId
     * @return
     */
    List<WorkOrderUnsatisfiedModel> getWorkOrderUnsatisfied(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-不满意工单的评价列表（分页）
     * @param page
     * @param param
     * @return
     */
    IPage<WorkEvaluateModel> pageEvaluate(@Param("page")IPage<WorkEvaluateModel> page, @Param("param") WorkUnsatisfiedPageParam param);

    /**
     * 工单大屏-计划外工单分布区域分析
     * @param tenantId
     * @return
     */
    List<WorkOrderScreenSpaceAnalysisModel> getWorkOrderSpaceAnalysis(@Param("tenantId") Long tenantId);

    /**
     * 工单大屏-获取工单的空间集合
     * @param tenantId
     * @return
     */
    List<Long> getSpaceIdList(@Param("tenantId") Long tenantId);

    /**
     * 材料大屏-获取工单使用材料统计
     * @param tenantId
     * @return
     */
    List<MaterialWorkUseModel> getMaterialWorkUse(@Param("tenantId") Long tenantId);

    /**
     * 大屏-工单的完成率
     * @param tenantId
     * @return
     */
    Double getWorkCompleteRate(@Param("tenantId") Long tenantId);

    /**
     * 大屏-获取报事报修工单的设备集合
     * @param tenantId
     * @return
     */
    List<Long> getProblemDeviceIds(@Param("tenantId") Long tenantId);

    /**
     * 查询会议室每日健康度-工单数量
     * @return 数据
     */
    List<WeekRoomHealth> getMeetingRoomHealth(@Param("tenantId")Long tenantId,@Param("types")List<String> types,@Param("deviceIds")List<Long> deviceIds,@Param("spaceIds")List<Long> spaceIds);
}
