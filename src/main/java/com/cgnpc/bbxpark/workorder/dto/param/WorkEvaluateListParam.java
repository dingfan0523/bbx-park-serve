
package com.cgnpc.bbxpark.workorder.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 工单评价列表参数模型
 * @author huangyongtao
 * @date 2025/11/4 16:48
 */
@Data
public class WorkEvaluateListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "分数.")
    private Integer score;

    @ApiModelProperty(value = "内容.")
    private String content;

    @ApiModelProperty(value = "操作人id.")
    private String operateUid;

    @ApiModelProperty(value = "操作人名称.")
    private String operateUname;

    @ApiModelProperty(value = "操作人工号.")
    private String operateStaffid;
}
