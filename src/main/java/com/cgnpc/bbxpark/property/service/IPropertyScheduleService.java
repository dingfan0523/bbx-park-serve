
package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.property.domain.PropertySchedule;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleListModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleModel;
import com.cgnpc.bbxpark.property.dto.model.PropertyScheduleUserModel;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleListParam;
import com.cgnpc.bbxpark.property.dto.param.PropertySchedulePageParam;
import com.cgnpc.bbxpark.property.dto.param.PropertyScheduleParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 物业排班服务接口
 */
public interface IPropertyScheduleService extends IBaseService<PropertySchedule> {

	/**
	 * 根据物业排班标识获得物业排班详情信息.
	 *
	 * @Param [id] 物业排班标识
	 * @Return 物业排班详情信息
	 */
	PropertyScheduleModel detail(Long id);

	/**
	 * 获取物业排班列表(分页).
	 *
	 * @Param param 物业排班查询条件
	 * @Return 物业排班信息列表（分页）
	 */
	IPage<PropertyScheduleListModel> page(PropertySchedulePageParam param);

	/**
	 * 获取物业排班列表.
	 *
	 * @Param param 物业排班查询条件
	 * @Return 物业排班信息列表
	 */
	List<PropertyScheduleListModel> list(PropertyScheduleListParam param);

	/**
	 * 新增物业排班.
	 *
	 * @Param param 物业排班信息
	 * @Return 新增物业排班是否成功
	 */
	Boolean add(PropertyScheduleParam param);

	/**
	 * 编辑物业排班信息.
	 *
	 * @Param param 物业排班信息
	 * @Return 编辑物业排班是否成功
	 */
	Boolean edit(PropertyScheduleParam param);

	/**
	 * 删除物业排班.
	 *
	 * @Param id 物业排班标识
	 * @Return 删除物业排班是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 获取物业排班人员列表.
	 *
	 * @param id 物业排班标识
	 * @return 物业排班人员列表
	 */
	List<PropertyScheduleUserModel> findUserList(Long id);

	/**
	 * 获取物业排班名称列表.
	 *
	 * @param scheduleIds 物业排班标识列表
	 * @return 物业排班名称列表
	 */
	Map<Long, String> getScheduleNameMap(List<Long> scheduleIds);
}
