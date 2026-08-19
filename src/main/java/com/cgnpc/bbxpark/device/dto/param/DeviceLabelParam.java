
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 设备标签入参数据模型
 */
@Data
public class DeviceLabelParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id.")
    private Long id;

    @NotBlank(message = "标签编码不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "标签编码.")
    private String labelCode;

    @NotBlank(message = "标签名称不能为空")
    @Length(max = 100)
    @ApiModelProperty(value = "标签名称.")
    private String labelName;

    @Length(max = 50)
    @ApiModelProperty(value = "标签颜色.")
    private String labelColour;

    @Length(max = 255)
    @ApiModelProperty(value = "标签描述.")
    private String labelDescribe;

    @ApiModelProperty(value = "租户号.")
    private Long tenantId;

    @ApiModelProperty(value = "id集合.")
    private List<Long> ids;

    @ApiModelProperty(value = "设备id集合.")
    private List<Long> deviceIds;
}
