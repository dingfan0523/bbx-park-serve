
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SpaceBasicInfoModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3238461757583605392L;

    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "上级空间名称.")
    private String parentSpaceName;
    @ApiModelProperty(value = "空间编码.")
    private String spaceCode;
    @ApiModelProperty(value = "空间名称.")
    private String spaceName;
    @ApiModelProperty(value = "排序序号.")
    private Integer orderCode;
    @ApiModelProperty(value = "空间类型.")
    private Integer type;
    @ApiModelProperty(value = "使用单位id.")
    private String departmentId;
    @ApiModelProperty(value = "使用单位名称.")
    private String departmentName;
    @ApiModelProperty(value = "面积（㎡）.")
    private BigDecimal area;
    @ApiModelProperty(value = "空间容量(人).")
    private Integer capacity;
    @ApiModelProperty(value = "空间用途.")
    private String purpose;
    @ApiModelProperty(value = "工位管理员id.")
    private String managerId;
    @ApiModelProperty(value = "工位管理员名称.")
    private String managerName;
    @ApiModelProperty(value = "工位管理员工号.")
    private String managerStaffid;
}
