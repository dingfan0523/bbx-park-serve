
package com.cgnpc.bbxpark.workorder.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.workorder.domain.WorkScheduleUser;
import com.cgnpc.bbxpark.workorder.dto.model.WorkScheduleUserModel;
import com.cgnpc.bbxpark.workorder.dto.param.WorkScheduleUserParam;
import com.cgnpc.bbxpark.workorder.mapper.WorkScheduleUserRepository;
import com.cgnpc.bbxpark.workorder.service.IWorkScheduleUserService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @Description 工单排班人员服务实现
 * @author huangyongtao
 * @date 2025/11/4 16:35
 */
@Service("workScheduleUserService")
public class WorkScheduleUserServiceImpl extends ServiceImpl<WorkScheduleUserRepository, WorkScheduleUser> implements IWorkScheduleUserService {


	/**
	 * 获取工单排班人员列表.
	 * @Param workId 工单标识
	 * @Return 工单排班人员信息列表
	 */
	@Override
	@SneakyThrows
	public List<WorkScheduleUserModel> list(Long workId) {
		List<WorkScheduleUser> workScheduleUsers = this.list(buildQuery(workId));
		return BeanUtils.convertListTo(workScheduleUsers, WorkScheduleUserModel::new);
	}

	/**
	 * 新增工单排班人员.
	 * @Param param 工单排班人员信息
	 * @Return 新增工单排班人员是否成功
	 */
	@Override
	public Boolean add(WorkScheduleUserParam param) {
		WorkScheduleUser workScheduleUser = BeanUtils.convertTo(param, WorkScheduleUser::new);
		workScheduleUser.setId(null);
		return this.save(workScheduleUser);
	}

	/**
	 * 批量新增工单排班人员.
	 * @Param params 工单排班人员信息列表
	 * @Return 批量新增工单排班人员是否成功
	 */
	@Override
	public Boolean addBatch(List<WorkScheduleUserParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		List<WorkScheduleUser> workScheduleUsers = BeanUtils.convertListTo(params, WorkScheduleUser::new);
		return this.saveBatch(workScheduleUsers);
	}

	/**
	 * 删除工单排班人员.
	 * @Param id 工单排班人员标识
	 * @Return 删除工单排班人员是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		return this.remove(Wrappers.<WorkScheduleUser>lambdaQuery().eq(WorkScheduleUser::getId, id));
	}
	
	private LambdaQueryWrapper<WorkScheduleUser> buildQuery(Long workId) {
		LambdaQueryWrapper<WorkScheduleUser> query = new LambdaQueryWrapper<>();
		query.eq(ObjectUtil.isNotEmpty(workId), WorkScheduleUser::getWorkId, workId);
		return query;
	}
}
