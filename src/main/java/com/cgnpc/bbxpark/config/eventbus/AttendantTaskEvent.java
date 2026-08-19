package com.cgnpc.bbxpark.config.eventbus;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/***
 * @Description 会服事件
 * @author huangyongtao
 * @date 2024/12/30 19:35
 */
@Data
public class AttendantTaskEvent implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会议室id.")
    private Long roomId;

    @ApiModelProperty(value = "会服类型;(1->会前布置；2->会中呼叫；3->会后清洁).")
    private Integer serviceType;

    @ApiModelProperty(value = "会服状态;(1->未处理；2->已确认； 3->已完成).")
    private Integer serviceStatus;

    @ApiModelProperty(value = "会服是否有效;(0->有效；1->无效).")
    private Integer serviceValid;

    @ApiModelProperty(value = "服务id集合")
    private List<Long> serviceIds;
}
