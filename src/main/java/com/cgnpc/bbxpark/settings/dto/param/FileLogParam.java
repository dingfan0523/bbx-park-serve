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
public class FileLogParam implements Serializable {
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件大小")
    private Long size;
}
