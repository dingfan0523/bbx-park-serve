
package com.cgnpc.bbxpark.invitation.dto.model;

import com.cgnpc.bbxpark.settings.dto.model.SecurityManageModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 邀约空间关联业务数据模型
 * @author huangyongtao
 * @date 2025/8/1 13:58
 */
@Data
public class InvitationApproveModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称.")
    private String deviceName;

    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "安全员集合.")
    private List<SecurityManageModel> securityManageList;
}
