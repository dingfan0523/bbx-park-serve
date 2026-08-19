
package com.cgnpc.bbxpark.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.device.domain.DeviceControlLog;
import com.cgnpc.bbxpark.device.dto.model.DeviceControlLogModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceControlLogPageParam;

import javax.servlet.http.HttpServletResponse;


public interface IDeviceControlLogService extends IService<DeviceControlLog> {

	IPage<DeviceControlLogModel> page(DeviceControlLogPageParam param);

	Boolean saveLog(Long deviceId,String channel,String result);

	/**
	 * 导出控制日志.
	 * @param response 响应对象
	 * @param param 控制日志查询条件
	 * @return 导出结果
	 */
	Boolean export(HttpServletResponse response, DeviceControlLogPageParam param);
}
