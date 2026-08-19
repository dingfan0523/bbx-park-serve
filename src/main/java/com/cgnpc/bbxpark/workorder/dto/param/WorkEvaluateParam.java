
package com.cgnpc.bbxpark.workorder.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/***
 * @value 工单评价入参数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:49
 */
@Data
public class WorkEvaluateParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "分数.")
    private Integer score;

    @Length(max = 255)
    @ApiModelProperty(value = "内容.")
    private String content;

    @Length(max = 100)
    @ApiModelProperty(value = "操作人名称.")
    private String operateUname;

    @Length(max = 100)
    @ApiModelProperty(value = "操作人工号.")
    private String operateStaffid;

    @Length(max = 100)
    @ApiModelProperty(value = "操作人id.")
    private String operateUid;
}
