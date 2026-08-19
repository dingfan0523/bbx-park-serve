
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
/***
 * @Description 关注人管理入参数据模型
 * @author huangyongtao
 * @date 2025/3/11 10:21
 */
@Data
public class AttentionManageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "关注人id.")
    private String attentionUid;

    @Length(max = 30)
    @ApiModelProperty(value = "关注人名称.")
    private String attentionUname;

    @Length(max = 30)
    @ApiModelProperty(value = "关注人工号.")
    private String attentionStaffid;

}
