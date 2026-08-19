
package com.cgnpc.bbxpark.message.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.message.domain.MessageLog;
import com.cgnpc.bbxpark.message.dto.req.MessageLogListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageLogPageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageLogParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageLogModel;

import java.util.List;

/***
 * @Description 消息日志服务接口
 * @author huangyongtao
 * @date 2024/10/24 16:49
 */
public interface IMessageLogService extends IService<MessageLog> {

	/**
	 * 根据消息日志标识获得消息日志详情信息.
	 * @Param [id] 消息日志标识
	 * @Return 消息日志详情信息
	 */
	MessageLogModel detail(MessageLogParam param);

	/**
	 * 获取消息日志列表(分页).
	 * @Param param 消息日志查询条件
	 * @Return 消息日志信息列表（分页）
	 */
	IPage<MessageLogModel> page(MessageLogPageParam param);

	/**
	 * 获取消息日志列表.
	 * @Param param 消息日志查询条件
	 * @Return 消息日志信息列表
	 */
	List<MessageLogModel> list(MessageLogListParam param);

	/**
	 * 新增消息日志.
	 * @Param param 消息日志信息
	 * @Return 新增消息日志是否成功
	 */
	Boolean add(MessageLogParam param);

	/**
	 * 编辑消息日志信息.
	 * @Param param 消息日志信息
	 * @Return 编辑消息日志是否成功
	 */
	Boolean edit(MessageLogParam param);

}
