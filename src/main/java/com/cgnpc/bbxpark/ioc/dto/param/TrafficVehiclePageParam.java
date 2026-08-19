package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆列表分页查询参数
 */
@Data
public class TrafficVehiclePageParam extends CudPageDto implements Serializable {

    /**
     * 车牌号（模糊查询）
     */
    private String licensePlate;

    /**
     * 车型划分
     */
    private String vehicleType;

    /**
     * 车辆使用状态
     */
    private String useStatus;

    /**
     * 车辆运行状态
     */
    private String runStatus;
}