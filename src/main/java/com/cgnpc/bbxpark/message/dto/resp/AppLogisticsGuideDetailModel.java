package com.cgnpc.bbxpark.message.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 移动端-后勤指南业务数据模型
 * @author dingfan
 * @version 1.0
 * @date 2024/10/14 13:41
 */
@Data
public class AppLogisticsGuideDetailModel implements Serializable {
    @ApiModelProperty(value = "指南id")
    private Long id;
    @ApiModelProperty(value = "指南标题")
    private String title;
    @ApiModelProperty(value = "指南摘要")
    private String summary;
    @ApiModelProperty(value = "指南内容")
    private String content;
    @ApiModelProperty(value = "发布人Id")
    private String publisherId;
    @ApiModelProperty(value = "发布人名称")
    private String publisherName;
    @ApiModelProperty(value = "发布人工号")
    private String publisherStaffid;
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;
}
