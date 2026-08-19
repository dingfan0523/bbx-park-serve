
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/***
 * @value 安全管理员列表参数模型
 * @author huangyongtao
 * @date 2025/8/1 10:55
 */
@Data
public class SecurityManageListParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "安全管理员id.")
    private String securityUid;

    @ApiModelProperty(value = "安全管理员名称.")
    private String securityUname;

    @ApiModelProperty(value = "安全管理员工号.")
    private String securityStaffid;
}
