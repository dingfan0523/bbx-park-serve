
package com.cgnpc.bbxpark.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.MessageTemplateConfigConstant;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ObjectUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.message.domain.MessageTemplateConfig;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageTemplateConfigModel;
import com.cgnpc.bbxpark.message.mapper.MessageTemplateConfigRepository;
import com.cgnpc.bbxpark.message.service.IMessageTemplateConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/***
 * @Description 消息模版配置服务实现
 * @author huangyongtao
 * @date 2024/10/24 17:31
 */
@Service
public class MessageTemplateConfigServiceImpl extends ServiceImpl<MessageTemplateConfigRepository, MessageTemplateConfig> implements IMessageTemplateConfigService {

	/**
	 * 根据消息模版配置标识获得消息模版配置详情信息.
	 * @Param [id] 消息模版配置标识
	 * @Return 消息模版配置详情信息
	 */
	@Override
	public List<MessageTemplateConfigModel> list(Long templateId) {
		List<MessageTemplateConfig> messageTemplateConfigs = list(handlePublicQuery(templateId, null));
		if(CollectionUtil.isEmpty(messageTemplateConfigs)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(messageTemplateConfigs, MessageTemplateConfigModel::new);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(MessageTemplateParam param) {
        this.remove(Wrappers.<MessageTemplateConfig>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(param.getId()), MessageTemplateConfig::getTemplateId,param.getId())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MessageTemplateConfig::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		List<MessageTemplateConfig> configs = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(param.getRoleIds())){
			param.getRoleIds().forEach(roleId -> {
				MessageTemplateConfig config = new MessageTemplateConfig();
				config.setTemplateId(param.getId());
				config.setPushId(roleId);
				config.setType(MessageTemplateConfigConstant.ROLE);
                config.setTenantId(WebFrameworkUtils.getHeaderTenantId());
				configs.add(config);
			});
		}
		if(CollectionUtil.isNotEmpty(param.getPersonIds())){
			param.getPersonIds().forEach(personId -> {
				MessageTemplateConfig config = new MessageTemplateConfig();
				config.setTemplateId(param.getId());
				config.setPushId(personId);
				config.setType(MessageTemplateConfigConstant.PERSON);
                config.setTenantId(WebFrameworkUtils.getHeaderTenantId());
				configs.add(config);
			});
		}
		if(CollectionUtil.isEmpty(configs)){
			return Boolean.TRUE;
		}
		return saveBatch(configs);
	}

	@Override
	public List<MessageTemplateConfigModel> findConfigs(List<Long> templateIds) {
		List<MessageTemplateConfig> messageTemplateConfigs = list(Wrappers.<MessageTemplateConfig>lambdaQuery().in(MessageTemplateConfig::getTemplateId, templateIds));
		return BeanUtils.convertListTo(messageTemplateConfigs, MessageTemplateConfigModel::new);
	}

	/***
	 * @Description 查询字段的处理
	 * @author huangyongtao
	 * @date 2024/10/28 11:07
	 * @param templateId
	 * @param type
	 */
	private LambdaQueryWrapper<MessageTemplateConfig> handlePublicQuery(Long templateId, String type){
		LambdaQueryWrapper<MessageTemplateConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(ObjectUtil.isNotEmpty(templateId), MessageTemplateConfig::getTemplateId,templateId)
				.eq(ObjectUtil.isNotEmpty(type), MessageTemplateConfig::getType, type)
				.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MessageTemplateConfig::getTenantId, WebFrameworkUtils.getHeaderTenantId())
				.orderByDesc(MessageTemplateConfig::getCreateTime);
		return queryWrapper;
	}

}
