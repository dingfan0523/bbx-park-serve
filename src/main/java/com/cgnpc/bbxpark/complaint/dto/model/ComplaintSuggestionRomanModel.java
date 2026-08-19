
package com.cgnpc.bbxpark.complaint.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ComplaintSuggestionRomanModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "父id.")
    private Long pid;

    @ApiModelProperty(value = "投诉建议id.")
    private Long complaintSuggestionId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "流转内容.")
    private String romanRemark;

    @ApiModelProperty(value = "流转人工号.")
    private String romanStaffid;

    @ApiModelProperty(value = "流转时间.")
    private Date romanTime;

    @ApiModelProperty(value = "流转类型;reply：回复；audit：审核.")
    private String romanType;

    @ApiModelProperty(value = "流转人id.")
    private String romanUid;

    @ApiModelProperty(value = "流转人名称.")
    private String romanUname;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "审核人内容.")
    private String auditRemark;

    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "审核时间.")
    private Date auditTime;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @ApiModelProperty(value = "审核名称.")
    private String auditUname;


}
