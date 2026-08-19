package com.cgnpc.bbxpark.config.eventbus;

import com.cgnpc.bbxpark.message.dto.resp.ExtModel;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 消息事件
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/24 17:19
 */
@Data
@Builder
public class MessageEvent {
    /**
     * 模板id
     */
    private Long templateId;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 消息日志id集合
     */
    private List<Long> messageLogIdList;
    /**
     * 推送渠道
     */
    private String channel;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息内容
     */
    private ExtModel extModel;
}
