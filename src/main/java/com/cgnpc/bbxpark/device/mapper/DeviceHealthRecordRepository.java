package com.cgnpc.bbxpark.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.DeviceHealthRecord;
import com.cgnpc.bbxpark.ioc.dto.model.DeviceHealthTrendModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceHealthRecordRepository extends BaseMapper<DeviceHealthRecord> {
    List<DeviceHealthTrendModel> findDeviceHealthRecord(@Param("tenantId")Long tenantId, @Param("sslcCode")String sslcCode);
}
