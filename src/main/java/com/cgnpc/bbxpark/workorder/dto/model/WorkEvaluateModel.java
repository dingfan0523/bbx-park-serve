
package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @value 工单评价业务数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:58
 */
@Data
public class WorkEvaluateModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "分数.")
    private Integer score = 0;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "操作人id.")
    private String operateUid;

    @ApiModelProperty(value = "操作人名称.")
    private String operateUname;

    @ApiModelProperty(value = "操作人工号.")
    private String operateStaffid;

    @ApiModelProperty(value = "创建人工号")
    private String creatorId;

    @ApiModelProperty(value = "创建人名称.")
    private String createBy;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;

    @ApiModelProperty(value = "工单编码.")
    private String workCode;

}
