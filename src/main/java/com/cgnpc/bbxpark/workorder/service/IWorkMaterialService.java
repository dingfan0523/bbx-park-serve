
package com.cgnpc.bbxpark.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.workorder.domain.WorkMaterial;
import com.cgnpc.bbxpark.workorder.dto.model.WorkMaterialModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkMaterialParam;

import java.util.List;

/***
 * @Description 工单材料服务接口
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
public interface IWorkMaterialService extends IService<WorkMaterial> {

	/**
	 * 获取工单材料列表.
	 * @Param workId 工单标识
	 * @Return 工单材料信息列表
	 */
	List<WorkMaterialModel> list(Long workId);

	/**
	 * 新增工单材料.
	 * @Param param 工单材料信息
	 * @Return 新增工单材料是否成功
	 */
	Boolean add(WorkMaterialParam param);

	/**
	 * 批量新增工单材料.
	 * @Param params 工单材料信息列表
	 * @Return 批量新增工单材料是否成功
	 */
	Boolean addBatch(List<WorkMaterialParam> params);

	/**
	 * 删除工单材料.
	 * @Param id 工单材料标识
	 * @Return 删除工单材料是否成功
	 */
	Boolean remove(Long id);
}
