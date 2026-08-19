
package com.cgnpc.bbxpark.workorder.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 工单材料业务数据模型
 * @author huangyongtao
 * @date 2025/11/4 16:58
 */
@Data
public class WorkMaterialModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "工单id.")
    private Long workId;

    @ApiModelProperty(value = "材料id.")
    private Long materialId;

    @ApiModelProperty(value = "材料名称.")
    private String materialName;

    @ApiModelProperty(value = "材料编码.")
    private String materialCode;

    @ApiModelProperty(value = "材料类型;（1：器材；2：耗材）.")
    private Integer materialType = 1;

    @ApiModelProperty(value = "材料数量.")
    private Integer materialNum;

    @ApiModelProperty(value = "材料使用数量.")
    private Integer materialUseNum;

}
