package com.cgnpc.bbxpark.config.eventbus;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 邀约结束事件
 */
@Data
public class InviteEndEvent implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "邀约id.")
    private List<Long> inviteIdList;
}
