
package com.cgnpc.bbxpark.workorder.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 工单材料分页参数模型
 * @author huangyongtao
 * @date 2025/11/4 16:46
 */
@Data
public class WorkMaterialPageParam extends CudPageDto implements Serializable {
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
