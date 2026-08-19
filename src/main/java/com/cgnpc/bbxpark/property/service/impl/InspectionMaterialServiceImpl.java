package com.cgnpc.bbxpark.property.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.InspectionMaterial;
import com.cgnpc.bbxpark.property.dto.model.InspectionMaterialModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionMaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionMaterialParam;
import com.cgnpc.bbxpark.property.mapper.InspectionMaterialRepository;
import com.cgnpc.bbxpark.property.service.IInspectionMaterialService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 巡检材料服务实现
 */
@Service
public class InspectionMaterialServiceImpl extends ServiceImpl<InspectionMaterialRepository, InspectionMaterial> implements IInspectionMaterialService {
	@Override
	public List<InspectionMaterialModel> list(InspectionMaterialListParam param) {
		List<InspectionMaterial> materials = list(Wrappers.<InspectionMaterial>lambdaQuery().eq(InspectionMaterial::getInspectionId, param.getInspectionId()));
		return BeanUtils.convertListTo(materials, InspectionMaterialModel::new);
	}

	@Override
	public Boolean addBatch(List<InspectionMaterialParam> params) {
		if(!CollectionUtils.isEmpty(params)){
			return saveBatch(BeanUtils.convertListTo(params, InspectionMaterial::new));
		}
		return true;
	}

	@Override
	public Boolean remove(Long inspectionId) {
		return remove(Wrappers.<InspectionMaterial>lambdaQuery().eq(InspectionMaterial::getInspectionId, inspectionId));
	}
}
