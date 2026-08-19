
package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 会服人员任务业务数据模型
 * @author huangyongtao
 * @date 2024/12/23 15:25
 */
@Data
public class SimpleMeetingAttendantModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;
    @ApiModelProperty(value = "用户id.")
    private String userId;
    @ApiModelProperty(value = "用户名称.")
    private String userName;
    @ApiModelProperty(value = "用户工号.")
    private String staffid;
    @ApiModelProperty(value = "用户手机号.")
    private String mobile;
}
