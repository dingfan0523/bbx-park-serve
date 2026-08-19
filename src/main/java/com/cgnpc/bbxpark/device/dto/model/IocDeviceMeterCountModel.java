package com.cgnpc.bbxpark.device.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description ioc设备抄表统计数据模型
 * @author huangyongtao
 * @date 2025/2/24 17:07
 */
@Data
public class IocDeviceMeterCountModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "电表数量")
    private Long electricityNum = 0L;

    @ApiModelProperty(value = "电表智能数量")
    private Long electricityAutoNum = 0L;;

    @ApiModelProperty(value = "电表非智能数量")
    private Long electricityPersonNum = 0L;;

    @ApiModelProperty(value = "水表数量")
    private Long waterNum = 0L;;

    @ApiModelProperty(value = "水表智能数量")
    private Long waterAutoNum = 0L;;

    @ApiModelProperty(value = "电表非智能数量")
    private Long waterPersonNum = 0L;;

    @ApiModelProperty(value = "燃气数量")
    private Long gasNum = 0L;;

    @ApiModelProperty(value = "燃气智能数量")
    private Long gasAutoNum = 0L;;

    @ApiModelProperty(value = "燃气非智能数量")
    private Long gasPersonNum = 0L;;


}
