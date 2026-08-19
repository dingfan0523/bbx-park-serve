package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc IOC产品文件表
 */
@Data
@TableName("bbx_ioc_product_file")
public class IocProductFile extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
//    @ApiModelProperty(value = "id")
//    private Long id;
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
    /** 乐观锁 */
    @ApiModelProperty(value = "乐观锁")
    private String revision ;
}
