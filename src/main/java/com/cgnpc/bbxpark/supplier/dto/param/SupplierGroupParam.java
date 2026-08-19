
package com.cgnpc.bbxpark.supplier.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/***
 * @Description 服务商分组入参数据模型
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
public class SupplierGroupParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class, message = "id不能为空")
    @ApiModelProperty(value = "主键.")
    private Long id;

    @NotNull(groups = InsertGroup.class, message = "服务商ID不能为空")
    @ApiModelProperty(value = "服务商ID.")
    private Long supplierId;

    @NotBlank(groups = InsertGroup.class, message = "名称不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "名称.")
    private String name;

    @Length(max = 255)
    @ApiModelProperty(value = "描述.")
    private String remark;

}