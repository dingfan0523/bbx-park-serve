
package com.cgnpc.bbxpark.complaint.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ComplaintSuggestionRomanParam implements Serializable {
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

    @Length(max = 255)
    @ApiModelProperty(value = "流转内容.")
    private String romanRemark;

    @Length(max = 50)
    @ApiModelProperty(value = "流转人工号.")
    private String romanStaffid;

    @ApiModelProperty(value = "流转时间.")
    private Date romanTime;

    @Length(max = 30)
    @ApiModelProperty(value = "流转类型;reply：回复；audit：审核.")
    private String romanType;

    @ApiModelProperty(value = "流转人id.")
    private String romanUid;

    @Length(max = 50)
    @ApiModelProperty(value = "流转人名称.")
    private String romanUname;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "主键id集合.")
    private List<Integer> ids;
}
