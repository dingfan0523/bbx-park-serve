package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.InspectionPlan;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 巡检计划管理数据操作接口
 */
public interface InspectionPlanRepository extends BaseMapper<InspectionPlan> {


    /**
     * 纯手写SQL，绕过所有数据权限拦截器
     */
    @Select("SELECT * FROM bbx_inspection_plan WHERE deleted = 1 AND status = 1 ")
    List<InspectionPlan> selectListBySql();
}