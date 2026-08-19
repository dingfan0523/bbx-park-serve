
package com.cgnpc.bbxpark.workorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单关联设备数据操作接口
 */
@Mapper
public interface WorkOrderDeviceRepository extends BaseMapper<WorkOrderDevice> {

}
