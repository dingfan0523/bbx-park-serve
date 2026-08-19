
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.workorder.domain.WorkEvaluate;
import com.cgnpc.bbxpark.workorder.dto.model.WorkEvaluateModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkEvaluateParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkEvaluateRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkEvaluateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @Description 工单评价服务实现
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
@Service("workEvaluateService")
public class WorkEvaluateServiceImpl extends ServiceImpl<WorkEvaluateRepository, WorkEvaluate> implements IWorkEvaluateService {


	/**
	 * 获取工单评价列表.
	 * @Param workId 工单标识
	 * @Return 工单评价信息列表
	 */
	@Override
	@SneakyThrows
	public List<WorkEvaluateModel> list(Long workId) {
		List<WorkEvaluate> workEvaluates = this.list(buildQuery(workId));
		return BeanUtils.convertListTo(workEvaluates, WorkEvaluateModel::new);
	}

	/**
	 * 新增工单评价.
	 * @Param param 工单评价信息
	 * @Return 新增工单评价是否成功
	 */
	@Override
	public Boolean add(WorkEvaluateParam param) {
		WorkEvaluate workEvaluate = BeanUtils.convertTo(param, WorkEvaluate::new);
		workEvaluate.setId(null);
		return this.save(workEvaluate);
	}

	/**
	 * 删除工单评价.
	 * @Param id 工单评价标识
	 * @Return 删除工单评价是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		return this.remove(Wrappers.<WorkEvaluate>lambdaQuery().eq(WorkEvaluate::getId, id));
	}
	
	private LambdaQueryWrapper<WorkEvaluate> buildQuery(Long workId) {
		LambdaQueryWrapper<WorkEvaluate> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(workId), WorkEvaluate::getWorkId, workId);
		return query;
	}
}
