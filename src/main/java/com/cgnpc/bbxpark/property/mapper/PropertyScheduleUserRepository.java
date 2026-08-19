
package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.PropertyScheduleUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物业排班人员数据操作接口
 */
@Mapper
public interface PropertyScheduleUserRepository extends BaseMapper<PropertyScheduleUser> {
}
