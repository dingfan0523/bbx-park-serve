
package com.cgnpc.bbxpark.message.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 简单的后勤指南业务数据模型
 * @author dingfan
 * @date 2024/10/12 13:56
 */
@Data
public class LogisticsGuideListModel implements Serializable {

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
    @ApiModelProperty(value = "发布人id")
    private String publisherId;
    @ApiModelProperty(value = "发布人名称")
    private String publisherName;
    @ApiModelProperty(value = "发布人工号")
    private String publisherStaffid;
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;
}
