
package com.cgnpc.bbxpark.message.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后勤指南业务数据模型
 * @author dingfan
 * @date 2024/10/12 13:56
 */
@Data
public class LogisticsGuideDetailModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "指南id")
    private Long id;
    @ApiModelProperty(value = "指南标题")
    private String title;
    @ApiModelProperty(value = "指南摘要")
    private String summary;
    @ApiModelProperty(value = "指南内容")
    private String content;
    @ApiModelProperty(value = "排序号")
    private Integer orderCode;
}
