
package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.DeviceOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备控制（操作）日志数据操作接口
 */
@Mapper
public interface DeviceOperationLogRepository extends BaseMapper<DeviceOperationLog> {

}
