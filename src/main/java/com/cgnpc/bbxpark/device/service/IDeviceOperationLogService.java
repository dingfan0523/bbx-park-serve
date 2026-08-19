
package com.cgnpc.bbxpark.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.device.domain.DeviceOperationLog;
import com.cgnpc.bbxpark.device.dto.model.DeviceOperationLogModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceOperationLogPageParam;
import com.cgnpc.cud.core.service.IBaseService;

/**
 * 设备控制（操作）日志服务接口
 */
public interface IDeviceOperationLogService extends IBaseService<DeviceOperationLog> {


	/**
	 * 保存设备操作日志
	 * @param msg kafka消息
	 */
	void saveDeviceOperationLog(String msg);

    IPage<DeviceOperationLogModel> findPage(DeviceOperationLogPageParam param);
}
