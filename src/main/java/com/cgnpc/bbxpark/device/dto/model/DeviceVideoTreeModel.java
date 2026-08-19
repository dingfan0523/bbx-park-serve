package com.cgnpc.bbxpark.device.dto.model;

import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/***
 * @Description 视频设备树
 * @author huangyongtao
 * @date 2025/8/20 16:30
 */
@Data
public class DeviceVideoTreeModel {
    /*** 有位置的设备地图 */
    @ApiModelProperty(value = "有位置的设备地图.")
    private List<ParkSpaceTreeModel> parkSpaceTrees;

    /*** 无位置的设备地图 */
    @ApiModelProperty(value = "无位置的设备")
    private List<DeviceVideoModel> noSpaceDeviceVideos;

}