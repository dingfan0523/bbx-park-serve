
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @Description 关注人管理业务数据模型
 * @author huangyongtao
 * @date 2025/3/11 10:19
 */
@Data
public class AttentionManageModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "关注人id.")
    private String attentionUid;

    @ApiModelProperty(value = "关注人名称.")
    private String attentionUname;

    @ApiModelProperty(value = "关注人工号.")
    private String attentionStaffid;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;


}
