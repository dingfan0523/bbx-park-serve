
package com.cgnpc.bbxpark.space.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SpaceBasicInfoPageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3362107196305622138L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "空间类型.")
    private String type;

    @ApiModelProperty(value = "使用单位id.")
    private String departmentId;

    @ApiModelProperty(value = "面积（㎡）.")
    private Long area;

    @ApiModelProperty(value = "空间容量(人).")
    private Integer capacity;

    @ApiModelProperty(value = "工位管理员id.")
    private String managerId;

    @ApiModelProperty(value = "空间用途.")
    private String purpose;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    private Integer deleted = 0;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "乐观锁.")
    private Integer revision;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

}
