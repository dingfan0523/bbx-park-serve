package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.InventoryPlan;
import com.cgnpc.bbxpark.property.domain.MaintainPlan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/***
 * @Description 维保计划管理数据操作接口
 * @author huangyongtao
 * @date 2025/10/16 15:12
 */
public interface MaintainPlanRepository extends BaseMapper<MaintainPlan> {

    /**
     * 纯手写SQL，绕过所有数据权限拦截器
     */
    @Select("SELECT * FROM bbx_maintain_plan WHERE deleted = 1 AND status = 1 ")
    List<MaintainPlan> selectListBySql();
}
