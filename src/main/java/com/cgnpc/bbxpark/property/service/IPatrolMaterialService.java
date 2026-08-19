package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.PatrolMaterial;
import com.cgnpc.bbxpark.property.dto.model.PatrolMaterialModel;
import com.cgnpc.bbxpark.property.dto.param.PatrolMaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolMaterialParam;

import java.util.List;

/**
 * 巡更材料服务接口
 */
public interface IPatrolMaterialService extends IService<PatrolMaterial> {

	/**
	 * 获取巡更材料列表.
	 * @Param param 巡更材料查询条件
	 * @Return 巡更材料信息列表
	 */
	List<PatrolMaterialModel> list(PatrolMaterialListParam param);

	/**
	 * 批量新增巡更材料.
	 * @Param params 巡更材料信息列表
	 * @Return 批量新增巡更材料是否成功
	 */
	Boolean addBatch(List<PatrolMaterialParam> params);

	/**
	 * 根据巡更id删除巡更材料.
	 * @param patrolId 巡更id
	 * @return 删除巡更材料是否成功
	 */
	Boolean remove(Long patrolId);
}