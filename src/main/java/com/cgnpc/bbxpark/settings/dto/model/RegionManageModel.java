
package com.cgnpc.bbxpark.settings.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @value 区域管理员管理业务数据模型
 * @author huangyongtao
 * @date 2025/3/11 11:11
 */
@Data
public class RegionManageModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id.")
    private Long id;

    @ApiModelProperty(value = "区域管理员名称.")
    private String regionUname;

    @ApiModelProperty(value = "区域管理员工号.")
    private String regionStaffid;

    @ApiModelProperty(value = "创建时间.")
    private Date createTime;



}
