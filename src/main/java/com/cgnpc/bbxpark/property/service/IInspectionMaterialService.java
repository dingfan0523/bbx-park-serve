package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.InspectionMaterial;
import com.cgnpc.bbxpark.property.dto.model.InspectionMaterialModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionMaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionMaterialParam;

import java.util.List;

/**
 * 巡检材料服务接口
 */
public interface IInspectionMaterialService extends IService<InspectionMaterial> {

	/**
	 * 获取巡检材料列表.
	 * @Param param 巡检材料查询条件
	 * @Return 巡检材料信息列表
	 */
	List<InspectionMaterialModel> list(InspectionMaterialListParam param);

    /**
     * 批量新增巡检材料.
     * @param params 巡检材料信息列表
     * @return 批量新增巡检材料是否成功
     */
    Boolean addBatch(List<InspectionMaterialParam> params);

    /**
     * 根据巡检id删除巡检材料.
     * @param inspectionId 巡检id
     * @return 删除巡检材料是否成功
     */
    Boolean remove(Long inspectionId);
}
