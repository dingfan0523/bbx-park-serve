
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class ParkSpacePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4470220031023859787L;

    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;
    @ApiModelProperty(value = "所属空间ID.")
    private Long parentSpaceId;
    @ApiModelProperty(value = "所属园区ID-租户号.")
    private Long tenantId;
}
