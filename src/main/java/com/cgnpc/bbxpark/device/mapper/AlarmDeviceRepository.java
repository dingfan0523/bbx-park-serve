package com.cgnpc.bbxpark.device.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.device.domain.AlarmDevice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmDeviceRepository extends BaseMapper<AlarmDevice> {
    /**
     * 批量插入告警设备信息
     * @param alarmDevices
     */
    void beachInsert(@Param("alarmDevices") List<AlarmDevice> alarmDevices);
}