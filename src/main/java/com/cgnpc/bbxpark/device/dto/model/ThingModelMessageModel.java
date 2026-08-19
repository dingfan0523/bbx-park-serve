package com.cgnpc.bbxpark.device.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThingModelMessageModel implements Serializable {
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
    private String deviceId;

    /**
     * 产品key
     */
    private String productKey;
    /**
     * 设备name
     */
    private String productName;

    /**
     * 设备name
     */
    private String deviceName;

    /**
     * 所属用户ID
     */
    private String uid;

    /**
     * 消息类型
     * lifetime:生命周期
     * state:状态
     * property:属性
     * event:事件
     * service:服务
     */
    private String type;

    /**
     * set_reply-设置回复
     * report-上报 set-设置
     * online-上线
     * offLine-下线
     * register-注册
     * 可能还有其他值具体可参考前端代码
     */
    private String identifier;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 数据
     */
    private Object data;

    /**
     * 时间戳，设备上的事件或数据产生的本地时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date occurred;

    /**
     * 消息上报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private String spaceName;
}
