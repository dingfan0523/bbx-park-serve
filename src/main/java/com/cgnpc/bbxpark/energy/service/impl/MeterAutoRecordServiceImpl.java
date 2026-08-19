
package com.cgnpc.bbxpark.energy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.DevicePlatformEnum;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.device.dto.model.IocDeviceModel;
import com.cgnpc.bbxpark.device.dto.param.IocDeviceListParam;
import com.cgnpc.bbxpark.device.service.IIocDeviceService;
import com.cgnpc.bbxpark.energy.domain.MeterAutoRecord;
import com.cgnpc.bbxpark.energy.dto.model.MeterAutoRecordModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordExportModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterAutoRecordParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordPageParam;
import com.cgnpc.bbxpark.energy.mapper.MeterAutoRecordRepository;
import com.cgnpc.bbxpark.energy.service.IMeterAutoRecordService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/***
 * @Description 抄表自动上报记录服务实现
 * @author huangyongtao
 * @date 2025/4/21 9:35
 */
@Service("meterAutoRecordService")
public class MeterAutoRecordServiceImpl extends ServiceImpl<MeterAutoRecordRepository, MeterAutoRecord> implements IMeterAutoRecordService {

	@Autowired
	private ITenantInfoService tenantInfoService;

	@Autowired
	private IIocDeviceService iocDeviceService;

	/**
	 * 根据抄表自动上报记录标识获得抄表自动上报记录详情信息.
	 * @Param [id] 抄表自动上报记录标识
	 * @Return 抄表自动上报记录详情信息
	 */
	@Override
	public MeterAutoRecordModel detail(Long id) {
		MeterAutoRecord meterAutoRecord = this.getById(id);
		AssertUtils.notNull(meterAutoRecord, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(meterAutoRecord, MeterAutoRecordModel::new);
	}

	/**
	 * 获取抄表自动上报记录列表(分页).
	 * @Param param 抄表自动上报记录查询条件
	 * @Return 抄表自动上报记录信息列表（分页）
	 */
	@Override
	public IPage<MeterRecordModel> page(MeterRecordPageParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		IPage<MeterAutoRecord> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		List<MeterRecordModel> meterAutoRecordModels = BeanUtils.convertListTo(page.getRecords(), MeterRecordModel::new);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), meterAutoRecordModels);
	}

	/**
	 * 获取抄表自动上报记录列表.
	 * @Param param 抄表自动上报记录查询条件
	 * @Return 抄表自动上报记录信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeterAutoRecordModel> list(MeterAutoRecordParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		List<MeterAutoRecord> meterAutoRecords = this.list(buildQuery(BeanUtils.convertTo(param, MeterRecordPageParam::new)));
		return BeanUtils.convertListTo(meterAutoRecords, MeterAutoRecordModel::new);
	}

	@Override
	public Boolean meterAutoEasyExport(HttpServletResponse response, MeterRecordPageParam param) {
		//生成excel
		TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
		param.setSize(Integer.MAX_VALUE);
		param.setCurrent(1);

		String title = tenantInfoModel.getName() + DeviceReadingTypeEnum.getName(param.getReadingType()) + "流水-自动上报";
		IPage<MeterRecordModel> pageResult =  this.page(param);
		List<MeterRecordExportModel> exportModels = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
			pageResult.getRecords().forEach(model->{
				MeterRecordExportModel exportModel = new MeterRecordExportModel();
				BeanUtils.copyProperties(model, exportModel);
				if(DeviceReadingTypeEnum.ELECTRICITY.getCode().equals(model.getReadingType())){
					exportModel.setReadingValueDouble(model.getReadingValue().setScale(2).doubleValue());
				}else{
					exportModel.setReadingValueDouble(model.getReadingValue().setScale(3).doubleValue());
				}
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportModel.setDeviceTypeStr(model.getDeviceType().equals(Status.enabled.getKey()) ? "智能" : "非智能");
				exportModels.add(exportModel);
			});
		}
		ExcelExportUtils. exportExcel(response, title,exportModels, MeterRecordExportModel.class, title);
		return true;
	}

	@Override
	public Boolean executeAutoReading() {
		IocDeviceListParam param = new IocDeviceListParam();
		param.setIotDevicePlatformList(Arrays.asList(DevicePlatformEnum.OWN.getCode(), DevicePlatformEnum.UNIFIED.getCode(), DevicePlatformEnum.SECURE.getCode()));
		param.setReadingDevice(Status.enabled.getKey());
		List<IocDeviceModel> iocDevices = iocDeviceService.findListByJob(param);
		if(CollectionUtil.isEmpty(iocDevices)){
			return true;
		}
		List<MeterAutoRecord> meterAutoRecords = new ArrayList<>();
		iocDevices.forEach(iocDevice -> {
			MeterAutoRecord meterAutoRecord = this.getOne(Wrappers.<MeterAutoRecord>lambdaQuery().eq(MeterAutoRecord::getDeviceId, iocDevice.getId())
					.orderByDesc(MeterAutoRecord::getCreateTime)
					.last("limit 1")
			);
			MeterAutoRecord record = new MeterAutoRecord();
			BeanUtils.copyProperties(iocDevice, record);
			record.setDeviceId(iocDevice.getId());
			record.setDeviceType(Status.enabled.getKey());
			record.setCreateTime(new Date());
			record.setCreatorId("0");
			record.setUpdateTime(new Date());
			record.setUpdatorId("0");
			record.setId(null);
			if(ObjectUtil.isEmpty(meterAutoRecord)){
				record.setReadingValue(new BigDecimal(100));
			}else{
				//0-20小数的随机数
				BigDecimal randomAdd = new BigDecimal(RandomUtil.randomDouble(20))
						.setScale(2, RoundingMode.HALF_UP); // 保留两位小数
				record.setReadingValue(meterAutoRecord.getReadingValue().add(randomAdd));
			}
			meterAutoRecords.add(record);

		});
		return this.saveBatch(meterAutoRecords);
	}

	private LambdaQueryWrapper buildQuery(MeterRecordPageParam param) {
		LambdaQueryWrapper<MeterAutoRecord> query = new LambdaQueryWrapper<>();
		//设备名称
		query.like(ObjectUtil.isNotEmpty(param.getDeviceName()), MeterAutoRecord::getDeviceName, param.getDeviceName());
		//设备编码
		query.like(ObjectUtil.isNotEmpty(param.getReadingCode()), MeterAutoRecord::getReadingCode, param.getReadingCode());
		// 空间位置
		query.eq(ObjectUtil.isNotEmpty(param.getSpaceId()), MeterAutoRecord::getSpaceId, param.getSpaceId());
		//能源类型
		query.eq(ObjectUtil.isNotEmpty(param.getReadingType()), MeterAutoRecord::getReadingType, param.getReadingType());
		//设备类型
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceType()), MeterAutoRecord::getDeviceType, param.getDeviceType());
		//读表时间
		if (param.getCreateTimeStart() != null && param.getCreateTimeEnd() != null ){
			query.between(MeterAutoRecord::getCreateTime, param.getCreateTimeStart(), param.getCreateTimeEnd());
		}
		query.eq(ObjectUtil.isNotEmpty(param.getTenantId()), MeterAutoRecord::getTenantId, param.getTenantId());
		query.orderByDesc(MeterAutoRecord::getCreateTime);
		return query;
	}
}
