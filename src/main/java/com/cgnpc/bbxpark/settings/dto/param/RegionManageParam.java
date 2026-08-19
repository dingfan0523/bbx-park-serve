package com.cgnpc.bbxpark.settings.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 * @value 区域管理员管理入参数据模型
 * @author huangyongtao
 * @date 2025/3/11 11:19
 */
@Data
public class RegionManageParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "区域管理员工号.")
    private List<String> userIds;
}
