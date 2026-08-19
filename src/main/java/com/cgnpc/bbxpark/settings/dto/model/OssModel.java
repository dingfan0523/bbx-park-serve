package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OssModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3744933957115288452L;

    @ApiModelProperty(value = "对象存储主键.")
    private Long ossId;

    @ApiModelProperty(value = "文件名.")
    private String fileName;

    @ApiModelProperty(value = "原名.")
    private String originalName;

    @ApiModelProperty(value = "文件后缀名.")
    private String fileSuffix;

    @ApiModelProperty(value = "URL地址.")
    private String url;

    @ApiModelProperty(value = "状态（0正常 1停用）.")
    private Integer status = 0;
}
