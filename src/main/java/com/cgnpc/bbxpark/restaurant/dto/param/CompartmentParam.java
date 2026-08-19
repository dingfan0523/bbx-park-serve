
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class CompartmentParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3310275176281652034L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "所属餐厅id.")
    private Long restaurantId;

    @Length(max = 64)
    @ApiModelProperty(value = "包间名称.")
    private String name;

    @Length(max = 128)
    @ApiModelProperty(value = "包间图片.")
    private String imageUrl;

    @ApiModelProperty(value = "包间位置id.")
    private Long spaceId;

    @Length(max = 128)
    @ApiModelProperty(value = "位置名称(冗余字段).")
    private String spaceName;

    @ApiModelProperty(value = "容纳人数.")
    private Integer people;

    @ApiModelProperty(value = "面积.")
    private Double area;

    @Length(max = 255)
    @ApiModelProperty(value = "包间介绍.")
    private String introduce;

    @ApiModelProperty(value = "状态(1->启用;0->禁用).")
    private Integer status;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted;


    @ApiModelProperty(value = "营业时间.")
    private List<CompartmentTimeParam> compartmentTimeParams;

    @ApiModelProperty(value = "包间套餐关联入参数.")
    private List<CompartmentComboParam> compartmentComboParams;

    @ApiModelProperty(value = "包间设施关联入参.")
    private List<CompartmentDeviceParam> compartmentDeviceParams;

}
