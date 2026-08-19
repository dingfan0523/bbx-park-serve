package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/9/23 22:31
 */
@Data
public class FileParam implements Serializable {
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件地址")
    private String url;
}
