package com.cgnpc.bbxpark.config.eventbus;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会服删除事件
 */
@Data
public class MeetingServeDelEvent implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会服id.")
    private Long serviceId;
}
