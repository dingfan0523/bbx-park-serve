
package com.cgnpc.bbxpark.workorder.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 工单材料列表参数模型
 * @author huangyongtao
 * @date 2025/11/4 16:47
 */
@Data
public class WorkMaterialListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType;

    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;
}
