package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.MeterReadingPlan;
import com.cgnpc.bbxpark.property.domain.PatrolPlan;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 巡更计划管理数据操作接口
 */
@Repository
public interface PatrolPlanRepository extends BaseMapper<PatrolPlan> {


    /**
     * 纯手写SQL，绕过所有数据权限拦截器
     */
    @Select("SELECT * FROM bbx_patrol_plan WHERE deleted = 1 AND status = 1 ")
    List<PatrolPlan> selectListBySql();
}