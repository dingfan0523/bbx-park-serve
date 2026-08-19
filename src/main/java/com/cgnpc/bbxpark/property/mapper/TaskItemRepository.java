package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.property.domain.TaskItem;
import org.springframework.stereotype.Repository;


/**
 * 任务明细数据操作接口
 */
@Repository
public interface TaskItemRepository extends BaseMapper<TaskItem> {

}
