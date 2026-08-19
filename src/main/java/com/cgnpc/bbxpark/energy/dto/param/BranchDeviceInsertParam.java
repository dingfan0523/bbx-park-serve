package com.cgnpc.bbxpark.energy.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/4/18
 * @desc 支路设备新增参数
 */
@Data
public class BranchDeviceInsertParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    @ApiModelProperty(value = "支路ID")
    private Long branchId;

    @ApiModelProperty(value = "支路设备信息")
    private List<BranchDeviceParam> branchDeviceParamList;
}
