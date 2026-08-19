
package com.cgnpc.bbxpark.settings.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.domain.LoginLog;
import com.cgnpc.bbxpark.settings.dto.model.LoginLogModel;
import com.cgnpc.bbxpark.settings.dto.param.LoginLogPageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;


public interface ILoginLogService extends IBaseService<LoginLog> {

	/**
	 * 根据登录日志标识获得登录日志详情信息.
	 * @Param [id] 登录日志标识
	 * @Return 登录日志详情信息
	 */
	LoginLogModel detail(Long id);

	/**
	 * 获取登录日志列表(分页).
	 *
	 * @Param param 登录日志查询条件
	 * @Return 登录日志信息列表（分页）
	 */
	IPage<LoginLogModel> page(LoginLogPageParam param);


	/**
	 * 批量删除登录日志.
	 * @Param ids 登录日志标识列表
	 * @Return 批量删除登录日志是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/**
	 * 清空日志
	 *
	 * @return
	 */
	Boolean clean();

}
