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
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.property.domain.InspectionPoint;
import com.cgnpc.bbxpark.property.dto.model.InspectionPointModel;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointListParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointPageParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointParam;
import com.cgnpc.bbxpark.property.dto.param.InspectionPointStatusParam;
import com.cgnpc.bbxpark.property.mapper.InspectionPointRepository;
import com.cgnpc.bbxpark.property.service.IInspectionPointService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡检点服务实现
 */
@Service
public class InspectionPointServiceImpl extends ServiceImpl<InspectionPointRepository, InspectionPoint> implements IInspectionPointService {
	@Resource
	private IParkSpaceService parkSpaceService;
	@Resource
	private IIocDeviceService iocDeviceService;
	@Resource
	private IUserApiService userApiService;

	/**
	 * 获取巡检点列表(分页).
	 * @Param param 巡检点查询条件
	 * @Return 巡检点信息列表（分页）
	 */
	@Override
	public IPage<InspectionPointModel> page(InspectionPointPageParam param) {
		IPage<InspectionPoint> page = this.page(new Page<>(param.getCurrent(),param.getSize()), buildQuery(BeanUtils.convertTo(param, InspectionPointListParam::new)));
		return ConvertUtil.pageConvert(page,convert(page.getRecords()));
	}

	/**
	 * 获取巡检点列表.
	 * @Param param 巡检点查询条件
	 * @Return 巡检点信息列表
	 */
	@Override
	@SneakyThrows
	public List<InspectionPointModel> list(InspectionPointListParam param) {
		List<InspectionPoint> list = this.list(buildQuery(param));
		return convert(list);
	}

	/**
	 * 根据巡检点标识获得巡检点详情信息.
	 * @Param [id] 巡检点标识
	 * @Return 巡检点详情信息
	 */
	@Override
	public InspectionPointModel detail(Long id) {
		InspectionPoint inspectionPoint = this.getById(id);
		AssertUtils.notNull(inspectionPoint, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(inspectionPoint, InspectionPointModel::new);
	}

	/**
	 * 新增巡检点.
	 * @Param param 巡检点信息
	 * @Return 新增巡检点是否成功
	 */
	@Override
	public Boolean add(InspectionPointParam param) {
		// 检查编码是否重复
		checkCodeDuplicate(param.getCode(), null);
		
		InspectionPoint inspectionPoint = BeanUtils.convertTo(param, InspectionPoint::new);
		//创建人
		UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        inspectionPoint.setCreateBy(userInfo.getUserName());
		inspectionPoint.setId(null);
		return save(inspectionPoint);
	}

	/**
	 * 编辑巡检点信息.
	 * @Param param 巡检点信息
	 * @Return 编辑巡检点是否成功
	 */
	@Override
	public Boolean edit(InspectionPointParam param) {
		InspectionPoint existingPoint = this.getById(param.getId());
		AssertUtils.notNull(existingPoint, SystemResultCode.RESULT_DATA_NONE.message());
		
		// 检查编码是否重复（排除自己）
		checkCodeDuplicate(param.getCode(), param.getId());
		
		InspectionPoint inspectionPoint = this.getById(param.getId());
		AssertUtils.notNull(inspectionPoint, SystemResultCode.RESULT_DATA_NONE.message());
		BeanUtils.copyProperties(param, inspectionPoint);
		return updateById(inspectionPoint);
	}

	/**
	 * 启用巡检点.
	 * @Param id 巡检点标识
	 * @Return 启用巡检点是否成功
	 */
	@Override
	public Boolean enable(Long id) {
		InspectionPoint inspectionPoint = new InspectionPoint();
	 	inspectionPoint.setId(id);
		inspectionPoint.setStatus(Status.enabled.getKey());
		return updateById(inspectionPoint);
	}

	/**
	 * 禁用巡检点.
	 * @Param id 巡检点标识
	 * @Return 禁用巡检点是否成功
	 */
	@Override
	public Boolean disable(Long id) {
		InspectionPoint inspectionPoint = new InspectionPoint();
		inspectionPoint.setId(id);
		inspectionPoint.setStatus((int) Status.disabled.getKey());
		return updateById(inspectionPoint);
	}

	@Override
	public Boolean statusEdit(InspectionPointStatusParam param) {
		InspectionPoint inspectionPoint = this.getById(param.getId());
		AssertUtils.notNull(inspectionPoint, SystemResultCode.RESULT_DATA_NONE.message());
		inspectionPoint.setStatus(param.getStatus());
		return updateById(inspectionPoint);
	}

	/**
	 * 删除巡检点.
	 * @Param id 巡检点标识
	 * @Return 删除巡检点是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		InspectionPoint inspectionPoint = this.getById(id);
		AssertUtils.notNull(inspectionPoint, SystemResultCode.RESULT_DATA_NONE.message());
		inspectionPoint.setDeleted(Delete.DELETED.getKey());
		return this.updateById(inspectionPoint);
	}

	/**
	 * 检查编码是否重复
	 * @param code 编码
	 * @param id 当前记录ID，为空时表示新增，不为空时表示编辑
	 */
	private void checkCodeDuplicate(String code, Long id) {
		LambdaQueryWrapper<InspectionPoint> queryWrapper = Wrappers.<InspectionPoint>lambdaQuery()
				.eq(InspectionPoint::getCode, code)
				.ne(id != null,InspectionPoint::getId, id)
				.eq(InspectionPoint::getDeleted, Delete.NORMAL.getKey());
		AssertUtils.isTrue(this.count(queryWrapper) == 0, "巡检点编码已存在");
	}

	/**
	 * 构建查询条件
	 * @param param 参数
	 * @return 查询条件
	 */
	private LambdaQueryWrapper<InspectionPoint> buildQuery(InspectionPointListParam param){
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		return Wrappers.<InspectionPoint>lambdaQuery().like(StringUtils.isNotEmpty(param.getName()),InspectionPoint::getName, param.getName())
				.eq(param.getSpaceId() != null,InspectionPoint::getSpaceId, param.getSpaceId())
				.eq(param.getStatus() != null,InspectionPoint::getStatus, param.getStatus())
				.in(!CollectionUtils.isEmpty(param.getIds()),InspectionPoint::getId, param.getIds())
				.eq(tenantId != null,InspectionPoint::getTenantId, tenantId)
				.eq(InspectionPoint::getDeleted, Delete.NORMAL.getKey())
				.orderByDesc(InspectionPoint::getCreateTime);
	}

	/**
	 * 批量转换
	 * @param list 巡检点列表
	 * @return 巡检点模型列表
	 */
	private List<InspectionPointModel> convert(List<InspectionPoint> list) {
		if (CollectionUtils.isEmpty(list)) {
			return Collections.emptyList();
		}
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		//批量查询空间信息
		List<Long> spaceIds = list.stream().map(InspectionPoint::getSpaceId).collect(Collectors.toList());
		Map<Long, ParkSpaceFullModel> spaceFullMap = parkSpaceService.findFullSpaceMap(spaceIds,WebFrameworkUtils.getHeaderTenantId());
		//批量查询设备信息
		Set<Long> deviceIds = list.stream().filter(point -> StringUtils.isNotEmpty(point.getDeviceId()))
				.flatMap(point -> Arrays.stream(point.getDeviceId().split(","))).filter(StringUtils::isNotEmpty)
				.map(String::trim).filter(s -> s.matches("\\d+"))
				.map(Long::valueOf).collect(Collectors.toSet());
		Map<Long, IocDevice> deviceMap = new HashMap<>(4);
		if (!deviceIds.isEmpty()) {
			List<IocDevice> deviceList = iocDeviceService.list(Wrappers.<IocDevice>lambdaQuery()
					.in(IocDevice::getId, deviceIds).eq(IocDevice::getDeleted, Status.enabled.getKey())
					.eq(tenantId != null, IocDevice::getTenantId, tenantId));
			deviceList.forEach(device-> deviceMap.put(device.getId(), device));
//			deviceMap = deviceList.stream().collect(Collectors.toMap(IocDevice::getId, device -> device, (existing, replacement) -> existing));
		}

		return list.stream().map(point->{
			InspectionPointModel model = BeanUtils.convertTo(point, InspectionPointModel::new);
			ParkSpaceFullModel spaceModel = spaceFullMap.get(point.getSpaceId());
			if (spaceModel != null) {
				model.setSpaceName(spaceModel.getFullPath());
			}
			if(StringUtils.isNotEmpty(point.getDeviceId())){
				model.setDeviceName(Arrays.stream(point.getDeviceId().split(","))
						.filter(StringUtils::isNotEmpty).map(String::trim)
						.filter(s -> s.matches("\\d+"))
						.map(Long::valueOf)
						.map(deviceMap::get)
						.filter(Objects::nonNull)
						.map(IocDevice::getDeviceName)
						.collect(Collectors.joining(",")));
			}
			return model;
		}).collect(Collectors.toList());
	}
}