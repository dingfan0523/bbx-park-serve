
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.workorder.domain.WorkMaterial;
import com.cgnpc.bbxpark.workorder.dto.model.WorkMaterialModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkMaterialParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkMaterialRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkMaterialService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 工单材料服务实现
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
@Service("workMaterialService")
public class WorkMaterialServiceImpl extends ServiceImpl<WorkMaterialRepository, WorkMaterial> implements IWorkMaterialService {

	/**
	 * 获取工单材料列表.
	 * @Param workId 工单标识
	 * @Return 工单材料信息列表
	 */
	@Override
	@SneakyThrows
	public List<WorkMaterialModel> list(Long workId) {
		List<WorkMaterial> workMaterials = this.list(buildQuery(workId));
		return BeanUtils.convertListTo(workMaterials, WorkMaterialModel::new);
	}

	/**
	 * 新增工单材料.
	 * @Param param 工单材料信息
	 * @Return 新增工单材料是否成功
	 */
	@Override
	public Boolean add(WorkMaterialParam param) {
		WorkMaterial workMaterial = BeanUtils.convertTo(param, WorkMaterial::new);
		workMaterial.setId(null);
		return this.save(workMaterial);
	}

	/**
	 * 批量新增工单材料.
	 * @Param params 工单材料信息列表
	 * @Return 批量新增工单材料是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<WorkMaterialParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		List<WorkMaterial> workMaterials = BeanUtils.convertListTo(params, WorkMaterial::new);
		return this.saveBatch(workMaterials);
	}

	/**
	 * 删除工单材料.
	 * @Param id 工单材料标识
	 * @Return 删除工单材料是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		return this.remove(Wrappers.<WorkMaterial>lambdaQuery().eq(WorkMaterial::getId, id));
	}
	
	private LambdaQueryWrapper<WorkMaterial> buildQuery(Long workId) {
		LambdaQueryWrapper<WorkMaterial> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(workId), WorkMaterial::getWorkId, workId);
		return query;
	}
}
