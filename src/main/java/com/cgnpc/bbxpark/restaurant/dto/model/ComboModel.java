package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * ComboModel
 * </p>
 *
 * @author gujun
 * @time 2024-07-22
 */
@Data
public class ComboModel implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id ;
    @ApiModelProperty(value = "套餐名称")
    private String name ;
    @ApiModelProperty(value = "套餐图片")
    private String imageUrl ;
    @ApiModelProperty(value = "套餐类型(字典)")
    private String type ;
    @ApiModelProperty(value = "套餐价格")
    private String price ;
    @ApiModelProperty(value = "套餐描述")
    private String description ;
    @ApiModelProperty(value = "状态(1->上架;0->下架)")
    private Integer status ;

    @ApiModelProperty(value = "创建人id")
    private String creatorId ;
    @ApiModelProperty(value = "创建时间")
    private Date createTime ;

}
