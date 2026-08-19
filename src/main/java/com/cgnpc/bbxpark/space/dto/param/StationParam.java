
package com.cgnpc.bbxpark.space.dto.param;


import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class StationParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4341770852751334218L;

    @NotNull(groups = {InsertGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "关联空间ID.")
    private Long spaceId;
    @ApiModelProperty(value = "人员id集合")
    private List<String> userIdList;
}
