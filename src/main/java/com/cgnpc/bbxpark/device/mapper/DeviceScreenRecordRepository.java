package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.DeviceScreenRecord;
import com.cgnpc.bbxpark.ioc.dto.model.ExecutionTrend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceScreenRecordRepository extends BaseMapper<DeviceScreenRecord> {
    /**
     * 获取30天内每日场景执行次数
     * @return 数据
     */
    List<ExecutionTrend> getExecutionTrend(@Param("tenantId")Long tenantId);
}
