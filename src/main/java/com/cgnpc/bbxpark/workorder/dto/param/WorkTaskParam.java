
package com.cgnpc.bbxpark.workorder.dto.param;


import com.cgnpc.bbxpark.settings.dto.param.FileParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 工单任务入参数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:56
 */
@Data
public class WorkTaskParam implements Serializable {
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

    @Length(max = 50)
    @ApiModelProperty(value = "任务名称.")
    private String name;

    @Length(max = 50)
    @ApiModelProperty(value = "任务编码.")
    private String code;

    @ApiModelProperty(value = "任务分类.")
    private Integer category;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;

    @Length(max = 255)
    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup;

    @Length(max = 255)
    @ApiModelProperty(value = "任务要求.")
    private String remark;

    @Length(max = 255)
    @ApiModelProperty(value = "冗余字段1.")
    private String redundancyOne;

    @Length(max = 255)
    @ApiModelProperty(value = "冗余字段2.")
    private String redundancyTwo;

    @ApiModelProperty(value = "冗余时间字段1.")
    private Date redundancyTimeOne;

    @ApiModelProperty(value = "租户id.")
    private Long tenantId;

    @ApiModelProperty(value = "异常状态（1->是;0->否）.")
    private Integer errorStatus;

    @Length(max = 255)
    @ApiModelProperty(value = "异常说明")
    private String errorRemark;

    @ApiModelProperty(value = "异常空间id.")
    private Long errorSpaceId;

    @ApiModelProperty(value = "异常空间全路径名称.")
    private String errorSpaceName;

    @ApiModelProperty(value = "文件列表")
    private List<FileParam> fileParamList;

    @ApiModelProperty(value = "问题状态（1->是;0->否）.")
    private Integer problemStatus;
}
