package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.PatrolPoint;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointPageParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointParam;

import java.util.List;

/**
 * 巡更点服务接口
 */
public interface IPatrolPointService extends IService<PatrolPoint> {

    /**
	 * 获取巡更点列表(分页).
	 * @Param param 巡更点查询条件
	 * @Return 巡更点信息列表（分页）
	 */
	IPage<PatrolPointModel> page(PatrolPointPageParam param);

	/**
	 * 获取巡更点列表.
	 * @Param param 巡更点查询条件
	 * @Return 巡更点信息列表
	 */
	List<PatrolPointModel> list(PatrolPointListParam param);

	/**
	 * 根据巡更点标识获得巡更点详情信息.
	 * @Param [id] 巡更点标识
	 * @Return 巡更点详情信息
	 */
	PatrolPointModel detail(Long id);

	/**
	 * 新增巡更点.
	 * @Param param 巡更点信息
	 * @Return 新增巡更点是否成功
	 */
	Boolean add(PatrolPointParam param);

	/**
	 * 编辑巡更点信息.
	 * @Param param 巡更点信息
	 * @Return 编辑巡更点是否成功
	 */
	Boolean edit(PatrolPointParam param);

	/**
	 * 启用巡更点.
	 * @Param id 巡更点标识
	 * @Return 启用巡更点是否成功
	 */
	Boolean enable(Long id);

	/**
	 * 禁用巡更点.
	 * @Param id 巡更点标识
	 * @Return 禁用巡更点是否成功
	 */
	Boolean disable(Long id);

	/**
	 * 启用/禁用巡更点.
	 * @param param 巡更点状态信息
	 * @return 启用/禁用巡更点是否成功
	 */
	Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 删除巡更点.
	 * @Param id 巡更点标识
	 * @Return 删除巡更点是否成功
	 */
	Boolean remove(Long id);

}