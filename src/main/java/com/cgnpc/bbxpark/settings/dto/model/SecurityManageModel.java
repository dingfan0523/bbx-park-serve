
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/***
 * @value 安全管理员业务数据模型
 * @author huangyongtao
 * @date 2025/7/31 17:41
 */
@Data
public class SecurityManageModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "安全管理员名称.")
    private String securityUname;

    @ApiModelProperty(value = "安全管理员工号.")
    private String securityStaffid;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    private String securityUid;

}
