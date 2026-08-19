package com.cgnpc.bbxpark.space.dto.kafka;

import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * kafka告警
 * @author fys
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThingModelKaFkaMessage {

    public static final String CONTENT = "撤销告警";
    public final static String RULE = "规则管理";
    public final static String SCENE_RULE = "场景编排";
    public final static String ALARM = "0";
    public final static String CANCEL_ALARM = "1";

    /**
     * 设备控制,来自规则管理
     */
    public final static Integer DEVICE_OPERATION_RULE = 1;

    /**
     * 设备控制,来自规场景编排
     */
    public final static Integer DEVICE_OPERATION_SCENE_RULE = 2;

    /**
     * 设备控制,来自规第三方api接口
     */
    public final static Integer DEVICE_OPERATION_SCENE_THIRD_API = 3;

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
     * 设备id
     */
    private List<String> deviceIds;

    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 告警名称
     */
    private String alterName;

    /**
     * 产品key
     */
    private String productKey;

    /**
     * 设备name
     */
    private String deviceName;


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
     * set_reply-设置回复   report-上报 set-设置   online-上线   offLine-下线   register-注册  可能还有其他值具体可参考前端代码
     */
    private String identifier;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 数据
     */
    private String data;

    /**
     * 消息内容组装完成,获取当前系统时间
     */
    private Long occurred;

    /**
     * 告警内容
     */
    private String content;

    /**
     * 告警严重度
     */
    private String level;

    /**
     * 1,规则管理,2场景编排
     */
    private String ruleType;


    /**
     * 类型  1告警,2撤销告警
     */
    private String alterType;


    /**
     * 临时参数
     */
    private Long tenantId;

    /**
     * 绑定的ioc设备
     */
    private List<IocDevice> iocDeviceList;

    /**
     * 绑定的ioc设备的空间信息
     */
    private Map<Long, ParkSpaceFullModel> fullSpaceMap;
}
