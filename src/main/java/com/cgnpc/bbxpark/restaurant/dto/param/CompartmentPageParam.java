
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class CompartmentPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3878573008682883217L;

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

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
