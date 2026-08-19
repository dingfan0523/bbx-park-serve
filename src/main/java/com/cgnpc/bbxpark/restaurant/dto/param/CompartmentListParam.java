
package com.cgnpc.bbxpark.restaurant.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;



@Data
public class CompartmentListParam  implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3957841481111237327L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "所属餐厅id.")
    private Long restaurantId;

    @ApiModelProperty(value = "包间名称.")
    private String name;

    @ApiModelProperty(value = "包间图片.")
    private String imageUrl;

    @ApiModelProperty(value = "包间位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "位置名称(冗余字段).")
    private String spaceName;

    @ApiModelProperty(value = "容纳人数.")
    private Integer people;

    @ApiModelProperty(value = "面积.")
    private Double area;

    @ApiModelProperty(value = "包间介绍.")
    private String introduce;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted;

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
