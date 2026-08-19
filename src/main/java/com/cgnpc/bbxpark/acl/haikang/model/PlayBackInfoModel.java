package com.cgnpc.bbxpark.acl.haikang.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: ccy
 * @Date: 2023/10/31
 * @Description:
 */
@Data
@Accessors(chain = true)
public class PlayBackInfoModel implements Serializable {
    @ApiModelProperty("查询录像的锁定类型，0-全部录像；1-未锁定录像；2-已锁定录像")
    private Integer lockType;
    @ApiModelProperty("开始时间\n" +
            "录像片段的开始时间（IOS8601格式yyyy-MM-dd’T’HH:mm:ss.SSSzzz）")
    private String beginTime;
    @ApiModelProperty("结束时间\n" +
            "录像片段的开始时间（IOS8601格式yyyy-MM-dd’T’HH:mm:ss.SSSzzz）")
    private String endTime;
    @ApiModelProperty("录像片段大小")
    private Long size;

}
