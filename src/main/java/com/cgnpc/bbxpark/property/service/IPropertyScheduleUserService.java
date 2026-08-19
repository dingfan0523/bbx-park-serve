
package com.cgnpc.bbxpark.property.service;

import com.cgnpc.bbxpark.property.domain.PropertyScheduleUser;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleUserParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/**
 * 物业排班人员服务接口
 */
public interface IPropertyScheduleUserService extends IBaseService<PropertyScheduleUser> {

	/**
	 * 获取物业排班人员列表.
	 * @Param scheduleId 物业排班人员查询条件
	 * @Return 物业排班人员信息列表
	 */
	List<PropertyScheduleUserModel> list(List<Long> scheduleId);

	/**
	 * 批量新增物业排班人员.
	 * @Param params 物业排班人员信息列表
	 * @Return 批量新增物业排班人员是否成功
	 */
	Boolean addBatch(Long scheduleId, List<PropertyScheduleUserParam> params);
}
