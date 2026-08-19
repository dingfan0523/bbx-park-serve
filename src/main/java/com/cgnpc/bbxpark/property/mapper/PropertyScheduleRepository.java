
package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.PropertySchedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物业排班数据操作接口
 */
@Mapper
public interface PropertyScheduleRepository extends BaseMapper<PropertySchedule> {
}
