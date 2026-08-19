
package com.cgnpc.bbxpark.invitation.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class ApproverModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3727328691375185187L;

    @ApiModelProperty(value = "用户id.")
    private String userId;
    @ApiModelProperty(value = "工号.")
    private String staffid;
    @ApiModelProperty(value = "名称.")
    private String userName;
    @ApiModelProperty(value = "是否审批(0->是;1->否).")
    private Integer approved = 1;
}
