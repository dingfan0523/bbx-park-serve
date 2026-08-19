package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆里程分页查询参数
 */
@Data
public class TrafficMileagePageParam extends CudPageDto implements Serializable {

    /**
     * 车牌号（模糊查询）
     */
    private String licensePlate;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;
}