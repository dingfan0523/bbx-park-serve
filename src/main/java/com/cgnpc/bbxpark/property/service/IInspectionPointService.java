package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.InspectionPoint;
import com.cgnpc.bbxpark.property.dto.model.InspectionPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointPageParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;

import java.util.List;

/**
 * 巡检点服务接口.
 * @author 54766
 */
public interface IInspectionPointService extends IService<InspectionPoint> {

	/**
	 * 获取巡检点列表(分页).
	 * @Param param 巡检点查询条件
	 * @Return 巡检点信息列表（分页）
	 */
	IPage<InspectionPointModel> page(InspectionPointPageParam param);

	/**
	 * 获取巡检点列表.
	 * @Param param 巡检点查询条件
	 * @Return 巡检点信息列表
	 */
	List<InspectionPointModel> list(InspectionPointListParam param);

	/**
	 * 根据巡检点标识获得巡检点详情信息.
	 * @Param [id] 巡检点标识
	 * @Return 巡检点详情信息
	 */
	InspectionPointModel detail(Long id);


	/**
	 * 新增巡检点.
	 * @Param param 巡检点信息
	 * @Return 新增巡检点是否成功
	 */
	Boolean add(InspectionPointParam param);

	/**
	 * 编辑巡检点信息.
	 * @Param param 巡检点信息
	 * @Return 编辑巡检点是否成功
	 */
	Boolean edit(InspectionPointParam param);

	/**
	 * 启用巡检点.
	 * @Param id 巡检点标识
	 * @Return 启用巡检点是否成功
	 */
	Boolean enable(Long id);

	/**
	 * 禁用巡检点.
	 * @Param id 巡检点标识
	 * @Return 禁用巡检点是否成功
	 */
	Boolean disable(Long id);

	/**
	 * 启用/禁用巡检点.
	 * @param param 启用/禁用参数
	 * @return 启用/禁用结果
	 */
	Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 删除巡检点.
	 * @Param id 巡检点标识
	 * @Return 删除巡检点是否成功
	 */
	Boolean remove(Long id);
}