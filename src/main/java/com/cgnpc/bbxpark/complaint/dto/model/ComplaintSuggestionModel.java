
package com.cgnpc.bbxpark.complaint.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class ComplaintSuggestionModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "分配人工号.")
    private String assignmentStaffid;

    @ApiModelProperty(value = "分配时间.")
    private Date assignmentTime;

    @ApiModelProperty(value = "分配人id.")
    private Long assignmentUid;

    @ApiModelProperty(value = "分配人名称.")
    private String assignmentUname;

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

    @ApiModelProperty(value = "评价内容.")
    private String commentRemark;

    @ApiModelProperty(value = "评价分数.")
    private Long commentScore;

    @ApiModelProperty(value = "评价时间.")
    private Date commentTime;

    @ApiModelProperty(value = "完成时间.")
    private Date completeTime;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建人.")
    private String creatorId;

    @ApiModelProperty(value = "图片地址.")
    private String imageUrl;

    @ApiModelProperty(value = "回复时间.")
    private Date replyDate;

    @ApiModelProperty(value = "回复内容.")
    private String replyRemark;

    @ApiModelProperty(value = "回复人工号.")
    private String replyStaffid;

    @ApiModelProperty(value = "回复人id.")
    private Long replyUid;

    @ApiModelProperty(value = "回复人名称.")
    private String replyUname;

    @ApiModelProperty(value = "状态;10：待回复；20：已分配（待处理）；30：待审核；40：审核驳回；50：已完成；60：已评价")
    private Integer status;

    @ApiModelProperty(value = "提交人工号.")
    private String submitStaffid;

    @ApiModelProperty(value = "提交时间.")
    private Date submitTime;

    @ApiModelProperty(value = "提交人id.")
    private String submitUid;

    @ApiModelProperty(value = "提交人名称.")
    private String submitUname;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "标题.")
    private String title;

    @ApiModelProperty(value = "类型;complaint：投诉；suggestion：建议.")
    private String type;

    @ApiModelProperty(value = "更新时间.")
    private Date updateTime;

    @ApiModelProperty(value = "更新人.")
    private String updatorId;

    @ApiModelProperty(value = "投诉建议流转信息集合")
    private List<ComplaintSuggestionRomanModel> romanModels;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

}
