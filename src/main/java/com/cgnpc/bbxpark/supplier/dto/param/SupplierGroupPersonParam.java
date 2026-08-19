
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
 * @Description 服务商分组人员入参数据模型
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
public class SupplierGroupPersonParam implements Serializable {
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

    @NotNull(groups = InsertGroup.class, message = "服务商分组ID不能为空")
    @ApiModelProperty(value = "服务商分组ID.")
    private Long supplierGroupId;

    @NotBlank(groups = InsertGroup.class, message = "姓名不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "姓名.")
    private String name;

    @NotBlank(groups = InsertGroup.class, message = "联系方式不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "联系方式.")
    private String phone;

    @Length(max = 50)
    @ApiModelProperty(value = "职务.")
    private String post;

}
