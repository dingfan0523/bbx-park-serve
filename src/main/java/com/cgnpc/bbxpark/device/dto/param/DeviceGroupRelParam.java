
package com.cgnpc.bbxpark.device.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/***
 * @value 设备分组关系入参数据模型
 * @author huangyongtao
 * @date 2024/8/12 11:52
 */
@Data
public class DeviceGroupRelParam implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关系id.")
    private Long id;

    @ApiModelProperty(value = "分组id.")
    private Long groupId;

    @ApiModelProperty(value = "分组编码")
    private String groupCode;

    @Length(max = 255)
    @ApiModelProperty(value = "设备id.")
    private Long deviceId;

    @ApiModelProperty(value = "设备id集合")
    private List<Long> deviceIdList;

    @ApiModelProperty(value = "园区id.")
    private Long tenantId;

}
