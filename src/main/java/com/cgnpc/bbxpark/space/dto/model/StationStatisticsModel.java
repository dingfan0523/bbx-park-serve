
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class StationStatisticsModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4557652001901646177L;

    @ApiModelProperty(value = "已分配数量")
    private Long allocatedCount;
    @ApiModelProperty(value = "空闲数量.")
    private Long idleCount;
}
