package com.cgnpc.bbxpark.device.dto.model;

import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/25
 * @desc
 */
@Data
public class IocProductModel extends BaseExEntity implements Serializable {
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
    /** 文件列表 */
    @ApiModelProperty(value = "文件列表")
    List<FileModel> fileList;
}
