package com.cgnpc.bbxpark.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.property.domain.PatrolPoint;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointListParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointPageParam;
import com.cgnpc.bbxpark.property.dto.param.PatrolPointParam;
import com.cgnpc.bbxpark.property.mapper.PatrolPointRepository;
import com.cgnpc.bbxpark.property.service.IPatrolPointService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 巡更点服务实现
 */
@Service
public class PatrolPointServiceImpl extends ServiceImpl<PatrolPointRepository, PatrolPoint> implements IPatrolPointService {
	@Resource
	private IParkSpaceService parkSpaceService;
	@Resource
	private IUserApiService userApiService;

	/**
	 * 获取巡更点列表(分页).
	 * @Param param 巡更点查询条件
	 * @Return 巡更点信息列表（分页）
	 */
	@Override
	public IPage<PatrolPointModel> page(PatrolPointPageParam param) {
		IPage<PatrolPoint> page = this.page(new Page<>(param.getCurrent(),param.getSize()), buildQuery(BeanUtils.convertTo(param, PatrolPointListParam::new)));
		return ConvertUtil.pageConvert(page,convert(page.getRecords()));
	}

	/**
	 * 获取巡更点列表.
	 * @Param param 巡更点查询条件
	 * @Return 巡更点信息列表
	 */
	@Override
	@SneakyThrows
	public List<PatrolPointModel> list(PatrolPointListParam param) {
		return convert(this.list(buildQuery(param)));
	}

	/**
	 * 根据巡更点标识获得巡更点详情信息.
	 * @Param [id] 巡更点标识
	 * @Return 巡更点详情信息
	 */
	@Override
	public PatrolPointModel detail(Long id) {
		PatrolPoint patrolPoint = this.getById(id);
		AssertUtils.notNull(patrolPoint, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(patrolPoint, PatrolPointModel::new);
	}

	/**
	 * 新增巡更点.
	 * @Param param 巡更点信息
	 * @Return 新增巡更点是否成功
	 */
	@Override
	public Boolean add(PatrolPointParam param) {
		// 检查编码是否重复
		checkCodeDuplicate(param.getCode(), null);
		
		PatrolPoint patrolPoint = BeanUtils.convertTo(param, PatrolPoint::new);
		//创建人
		UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        patrolPoint.setCreateBy(userInfo.getUserName());
		patrolPoint.setId(null);
		return save(patrolPoint);
	}

	/**
	 * 编辑巡更点信息.
	 * @Param param 巡更点信息
	 * @Return 编辑巡更点是否成功
	 */
	@Override
	public Boolean edit(PatrolPointParam param) {
		PatrolPoint existingPoint = this.getById(param.getId());
		AssertUtils.notNull(existingPoint, SystemResultCode.RESULT_DATA_NONE.message());
		
		// 检查编码是否重复（排除自己）
		checkCodeDuplicate(param.getCode(), param.getId());
		
		PatrolPoint patrolPoint = this.getById(param.getId());
		AssertUtils.notNull(patrolPoint, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param, patrolPoint);
		//编码不可修改
		patrolPoint.setCode(null);
		return updateById(patrolPoint);
	}

	/**
	 * 启用巡更点.
	 * @Param id 巡更点标识
	 * @Return 启用巡更点是否成功
	 */
	@Override
	public Boolean enable(Long id) {
		PatrolPoint patrolPoint = new PatrolPoint();
		patrolPoint.setId(id);
		patrolPoint.setStatus(Status.enabled.getKey());
		return updateById(patrolPoint);
	}

	/**
	 * 禁用巡更点.
	 * @Param id 巡更点标识
	 * @Return 禁用巡更点是否成功
	 */
	@Override
	public Boolean disable(Long id) {
		PatrolPoint patrolPoint = new PatrolPoint();
		patrolPoint.setId(id);
		patrolPoint.setStatus(Status.disabled.getKey());
		return updateById(patrolPoint);
	}

	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		PatrolPoint patrolPoint = this.getById(param.getId());
		AssertUtils.notNull(patrolPoint, SystemResultCode.RESULT_DATA_NONE.message());
		patrolPoint.setStatus(param.getStatus());
		return updateById(patrolPoint);
	}

	/**
	 * 删除巡更点.
	 * @Param id 巡更点标识
	 * @Return 删除巡更点是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		PatrolPoint patrolPoint = this.getById(id);
		AssertUtils.notNull(patrolPoint, SystemResultCode.RESULT_DATA_NONE.message());
		patrolPoint.setDeleted(Delete.DELETED.getKey());
		return updateById(patrolPoint);
	}

	/**
	 * 构建查询条件
	 * @param param 参数
	 * @return 查询条件
	 */
	private LambdaQueryWrapper<PatrolPoint> buildQuery(PatrolPointListParam param){
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<PatrolPoint>lambdaQuery().like(StringUtils.isNotEmpty(param.getName()),PatrolPoint::getName, param.getName())
				.like(StringUtils.isNotEmpty(param.getCode()),PatrolPoint::getCode, param.getCode())
				.eq(param.getType() != null,PatrolPoint::getType, param.getType())
				.eq(param.getWay() != null,PatrolPoint::getWay, param.getWay())
				.eq(param.getKeyPoint() != null,PatrolPoint::getKeyPoint, param.getKeyPoint())
				.eq(param.getSpaceId() != null,PatrolPoint::getSpaceId, param.getSpaceId())
				.eq(param.getStatus() != null,PatrolPoint::getStatus, param.getStatus())
				.in(!CollectionUtils.isEmpty(param.getIds()),PatrolPoint::getId, param.getIds())
				.eq(tenantId != null,PatrolPoint::getTenantId, tenantId)
				.eq(PatrolPoint::getDeleted, Delete.NORMAL.getKey())
				.orderByDesc(PatrolPoint::getCreateTime);
	}

	/**
	 * 批量转换
	 * @param list 巡检点列表
	 * @return 巡检点模型列表
	 */
	private List<PatrolPointModel> convert(List<PatrolPoint> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		//批量查询空间信息
		List<Long> spaceIds = list.stream().map(PatrolPoint::getSpaceId).collect(Collectors.toList());
		Map<Long, ParkSpaceFullModel> spaceFullMap = parkSpaceService.findFullSpaceMap(spaceIds,WebFrameworkUtils.getHeaderTenantId());

		return list.stream().map(point->{
			PatrolPointModel model = BeanUtils.convertTo(point, PatrolPointModel::new);
			if(spaceFullMap.containsKey(point.getSpaceId())){
				model.setSpaceName(spaceFullMap.get(point.getSpaceId()).getFullPath());
			}
			return model;
		}).collect(Collectors.toList());
	}
	
	/**
	 * 检查编码是否重复
	 * @param code 编码
	 * @param id 当前记录ID，为空时表示新增，不为空时表示编辑
	 */
	private void checkCodeDuplicate(String code, Long id) {
		LambdaQueryWrapper<PatrolPoint> queryWrapper = Wrappers.<PatrolPoint>lambdaQuery()
				.eq(PatrolPoint::getCode, code)
				.ne(id != null,PatrolPoint::getId, id)
				.eq(PatrolPoint::getDeleted, Delete.NORMAL.getKey());
		AssertUtils.isTrue(this.count(queryWrapper) == 0, "巡更点编码已存在");
	}
}