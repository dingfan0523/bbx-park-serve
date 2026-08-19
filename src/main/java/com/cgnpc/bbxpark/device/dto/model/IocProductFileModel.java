package com.cgnpc.bbxpark.device.dto.model;

import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/2/26
 * @desc
 */
@Data
public class IocProductFileModel extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 文件名称 */
    @ApiModelProperty(value = "文件名称")
    private String name ;
    /** 业务id */
    @ApiModelProperty(value = "业务id")
    private Long businessId ;
    /** 文件路径 */
    @ApiModelProperty(value = "文件路径")
    private String url ;
    /** 标签类型(1-安装说明书;2-施工说明书;3-其他文件) */
    @ApiModelProperty(value = "标签类型(1-安装说明书;2-施工说明书;3-其他文件)")
    private Integer label ;
    /** 业务类型枚举（1-产品，2-设备） */
    @ApiModelProperty(value = "业务类型枚举（1-产品，2-设备）")
    private Integer type ;
    @ApiModelProperty(value = "是否为文件上传人")
    private Boolean isCreator ;
}
