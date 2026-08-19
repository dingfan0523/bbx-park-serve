
package com.cgnpc.bbxpark.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.message.domain.MessageTemplateConfig;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageTemplateConfigModel;

import java.util.List;

/***
 * @Description 消息模版配置服务接口
 * @author huangyongtao
 * @date 2024/10/24 16:51
 */
public interface IMessageTemplateConfigService extends IService<MessageTemplateConfig> {

	/**
	 * 查询模版配置列表
	 * @Param [id] 消息模版配置标识
	 * @Return 消息模版配置详情信息
	 */
	List<MessageTemplateConfigModel> list(Long templateId);

	/**
	 * 新增消息日志.
	 * @Param param 消息日志信息
	 * @Return 新增消息日志是否成功
	 */
	Boolean add(MessageTemplateParam param);

	/**
	 * 查询模版配置列表
	 * @Param [id] 消息模版配置标识
	 * @Return 消息模版配置详情信息
	 */
	List<MessageTemplateConfigModel> findConfigs(List<Long> templateIds);

}
