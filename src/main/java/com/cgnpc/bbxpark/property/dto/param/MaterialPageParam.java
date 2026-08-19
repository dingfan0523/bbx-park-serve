package com.cgnpc.bbxpark.property.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @Description 材料分页参数模型
 * @author huangyongtao
 * @date 2025/9/22 16:05
 */
@Data
public class MaterialPageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空间id")
    private Long spaceId;

    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType;

    @ApiModelProperty(value = "库存数量.")
    private Integer stockQuantity;

    @ApiModelProperty(value = "库存预警值.")
    private Integer stockWarning;

    @ApiModelProperty(value = "库存状态;（1：库存充足；2：库存不足；3：缺货）.")
    private Integer stockStatus;

    @ApiModelProperty(value = "备注.")
    private String remark;

    @ApiModelProperty(value = "需要排除的id集合.")
    private List<Long> noIds;
}
