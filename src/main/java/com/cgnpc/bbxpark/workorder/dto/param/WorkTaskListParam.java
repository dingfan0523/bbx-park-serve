
package com.cgnpc.bbxpark.workorder.dto.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 工单任务列表参数模型
 * @author huangyongtao
 * @date 2025/11/4 16:55
 */
@Data
public class WorkTaskListParam  implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "业务类型:1->维保设备;2->巡检点；3->巡更路线；4->盘点设备；5->盘点材料；6->任务.")
    private Integer businessType;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "任务名称.")
    private String name;

    @ApiModelProperty(value = "任务编码.")
    private String code;

    @ApiModelProperty(value = "任务分类.")
    private Integer category;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup;

    @ApiModelProperty(value = "任务要求.")
    private String remark;

    @ApiModelProperty(value = "冗余字段1.")
    private String redundancyOne;

    @ApiModelProperty(value = "冗余字段2.")
    private String redundancyTwo;
}
