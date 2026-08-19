package com.cgnpc.bbxpark.energy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/22
 * @desc 能量异常提醒实体类
 */
@Data
@TableName("bbx_energy_abnormal_remind")
public class EnergyAbnormalRemind extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 提醒标题 */
    private String remindName;
    /** 提醒内容 */
    private String remindContent;
    /** 报单状态（0未报单1已报单） */
    private Integer status;
    /** 报单业务id */
    private Long businessId;
    /** 设备id */
    private String deviceId;
    /** 设备名称 */
    private String deviceName;
    /** 空间id */
    private Long spaceId;
    /** 空间名称 */
    private String spaceName;
    /** 乐观锁 */
    private Integer revision;
    /** 提醒类型;（water：水表；electricity：电表；gas：燃气表） */
    private String remindType;

}
