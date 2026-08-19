
package com.cgnpc.bbxpark.invitation.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.enums.*;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.invitation.domain.AccessRecord;
import com.cgnpc.bbxpark.invitation.dto.model.AccessRecordExportModel;
import com.cgnpc.bbxpark.invitation.dto.model.AccessRecordModel;
import com.cgnpc.bbxpark.invitation.dto.model.AccessRecordVisitorExportModel;
import com.cgnpc.bbxpark.invitation.dto.param.AccessRecordPageParam;
import com.cgnpc.bbxpark.invitation.mapper.AccessRecordRepository;
import com.cgnpc.bbxpark.invitation.service.IAccessRecordService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Service("visitRecordService")
public class AccessRecordServiceImpl extends ServiceImpl<AccessRecordRepository, AccessRecord> implements IAccessRecordService {
    /**
     * 注入repository.
     */
	@Autowired
	private AccessRecordRepository accessRecordRepository;
	@Resource
	private ITenantInfoService tenantInfoService;

	@Override
	public IPage<AccessRecordModel> page(AccessRecordPageParam param) {
		IPage<AccessRecord> resultPage = page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtils.isEmpty(resultPage.getRecords())) {
			return ConvertUtil.pageEmptyConvert(resultPage.getCurrent(), resultPage.getSize());
		}
		List<AccessRecordModel> list = BeanUtils.convertListTo(resultPage.getRecords(), AccessRecordModel::new);
		return ConvertUtil.pageConvert(resultPage.getCurrent(),resultPage.getTotal(),resultPage.getSize(), list);
	}

	@Override
	public AccessRecordModel detail(Long id) {
		AccessRecord accessRecord = accessRecordRepository.selectById(id);
		AssertUtils.notNull(accessRecord, "通行记录不存在");
		return BeanUtils.convertTo(accessRecord, AccessRecordModel::new);
	}

	@Override
	public Boolean export(HttpServletResponse response, AccessRecordPageParam param) {
		//生成excel
		TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
		param.setSize(Integer.MAX_VALUE);
		param.setCurrent(1);

		String title = tenantInfoModel.getName() + "访客来访信息统计表";
		IPage<AccessRecordModel> pageResult =  this.page(param);
		List<AccessRecordVisitorExportModel> exportList = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
			pageResult.getRecords().forEach(model->{
				AccessRecordVisitorExportModel exportModel = new AccessRecordVisitorExportModel();
				BeanUtils.copyProperties(model, exportModel);
				exportModel.setAccessDir(AccessDirEnum.getName(model.getAccessDir()));
				exportModel.setAccessWay(AccessWayEnum.getName(model.getAccessWay()));
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportList.add(exportModel);
			});
		}
		ExcelExportUtils.exportExcel(response, title,exportList, AccessRecordVisitorExportModel.class, title);
		return true;
	}

	@Override
	public Boolean doorExport(HttpServletResponse response, AccessRecordPageParam param) {
		//生成excel
        TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
        param.setSize(Integer.MAX_VALUE);
        param.setCurrent(1);

		String title = tenantInfoModel.getName() + "通行记录统计表";
		IPage<AccessRecordModel> pageResult =  this.page(param);
		List<AccessRecordExportModel> exportList = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
			pageResult.getRecords().forEach(model->{
				AccessRecordExportModel exportModel = new AccessRecordExportModel();
				BeanUtils.copyProperties(model, exportModel);
				exportModel.setPersonTypeStr(PersonTypeEnum.getName(model.getPersonType()));
				exportModel.setAccessDir(AccessDirEnum.getName(model.getAccessDir()));
				exportModel.setAccessWay(AccessWayEnum.getName(model.getAccessWay()));
				exportModel.setAccessResult(AccessResultEnum.getName(model.getAccessResult()));
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportList.add(exportModel);
			});
		}
		ExcelExportUtils.exportExcel(response, title,exportList, AccessRecordExportModel.class, title);
		return true;
	}


	private LambdaQueryWrapper<AccessRecord> buildQuery(AccessRecordPageParam param) {
		LambdaQueryWrapper<AccessRecord> query = new LambdaQueryWrapper<>();
		//人员类型筛选
		query.eq(ObjectUtil.isNotEmpty(param.getPersonType()), AccessRecord::getPersonType, param.getPersonType());
		// 姓名或工号模糊筛选
		query.and(ObjectUtil.isNotEmpty(param.getName()), wrapper -> wrapper
				.like(AccessRecord::getName, param.getName()).or().like(AccessRecord::getStaffid, param.getName()));
		//设备id筛选
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceId()), AccessRecord::getDeviceId, param.getDeviceId());
		query.in(!CollectionUtils.isEmpty(param.getDeviceIds()), AccessRecord::getDeviceId, param.getDeviceIds());
		//设备位置id筛选
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceLocationId()), AccessRecord::getDeviceLocationId, param.getDeviceLocationId());
		query.in(!CollectionUtils.isEmpty(param.getDeviceLocationIds()), AccessRecord::getDeviceLocationId, param.getDeviceLocationIds());
		//通行方向筛选
		query.eq(ObjectUtil.isNotEmpty(param.getAccessDir()), AccessRecord::getAccessDir, param.getAccessDir());
		//通行方式筛选
		query.eq(ObjectUtil.isNotEmpty(param.getAccessWay()), AccessRecord::getAccessWay, param.getAccessWay());
		//通行结果筛选
		query.eq(ObjectUtil.isNotEmpty(param.getAccessResult()), AccessRecord::getAccessResult, param.getAccessResult());
		//通行时间筛选
		query.between(ObjectUtil.isNotEmpty(param.getStartTime()) && ObjectUtil.isNotEmpty(param.getEndTime()), AccessRecord::getCreateTime, param.getStartTime(), param.getEndTime());
		//是否删除
		query.eq( AccessRecord::getDeleted, Status.enabled.getKey());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), AccessRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(AccessRecord::getCreateTime);
		return query;
	}
}
