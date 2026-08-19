package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡更点分页参数模型
 */
@Data
public class PatrolPointPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4218484514853099160L;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "编码.")
    private String code;

    @ApiModelProperty(value = "类型（10：安保；20：保洁；30：消控；40：环境；50：设备）.")
    private Integer type;

    @ApiModelProperty(value = "方式（10：拍照；20：其他）.")
    private Integer way;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "重点检查(0->否;1->是).")
    private Integer keyPoint;

    @ApiModelProperty(value = "启用状态;0->否;1->是.")
    private Integer status;
}
