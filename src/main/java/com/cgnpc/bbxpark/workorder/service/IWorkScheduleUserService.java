
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkScheduleUser;
import com.cgnpc.bbxpark.workorder.dto.model.WorkScheduleUserModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkScheduleUserParam;

import java.util.List;

/***
 * @Description 工单排班人员服务接口
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
public interface IWorkScheduleUserService extends IService<WorkScheduleUser> {

	/**
	 * 获取工单排班人员列表.
	 * @Param workId 工单标识
	 * @Return 工单排班人员信息列表
	 */
	List<WorkScheduleUserModel> list(Long workId);

	/**
	 * 新增工单排班人员.
	 * @Param param 工单排班人员信息
	 * @Return 新增工单排班人员是否成功
	 */
	Boolean add(WorkScheduleUserParam param);

	/**
	 * 批量新增工单排班人员.
	 * @Param params 工单排班人员信息列表
	 * @Return 批量新增工单排班人员是否成功
	 */
	Boolean addBatch(List<WorkScheduleUserParam> params);

	/**
	 * 删除工单排班人员.
	 * @Param id 工单排班人员标识
	 * @Return 删除工单排班人员是否成功
	 */
	Boolean remove(Long id);
}
