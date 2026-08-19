
package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.device.domain.DeviceInfo;
import com.cgnpc.bbxpark.device.domain.DeviceOperationLog;
import com.cgnpc.bbxpark.device.dto.model.DeviceOperationLogModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceOperationLogPageParam;
import com.cgnpc.bbxpark.device.kafka.DeviceOperationLogMessage;
import com.cgnpc.bbxpark.device.mapper.DeviceOperationLogRepository;
import com.cgnpc.bbxpark.device.service.IDeviceInfoService;
import com.cgnpc.bbxpark.device.service.IDeviceOperationLogService;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备控制（操作）日志服务实现
 */
@Slf4j
@Service("deviceOperationLogService")
public class DeviceOperationLogServiceImpl extends BaseServiceImpl<DeviceOperationLogRepository, DeviceOperationLog> implements IDeviceOperationLogService {
    /**
     * 注入repository.
     */
	@Autowired
	private DeviceOperationLogRepository deviceOperationLogRepository;

	@Autowired
	private IDeviceInfoService deviceInfoService;
	@Autowired
	private IParkSpaceService parkSpaceService;

    @Autowired
    private IUserApiService userApiService;

    @Autowired
    private ITenantMemberService tenantMemberService;

    @Autowired
    private IRoleApiService roleApiService;
    @Value("${bbx.role.system:1}")
    private String systemRoleCode;

//	@Override
	public void saveDeviceOperationLog(String msg) {
		DeviceOperationLogMessage kafkaModel = null;
		try {
			kafkaModel = JSON.parseObject(msg, DeviceOperationLogMessage.class);
		} catch (Exception e) {
			log.error("解析kafka消息失败", e);
			throw new RuntimeException("解析kafka消息失败");
		}
		DeviceOperationLog deviceOperationLog = buildOperationLog(kafkaModel);
		save(deviceOperationLog);
	}

	@Override
	public IPage<DeviceOperationLogModel> findPage(DeviceOperationLogPageParam param) {
//        IPage<DeviceOperationLog> page = new Page<>(param.getCurrent(), param.getSize());
//		IPage<DeviceOperationLog> pageList = deviceOperationLogRepository.selectPage(page, buildQuery(param));
//		List<DeviceOperationLogModel> deviceOperationLogModels = BeanUtils.convertListTo(pageList.getRecords(), DeviceOperationLogModel::new);
//		updateSpaceNames(deviceOperationLogModels);
        return ConvertUtil.pageConvert(param.getCurrent(), 0, param.getSize(), new ArrayList<>());
	}

	private void updateSpaceNames(List<DeviceOperationLogModel> deviceOperationLogModels) {
		if (ObjectUtil.isNotEmpty(deviceOperationLogModels)) {
			List<Long> spaceIds = deviceOperationLogModels.stream()
					.map(DeviceOperationLogModel::getSpaceId)
					.filter(ObjectUtil::isNotEmpty)
					.collect(Collectors.toList());

			if (ObjectUtil.isNotEmpty(spaceIds)) {
				Map<Long, ParkSpaceFullModel> fullSpaceMap = parkSpaceService.findFullSpaceMap(spaceIds, null);
				deviceOperationLogModels.forEach(device -> {
					ParkSpaceFullModel parkSpaceFullModel = fullSpaceMap.get(device.getSpaceId());
					Optional.ofNullable(parkSpaceFullModel).ifPresent(model -> device.setSpaceName(model.getFullPath()));
				});
			}
		}
	}

	private LambdaQueryWrapper<DeviceOperationLog> buildQuery(DeviceOperationLogPageParam param){
		LambdaQueryWrapper<DeviceOperationLog> query = new LambdaQueryWrapper<>();
		String headerUserId = WebFrameworkUtils.getHeaderUserId();
		// 园区管理员角色  查当前园区下所有设备
		query.and(param.getUserId() == null && param.getTenantId() != null,wrapper -> {
            return wrapper.and(wrapper1 -> {
return wrapper1.eq(DeviceOperationLog::getTenantId, param.getTenantId());
})
                    .and(wrapper2 -> {
return wrapper2.or().isNull(DeviceOperationLog::getSpaceId).or().apply(" space_id in (select space_id from bbx_user_space where tenant_id = {0} and user_id = {1} )", param.getTenantId(), headerUserId);
});
		});
		// 普通角色 差所属空间下所有用户
		query.and((param.getUserId()!=null && param.getTenantId()!= null), wrapper -> {
            return wrapper.apply(" space_id in (select space_id from bbx_user_space where tenant_id = {0} and user_id = {1} )", param.getTenantId(), param.getUserId());
		});
		// 空间位置查询
		query.eq(ObjectUtil.isNotEmpty(param.getSpaceId()), DeviceOperationLog::getSpaceId, param.getSpaceId());
		// 操作人查询
		query.like(ObjectUtil.isNotEmpty(param.getOperator()),DeviceOperationLog::getCreateUserName, param.getOperator());
		// 设备名称查询
		query.like(ObjectUtil.isNotEmpty(param.getDeviceName()), DeviceOperationLog::getDeviceName, param.getDeviceName());
		// 操作时间查询
		if (param.getStartTime() != null) {
			query.ge(DeviceOperationLog::getCreateTime, DateUtils.truncate(param.getStartTime(), Calendar.DATE));
		}
		if (param.getEndTime() != null) {
			Date endTime = DateUtils.truncate(param.getEndTime(), Calendar.DATE);
			endTime = DateUtils.addDays(endTime, 1);
			endTime = DateUtils.addSeconds(endTime, -1);
			query.le(DeviceOperationLog::getCreateTime, endTime);
		}
		query.orderByDesc(DeviceOperationLog::getCreateTime);
		return query;
	}

	private void buildOperationLogRole(DeviceOperationLogPageParam param){
		String userId = userApiService.getCurrentStaffNo();
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        if(roleApiService.hasRole(userApiService.getCurrentStaffNo(),systemRoleCode)){
            //如果是超级管理员
            return;
        }

		TenantMemberListParam param1= new TenantMemberListParam();
		param1.setTenantId(tenantId);
		param1.setUserId(userId);
		List<TenantMemberDomain> domainList = tenantMemberService.list(param1);

		TenantMemberDomain domain = domainList.get(0);
		// 如果是园区管理员
		if(domain.getIdentity() != null && domain.getIdentity() == 1){
			param.setTenantId(tenantId);
			return;
		}
		// 否则就是普通人员
		param.setTenantId(tenantId);
		param.setUserId(userId);
	}

	private DeviceOperationLog buildOperationLog(DeviceOperationLogMessage kafkaModel) {
		DeviceOperationLog operationLog = new DeviceOperationLog();
		operationLog.setDeviceId(kafkaModel.getDeviceId());
		operationLog.setCreateUserName(kafkaModel.getCreateUserName());
		operationLog.setCreatorId(kafkaModel.getCreateUserId());
		operationLog.setCreateTime(kafkaModel.getCreateTime());
		operationLog.setUpdatorId(kafkaModel.getCreateUserId());
		operationLog.setUpdateTime(kafkaModel.getCreateTime());
		operationLog.setType(kafkaModel.getType());
		operationLog.setParams(kafkaModel.getParams());
		operationLog.setResponse(kafkaModel.getResponse());
		operationLog.setTenantId(kafkaModel.getGroupId());
		DeviceInfo deviceInfo = deviceInfoService.getOne(new LambdaQueryWrapper<DeviceInfo>().eq(DeviceInfo::getDeviceId, kafkaModel.getDeviceId()));
		if (ObjectUtil.isNotEmpty(deviceInfo)){
			operationLog.setDeviceName(deviceInfo.getDeviceAlias());
			operationLog.setSpaceId(deviceInfo.getSpaceId());
		}
		return operationLog;
	}
}
