package com.cgnpc.bbxpark.energy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路设备实体类
 */
@Data
@TableName("bbx_branch_device")
public class BranchDevice extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 设备id */
    private Long deviceId;
    /** 设备名称 */
    private String deviceName;
    /** 空间位置名称 */
    private String spaceName;
    /** 空间位置id */
    private Long spaceId;
    /** 支路的主键id */
    private Long branchId;
    /** 乐观锁 */
    private Integer revision;
}
