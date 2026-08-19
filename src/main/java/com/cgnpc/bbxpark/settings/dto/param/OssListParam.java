package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OssListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3246272444259739804L;

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

    @ApiModelProperty(value = "对象存储主键集合.")
    private List<Long> ossIds;
}
