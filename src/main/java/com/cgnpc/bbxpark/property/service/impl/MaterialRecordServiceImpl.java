package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.MaterialRecordSourceEnum;
import com.cgnpc.bbxpark.common.enums.MaterialRecordTypeEnum;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.property.domain.MaterialRecord;
import com.cgnpc.bbxpark.property.dto.model.MaterialModel;
import com.cgnpc.bbxpark.property.dto.model.MaterialRecordExportModel;
import com.cgnpc.bbxpark.property.dto.model.MaterialRecordModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialRecordPageParam;
import com.cgnpc.bbxpark.property.mapper.MaterialRecordRepository;
import com.cgnpc.bbxpark.property.service.IMaterialRecordService;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
/***
 * @Description 材料记录服务实现
 * @author huangyongtao
 * @date 2025/9/22 17:28
 */
@Service
public class MaterialRecordServiceImpl extends ServiceImpl<MaterialRecordRepository, MaterialRecord> implements IMaterialRecordService {

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private IUserApiService userApiService;
	/**
	 * 根据材料记录标识获得材料记录详情信息.
	 * @Param [id] 材料记录标识
	 * @Return 材料记录详情信息
	 */
	@Override
	public MaterialRecordModel detail(Long id) {
		MaterialRecord materialRecord = this.getById(id);
		AssertUtils.notNull(materialRecord, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(materialRecord, MaterialRecordModel::new);
	}

	/**
	 * 获取材料记录列表(分页).
	 * @Param param 材料记录查询条件
	 * @Return 材料记录信息列表（分页）
	 */
	@Override
	public IPage<MaterialRecordModel> page(MaterialRecordPageParam param) {
		IPage<MaterialRecord> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<MaterialRecordModel> models = BeanUtils.convertListTo(result.getRecords(), MaterialRecordModel::new);
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);

	}

	/**
	 * 获取材料记录列表.
	 * @Param param 材料记录查询条件
	 * @Return 材料记录信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaterialRecordModel> list(MaterialRecordListParam param) {
		List<MaterialRecord> materialRecords = this.list(buildQuery(BeanUtils.convertTo(param, MaterialRecordPageParam::new)));
		return BeanUtils.convertListTo(materialRecords, MaterialRecordModel::new);
	}

	@Override
	public Boolean materialRecordEasyExport(HttpServletResponse response, MaterialRecordPageParam param) {
		AssertUtils.notNull(param.getMaterialId(), "材料id不能为空");
		MaterialModel materialModel = materialService.detail(param.getMaterialId());
		//生成excel
//		TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
		param.setSize(-1);
		param.setCurrent(1);
		String title = materialModel.getMaterialName() + "（" + materialModel.getMaterialCode() + "）出入库记录";
		IPage<MaterialRecordModel> pageResult =  this.page(param);
		List<MaterialRecordExportModel> exportModels = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
			pageResult.getRecords().forEach(model->{
				MaterialRecordExportModel exportModel = new MaterialRecordExportModel();
				BeanUtils.copyProperties(model, exportModel);
				exportModel.setRecordTypeStr(MaterialRecordTypeEnum.getName(model.getRecordType()));
				exportModel.setDataSourceStr(MaterialRecordSourceEnum.getName(model.getDataSource()));
				exportModel.setInboundNo("NO " + model.getInboundNo());
				exportModel.setCreatorUname(model.getCreateBy());
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportModels.add(exportModel);
			});
		}
		ExcelExportUtils.exportExcel(response, title,exportModels, MaterialRecordExportModel.class, title);
		return true;
	}

	private LambdaQueryWrapper buildQuery(MaterialRecordPageParam param) {
		LambdaQueryWrapper<MaterialRecord> query = new LambdaQueryWrapper<>();
		// 根据入库单号筛选
		query.eq(ObjectUtil.isNotEmpty(param.getInboundNo()), MaterialRecord::getInboundNo, param.getInboundNo());
		//根据材料id
		query.eq(ObjectUtil.isNotEmpty(param.getMaterialId()), MaterialRecord::getMaterialId, param.getMaterialId());
		// 根据材料名称筛选
		query.like(ObjectUtil.isNotEmpty(param.getMaterialName()), MaterialRecord::getMaterialName, param.getMaterialName());
		//根据材料编码筛选
		query.eq(ObjectUtil.isNotEmpty(param.getMaterialCode()), MaterialRecord::getMaterialCode, param.getMaterialCode());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MaterialRecord::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(MaterialRecord::getCreateTime);
		return query;
	}


}
