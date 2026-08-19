package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备场景执行记录
 */
@Data
@TableName("bbx_device_screen_record")
public class DeviceScreenRecord implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 类型:1->人来灯开;2->人走灯关
     */
    private Integer type;
    /**
     * 记录时间
     */
    private Date recordTime;
    private Long tenantId;
}
