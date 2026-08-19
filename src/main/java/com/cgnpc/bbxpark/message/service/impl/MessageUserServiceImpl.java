package com.cgnpc.bbxpark.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.MessageTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.message.domain.MessageInfo;
import com.cgnpc.bbxpark.message.domain.MessageUser;
import com.cgnpc.bbxpark.message.dto.req.*;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import com.cgnpc.bbxpark.message.dto.resp.UnreadModel;
import com.cgnpc.bbxpark.message.mapper.MessageInfoRepository;
import com.cgnpc.bbxpark.message.mapper.MessageUserRepository;
import com.cgnpc.bbxpark.message.service.IMessageUserService;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户消息服务实现
 */
@Service
public class MessageUserServiceImpl extends ServiceImpl<MessageUserRepository, MessageUser> implements IMessageUserService {
    /**
     * 注入repository.
     */
	@Autowired
	private MessageUserRepository messageUserRepository;

	@Autowired
	private MessageInfoRepository messageInfoRepository;
    @Autowired
    private IUserApiService userApiService;

	/**
	 * 根据用户消息标识获得用户消息详情信息.
	 * @Param [id] 用户消息标识
	 * @Return 用户消息详情信息
	 */
	@Override
	@Transactional
	public synchronized MessageUserModel detail(Long id) {
		MessageUser messageUser = this.getById(id);
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		MessageUser editParam = new MessageUser();
		editParam.setId(id);
		editParam.setReadStatus(1);
		updateById(editParam);
		MessageInfo messageInfoOne = messageInfoRepository.selectById(messageUser.getMsgId());
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(messageUser.getMsgId());
		messageInfo.setReadCount(messageInfoOne.getReadCount()+1);
		messageInfo.setPublishTime(messageInfoOne.getPublishTime());
		messageInfoRepository.updateById(messageInfo);
		MessageUserModel model = BeanUtils.convertTo(messageUser, MessageUserModel::new);
        model.setContent(messageInfoOne.getContent());
        model.setSummary(messageInfoOne.getSummary());
        model.setTitle(messageInfoOne.getTitle());
        model.setPublishTime(messageInfo.getPublishTime());
        if(StringUtils.isNotEmpty(messageInfoOne.getPublisher())){
            UserInfoModel user = userApiService.getByStaffNo(messageInfoOne.getPublisher());
            model.setPublisherName(user.getUserName());
        }
        return model;
	}

	/**
	 * 用户签收
	 *
	 * @param id [id] 用户消息标识
	 * @return 是否成功
	 */
	@Override
	@Transactional
	public synchronized Boolean confirm(Long id,Integer status) {
		MessageUser messageUser = this.getById(id);
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		MessageUser editParam = new MessageUser();
		editParam.setId(id);
		editParam.setConfirmStatus(status);
		MessageInfo messageInfoOne = messageInfoRepository.selectById(messageUser.getMsgId());
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(messageUser.getMsgId());
		messageInfo.setConfirmCount(messageInfoOne.getConfirmCount()+1);
		messageInfo.setPublishTime(messageInfoOne.getPublishTime());
		messageInfoRepository.updateById(messageInfo);
		return updateById(editParam);
	}

	/**
	 * 用户点赞
	 *
	 * @param id [id] 用户消息标识
	 * @return 是否成功
	 */
	@Override
	@Transactional
	public synchronized Boolean like(Long id,Integer status) {
		MessageUser messageUser = this.getById(id);
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		MessageUser editParam = new MessageUser();
		editParam.setId(id);
		editParam.setLikeStatus(status);
		MessageInfo messageInfoOne = messageInfoRepository.selectById(messageUser.getMsgId());
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(messageUser.getMsgId());
		messageInfo.setLikeCount(messageInfoOne.getLikeCount()+1);
		messageInfo.setPublishTime(messageInfoOne.getPublishTime());
		messageInfoRepository.updateById(messageInfo);
		return updateById(editParam);
	}

	/**
	 * 回复
	 *
	 * @param param 回复参数
	 * @return 是否成功
	 */
	@Override
	@Transactional
	public synchronized Boolean reply(MessageReplyParam param) {
		MessageUser messageUser = this.getById(param.getId());
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		MessageUser editParam = new MessageUser();
		editParam.setId(param.getId());
		editParam.setReplyContent(param.getReplyContent());
		MessageInfo messageInfoOne = messageInfoRepository.selectById(messageUser.getMsgId());
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(messageUser.getMsgId());
		messageInfo.setReplyCount(messageInfoOne.getReplyCount()+1);
		messageInfo.setPublishTime(messageInfoOne.getPublishTime());
		messageInfoRepository.updateById(messageInfo);
		return updateById(editParam);
	}

	/**
	 * 用户收藏
	 *
	 * @param id [id] 用户消息标识
	 * @return 是否成功
	 */
	@Override
	@Transactional
	public synchronized Boolean collect(Long id,Integer status) {
		MessageUser messageUser = this.getById(id);
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		MessageUser editParam = new MessageUser();
		editParam.setId(id);
		editParam.setCollectStatus(status);
		MessageInfo messageInfoOne = messageInfoRepository.selectById(messageUser.getMsgId());
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(messageUser.getMsgId());
		messageInfo.setCollectCount(messageInfoOne.getCollectCount()+1);
		messageInfo.setPublishTime(messageInfoOne.getPublishTime());
		messageInfoRepository.updateById(messageInfo);
		return updateById(editParam);
	}

	/**
	 * 获取用户消息列表(分页).
	 * @Param param 用户消息查询条件
	 * @Return 用户消息信息列表（分页）
	 */
	@Override
	public IPage<MessageUserModel> page(MessageUserPageParam param) {
        param.setUserId(userApiService.getCurrentStaffNo());
		if (ObjectUtil.isEmpty(param.getStatus())){
			param.setStatus(3);
		}
        return this.getBaseMapper().findPage(new Page<>(param.getCurrent(),param.getSize()),param);
	}

	/**
	 * 获取用户消息列表.
	 * @Param param 用户消息查询条件
	 * @Return 用户消息信息列表
	 */
	@Override
	@SneakyThrows
	public List<MessageUserModel> list(MessageUserListParam param) {
		param.setStatus(6);
//		MessageUser messageUser = BeanUtils.objConvert(param, MessageUser.class);
        return this.getBaseMapper().findList(param);
	}

	@Override
	public IPage<MessageUserModel> boxPage(MessageUserPageParam param) {
        param.setUserId(userApiService.getCurrentStaffNo());
		if (ObjectUtil.isEmpty(param.getStatus())){
			param.setStatus(6);
		}
        return this.getBaseMapper().findPage(new Page<>(param.getCurrent(),param.getSize()),param);
	}

	/**
	 * 新增用户消息.
	 * @Param param 用户消息信息
	 * @Return 新增用户消息是否成功
	 */
	@Override
	public Boolean add(MessageUserParam param) {
		MessageUser messageUser = BeanUtils.convertTo(param, MessageUser::new);
		messageUser.setId(null);
        return save(messageUser);
	}

	/**
	 * 批量新增用户消息.
	 * @Param params 用户消息信息列表
	 * @Return 批量新增用户消息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<MessageUserParam> params) {
		List<MessageUser> messageUsers = BeanUtils.convertListTo(params, MessageUser::new);
        return this.saveBatch(messageUsers);
	}

	/**
	 * 删除用户消息.
	 * @Param id 用户消息标识
	 * @Return 删除用户消息是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		MessageUser messageUser = this.getById(id);
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		messageUser.setStatus(Status.disabled.getKey());
        messageUser.setDeleted(Delete.DELETED.getKey());
		messageUserRepository.updateById(messageUser);
		MessageInfo messageInfoOne = messageInfoRepository.selectById(messageUser.getMsgId());
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(messageUser.getMsgId());
		messageInfo.setStatus(3);
		messageInfo.setPublishTime(messageInfoOne.getPublishTime());
		return messageInfoRepository.updateById(messageInfo) > 0;
	}

	/**
	 * 清空用户消息.
	 * @Param id 用户消息标识
	 * @Return 删除用户消息是否成功
	 */
	@Override
	public Boolean empty() {
        List<MessageUser> messageUsers = this.list(Wrappers.<MessageUser>lambdaQuery().eq(MessageUser::getUserId,userApiService.getCurrentStaffNo()));
		List<MessageUser> collect = messageUsers.stream().map(a -> {
			MessageUser message = new MessageUser();
			message.setId(a.getId());
			message.setStatus(2);
			return message;
		}).collect(Collectors.toList());
        return this.updateBatchById(collect);
	}

	/**
	 * 未读数量
	 *
	 * @param param 未读参数
	 * @return 未读数量
	 */
	@Override
	public Integer unread(UnreadParam param) {
//        param.setUserId(userApiService.getCurrentStaffNo());
//		List<MessageConfig> messageConfigs = messageConfigRepository.selectList(new WhereClause().addEq(MessageConfigExt.C_MUTE, MessageConfigExt.DEFAULT_0).addEq
//				(MessageConfigExt.C_ACTION, param.getAction()));
//		Integer integer = 0;
//		if (ObjectUtil.isNotEmpty(messageConfigs)){
//			List<Long> typeIdList = messageConfigs.stream().distinct().map(MessageConfig::getTypeId).collect(Collectors.toList());
//			List<MessageInfo> messageInfos = messageInfoRepository.selectList(new WhereClause().addIn(MessageInfoExt.C_TYPE_ID, typeIdList));
//			List<Long> infoIds = messageInfos.stream().map(MessageInfo::getId).collect(Collectors.toList());
//			 integer = messageUserRepository.selectCount(new WhereClause().addEq(MessageUserExt.C_USER_ID, param.getUserId()).addIn(MessageUserExt.C_MSG_ID, infoIds).addEq(MessageUserExt.C_READ_STATUS,MessageUserExt.DEFAULT_0).addEq(MessageUserExt.C_STATUS,MessageUserExt.DEFAULT_0));
//
//		}else {
//			integer = messageUserRepository.selectCount(new WhereClause().addEq(MessageUserExt.C_USER_ID, param.getUserId()).addEq(MessageUserExt.C_READ_STATUS,MessageUserExt.DEFAULT_0).addEq(MessageUserExt.C_STATUS,MessageUserExt.DEFAULT_0));
//		}
//		return integer;
        List<UnreadModel> list = appUnread(param);
        return list.stream().filter(m->m.getType() != 0 && m.getType() != 1).mapToInt(m-> Math.toIntExact(m.getUnreadCount())).sum();
//        return this.count(Wrappers.<MessageUser>lambdaQuery()
//                .eq(StringUtils.isNotEmpty(param.getUserId()),MessageUser::getUserId,param.getUserId())
//                .eq(MessageUser::getReadStatus,0).eq(MessageUser::getDeleted, Delete.NORMAL.getKey()));
	}

	/**
	 * 批量删除用户消息.
	 * @Param ids 用户消息标识列表
	 * @Return 批量删除用户消息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeBatch(List<Long> ids) {
        return this.removeByIds(ids);
	}

	/**
	 * 编辑用户消息信息.
	 * @Param param 用户消息信息
	 * @Return 编辑用户消息是否成功
	 */
	@Override
	public Boolean edit(Long id, MessageUserParam param) {
		MessageUser messageUser = this.getById(id);
		AssertUtils.notNull(messageUser, SystemResultCode.RESULT_DATA_NONE.message());
		MessageUser editParam = BeanUtils.convertTo(param, MessageUser::new);
		/**
		 * 保护不可编辑字段
		 */
		return this.updateById(editParam);
	}

	/**
	 * 批量编辑用户消息信息.
	 * @Param params 用户消息信息列表
	 * @Return 批量编辑用户消息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean editBatch(List<MessageUserParam> params) {
		List<MessageUser> messageUsers = BeanUtils.convertListTo(params, MessageUser::new);
        return updateBatchById(messageUsers);
	}

	/**
	 * 启用用户消息.
	 * @Param id 用户消息标识
	 * @Return 启用用户消息是否成功
	 */
	@Override
	public Boolean enable(Long id) {
		MessageUser messageUser = new MessageUser();
	 	messageUser.setId(id);
		messageUser.setStatus(Status.enabled.getKey());
        return this.updateById(messageUser);
	}

	/**
	 * 批量启用用户消息信息.
	 * @Param ids 用户消息标识列表
	 * @Return 批量启用用户消息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean enableBatch(List<Long> ids) {
		List<MessageUser> messageUsers = ids.stream().map(id -> {
			MessageUser messageUser = new MessageUser();
			messageUser.setId(id);
			messageUser.setStatus(Status.enabled.getKey());
			return messageUser;
		}).collect(Collectors.toList());
        return this.updateBatchById(messageUsers);
	}

	/**
	 * 禁用用户消息.
	 * @Param id 用户消息标识
	 * @Return 禁用用户消息是否成功
	 */
	@Override
	public Boolean disable(Long id) {
		MessageUser messageUser = new MessageUser();
		messageUser.setId(id);
		messageUser.setStatus(Status.disabled.getKey());
        return this.updateById(messageUser);
	}

	/**
	 * 批量禁用用户消息信息.
	 * @Param ids 用户消息标识列表
	 * @Return 批量禁用用户消息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean disableBatch(List<Long> ids) {
		List<MessageUser> messageUsers = ids.stream().map(id -> {
			MessageUser messageUser = new MessageUser();
			messageUser.setId(id);
			messageUser.setStatus(Status.disabled.getKey());
			return messageUser;
		}).collect(Collectors.toList());
        return this.updateBatchById(messageUsers);
	}

	@Override
	public List<MessageUserModel> findByTypeMessage(MessageUserPageParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        param.setUserId(userApiService.getCurrentStaffNo());
		return messageInfoRepository.findByTypeMessage(param);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeUserBatch(MessageUserRemoveParam removeParam) {
        List<MessageUser> list = this.list(Wrappers.<MessageUser>lambdaQuery()
                .eq(removeParam.getMsgId() != null,MessageUser::getMsgId,removeParam.getMsgId())
                .in(CollectionUtils.isNotEmpty(removeParam.getUserIds()),MessageUser::getUserId,removeParam.getUserIds()));
		if(CollectionUtils.isNotEmpty(list)){
            List<Long> ids = list.stream().map(MessageUser::getId).collect(Collectors.toList());
            this.removeByIds(ids);
			MessageInfo messageInfo = messageInfoRepository.selectById(removeParam.getMsgId());
			String receiverId = messageInfo.getReceiverId();
			String[] split = receiverId.split(",");
			String newReceiverId = Arrays.stream(split).filter(id -> !removeParam.getUserIds().contains(Long.parseLong(id))).collect(Collectors.joining(","));
			messageInfo.setReceiverId(newReceiverId);
			return messageInfoRepository.updateById(messageInfo) > 0;
		}
		return true;
	}

	@Override
	public IPage<MessageUserModel> pageZy(MessageUserPageParam param) {
        param.setUserId(userApiService.getCurrentStaffNo());
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        return messageUserRepository.pageZy(new Page<>(param.getCurrent(),param.getSize()),param);
	}

	@Override
	public List<UnreadModel> appUnread(UnreadParam param) {
        param.setUserId(userApiService.getCurrentStaffNo());
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		return messageUserRepository.appUnread(param);
	}

	@Override
	public Boolean batchRead(List<Long> ids) {
		AssertUtils.notEmpty(ids, "请选择需要标记已读的消息！");
		List<MessageUser> messageUsers = list(Wrappers.<MessageUser>lambdaQuery().in(MessageUser::getId,ids));
		AssertUtils.notNull(messageUsers, SystemResultCode.RESULT_DATA_NONE.message());
		messageUsers.forEach(messageUser -> messageUser.setReadStatus(1));
        return this.updateBatchById(messageUsers);
	}

    public int count() {
        UnreadParam param = new UnreadParam();
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        //查询用户未读消息数量分组
        List<UnreadModel> list = this.appUnread(param);
        return Math.toIntExact(list.stream().filter(map -> Integer.valueOf(MessageTypeEnum.SECURE.getCode()).equals(map.getType()))
                .map(map -> map.getUnreadCount()).findFirst().orElse(0L));
    }
}
