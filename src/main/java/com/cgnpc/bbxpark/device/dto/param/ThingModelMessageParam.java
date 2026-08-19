package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.common.web.CudPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThingModelMessageParam extends CudPageDto implements Serializable {
    /**
     * es id
     */
    private String id;
    /**
     * RID+时间+序列号  的id
     */
    private String mid;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * 产品key
     */
    @ApiModelProperty(value = "产品key")
    private String productKey;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;
    /**
     * 设备name
     */
    @ApiModelProperty(value = "设备name")
    private String deviceName;
    /**
     * 消息类型
     * lifetime:生命周期
     * state:状态
     * property:属性
     * event:事件
     * service:服务
     */
    @ApiModelProperty(value = "消息类型")
    private String type;

    /**
     * set_reply-设置回复
     * report-上报
     * set-设置
     * online-上线
     * offLine-下线
     * register-注册
     * 可能还有其他值具体可参考前端代码
     */
    @ApiModelProperty(value = "设置回复")
    private String identifier;
    /**
     * 消息状态码
     */
    @ApiModelProperty(value = "消息状态码")
    private int code;
    /**
     * 数据
     */
    private Object data;

    private Object thingModel;
    /**
     * 时间戳，设备上的事件或数据产生的本地时间
     */
    @ApiModelProperty(value = "时间戳，设备上的事件或数据产生的本地时间")
    private Long occurred;
    /**
     * 消息上报时间
     */
    @ApiModelProperty(value = "消息上报时间")
    private Long time;

    /**
     * 要排除的id
     */
    private String noId;
    /**
     * 下一页 要排除的id
     */
    private List<String> noIdList;
    /**
     * 下一页翻页时  当前页 最后一条记录的时间
     */
    private Date nextTime;

    /**
     * 上一页翻页的时候 但前页最后一条记录的时间
     */
    private Date upTime;

    /*********查询条件*******/
    @ApiModelProperty(value = "当前登录用户.")
    private String userId;
    @ApiModelProperty(value = "园区管理员.")
    private Long tenantId;
    private Integer auth;

    @ApiModelProperty(value = "空间id.")
    private Long spaceId;
}
