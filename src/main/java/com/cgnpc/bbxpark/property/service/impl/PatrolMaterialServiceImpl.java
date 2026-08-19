package com.cgnpc.bbxpark.property.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.PatrolMaterial;
import com.cgnpc.bbxpark.property.dto.model.PatrolMaterialModel;
import com.cgnpc.bbxpark.property.dto.param.PatrolMaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolMaterialParam;
import com.cgnpc.bbxpark.property.mapper.PatrolMaterialRepository;
import com.cgnpc.bbxpark.property.service.IPatrolMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 巡更材料服务实现
 */
@Service
public class PatrolMaterialServiceImpl extends ServiceImpl<PatrolMaterialRepository, PatrolMaterial> implements IPatrolMaterialService {
    /**
     * 注入repository.
     */
	@Autowired
	private PatrolMaterialRepository patrolMaterialRepository;

	@Override
	public List<PatrolMaterialModel> list(PatrolMaterialListParam param) {
		List<PatrolMaterial> materials = list(Wrappers.<PatrolMaterial>lambdaQuery().eq(PatrolMaterial::getPatrolId, param.getPatrolId()));
		return BeanUtils.convertListTo(materials, PatrolMaterialModel::new);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<PatrolMaterialParam> params) {
		if(!CollectionUtils.isEmpty(params)){
			return saveBatch(BeanUtils.convertListTo(params, PatrolMaterial::new));
		}
		return true;
	}

	@Override
	public Boolean remove(Long patrolId) {
		return remove(Wrappers.<PatrolMaterial>lambdaQuery().eq(PatrolMaterial::getPatrolId, patrolId));
	}
}