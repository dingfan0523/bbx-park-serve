
package com.cgnpc.bbxpark.energy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.DeviceReadingTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecord;
import com.cgnpc.bbxpark.energy.dto.model.MeterPersonRecordModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordExportModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterPersonRecordParam;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordPageParam;
import com.cgnpc.bbxpark.energy.mapper.MeterPersonRecordRepository;
import com.cgnpc.bbxpark.energy.service.IMeterPersonRecordService;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
/***
 * @Description 抄表人工抄表记录服务实现
 * @author huangyongtao
 * @date 2025/4/21 9:36
 */
@Service("meterPersonRecordService")
public class MeterPersonRecordServiceImpl extends ServiceImpl<MeterPersonRecordRepository, MeterPersonRecord> implements IMeterPersonRecordService {

	@Autowired
	private ITenantInfoService tenantInfoService;

	/**
	 * 根据抄表人工抄表记录标识获得抄表人工抄表记录详情信息.
	 * @Param [id] 抄表人工抄表记录标识
	 * @Return 抄表人工抄表记录详情信息
	 */
	@Override
	public MeterPersonRecordModel detail(Long id) {
		MeterPersonRecord meterPersonRecord = this.getById(id);
		AssertUtils.notNull(meterPersonRecord, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(meterPersonRecord, MeterPersonRecordModel::new);
	}

	/**
	 * 获取抄表人工抄表记录列表(分页).
	 * @Param param 抄表人工抄表记录查询条件
	 * @Return 抄表人工抄表记录信息列表（分页）
	 */
	@Override
	public IPage<MeterRecordModel> page(MeterRecordPageParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		IPage<MeterPersonRecord> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		List<MeterRecordModel> meterPersonRecordModels = BeanUtils.convertListTo(page.getRecords(), MeterRecordModel::new);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), meterPersonRecordModels);

	}

	/**
	 * 获取抄表人工抄表记录列表.
	 * @Param param 抄表人工抄表记录查询条件
	 * @Return 抄表人工抄表记录信息列表
	 */
	@Override
	@SneakyThrows
	public List<MeterPersonRecordModel> list(MeterPersonRecordParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		List<MeterPersonRecord> meterPersonRecords = this.list(buildQuery(BeanUtils.convertTo(param, MeterRecordPageParam::new)));
		return BeanUtils.convertListTo(meterPersonRecords, MeterPersonRecordModel::new);
	}

	@Override
	public Boolean meterPersonEasyExport(HttpServletResponse response, MeterRecordPageParam param) {
		//生成excel
        TenantInfo tenantInfo = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
        param.setSize(Integer.MAX_VALUE);
		param.setCurrent(1);

		String title = tenantInfo.getName() + DeviceReadingTypeEnum.getName(param.getReadingType()) + "流水-人工抄表";
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

	private LambdaQueryWrapper buildQuery(MeterRecordPageParam param) {
		LambdaQueryWrapper<MeterPersonRecord> query = new LambdaQueryWrapper<>();
		//设备名称
		query.like(ObjectUtil.isNotEmpty(param.getDeviceName()), MeterPersonRecord::getDeviceName, param.getDeviceName());
		//设备编码
		query.like(ObjectUtil.isNotEmpty(param.getReadingCode()), MeterPersonRecord::getReadingCode, param.getReadingCode());
		// 空间位置
		query.eq(ObjectUtil.isNotEmpty(param.getSpaceId()), MeterPersonRecord::getSpaceId, param.getSpaceId());
		//能源类型
		query.eq(ObjectUtil.isNotEmpty(param.getReadingType()), MeterPersonRecord::getReadingType, param.getReadingType());
		//设备类型
		query.eq(ObjectUtil.isNotEmpty(param.getDeviceType()), MeterPersonRecord::getDeviceType, param.getDeviceType());
		//读表时间
		if (param.getCreateTimeStart() != null && param.getCreateTimeEnd() != null ){
			query.between(MeterPersonRecord::getCreateTime, param.getCreateTimeStart(), param.getCreateTimeEnd());
		}
		query.eq(ObjectUtil.isNotEmpty(param.getTenantId()), MeterPersonRecord::getTenantId, param.getTenantId());
		query.orderByDesc(MeterPersonRecord::getCreateTime);
		return query;
	}

}
