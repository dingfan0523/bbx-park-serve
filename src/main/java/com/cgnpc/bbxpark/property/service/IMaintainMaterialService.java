package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.MaintainMaterial;
import com.cgnpc.bbxpark.property.dto.model.MaintainMaterialModel;
import com.cgnpc.bbxpark.property.dto.param.MaintainMaterialParam;

import java.util.List;

/***
 * @Description 维保材料服务接口
 * @author huangyongtao
 * @date 2025/10/16 15:14
 */
public interface IMaintainMaterialService extends IService<MaintainMaterial> {


	/**
	 * 获取维保材料列表.
	 * @Param param 维保材料查询条件
	 * @Return 维保材料信息列表
	 */
	List<MaintainMaterialModel> list(Long maintainId);

	/**
	 * 新增维保材料.
	 * @Param param 维保材料信息
	 * @Return 新增维保材料是否成功
	 */
	Boolean add(MaintainMaterialParam param);

	/**
	 * 批量新增维保材料.
	 * @Param params 维保材料信息列表
	 * @Return 批量新增维保材料是否成功
	 */
	Boolean addBatch(List<MaintainMaterialParam> params);

	/**
	 * 删除维保材料.
	 * @Param maintainId 维保材料标识
	 * @Return 删除维保材料是否成功
	 */
	Boolean remove(Long maintainId);

}