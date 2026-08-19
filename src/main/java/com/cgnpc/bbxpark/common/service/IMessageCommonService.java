package com.cgnpc.bbxpark.common.service;

import java.util.Map;
import java.util.Set;

/**
 * 消息中心公用服务
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/29 17:10
 */
public interface IMessageCommonService {

    /**
     * 发送消息
     *
     * @param templateCode 模板编码
     * @param tenantId     租户id
     * @param businessId   业务id
     * @param receiver     系统默认推送人
     * @param variables    动态参数
     */
    void sendMessage(String templateCode, Long tenantId, Long businessId, String receiver, Map<String, String> variables);

    /**
     * 发送消息
     *
     * @param templateCode 模板编码
     * @param tenantId     租户id
     * @param businessId   业务id
     * @param receivers    系统默认推送人集合
     * @param variables    动态参数
     */
    void sendMessage(String templateCode, Long tenantId, Long businessId, Set<String> receivers, Map<String, String> variables);
}
