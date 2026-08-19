
package com.cgnpc.bbxpark.restaurant.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class RestaurantTimePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3293102421766891830L;

    private Long id;

    @ApiModelProperty(value = "餐厅id.")
    private Long restaurantId;

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

}
