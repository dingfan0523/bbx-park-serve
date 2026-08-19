
package com.cgnpc.bbxpark.supplier.dto.param;

import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @Description 服务商入参数据模型
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Data
public class SupplierParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @NotNull(groups = UpdateGroup.class, message = "id不能为空")
    @ApiModelProperty(value = "主键.")
    private Long id;

    @NotBlank(groups = InsertGroup.class, message = "服务商名称不能为空")
    @Length(max = 50)
    @ApiModelProperty(value = "服务商名称.")
    private String name;

    @ApiModelProperty(value = "服务商名称拼音.")
    private String pinyin;

    @ApiModelProperty(value = "服务商类型（1->集成商；2->销售方；3->供货方；4->运维服务商）.")
    private Integer type;

    @ApiModelProperty(value = "进驻时间..")
    private Date occupancyDate;

    @Length(max = 255)
    @ApiModelProperty(value = "所在地区.")
    private String region;

    @Length(max = 255)
    @ApiModelProperty(value = "详细地址.")
    private String address;

    @Length(max = 255)
    @ApiModelProperty(value = "服务商描述.")
    private String remark;

    @ApiModelProperty(value = "服务商人员列表.")
    private List<SupplierPersonParam> supplierPersonParams;

}