
package com.cgnpc.bbxpark.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.MessageTemplateConfigConstant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.MessagePushChannelEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.message.domain.MessageTemplate;
import com.cgnpc.bbxpark.message.dto.req.MessageRoleParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplatePageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageTemplateParam;
import com.cgnpc.bbxpark.message.dto.resp.*;
import com.cgnpc.bbxpark.message.mapper.MessageTemplateRepository;
import com.cgnpc.bbxpark.message.service.IMessageTemplateConfigService;
import com.cgnpc.bbxpark.message.service.IMessageTemplateService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserInfoListParam;
import com.cgnpc.pro.api.ICudUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/***
 * @Description 消息模版服务实现
 * @author huangyongtao
 * @date 2024/10/24 17:25
 */
@Service("messageTemplateService")
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateRepository, MessageTemplate> implements IMessageTemplateService {

    @Autowired
    private ICudUserService cudUserService;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IRoleApiService roleApiService;

	@Autowired
	private IMessageTemplateConfigService messageTemplateConfigService;


	/**
	 * 根据消息模版标识获得消息模版详情信息.
	 * @Param [id] 消息模版标识
	 * @Return 消息模版详情信息
	 */
	@Override
	public MessageTemplateModel detail(MessageTemplateParam param) {
		MessageTemplate messageTemplate = getById(param.getId());
		AssertUtils.notNull(messageTemplate, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(messageTemplate, MessageTemplateModel::new);
	}

	/**
	 * 获取消息模版列表(分页).
	 * @Param param 消息模版查询条件
	 * @Return 消息模版信息列表（分页）
	 */
	@Override
	public IPage<MessageTemplateModel> page(MessageTemplatePageParam param) {
		IPage<MessageTemplate> page = new Page<>(param.getCurrent(), param.getSize());
		MessageTemplateListParam listParam =BeanUtils.convertTo(param, MessageTemplateListParam::new);
		listParam.setTenantId(ObjectUtil.isEmpty(param.getTenantId()) ? WebFrameworkUtils.getHeaderTenantId():param.getTenantId());
		IPage<MessageTemplate> messageTemplatePage = this.page(page, handlePublicQuery(listParam));
        return ConvertUtil.pageConvert(messageTemplatePage,BeanUtils.convertListTo(messageTemplatePage.getRecords(), MessageTemplateModel::new));
	}

	/**
	 * 获取消息模版列表.
	 * @Param param 消息模版查询条件
	 * @Return 消息模版信息列表
	 */
	@Override
	@SneakyThrows
	public List<MessageTemplateModel> list(MessageTemplateListParam param) {
		param.setTenantId(ObjectUtil.isEmpty(param.getTenantId()) ? WebFrameworkUtils.getHeaderTenantId():param.getTenantId());
		List<MessageTemplate> messageTemplates = this.list(handlePublicQuery(param));
		if(CollectionUtil.isEmpty(messageTemplates)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(messageTemplates, MessageTemplateModel::new);
	}

	/***
	 * @Description 查询字段的处理
	 * @author huangyongtao
	 * @date 2024/10/25 11:31
	 * @param param
	 */
	private LambdaQueryWrapper<MessageTemplate> handlePublicQuery(MessageTemplateListParam param){
		LambdaQueryWrapper<MessageTemplate> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(ObjectUtil.isNotEmpty(param.getType()), MessageTemplate::getType, param.getType())
				.eq(ObjectUtil.isNotEmpty(param.getStatus()), MessageTemplate::getStatus, param.getStatus())
				.eq(ObjectUtil.isNotEmpty(param.getCode()), MessageTemplate::getCode, param.getCode())
				.like(ObjectUtil.isNotEmpty(param.getPushChannel()), MessageTemplate::getPushChannel, param.getPushChannel())
				.like(ObjectUtil.isNotEmpty(param.getTitle()), MessageTemplate::getTitle, param.getTitle())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), MessageTemplate::getTenantId, param.getTenantId())
				.eq(MessageTemplate::getDeleted, Status.enabled.getKey())
				.orderByDesc(MessageTemplate::getCreateTime);
		return queryWrapper;
	}

	/**
	 * 新增消息模版.
	 * @Param param 消息模版信息
	 * @Return 新增消息模版是否成功
	 */
	@Override
	public Boolean add(MessageTemplateParam param) {
		MessageTemplate messageTemplate = BeanUtils.convertTo(param, MessageTemplate::new);
		messageTemplate.setId(null);
        messageTemplate.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		messageTemplate.setPushChannel(MessagePushChannelEnum.INFO.getCode());
		AssertUtils.isFalse(checkCode(param.getCode()), "模版编号已存在，请确认");
		return save(messageTemplate);
	}

	/**
	 * 删除消息模版.
	 * @Param id 消息模版标识
	 * @Return 删除消息模版是否成功
	 */
	@Override
	public Boolean remove(MessageTemplateParam param) {
		MessageTemplate messageTemplate = getById(param.getId());
		AssertUtils.notNull(messageTemplate, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isNotEquals(messageTemplate.getStatus(), Status.enabled.getKey(), "请将消息模板禁用后再删除");
		MessageTemplate editParam = new MessageTemplate();
		editParam.setId(param.getId());
		editParam.setDeleted( Status.disabled.getKey());
		return updateById(editParam);
	}


	/**
	 * 编辑消息模版信息.
	 * @Param param 消息模版信息
	 * @Return 编辑消息模版是否成功
	 */
	@Override
	public Boolean edit(MessageTemplateParam param) {
		MessageTemplate messageTemplate = getById(param.getId());
		AssertUtils.notNull(messageTemplate, SystemResultCode.RESULT_DATA_NONE.message());
		MessageTemplate editParam = new MessageTemplate();
		editParam.setId(param.getId());
		editParam.setTitle(param.getTitle());
		editParam.setType(param.getType());
		editParam.setContent(param.getContent());
		return updateById(editParam);
	}

	/**
	 * 启用消息模版.
	 * @Param id 消息模版标识
	 * @Return 启用消息模版是否成功
	 */
	@Override
	public Boolean enable(MessageTemplateParam param) {
		MessageTemplate messageTemplate = new MessageTemplate();
	 	messageTemplate.setId(param.getId());
		messageTemplate.setStatus( Status.enabled.getKey());
		return updateById(messageTemplate);
	}


	/**
	 * 禁用消息模版.
	 * @Param id 消息模版标识
	 * @Return 禁用消息模版是否成功
	 */
	@Override
	public Boolean disable(MessageTemplateParam param) {
		MessageTemplate messageTemplate = new MessageTemplate();
		messageTemplate.setId(param.getId());
		messageTemplate.setStatus( Status.disabled.getKey());
		return updateById(messageTemplate);
	}

	@Override
	public MessageTemplateModel detailConfig(MessageTemplateParam param) {
		MessageTemplate messageTemplate = getById(param.getId());
		AssertUtils.notNull(messageTemplate, SystemResultCode.RESULT_DATA_NONE.message());
		MessageTemplateModel model =  BeanUtils.convertTo(messageTemplate, MessageTemplateModel::new);
		if(ObjectUtil.isNotEmpty(model.getPushChannel())){
			model.setPushChannelList(Arrays.asList(model.getPushChannel().split(",")));
		}
		List<MessageTemplateConfigModel> configModels = messageTemplateConfigService.list(param.getId());
		if(CollectionUtil.isNotEmpty(configModels)){
			List<MessageTemplateConfigModel> roles = configModels.stream().filter(item -> MessageTemplateConfigConstant.ROLE.equals(item.getType())).collect(Collectors.toList());
			List<MessageTemplateConfigModel> users = configModels.stream().filter(item -> MessageTemplateConfigConstant.PERSON.equals(item.getType())).collect(Collectors.toList());
			handelPersonConfig(users);
			model.setRoleConfigList(roles);
			model.setPersonConfigList(users);
		}
		return model;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addConfig(MessageTemplateParam param) {
		MessageTemplate messageTemplate = getById(param.getId());
		AssertUtils.notNull(messageTemplate, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.notNull(param.getPushFlag(), "推送标识不能为空");
		if(CollectionUtil.isEmpty(param.getPushChannelList())){
			param.setPushChannel(null);
		}
		LambdaUpdateWrapper<MessageTemplate> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.set(MessageTemplate::getPushChannel, String.join(",", param.getPushChannelList()));
		updateWrapper.set(MessageTemplate::getPushFlag, param.getPushFlag());
		updateWrapper.eq(MessageTemplate::getId, param.getId());
		update(updateWrapper);
		if(param.getPushFlag().equals(Status.enabled.getKey())){
			messageTemplateConfigService.add(param);
		}
		return Boolean.TRUE;
	}

	@Override
	public List<MessageTemplateModel> findTemplates(MessageTemplateListParam param) {
		List<MessageTemplateModel> models = this.list(param);
		if(CollectionUtil.isEmpty(models)){
			return new ArrayList<>();
		}
		List<Long> ids = models.stream().map(MessageTemplateModel::getId).collect(Collectors.toList());
		List<MessageTemplateConfigModel> configModels = messageTemplateConfigService.findConfigs(ids);
		if(CollectionUtil.isEmpty(configModels)){
			return models;
		}
		handelTemplateConfig(configModels, models);
		return models;
	}

    @Override
    public List<MessageUserInfoModel> findUserList(UserInfoListParam condition) {
        if(StringUtils.isEmpty(condition.getId()) && CollectionUtils.isEmpty(condition.getIds())){
            return Collections.emptyList();
        }
        List<String> staffNos = new ArrayList<>();
        if(StringUtils.isNotEmpty(condition.getId())){
            staffNos.add(condition.getId());
        }
        if(CollectionUtils.isNotEmpty(condition.getIds())){
            staffNos.addAll(condition.getIds());
        }
        List<UserInfoModel> staffs = userApiService.getByStaffNos(staffNos);
        return staffs.stream().map(staff->{
            MessageUserInfoModel userInfo = new MessageUserInfoModel();
            userInfo.setId(staff.getStaffNo());
            userInfo.setNickName(staff.getUserName());
            userInfo.setEmail(staff.getEmail());
            userInfo.setMobile(staff.getMobile());
            userInfo.setStaffid(staff.getStaffNo());
            userInfo.setSexDesc(staff.getSexDesc());
            return userInfo;
        }).collect(Collectors.toList());
    }

    @Override
	public List<MessageRoleModel> findRoleList(MessageRoleParam param) {
        List<RoleModel> roles = roleApiService.findRoles();
        return roles.stream().map(r->{
            MessageRoleModel model = new MessageRoleModel();
            model.setId(r.getRoleId());
            model.setCode(r.getRoleCode());
            model.setName(r.getRoleName());
            return model;
        }).collect(Collectors.toList());
	}

	private void handelTemplateConfig(List<MessageTemplateConfigModel> configModels, List<MessageTemplateModel> models){
		Map<Long, List<MessageTemplateConfigModel>> configMap = configModels.stream().collect(Collectors.groupingBy(MessageTemplateConfigModel::getTemplateId));
		List<String> roleIds = new ArrayList<>();
		List<String> personIds = new ArrayList<>();
        List<String> staffNos = new ArrayList<>();
		configModels.forEach(item->{
			if(MessageTemplateConfigConstant.ROLE.equals(item.getType())){
				if(!roleIds.contains(item.getPushId())){
					roleIds.add(item.getPushId());
				}
			}else{
				if(!personIds.contains(item.getPushId())){
					personIds.add(item.getPushId());
				}
			}
		});
		Map<String, String> roleNameMap = new HashMap<>();
		Map<String, List<String>> userRoleMap = new HashMap<>();
		if(CollectionUtil.isNotEmpty(roleIds)){
            //先查出所有角色
            List<RoleModel> roles = roleApiService.findRoles();
            if(CollectionUtil.isNotEmpty(roles)){
                roles.stream().filter(r->roleIds.contains(r.getRoleId())).forEach(role->{
                    roleNameMap.put(role.getRoleId(),role.getRoleName());
                    List<String> userIds = roleApiService.findStaffNoByRoleCode(role.getRoleCode());
                    if(CollectionUtil.isNotEmpty(userIds)){
                        personIds.addAll(userIds);
                        userRoleMap.put(role.getRoleId(),userIds);
                    }
                });
            }
		}

		UserInfoListParam userParam = new UserInfoListParam();
		userParam.setIds(personIds);
		List<MessageUserInfoModel> userInfoModels = findUserList(userParam);
		if(CollectionUtil.isEmpty(userInfoModels)){
			return;
		}
		Map<String, MessageUserInfoModel> userMap = userInfoModels.stream().collect(Collectors.toMap(MessageUserInfoModel::getId, Function.identity()));
		for(MessageTemplateModel model : models){
			List<MessageUserInfoModel> userModelList = new ArrayList<>();
			List<String> userIds = new ArrayList<>();
			List<MessageTemplateConfigModel> configs = configMap.get(model.getId());
			if(CollectionUtil.isEmpty(configs) || model.getPushFlag().equals(Status.disabled.getKey())){
				continue;
			}

			List<MessageTemplateConfigModel> personConfigs = configs.stream().filter(item->MessageTemplateConfigConstant.PERSON.equals(item.getType())).collect(Collectors.toList());
			List<MessageTemplateConfigModel> roleConfigs = configs.stream().filter(item->MessageTemplateConfigConstant.ROLE.equals(item.getType())).collect(Collectors.toList());
			for(MessageTemplateConfigModel configModel : personConfigs){
				userIds.add(configModel.getPushId());
				userModelList.add(userMap.get(configModel.getPushId()));
			}
			for(MessageTemplateConfigModel configModel : roleConfigs){
				List<String> userRoleDomains = userRoleMap.get(configModel.getPushId());
                if(CollectionUtil.isEmpty(userRoleDomains)){
                    continue;
                }
                userRoleDomains.forEach(userId->{
                    if(!userIds.contains(userId)){
                        userIds.add(userId);
						MessageUserInfoModel userModel = userMap.get(userId);
						userModel.setRoleName(roleNameMap.get(configModel.getPushId()));
						userModelList.add(userModel);
                    }
                });
			}
			model.setUserModelList(userModelList);
		}
	}

	private void handelPersonConfig(List<MessageTemplateConfigModel> users){
		if(CollectionUtil.isEmpty(users)){
			return;
		}
        List<String> userIds = users.stream().map(MessageTemplateConfigModel::getPushId).collect(Collectors.toList());
        List<UserInfoModel> userModel = userApiService.getByStaffNos(userIds);
		if(CollectionUtil.isEmpty(userModel)){
			return;
		}
		Map<String, UserInfoModel> userMap = userModel.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo, Function.identity()));
		users.forEach(item->{
            UserInfoModel user = userMap.get(item.getPushId());
			if(user != null){
				item.setPushName(user.getUserName());
				item.setStaffid(user.getStaffNo());
			}
		});
	}

	private boolean checkCode(String code){
		MessageTemplateListParam param = new MessageTemplateListParam();
		param.setCode(code);
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		return  count(handlePublicQuery(param)) > 0;
	}

}
