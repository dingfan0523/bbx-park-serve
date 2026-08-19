package com.cgnpc.bbxpark.device.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc ioc产品表
 */
@Data
@TableName("bbx_ioc_product")
public class IocProduct extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** IOC产品名称 */
    @ApiModelProperty(value = "IOC产品名称")
    private String pdName ;
    /** 产品编码 */
    @ApiModelProperty(value = "产品编码")
    private String pdCode ;
    /** 产品品牌 */
    @ApiModelProperty(value = "产品品牌")
    private String pdBrand ;
    /** 产品型号 */
    @ApiModelProperty(value = "产品型号")
    private String pdModel ;
    /** 产品尺寸 */
    @ApiModelProperty(value = "产品尺寸")
    private String pdSize ;
    /** 产品单价 */
    @ApiModelProperty(value = "产品单价")
    private Double pdUnitPrice ;
    /** 产品描述 */
    @ApiModelProperty(value = "产品描述")
    private String pdDesc ;
    /** 乐观锁 */
    @ApiModelProperty(value = "乐观锁")
    private String revision ;
}
