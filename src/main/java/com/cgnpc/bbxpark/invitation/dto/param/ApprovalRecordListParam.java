
package com.cgnpc.bbxpark.invitation.dto.param;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ApprovalRecordListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4824953687514024122L;

    @ApiModelProperty(value = "业务id.")
    private Long businessId;
}
