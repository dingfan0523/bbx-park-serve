
package com.cgnpc.bbxpark.workorder.dto.model;

import com.cgnpc.bbxpark.settings.dto.model.FileModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 工单任务项业务数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:59
 */
@Data
public class WorkTaskItemModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;

    @ApiModelProperty(value = "业务类型:1->维保项目;2->巡更点.")
    private Integer businessType;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "编码.")
    private String code;

    @ApiModelProperty(value = "类型（10：安保；20：保洁；30：消控；40：环境；50：设备）.")
    private Integer type;

    @ApiModelProperty(value = "方式（10：拍照；20：其他）.")
    private Integer way;

    @ApiModelProperty(value = "位置id.")
    private Long spaceId;

    @ApiModelProperty(value = "空间全路径.")
    private String spaceFullPath;

    @ApiModelProperty(value = "巡更要求.")
    private String remark;

    @ApiModelProperty(value = "重点检查(1->是;0->否).")
    private Integer keyPoint = 0;

    @ApiModelProperty(value = "启用状态;1->是;0->否.")
    private Integer status = 1;

    @ApiModelProperty(value = "任务组.")
    private Integer taskGroup = 0;

    @ApiModelProperty(value = "异常状态（1->是;0->否）.")
    private Integer errorStatus;

    @ApiModelProperty(value = "异常说明")
    private String errorRemark;

    @ApiModelProperty(value = "文件列表")
    private List<FileModel> fileModelList;

    @ApiModelProperty(value = "问题状态（1->是;0->否）.")
    private Integer problemStatus;

}
