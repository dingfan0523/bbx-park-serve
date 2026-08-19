
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/***
 * @Description 评优评奖分页参数模型
 * @author huangyongtao
 * @date 2025/11/12 17:36
 */
@Data
public class AwardPageParam extends CudPageDto implements Serializable {
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

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "创建时间开始时间.")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建时间结束时间.")
    private Date createTimeEnd;

    @ApiModelProperty(value = "用户id.")
    private String userId;

    private Long tenantId;
}
