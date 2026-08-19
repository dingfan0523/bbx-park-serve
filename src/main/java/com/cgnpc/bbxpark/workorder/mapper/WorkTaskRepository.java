
package com.cgnpc.bbxpark.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.InspectionFaultRankModel;
import com.cgnpc.bbxpark.ioc.dto.model.PatrolExecOverviewModel;
import com.cgnpc.bbxpark.ioc.dto.model.PatrolRouteTypeStatModel;
import com.cgnpc.bbxpark.workorder.domain.WorkTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 工单任务数据操作接口
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
@Repository
public interface WorkTaskRepository extends BaseMapper<WorkTask> {
    /**
     * 大屏-巡更执行概览(查询预估里程和预估时间)
     */
    PatrolExecOverviewModel getPatrolExecOverview(@Param("businessType")Integer businessType,@Param("tenantId")Long tenantId);

    /**
     * 大屏-巡更路线类型统计
     */
    List<PatrolRouteTypeStatModel> getPatrolRouteTypeStat(@Param("tenantId")Long tenantId);

    /**
     * 大屏-巡检点故障次数排序TOP10
     */
    List<InspectionFaultRankModel> getInspectionFaultTop10(@Param("tenantId")Long tenantId);
}
