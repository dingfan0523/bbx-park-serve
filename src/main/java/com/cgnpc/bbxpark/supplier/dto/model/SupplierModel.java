
package com.cgnpc.bbxpark.supplier.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 服务商业务数据模型
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
public class SupplierModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    private Long id;

    @ApiModelProperty(value = "服务商名称.")
    private String name;

    @ApiModelProperty(value = "服务商名称拼音.")
    private String pinyin;

    @ApiModelProperty(value = "服务商类型（1->集成商；2->销售方；3->供货方；4->运维服务商）.")
    private Integer type;

    @ApiModelProperty(value = "进驻时间..")
    private Date occupancyDate;

    @ApiModelProperty(value = "所在地区.")
    private String region;

    @ApiModelProperty(value = "详细地址.")
    private String address;

    @ApiModelProperty(value = "服务商描述.")
    private String remark;

    @ApiModelProperty(value = "服务商分组列表.")
    private List<SupplierGroupModel> supplierGroupList;

    @ApiModelProperty(value = "服务商人员列表.")
    private List<SupplierPersonModel> supplierPersonModels;

}
