
package com.cgnpc.bbxpark.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.message.domain.MessageInfo;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoPageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageInfoParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageInfoModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.UserPageParam;

import java.util.List;


/**
 * 消息内容服务接口
 */
public interface IMessageInfoService extends IService<MessageInfo> {

	/**
	 * 根据消息内容标识获得消息内容详情信息.
	 * @Param [id] 消息内容标识
	 * @Return 消息内容详情信息
	 */
	MessageInfoModel detail(Long id, String userId);

	/**
	 * 获取消息内容列表(分页).
	 * @Param param 消息内容查询条件
	 * @Return 消息内容信息列表（分页）
	 */
	IPage<MessageInfoModel> page(MessageInfoPageParam param);

	/**
	 * 获取消息内容列表.
	 * @Param param 消息内容查询条件
	 * @Return 消息内容信息列表
	 */
	List<MessageInfoModel> list(MessageInfoListParam param);

	/**
	 * 新增消息内容.
	 * @Param param 消息内容信息
	 * @Return 新增消息内容是否成功
	 */
	Boolean add(MessageInfoParam param);

	/**
	 * 批量新增消息内容.
	 * @Param params 消息内容信息列表
	 * @Return 批量新增消息内容是否成功
	 */
	Boolean addBatch(List<MessageInfoParam> params);

	/**
	 * 删除消息内容.
	 * @Param id 消息内容标识
	 * @Return 删除消息内容是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 发布
	 * @return 是否成功
	 */
	 Boolean announce(Long id,Long typeId);
	/**
	 * 撤销
	 * @param id 消息内容标识
	 * @return 是否成功
	 */
	Boolean revocation(Long id);

	/**
	 * 批量删除消息内容.
	 * @Param ids 消息内容标识列表
	 * @Return 批量删除消息内容是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/**
	 * 编辑消息内容信息.
	 * @Param param 消息内容信息
	 * @Return 编辑消息内容是否成功
	 */
	Boolean edit(Long id, MessageInfoParam param);

	/**
	 * 批量编辑消息内容信息.
	 * @Param params 消息内容信息列表
	 * @Return 批量编辑消息内容是否成功
	 */
	Boolean editBatch(List<MessageInfoParam> params);

	/**
	 * 启用消息内容.
	 * @Param id 消息内容标识
	 * @Return 启用消息内容是否成功
	 */
	Boolean enable(Long id);

	/**
	 * 批量启用消息内容信息.
	 * @Param ids 消息内容标识列表
	 * @Return 批量启用消息内容是否成功
	 */
	Boolean enableBatch(List<Long> ids);

	/**
	 * 禁用消息内容.
	 * @Param id 消息内容标识
	 * @Return 禁用消息内容是否成功
	 */
	Boolean disable(Long id);

	/**
	 * 批量禁用消息内容信息.
	 * @Param ids 消息内容标识列表
	 * @Return 批量禁用消息内容是否成功
	 */
	Boolean disableBatch(List<Long> ids);

	IPage<UserInfoModel> message_user_page(UserPageParam param);
}
