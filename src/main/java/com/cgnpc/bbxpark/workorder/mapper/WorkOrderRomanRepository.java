
package com.cgnpc.bbxpark.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderRoman;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单流转数据操作接口
 * @author lhy
 * @date 2024/08/26 09:53:42
 */
@Mapper
public interface WorkOrderRomanRepository extends BaseMapper<WorkOrderRoman> {


}
