
package com.cgnpc.bbxpark.supplier.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @Description 服务商分组人员业务数据模型
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
public class SupplierGroupPersonModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "服务商ID.")
    private Long supplierId;

    @ApiModelProperty(value = "服务商分组ID.")
    private Long supplierGroupId;

    @ApiModelProperty(value = "姓名.")
    private String name;

    @ApiModelProperty(value = "联系方式.")
    private String phone;

    @ApiModelProperty(value = "职务.")
    private String post;

}
