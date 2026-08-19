package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 维保计划管理状态入参数据模型
 * @author huangyongtao
 * @date 2025/10/16 15:25
 */
@Data
public class MaintainPlanDeviceParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIds;
}
