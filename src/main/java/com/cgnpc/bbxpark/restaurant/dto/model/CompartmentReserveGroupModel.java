
package com.cgnpc.bbxpark.restaurant.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 包间预定分组业务数据模型
 * @author huangyongtao
 * @date 2024/7/31 16:03
 */
@Data
public class CompartmentReserveGroupModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "包间名称.")
    private String name;

    @ApiModelProperty(value = "包间图片.")
    private String imageUrl;

    @ApiModelProperty(value = "容纳人数.")
    private Integer people;

    @ApiModelProperty(value = "面积.")
    private Double area;

    @ApiModelProperty(value = "满意度")
    private Double satisfaction;

    @ApiModelProperty(value = "包间介绍.")
    private String introduce;

    @ApiModelProperty(value = "空间id")
    private Long spaceId;

    @ApiModelProperty(value = "是否有营业时间")
    private Boolean hasTime = true;

    @ApiModelProperty(value = "包间预约信息集合")
    private List<CompartmentReserveModel> CompartmentReserveModels;

}
