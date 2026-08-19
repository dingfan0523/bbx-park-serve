
package com.cgnpc.bbxpark.space.dto.param;

import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class SpaceBasicInfoParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3632635273885407774L;

    @NotNull(groups = UpdateGroup.class)
    @ApiModelProperty(value = "id.")
    private Long id;
    @ApiModelProperty(value = "空间名称.")
    @NotNull(message = "空间名称不能为空", groups = {Default.class, UpdateGroup.class})
    private String spaceName;
    @ApiModelProperty(value = "排序序号.")
    @NotNull(message = "排序序号不能为空", groups = {Default.class, UpdateGroup.class})
    private Integer orderCode;

    @ApiModelProperty(value = "空间类型.")
    @NotNull(message = "空间类型不能为空", groups = {Default.class, UpdateGroup.class})
    private Integer type;
    @ApiModelProperty(value = "使用单位id.")
    @NotNull(message = "使用单位不能为空", groups = {Default.class, UpdateGroup.class})
    private String departmentId;
    @ApiModelProperty(value = "空间容量(人).")
    @NotNull(message = "空间容量(人)不能为空", groups = {Default.class, UpdateGroup.class})
    private Integer capacity;
    @ApiModelProperty(value = "工位管理员id.")
    private String managerId;
    @ApiModelProperty(value = "面积（㎡）.")
    private BigDecimal area;
    @Length(max = 200)
    @ApiModelProperty(value = "空间用途.")
    private String purpose;
}
