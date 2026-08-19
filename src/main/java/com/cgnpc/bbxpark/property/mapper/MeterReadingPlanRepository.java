
package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.MaintainPlan;
import com.cgnpc.bbxpark.property.domain.MeterReadingPlan;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 抄表计划管理数据操作接口
 * @author huangyongtao
 * @date 2025/3/25 16:23
 */
@Repository
public interface MeterReadingPlanRepository extends BaseMapper<MeterReadingPlan> {

    /**
     * 纯手写SQL，绕过所有数据权限拦截器
     */
    @Select("SELECT * FROM bbx_meter_reading_plan WHERE deleted = 1 AND status = 1 ")
    List<MeterReadingPlan> selectListBySql();
}
