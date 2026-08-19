
package com.cgnpc.bbxpark.property.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 物业排班入参数据模型
 */
@Data
public class PropertyScheduleParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;
    @Length(max = 64)
    @ApiModelProperty(value = "分组名称.")
    @NotBlank
    private String name;
    @Length(max = 200)
    @ApiModelProperty(value = "分组描述.")
    private String remark;
    @ApiModelProperty(value = "人员集合")
    private List<PropertyScheduleUserParam> userList;
}
