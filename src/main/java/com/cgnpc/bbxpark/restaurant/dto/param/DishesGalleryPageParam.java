
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
public class DishesGalleryPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3864157071670718126L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "菜品名称.")
    private String name;

    @ApiModelProperty(value = "图片url.")
    private String imageUrl;

    @ApiModelProperty(value = "单价.")
    private BigDecimal price;

    @ApiModelProperty(value = "克重.")
    private String weight;

    @ApiModelProperty(value = "辣度建议(0,1,2,3,4,5).")
    private Integer pungencyDegree;

    @ApiModelProperty(value = "原料信息.")
    private String information;

    @ApiModelProperty(value = "满意度.")
    private Long satisfaction;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
