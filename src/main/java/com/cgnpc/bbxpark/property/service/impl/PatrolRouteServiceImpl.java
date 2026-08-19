package com.cgnpc.bbxpark.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.property.domain.PatrolRoute;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointListModel;
import com.cgnpc.bbxpark.property.dto.model.PatrolPointModel;
import com.cgnpc.bbxpark.property.dto.model.PatrolRouteModel;
import com.cgnpc.bbxpark.property.dto.param.*;
import com.cgnpc.bbxpark.property.mapper.PatrolRouteRepository;
import com.cgnpc.bbxpark.property.service.IPatrolPointService;
import com.cgnpc.bbxpark.property.service.IPatrolRouteService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡更路线服务实现
 */
@Service
public class PatrolRouteServiceImpl extends ServiceImpl<PatrolRouteRepository, PatrolRoute> implements IPatrolRouteService {
	@Resource
	private IPatrolPointService patrolPointService;
	@Override
	public IPage<PatrolRouteModel> page(PatrolRoutePageParam param) {
		IPage<PatrolRoute> page = this.page(new Page<>(param.getCurrent(),param.getSize()), buildQuery(BeanUtils.convertTo(param, PatrolRouteListParam::new)));
		return ConvertUtil.pageConvert(page,convert(page.getRecords()));
	}

	@Override
	public List<PatrolRouteModel> list(PatrolRouteListParam param) {
		return convert(list(buildQuery(param)));
	}

	@Override
	public PatrolRouteModel detail(Long id) {
		PatrolRoute patrolRoute = this.getById(id);
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(patrolRoute, PatrolRouteModel::new);
	}

	@Override
	public Boolean add(PatrolRouteParam param) {
		PatrolRoute patrolRoute = BeanUtils.convertTo(param, PatrolRoute::new);
		patrolRoute.setId(null);
		return save(patrolRoute);
	}

	@Override
	public Boolean edit(PatrolRouteParam param) {
		PatrolRoute patrolRoute = this.getById(param.getId());
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param, patrolRoute);
		return updateById(patrolRoute);
	}

	@Override
	public Boolean remove(Long id) {
		PatrolRoute patrolRoute = this.getById(id);
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		patrolRoute.setDeleted(Delete.DELETED.getKey());
		return updateById(patrolRoute);
	}

	@Override
	public Boolean enable(Long id) {
		PatrolRoute patrolRoute = new PatrolRoute();
		patrolRoute.setId(id);
		patrolRoute.setStatus(Status.enabled.getKey());
		return updateById(patrolRoute);
	}

	@Override
	public Boolean disable(Long id) {
		PatrolRoute patrolRoute = new PatrolRoute();
		patrolRoute.setId(id);
		patrolRoute.setStatus(Status.disabled.getKey());
		return updateById(patrolRoute);
	}

	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		PatrolRoute patrolRoute = this.getById(param.getId());
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		patrolRoute.setStatus(param.getStatus());
		return updateById(patrolRoute);
	}

	@Override
	public List<PatrolPointListModel> findPointList(Long routeId) {
		PatrolRoute patrolRoute = this.getById(routeId);
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		if(StringUtils.isEmpty(patrolRoute.getPointId()) || patrolRoute.getDeleted().equals(Delete.DELETED.getKey())){
			return Collections.emptyList();
		}
		//查询巡更点集合
		String[] pointIdArr = patrolRoute.getPointId().split(",");
		List<Long> pointIds = Arrays.stream(pointIdArr).map(Long::parseLong).collect(Collectors.toList());
		PatrolPointListParam patrolRouteListParam = new PatrolPointListParam();
		patrolRouteListParam.setIds(pointIds);
		List<PatrolPointModel> points = patrolPointService.list(patrolRouteListParam);
		
		// 创建一个Map以便根据ID快速查找巡更点
		Map<Long, PatrolPointModel> pointMap = points.stream().collect(Collectors.toMap(PatrolPointModel::getId, point -> point));
		
		// 根据pointIds的顺序构造结果列表
		return pointIds.stream().map(id -> {
					PatrolPointModel point = pointMap.get(id);
					if (point != null) {
						return BeanUtils.convertTo(point, PatrolPointListModel::new);
					} else {
						// 已经被删除的巡更点只返回基本字段
						PatrolPointListModel deletedPoint = new PatrolPointListModel();
						deletedPoint.setId(id);
						deletedPoint.setDeleted(Delete.DELETED.getKey());
						return deletedPoint;
					}
				}).collect(Collectors.toList());
	}

	@Override
	public Boolean savePoint(PatrolRoutePointParam param) {
		PatrolRoute patrolRoute = this.getById(param.getId());
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		if (param.getPointIds() != null) {
			patrolRoute.setPointId(StringUtils.join(param.getPointIds(),","));
		} else {
			patrolRoute.setPointId(null);
		}
		return updateById(patrolRoute);
	}

	@Override
	public Boolean removePoint(Long id, Long pointId) {
		PatrolRoute patrolRoute = this.getById(id);
		AssertUtils.notNull(patrolRoute, SystemResultCode.RESULT_DATA_NONE.message());
		
		if (StringUtils.isEmpty(patrolRoute.getPointId())) {
			// 如果没有设置巡更点，直接返回true
			return true;
		}
		List<Long> pointIds = Arrays.stream(patrolRoute.getPointId().split(",")).filter(StringUtils::isNotEmpty)
				.map(String::trim).map(pointIdStr -> {
					try {
						return Long.parseLong(pointIdStr);
					} catch (NumberFormatException e) {
						return null;
					}
				})
				.filter(Objects::nonNull).collect(Collectors.toList());

		// 移除指定的pointId
		pointIds.remove(pointId);
		patrolRoute.setPointId(StringUtils.join(pointIds, ","));
		return updateById(patrolRoute);
	}

	/**
	 * 构建查询条件
	 * @param param 参数
	 * @return 查询条件
	 */
	private LambdaQueryWrapper<PatrolRoute> buildQuery(PatrolRouteListParam param){
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<PatrolRoute>lambdaQuery().like(StringUtils.isNotEmpty(param.getName()),PatrolRoute::getName, param.getName())
				.eq(param.getType() != null,PatrolRoute::getType, param.getType())
				.eq(param.getLevel() != null,PatrolRoute::getLevel, param.getLevel())
				.eq(param.getSequence() != null,PatrolRoute::getSequence, param.getSequence())
				.eq(param.getStatus() != null,PatrolRoute::getStatus, param.getStatus())
				.in(!CollectionUtils.isEmpty(param.getIds()),PatrolRoute::getId, param.getIds())
				.eq(tenantId != null,PatrolRoute::getTenantId, tenantId)
				.eq(PatrolRoute::getDeleted, Delete.NORMAL.getKey())
				.orderByDesc(PatrolRoute::getCreateTime);
	}

	/**
	 * 批量转换
	 * @param list 巡检点列表
	 * @return 巡检点模型列表
	 */
	private List<PatrolRouteModel> convert(List<PatrolRoute> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		return list.stream().map(route->{
			PatrolRouteModel model = BeanUtils.convertTo(route, PatrolRouteModel::new);
			if(StringUtils.isNotEmpty(route.getPointId())){
				model.setPointCount(route.getPointId().split(",").length);
			}
			return model;
		}).collect(Collectors.toList());
	}
}