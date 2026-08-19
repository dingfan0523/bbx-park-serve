package com.cgnpc.bbxpark.space.dto.param;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 空间下载模板
 * @author lhy
 * @date 2024/7/1 17:08
 */
@Data
public class ParkSpaceTemplateParam implements Serializable {

    @ApiModelProperty(value = "上级空间编码（必填）.")
    @Excel(name = "*上级空间编码（必填）", width = 22.0)
    private String parentSpaceCode;

    @ApiModelProperty(value = "空间编码（必填）.")
    @Excel(name = "*空间编码（必填）", width = 18.0)
    private String spaceCode;

    @ApiModelProperty(value = "空间名称（必填）.")
    @Excel(name = "*空间名称（必填）", width = 18.0)
    private String spaceName;

    @ApiModelProperty(value = "排序序号.")
    @Excel(name = "排序序号", width = 12.0)
    private String orderCode;

}
