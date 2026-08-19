
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 评优评奖入参数据模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class, message = "id不能为空")
    @ApiModelProperty(value = "主键.")
    private Long id;

    @NotBlank(groups = InsertGroup.class, message = "奖项名称不能为空")
    @ApiModelProperty(value = "奖项名称.")
    private String awardName;

    @NotNull(groups = InsertGroup.class, message = "审核方式不能为空")
    @ApiModelProperty(value = "审核方式;1->是;0->否.")
    private Integer auditType;

    @ApiModelProperty(value = "审核人id.")
    private String auditUid;

    @ApiModelProperty(value = "审核人名称.")
    private String auditUname;

    @ApiModelProperty(value = "审核人工号.")
    private String auditStaffid;

    @NotNull(groups = InsertGroup.class, message = "评奖日期不能为空")
    @ApiModelProperty(value = "评奖日期.")
    private Date awardDate;

    @NotNull(groups = InsertGroup.class, message = "展示开始时间不能为空")
    @ApiModelProperty(value = "展示开始时间.")
    private Date displayStartTime;

    @NotNull(groups = InsertGroup.class, message = "展示结束时间不能为空")
    @ApiModelProperty(value = "展示结束时间.")
    private Date displayEndTime;

    @ApiModelProperty(value = "奖项简介.")
    private String remark;

    @NotNull(groups = InsertGroup.class, message = "展示顺序不能为空")
    @ApiModelProperty(value = "展示顺序（1：按排序；2：按拼音）.")
    private Integer sortRule;

    @ApiModelProperty(value = "状态（10：待提交；20：待审核；30：待展示；40：展示中；50展示结束）.")
    private Integer status;

    @ApiModelProperty(value = "获奖人信息集合.")
    private List<AwardWinnerParam> awardWinnerParams;
}