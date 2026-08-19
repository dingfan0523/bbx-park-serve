
package com.cgnpc.bbxpark.settings.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @Description 关注人管理分页参数模型
 * @author huangyongtao
 * @date 2025/3/11 10:23
 */
@Data
public class AttentionManagePageParam extends CudPageDto implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "关注人id.")
    private String attentionUid;

    @ApiModelProperty(value = "关注人名称.")
    private String attentionUname;

    @ApiModelProperty(value = "关注人工号.")
    private String attentionStaffid;

    @ApiModelProperty(value = "园区ID-租户号.")
    private Long tenantId;

}
