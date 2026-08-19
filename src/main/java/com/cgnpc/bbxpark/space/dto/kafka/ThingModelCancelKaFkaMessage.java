package com.cgnpc.bbxpark.space.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 撤销kafka告警
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThingModelCancelKaFkaMessage {

    /**
     *  id
     */
    private Long id;

    /**
     * 设备id
     */
    private String deviceId;


    /**
     * 规则id
     */
    private String ruleId;

    /**
     * 告警名称
     */
    private String alterName;


    /**
     * 设备dn
     */
    private String deviceDn;



    /**
     * 数据
     */
    private Object data;

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


    private Integer alarmTotal;



}
