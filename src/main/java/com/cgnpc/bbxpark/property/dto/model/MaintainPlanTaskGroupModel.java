
package com.cgnpc.bbxpark.property.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 维保计划管理业务数据模型
 * @author huangyongtao
 * @date 2025/10/16 15:21
 */
@Data
public class MaintainPlanTaskGroupModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup;

    @ApiModelProperty(value = "设备集合.")
    private List<MaintainDeviceModel> deviceModels;

    @ApiModelProperty(value = "维保项目集合.")
    private List<MaintainItemModel> itemModels;

    @ApiModelProperty(value = "设备数量.")
    private Long deviceCount = 0L;
}
