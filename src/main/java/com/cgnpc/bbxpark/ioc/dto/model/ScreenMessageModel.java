package com.cgnpc.bbxpark.ioc.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "大屏消息通知接口")
@Data
public class ScreenMessageModel {
    @ApiModelProperty(value = "消息id")
    private Long id;
    @ApiModelProperty(value = "消息类型")
    private String type;
    @ApiModelProperty(value = "阅读状态，0未读1已读.")
    private Integer readStatus;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "时间")
    private Date date;
}
