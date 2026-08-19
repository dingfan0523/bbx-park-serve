package com.cgnpc.bbxpark.space.dto.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 空间树模型
 * @author dingfan
 * @version 1.0
 * @date 2025/9/22 16:18
 */
@Data
public class SpaceTreeModel implements Serializable {
    private Long id;
    private Long parentSpaceId;
    private String spaceCode;
    private String spaceName;
    private Integer type;
    private Integer orderCode;
    private Boolean stationAllotAuth;
    private List<SpaceTreeModel> children;
}
