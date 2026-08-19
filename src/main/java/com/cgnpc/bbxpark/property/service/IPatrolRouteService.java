package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.PatrolRoute;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointListModel;
import com.cgnpc.bbxpark.property.dto.model.PatrolRouteModel;
import com.cgnpc.bbxpark.property.dto.param.*;

import java.util.List;

/**
 * 巡更路线服务接口
 */
public interface IPatrolRouteService extends IService<PatrolRoute> {

	/**
	 * 获取巡更路线列表(分页).
	 * @Param param 巡更路线查询条件
	 * @Return 巡更路线信息列表（分页）
	 */
	IPage<PatrolRouteModel> page(PatrolRoutePageParam param);

	/**
	 * 获取巡更路线列表.
	 * @Param param 巡更路线查询条件
	 * @Return 巡更路线信息列表
	 */
	List<PatrolRouteModel> list(PatrolRouteListParam param);

    /**
     * 根据巡更路线标识获得巡更路线详情信息.
     * @Param [id] 巡更路线标识
     * @Return 巡更路线详情信息
     */
    PatrolRouteModel detail(Long id);

	/**
	 * 新增巡更路线.
	 * @Param param 巡更路线信息
	 * @Return 新增巡更路线是否成功
	 */
	Boolean add(PatrolRouteParam param);

	/**
	 * 编辑巡更路线信息.
	 * @Param param 巡更路线信息
	 * @Return 编辑巡更路线是否成功
	 */
	Boolean edit(PatrolRouteParam param);

	/**
	 * 删除巡更路线.
	 * @Param id 巡更路线标识
	 * @Return 删除巡更路线是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 启用巡更路线.
	 * @Param id 巡更路线标识
	 * @Return 启用巡更路线是否成功
	 */
	Boolean enable(Long id);

	/**
	 * 禁用巡更路线.
	 * @Param id 巡更路线标识
	 * @Return 禁用巡更路线是否成功
	 */
	Boolean disable(Long id);

    /**
     * 修改巡更点状态.
     * @Param param 巡更点状态信息
     * @Return 修改巡更点状态是否成功
     */
    Boolean statusEdit(InspectionPointStatusParam param);

	/**
	 * 根据巡更路线标识获得巡更路线下的巡更点信息.
	 * @Param [routeId] 巡更路线标识
	 * @Return 巡更路线下的巡更点信息
	 */
	List<PatrolPointListModel> findPointList(Long routeId);

	/**
	 * 保存巡更路线下的巡更点点.
	 * @Param param 巡更路线点信息
	 * @Return 保存巡更路线点是否成功
	 */
	Boolean savePoint(PatrolRoutePointParam param);

	/**
	 * 删除巡更路线下的巡更点.
	 * @Param param 巡更路线点信息
	 * @Return 删除巡更路线点是否成功
	 */
	Boolean removePoint(Long id, Long pointId);
}