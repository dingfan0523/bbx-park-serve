package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文件分页
 */
@Data
public class FilePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件地址")
    private String url;
    @ApiModelProperty(value = "文件类型")
    private Integer type;
    @ApiModelProperty(value = "文件类型集合")
    private List<Integer> types;
}
