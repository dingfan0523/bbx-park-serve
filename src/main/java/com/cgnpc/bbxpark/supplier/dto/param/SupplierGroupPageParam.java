
package com.cgnpc.bbxpark.supplier.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 服务商分组分页参数模型
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
public class SupplierGroupPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "服务商ID.")
    private Long supplierId;

    @ApiModelProperty(value = "服务商ID集合.")
    private List<Long> supplierIds;

    @ApiModelProperty(value = "名称.")
    private String name;

    @ApiModelProperty(value = "描述.")
    private String remark;

}