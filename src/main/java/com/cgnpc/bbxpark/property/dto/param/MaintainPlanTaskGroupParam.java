package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 维保项目入参数据模型
 * @author huangyongtao
 * @date 2025/10/16 16:19
 */
@Data
public class MaintainPlanTaskGroupParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup;

    @ApiModelProperty(value = "设备集合.")
    private List<MaintainDeviceParam> deviceParams;

    @ApiModelProperty(value = "维保项目集合.")
    private List<MaintainItemParam> itemParams;
}
