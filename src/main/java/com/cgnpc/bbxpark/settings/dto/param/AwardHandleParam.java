
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/***
 * @Description 评优评奖操作入参数据模型
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Data
public class AwardHandleParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class, message = "id不能为空")
    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "操作结果;1->通过;0->不通过.")
    private Integer operatorResult;

    @ApiModelProperty(value = "说明备注.")
    private String remark;

}