
package com.cgnpc.bbxpark.message.dto.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 后勤指南入参数据模型
 * @author dingfan
 * @date 2024/10/12 13:56
 */
@Data
public class LogisticsGuideParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "指南id")
    private Long id;
    @ApiModelProperty(value = "指南标题")
    @NotEmpty(message = "指南标题不能为空")
    @Length(max = 6,message = "长度不超过6")
    private String title;
    @ApiModelProperty(value = "指南摘要")
    @Length(max = 30,message = "长度不超过30")
    private String summary;
    @ApiModelProperty(value = "指南内容")
    @NotEmpty(message = "指南内容不能为空")
    private String content;
    @ApiModelProperty(value = "排序号")
    @NotNull(message = "排序号不能为空")
    @Min(value = 0)
    @Max(value = 999)
    private Integer orderCode;
}
