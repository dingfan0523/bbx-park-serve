
package com.cgnpc.bbxpark.workorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.workorder.domain.WorkOrderDevice;
import com.cgnpc.bbxpark.workorder.mapper.WorkOrderDeviceRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkOrderDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工单关联设备服务实现
 */
@Service
public class WorkOrderDeviceServiceImpl extends ServiceImpl<WorkOrderDeviceRepository, WorkOrderDevice> implements IWorkOrderDeviceService {
    /**
     * 注入repository.
     */
	@Autowired
	private WorkOrderDeviceRepository workOrderDeviceRepository;


}
