
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 评优评奖业务数据模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "奖项名称.")
    private String awardName;

    @ApiModelProperty(value = "审核方式;1->是;0->否.")
    private Integer auditType;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @ApiModelProperty(value = "评奖日期.")
    private Date awardDate;

    @ApiModelProperty(value = "展示开始时间.")
    private Date displayStartTime;

    @ApiModelProperty(value = "展示结束时间.")
    private Date displayEndTime;

    @ApiModelProperty(value = "奖项简介.")
    private String remark;

    @ApiModelProperty(value = "展示顺序（1：按排序；2：按拼音）.")
    private Integer sortRule;

    @ApiModelProperty(value = "状态（10：待提交；20：待审核；30：待展示；40：展示中；50展示结束）.")
    private Integer status;

    @ApiModelProperty(value = "创建人id.")
    private String creatorId;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "流程信息集合.")
    private List<AwardRomanModel> awardRomanModels;

    @ApiModelProperty(value = "评奖人信息集合.")
    private List<AwardWinnerModel> awardWinnerModel;

    @ApiModelProperty(value = "编辑权限")
    private Boolean editFlag = false;

    @ApiModelProperty(value = "删除权限")
    private Boolean deleteFlag = false;

    @ApiModelProperty(value = "撤回权限")
    private Boolean recallFlag = false;

    @ApiModelProperty(value = "提交权限")
    private Boolean submitFlag = false;

    @ApiModelProperty(value = "审核权限")
    private Boolean auditFlag = false;

    @ApiModelProperty(value = "取消展示权限")
    private Boolean cancelFlag = false;
}