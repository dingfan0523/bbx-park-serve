package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.property.domain.MaintainMaterial;
import com.cgnpc.bbxpark.property.dto.model.MaintainMaterialModel;
import com.cgnpc.bbxpark.property.dto.param.MaintainMaterialParam;
import com.cgnpc.bbxpark.property.mapper.MaintainMaterialRepository;
import com.cgnpc.bbxpark.property.service.IMaintainMaterialService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 维保材料服务实现
 * @author huangyongtao
 * @date 2025/10/16 15:14
 */
@Service
public class MaintainMaterialServiceImpl extends ServiceImpl<MaintainMaterialRepository, MaintainMaterial> implements IMaintainMaterialService {

	/**
	 * 获取维保材料列表.
	 * @Param param 维保材料查询条件
	 * @Return 维保材料信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaintainMaterialModel> list(Long maintainId) {
		List<MaintainMaterial> maintainMaterials = this.list(buildQuery(maintainId));
		return BeanUtils.convertListTo(maintainMaterials, MaintainMaterialModel::new);
	}

	/**
	 * 新增维保材料.
	 * @Param param 维保材料信息
	 * @Return 新增维保材料是否成功
	 */
	@Override
	public Boolean add(MaintainMaterialParam param) {
		MaintainMaterial maintainMaterial = BeanUtils.convertTo(param, MaintainMaterial::new);
		maintainMaterial.setId(null);
		return this.save(maintainMaterial);
	}

	/**
	 * 批量新增维保材料.
	 * @Param params 维保材料信息列表
	 * @Return 批量新增维保材料是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<MaintainMaterialParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		this.remove(params.get(0).getMaintainId());
		List<MaintainMaterial> maintainMaterials = BeanUtils.convertListTo(params, MaintainMaterial::new);
		return this.saveBatch(maintainMaterials);
	}

	/**
	 * 删除维保材料.
	 * @Param maintainId 维保材料标识
	 * @Return 删除维保材料是否成功
	 */
	@Override
	public Boolean remove(Long maintainId) {
		return this.remove(Wrappers.<MaintainMaterial>lambdaQuery().eq(MaintainMaterial::getMaintainId, maintainId));
	}
	
	private LambdaQueryWrapper<MaintainMaterial> buildQuery(Long maintainId) {
		LambdaQueryWrapper<MaintainMaterial> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(maintainId), MaintainMaterial::getMaintainId, maintainId);
		return query;
	}
}