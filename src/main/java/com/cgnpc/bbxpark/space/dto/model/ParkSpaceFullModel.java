
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ParkSpaceFullModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4115487799614544671L;

    @ApiModelProperty(value = "空间ID.")
    private Long id;

    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;

    @ApiModelProperty(value = "空间名称.")
    private String spaceName;

    @ApiModelProperty(value = "所属空间ID.")
    private Long parentSpaceId;

    @ApiModelProperty(value = "全路径")
    private String fullPath;

    @ApiModelProperty(value = "id路径")
    private String idFullPath;
}
