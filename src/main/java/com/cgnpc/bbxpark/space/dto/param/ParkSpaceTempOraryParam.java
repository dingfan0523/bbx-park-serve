package com.cgnpc.bbxpark.space.dto.param;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 园区空间导入临时表
 * @author lhy
 * @date 2024/7/1 17:08
 */
@Data
public class ParkSpaceTempOraryParam implements Serializable {

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
    @Excel(name = "排序序号", width = 10.0)
    private String orderCode;

    @ApiModelProperty(value = "失败错误描述.")
    @Excel(name = "失败错误描述", width = 22.0)
    private String importErrorDesc;
}
