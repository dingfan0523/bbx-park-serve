
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 物业排班业务数据模型
 */
@Data
public class PropertyScheduleListModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @ApiModelProperty(value = "分组名称.")
    private String name;

    @ApiModelProperty(value = "分组描述.")
    private String remark;

    @ApiModelProperty(value = "人员数量")
    private Long userCount;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建人工号")
    private String creatorId;

    @ApiModelProperty(value = "创建人姓名.")
    private String createBy;
}
