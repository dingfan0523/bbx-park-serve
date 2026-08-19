package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class InventoryItemPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4338951579928948128L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "任务id.")
    private Long taskId;

    @ApiModelProperty(value = "类型.")
    private Integer type;

    @ApiModelProperty(value = "相关联id(多个以英文逗号隔开).")
    private String relatedId;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
