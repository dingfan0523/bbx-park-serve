
package com.cgnpc.bbxpark.invitation.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ApprovalTaskListModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4136616349030314036L;

    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "业务id.")
    private Long businessId;
    @ApiModelProperty(value = "状态:10->待审批;20->审批通过;30->审批不通过.")
    private Integer status;
    @ApiModelProperty(value = "扩展内容.")
    private String extendContent;
    @ApiModelProperty(value = "审批人集合")
    private List<ApproverModel> approverList;
}
