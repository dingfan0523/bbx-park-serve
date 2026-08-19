package com.cgnpc.bbxpark.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.message.domain.MessageUser;
import com.cgnpc.bbxpark.message.dto.req.*;
import com.cgnpc.bbxpark.message.dto.resp.MessageUserModel;
import com.cgnpc.bbxpark.message.dto.resp.UnreadModel;

import java.util.List;

/**
 * 用户消息服务接口
 */
public interface IMessageUserService extends IService<MessageUser> {

	/**
	 * 根据用户消息标识获得用户消息详情信息.
	 * @Param [id] 用户消息标识
	 * @Return 用户消息详情信息
	 */
	MessageUserModel detail(Long id);

	/**
	 * 用户签收
	 * @param id [id] 用户消息标识
	 * @return 是否成功
	 */
	Boolean confirm(Long id,Integer status);

	/**
	 * 用户点赞
	 * @param id [id] 用户消息标识
	 * @return 是否成功
	 */
	Boolean like(Long id,Integer status);

	/**
	 * 回复
	 * @param param 回复参数
	 * @return 是否成功
	 */
	Boolean reply(MessageReplyParam param);

	/**
	 * 用户收藏
	 * @param id [id] 用户消息标识
	 * @return 是否成功
	 */
	Boolean collect(Long id,Integer status);

	/**
	 * 获取用户消息列表(分页).
	 * @Param param 用户消息查询条件
	 * @Return 用户消息信息列表（分页）
	 */
	IPage<MessageUserModel> page(MessageUserPageParam param);

    IPage<MessageUserModel> boxPage(MessageUserPageParam param);

	/**
	 * 获取用户消息列表.
	 * @Param param 用户消息查询条件
	 * @Return 用户消息信息列表
	 */
	List<MessageUserModel> list(MessageUserListParam param);

	/**
	 * 新增用户消息.
	 * @Param param 用户消息信息
	 * @Return 新增用户消息是否成功
	 */
	Boolean add(MessageUserParam param);

	/**
	 * 批量新增用户消息.
	 * @Param params 用户消息信息列表
	 * @Return 批量新增用户消息是否成功
	 */
	Boolean addBatch(List<MessageUserParam> params);

	/**
	 * 删除用户消息.
	 * @Param id 用户消息标识
	 * @Return 删除用户消息是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 清空用户消息.
	 * @Param id 用户消息标识
	 * @Return 删除用户消息是否成功
	 */
	 Boolean empty();

	/**
	 * 未读数量
	 * @param param 未读参数
	 * @return 未读数量
	 */
	Integer unread(UnreadParam param);

	/**
	 * 批量删除用户消息.
	 * @Param ids 用户消息标识列表
	 * @Return 批量删除用户消息是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/**
	 * 编辑用户消息信息.
	 * @Param param 用户消息信息
	 * @Return 编辑用户消息是否成功
	 */
	Boolean edit(Long id, MessageUserParam param);

	/**
	 * 批量编辑用户消息信息.
	 * @Param params 用户消息信息列表
	 * @Return 批量编辑用户消息是否成功
	 */
	Boolean editBatch(List<MessageUserParam> params);

	/**
	 * 启用用户消息.
	 * @Param id 用户消息标识
	 * @Return 启用用户消息是否成功
	 */
	Boolean enable(Long id);

	/**
	 * 批量启用用户消息信息.
	 * @Param ids 用户消息标识列表
	 * @Return 批量启用用户消息是否成功
	 */
	Boolean enableBatch(List<Long> ids);

	/**
	 * 禁用用户消息.
	 * @Param id 用户消息标识
	 * @Return 禁用用户消息是否成功
	 */
	Boolean disable(Long id);

	/**
	 * 批量禁用用户消息信息.
	 * @Param ids 用户消息标识列表
	 * @Return 批量禁用用户消息是否成功
	 */
	Boolean disableBatch(List<Long> ids);

	/**
	 * 	根据消息类型和用户ID查询消息
	 * @return 消息
	 */
	List<MessageUserModel> findByTypeMessage(MessageUserPageParam param);

	Boolean removeUserBatch(MessageUserRemoveParam removeParam);

    IPage<MessageUserModel> pageZy(MessageUserPageParam param);

	/**
	 * 根据消息类型和用户ID查询未读数量
	 * @param param 未读参数
	 * @return 未读数量
	 */
	List<UnreadModel> appUnread(UnreadParam param);

	/**
	 * 批量已读用户消息
	 * @param ids
	 * @return
	 */
	Boolean batchRead(List<Long> ids);

    int count();
}
