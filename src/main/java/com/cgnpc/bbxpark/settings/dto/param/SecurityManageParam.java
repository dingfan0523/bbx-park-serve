
package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 安全管理员入参数据模型
 * @author huangyongtao
 * @date 2025/8/1 10:56
 */
@Data
public class SecurityManageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "安全管理员员工号集合.")
    private List<String> userIds;
}
