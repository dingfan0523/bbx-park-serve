
package com.cgnpc.bbxpark.meeting.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

/***
 * @Description 会议室-场景参数模型
 * @author huangyongtao
 * @date 2024/8/23 15:29
 */
@Data
public class MeetingSceneListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "会议室id")
    @NotNull(groups = Default.class,message = "会议室id不能为空")
    private Long roomId;
    @ApiModelProperty(value = "场景名称")
    private String sceneName;
}
