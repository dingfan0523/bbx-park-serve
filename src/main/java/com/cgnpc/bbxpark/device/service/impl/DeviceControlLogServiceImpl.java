
package com.cgnpc.bbxpark.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.AccessResultEnum;
import com.cgnpc.bbxpark.common.enums.ChannelEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.domain.DeviceControlLog;
import com.cgnpc.bbxpark.device.domain.IocDevice;
import com.cgnpc.bbxpark.device.dto.model.DeviceControlLogExportModel;
import com.cgnpc.bbxpark.device.dto.model.DeviceControlLogModel;
import com.cgnpc.bbxpark.device.dto.param.DeviceControlLogPageParam;
import com.cgnpc.bbxpark.device.mapper.DeviceControlLogRepository;
import com.cgnpc.bbxpark.device.service.IDeviceControlLogService;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DeviceControlLogServiceImpl extends ServiceImpl<DeviceControlLogRepository, DeviceControlLog> implements IDeviceControlLogService {
	@Resource
	private IUserApiService userApiService;
	@Resource
	private ITenantInfoService tenantInfoService;
	@Resource
	private IIocDeviceService iocDeviceService;
	@Override
	public IPage<DeviceControlLogModel> page(DeviceControlLogPageParam param) {
		IPage<DeviceControlLog> resultPage = page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtils.isEmpty(resultPage.getRecords())) {
			return ConvertUtil.pageEmptyConvert(resultPage.getCurrent(), resultPage.getSize());
		}
		List<DeviceControlLogModel> list = BeanUtils.convertListTo(resultPage.getRecords(), DeviceControlLogModel::new);
		return ConvertUtil.pageConvert(resultPage.getCurrent(),resultPage.getTotal(),resultPage.getSize(), list);
	}

	@Override
	public Boolean saveLog(Long deviceId,String channel, String result) {
		IocDevice device = iocDeviceService.getById(deviceId);
		UserInfoModel userInfo = userApiService.detail(WebFrameworkUtils.getHeaderUserId());
		DeviceControlLog log = new DeviceControlLog();
		log.setChannel(channel);
		log.setDeviceId(deviceId);
		log.setDeviceName(device.getDeviceName());
		log.setName(userInfo.getUserName());
		log.setStaffid(userInfo.getStaffid());
		log.setResult(result);
		save(log);
		return true;
	}

	@Override
	public Boolean export(HttpServletResponse response, DeviceControlLogPageParam param) {
		//生成excel
		TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
		param.setCurrent(Integer.MAX_VALUE);
		param.setSize(1);

		String title = tenantInfoModel.getName() + "设备控制日志信息统计表";
        IPage<DeviceControlLogModel> page = this.page(param);
        List<DeviceControlLogExportModel> exportList = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(page.getRecords())){
            page.getRecords().forEach(model->{
				DeviceControlLogExportModel exportModel = new DeviceControlLogExportModel();
				BeanUtils.copyProperties(model, exportModel);
				exportModel.setChannelStr(ChannelEnum.getName(model.getChannel()));
				exportModel.setResultStr(AccessResultEnum.getName(model.getResult()));
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportList.add(exportModel);
			});
		}
		ExcelExportUtils.exportExcel(response, title,exportList, DeviceControlLogExportModel.class, title);
		return true;
	}

	private LambdaQueryWrapper<DeviceControlLog> buildQuery(DeviceControlLogPageParam param) {
		LambdaQueryWrapper<DeviceControlLog> query = new LambdaQueryWrapper<>();
		// 姓名或工号模糊筛选
		query.and(ObjectUtil.isNotEmpty(param.getName()), wrapper -> wrapper
				.like(DeviceControlLog::getName, param.getName()).or().like(DeviceControlLog::getStaffid, param.getName()));
		//设备id筛选
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceId()), DeviceControlLog::getDeviceId, param.getDeviceId());
		//操作渠道筛选
		query.eq(ObjectUtil.isNotEmpty(param.getChannel()), DeviceControlLog::getChannel, param.getChannel());
		//操作结果筛选
		query.eq(ObjectUtil.isNotEmpty(param.getResult()), DeviceControlLog::getResult, param.getResult());
		//操作时间筛选
		query.between(ObjectUtil.isNotEmpty(param.getStartTime()) && ObjectUtil.isNotEmpty(param.getEndTime()), DeviceControlLog::getCreateTime, param.getStartTime(), param.getEndTime());
		//是否删除
		query.eq(DeviceControlLog::getDeleted, Status.enabled.getKey());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), DeviceControlLog::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(DeviceControlLog::getCreateTime);
		return query;
	}
}
