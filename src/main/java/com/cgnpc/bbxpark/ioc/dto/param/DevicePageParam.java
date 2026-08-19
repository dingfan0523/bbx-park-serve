package com.cgnpc.bbxpark.ioc.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DevicePageParam extends CudPageDto implements Serializable{
    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    @ApiModelProperty(value = "空间模型编码")
    private String sslcCode;
    @ApiModelProperty(value = "分组id")
    private Long groupId;
    @ApiModelProperty(value = "分组编码")
    private String groupCode;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "所属产品id")
    private Long productId;
    @ApiModelProperty(value = "设备等级")
    private Integer deviceLevel;
    @ApiModelProperty(value = "使用部门id")
    private Long departmentId;
    @ApiModelProperty(value = "支路id")
    private Long branchId;
    @ApiModelProperty(value = "抄表类型:water：水表；electricity：电表；gas：燃气表")
    private String readingType;
    @ApiModelProperty(value = "设备平台集合:0->非物联网平台;1：自有平台；2：统建平台；3：安消平台")
    private List<Integer> platforms;
}