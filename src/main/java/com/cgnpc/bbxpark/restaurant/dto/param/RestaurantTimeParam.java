
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class RestaurantTimeParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4024437158959452359L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

    @Length(max = 32)
    @ApiModelProperty(value = "用餐类型（字典）.")
    private String type;

    @ApiModelProperty(value = "开始时间.")
    private String startTime;

    @ApiModelProperty(value = "结束时间.")
    private String endTime;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private Integer revision;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private Long updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;
}
