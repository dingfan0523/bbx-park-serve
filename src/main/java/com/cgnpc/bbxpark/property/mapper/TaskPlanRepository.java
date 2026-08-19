package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.MeterReadingPlan;
import com.cgnpc.bbxpark.property.domain.TaskPlan;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务计划管理数据操作接口
 */
@Repository
public interface TaskPlanRepository extends BaseMapper<TaskPlan> {

    /**
     * 纯手写SQL，绕过所有数据权限拦截器
     */
    @Select("SELECT * FROM bbx_task_plan WHERE deleted = 1 AND status = 1 ")
    List<TaskPlan> selectListBySql();
}
