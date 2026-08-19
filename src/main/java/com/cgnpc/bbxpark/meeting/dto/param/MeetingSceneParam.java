
package com.cgnpc.bbxpark.meeting.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 场景入参参数模型
 */
@Data
public class MeetingSceneParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "场景ID")
    private Long id;
    @ApiModelProperty(value = "会议室id")
    @NotNull(groups = UpdateGroup.class,message = "会议室id不能为空")
    private Long roomId;
    @ApiModelProperty(value = "场景名称")
    @NotEmpty(groups = {UpdateGroup.class, InsertGroup.class},message = "场景名称不能为空")
    private String sceneName;
    @ApiModelProperty(value = "场景描述")
    private String sceneDesc;
    @ApiModelProperty(value = "场景配置集合")
    private List<MeetingSceneConfigParam> sceneConfigList;
}
