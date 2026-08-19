package com.cgnpc.bbxpark.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.message.domain.MessageInfo;
import com.cgnpc.bbxpark.message.domain.MessageNotice;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticePageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageNoticeParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageNoticeModel;

import java.util.List;


/**
 * 通知公告服务接口
 */
public interface IMessageNoticeService extends IService<MessageNotice> {

	/**
	 * 根据通知公告标识获得通知公告详情信息.
	 * @Param [id] 通知公告标识
	 * @Return 通知公告详情信息
	 */
	MessageNoticeModel detail(Long id);

	/**
	 * 获取通知公告列表(分页).
	 * @Param param 通知公告查询条件
	 * @Return 通知公告信息列表（分页）
	 */
	IPage<MessageNoticeModel> page(MessageNoticePageParam param);

	/**
	 * 发布
	 * @return 是否成功
	 */
 	 Boolean publishNotify(Long id);
	/**
	 * 获取通知公告列表.
	 * @Param param 通知公告查询条件
	 * @Return 通知公告信息列表
	 */
	List<MessageNoticeModel> list(MessageNoticeListParam param);

	/**
	 * 新增通知公告.
	 * @Param param 通知公告信息
	 * @Return 新增通知公告是否成功
	 */
	Boolean add(MessageNoticeParam param);

	/**
	 * 批量新增通知公告.
	 * @Param params 通知公告信息列表
	 * @Return 批量新增通知公告是否成功
	 */
	Boolean addBatch(List<MessageNoticeParam> params);

	/**
	 * 删除通知公告.
	 * @Param id 通知公告标识
	 * @Return 删除通知公告是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除通知公告.
	 * @Param ids 通知公告标识列表
	 * @Return 批量删除通知公告是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/**
	 * 编辑通知公告信息.
	 * @Param param 通知公告信息
	 * @Return 编辑通知公告是否成功
	 */
	Boolean edit(Long id, MessageNoticeParam param);

	/**
	 * 撤销通知公告信息
	 * @param id 消息标识
	 * @return
	 */
	Boolean revocation(Long id);

	/**
	 * 批量编辑通知公告信息.
	 * @Param params 通知公告信息列表
	 * @Return 批量编辑通知公告是否成功
	 */
	Boolean editBatch(List<MessageNoticeParam> params);

    void messageUserInsert(MessageInfo messageInfo, String creatorId);

    void messageLogic(Integer receiverScope,String userId,Long msgId);
}
