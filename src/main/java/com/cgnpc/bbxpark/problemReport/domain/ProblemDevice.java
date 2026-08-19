package com.cgnpc.bbxpark.problemReport.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修设备表
 */
@Data
@TableName("bbx_problem_device")
public class ProblemDevice extends BaseExEntity implements Serializable {

    /**
     * 报事报修id
     */
    private Long problemId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备空间id
     */
    private Long spaceId;

    /**
     * 设备空间位置
     */
    private String spaceName;
}
