package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 22:29
 */
@Data
public class FileModel implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "业务类型:1->智慧会议")
    private Integer type;
    @ApiModelProperty(value = "业务id")
    private Long relatedId;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件地址")
    private String url;
    @ApiModelProperty(value = "上传人id")
    private String creatorId;
    @ApiModelProperty(value = "上传人")
    private String createBy;
    @ApiModelProperty(value = "上传时间")
    private Date createTime;
}
