
package com.cgnpc.bbxpark.message.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.message.domain.MessageLog;
import com.cgnpc.bbxpark.message.dto.req.MessageLogListParam;
import com.cgnpc.bbxpark.message.dto.req.MessageLogPageParam;
import com.cgnpc.bbxpark.message.dto.req.MessageLogParam;
import com.cgnpc.bbxpark.message.dto.resp.MessageLogModel;
import com.cgnpc.bbxpark.message.mapper.MessageLogRepository;
import com.cgnpc.bbxpark.message.service.IMessageLogService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/***
 * @Description 消息日志服务实现
 * @author huangyongtao
 * @date 2024/10/25 11:59
 */
@Service("messageLogService")
public class MessageLogServiceImpl extends ServiceImpl<MessageLogRepository, MessageLog> implements IMessageLogService {

	/**
	 * 根据消息日志标识获得消息日志详情信息.
	 * @Param [id] 消息日志标识
	 * @Return 消息日志详情信息
	 */
	@Override
	public MessageLogModel detail(MessageLogParam param) {
		MessageLog messageLog = this.getById(param.getId());
		AssertUtils.notNull(messageLog, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(messageLog, MessageLogModel::new);
	}

	/**
	 * 获取消息日志列表(分页).
	 * @Param param 消息日志查询条件
	 * @Return 消息日志信息列表（分页）
	 */
	@Override
	public IPage<MessageLogModel> page(MessageLogPageParam param) {
		IPage<MessageLog> page = new Page<>(param.getCurrent(), param.getSize());
		MessageLogListParam listParam =BeanUtils.convertTo(param, MessageLogListParam::new);
		listParam.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		IPage<MessageLog> messageLogPage = this.page(page, handlePublicQuery(listParam));
        return ConvertUtil.pageConvert(messageLogPage,BeanUtils.convertListTo(messageLogPage.getRecords(), MessageLogModel::new));
	}

	/**
	 * 获取消息日志列表.
	 * @Param param 消息日志查询条件
	 * @Return 消息日志信息列表
	 */
	@Override
	@SneakyThrows
	public List<MessageLogModel> list(MessageLogListParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		List<MessageLog> messageLogs = this.list(handlePublicQuery(param));
		if(CollectionUtil.isEmpty(messageLogs)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(messageLogs, MessageLogModel::new);
	}

	/***
	 * @Description 查询字段的处理
	 * @author huangyongtao
	 * @date 2024/10/25 11:31
	 * @param param
	 */
	private LambdaQueryWrapper<MessageLog> handlePublicQuery(MessageLogListParam param){
		LambdaQueryWrapper<MessageLog> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(ObjectUtil.isNotEmpty(param.getType()), MessageLog::getType, param.getType())
				.eq(ObjectUtil.isNotEmpty(param.getStatus()), MessageLog::getStatus, param.getStatus())
				.eq(ObjectUtil.isNotEmpty(param.getPushChannel()), MessageLog::getPushChannel, param.getPushChannel())
				.like(ObjectUtil.isNotEmpty(param.getTitle()), MessageLog::getTitle, param.getTitle())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), MessageLog::getTenantId, param.getTenantId())
				.ge(ObjectUtil.isNotEmpty(param.getPushTimeStart()), MessageLog::getPushTime, param.getPushTimeStart())
				.le(ObjectUtil.isNotEmpty(param.getPushTimeEnd()), MessageLog::getPushTime, param.getPushTimeEnd())
				.orderByDesc(MessageLog::getPushTime);
		return queryWrapper;
	}

	/**
	 * 新增消息日志.
	 * @Param param 消息日志信息
	 * @Return 新增消息日志是否成功
	 */
	@Override
	public Boolean add(MessageLogParam param) {
		MessageLog messageLog = BeanUtils.convertTo(param, MessageLog::new);
		messageLog.setId(null);
        messageLog.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		return save(messageLog);
	}

	/**
	 * 编辑消息日志信息.
	 * @Param param 消息日志信息
	 * @Return 编辑消息日志是否成功
	 */
	@Override
	public Boolean edit(MessageLogParam param) {
		MessageLog messageLog = getById(param.getId());
		AssertUtils.notNull(messageLog, SystemResultCode.RESULT_DATA_NONE.message());
		MessageLog editParam = new MessageLog();
		editParam.setId(param.getId());
		editParam.setPushTime(new Date());
		return updateById(editParam);
	}

}
