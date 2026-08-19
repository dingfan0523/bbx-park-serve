package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.InspectionPlan;
import com.cgnpc.bbxpark.property.domain.InventoryPlan;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 盘点计划管理数据操作接口
 */
public interface InventoryPlanRepository extends BaseMapper<InventoryPlan> {


    /**
     * 纯手写SQL，绕过所有数据权限拦截器
     */
    @Select("SELECT * FROM bbx_inventory_plan WHERE deleted = 1 AND status = 1 ")
    List<InventoryPlan> selectListBySql();
}
