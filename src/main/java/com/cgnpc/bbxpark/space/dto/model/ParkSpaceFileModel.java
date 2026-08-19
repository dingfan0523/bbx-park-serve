package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 导入返回
 *
 * @author dingfan
 * @date 2024/7/1 15:33
 */
@Data
public class ParkSpaceFileModel implements Serializable {

    @ApiModelProperty(value = "导入失败总计")
    private SpaceImportBatchModel spaceImportBatchModel;

    @ApiModelProperty(value = "导入失败信息集合")
    private List<ParkSpaceImportTemporaryModel> parkSpaceImportTemporaryModels;
}
