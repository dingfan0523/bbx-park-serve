package com.cgnpc.bbxpark.acl.haikang.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: ccy
 * @Date: 2023/10/31
 * @Description:
 */
@Data
@Accessors(chain = true)
public class PlayBackURLsModel implements Serializable {
    @ApiModelProperty("分页信息")
    private List<PlayBackInfoModel> list;
    @ApiModelProperty("分页标记\n" +
            "标记本次查询的全部标识符，用于查询分片时的多次查询")
    private String uuid;
    @ApiModelProperty("取流短url，注：rtsp的回放url后面要指定?playBackMode=1 在vlc上才能播放")
    private String url;

}
