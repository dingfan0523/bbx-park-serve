
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 评优评奖流程业务数据模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardRomanModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "评优评奖表id.")
    private Long awardId;

    @ApiModelProperty(value = "状态（1->提交；2->撤回；3->审批；4->信息展示；5->展示结束）.")
    private Integer status;

    @ApiModelProperty(value = "操作人id.")
    private String operatorId;

    @ApiModelProperty(value = "操作人名称.")
    private String operatorName;

    @ApiModelProperty(value = "操作人工号.")
    private String operatorStaffid;

    @ApiModelProperty(value = "操作结果;1->通过;0->不通过.")
    private Integer operatorResult;

    @ApiModelProperty(value = "操作.")
    private String operator;

    @ApiModelProperty(value = "说明备注.")
    private String remark;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

}
