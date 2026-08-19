package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 材料入库分页参数模型
 * @author huangyongtao
 * @date 2025/9/22 15:55
 */
@Data
public class MaterialInboundPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "入库单号.")
    private String inboundNo;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType;

    @ApiModelProperty(value = "入库数量.")
    private Integer quantity;

    @ApiModelProperty(value = "备注.")
    private String remark;
}
