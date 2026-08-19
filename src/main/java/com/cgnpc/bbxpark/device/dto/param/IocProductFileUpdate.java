package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc ioc产品文件修改类
 */
@Data
public class IocProductFileUpdate implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 产品文件ID */
    @ApiModelProperty(value = "产品文件ID")
    private Long id ;

    /** 标签类型(1-安装说明书;2-施工说明书;3-其他文件) */
    @ApiModelProperty(value = "标签类型(1-安装说明书;2-施工说明书;3-其他文件)")
    private Integer label ;
}
