package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.enums.MaterialRecordSourceEnum;
import com.cgnpc.bbxpark.common.enums.MaterialRecordTypeEnum;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.domain.MaterialInbound;
import com.cgnpc.bbxpark.property.domain.MaterialRecord;
import com.cgnpc.bbxpark.property.dto.model.MaterialInboundModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialInboundParam;
import com.cgnpc.bbxpark.property.mapper.MaterialInboundRepository;
import com.cgnpc.bbxpark.property.service.IMaterialInboundService;
import com.cgnpc.bbxpark.property.service.IMaterialRecordService;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @Description 材料入库服务实现
 * @author huangyongtao
 * @date 2025/9/22 17:27
 */
@Service
public class MaterialInboundServiceImpl extends ServiceImpl<MaterialInboundRepository, MaterialInbound> implements IMaterialInboundService {

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private IMaterialRecordService materialRecordService;

	@Autowired
	private IUserApiService userApiService;

	/**
	 * 获取材料入库列表(分页).
	 * @Param param 材料入库查询条件
	 * @Return 材料入库信息列表（分页）
	 */
	@Override
	public IPage<MaterialInboundModel> page(MaterialInboundPageParam param) {
		IPage<MaterialInbound> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<MaterialInboundModel> models = BeanUtils.convertListTo(result.getRecords(), MaterialInboundModel::new);
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}

	/**
	 * 获取材料入库列表.
	 * @Param param 材料入库查询条件
	 * @Return 材料入库信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaterialInboundModel> list(MaterialInboundListParam param) {
		List<MaterialInbound> materialInbounds = this.list(buildQuery(BeanUtils.convertTo(param, MaterialInboundPageParam::new)));
		return BeanUtils.convertListTo(materialInbounds, MaterialInboundModel::new);
	}



	/**
	 * 批量新增材料入库.
	 * @Param params 材料入库信息列表
	 * @Return 批量新增材料入库是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized Boolean addBatch(List<MaterialInboundParam> params) {
		AssertUtils.notEmpty(params, "入库单信息不能为空");
		List<MaterialInbound> materialInbounds = BeanUtils.convertListTo(params, MaterialInbound::new);
		List<Long> materialIds = materialInbounds.stream().map(MaterialInbound::getMaterialId).collect(Collectors.toList());
		AssertUtils.notEmpty(materialIds, "入库单材料不能为空");
		List<Material> materials = (List<Material>)materialService.listByIds(materialIds);
		Map<Long, Material> materialMap = CollectionUtil.isEmpty(materials) ? new HashMap<>() : materials.stream().collect(Collectors.toMap(Material::getId, v -> v));
		List<Material> materialUpdates = new ArrayList<>();
		List<MaterialRecord> materialRecords = new ArrayList<>();
        String no = calInboundNo();
        String createBy = userApiService.getCurrentStaffName();
		materialInbounds.forEach(materialInbound -> {
			materialInbound.setInboundNo(no);
            materialInbound.setCreateBy(createBy);
			Material material = materialMap.get(materialInbound.getMaterialId());
			AssertUtils.notNull(materialInbound.getQuantity(), materialInbound.getMaterialName() + "材料数量不能为空");
			AssertUtils.notNull(material, materialInbound.getMaterialName() + "材料不存在");
			materialInbound.setMaterialName(material.getMaterialName());
			materialInbound.setMaterialCode(material.getMaterialCode());
			materialInbound.setMaterialType(material.getMaterialType());
			//入库材料库存更新
			Material materialUpdate = new Material();
			materialUpdate.setId(material.getId());
			materialUpdate.setStockQuantity(material.getStockQuantity() + materialInbound.getQuantity());
			materialUpdate.setStockWarning(material.getStockWarning());
            materialUpdate.setInbound(material.getInbound() + 1);
            materialUpdate.setInboundQuantity(material.getInboundQuantity() + materialInbound.getQuantity());
			materialService.handelStockStatus(materialUpdate);
            materialUpdate.setRemark(material.getRemark());
			materialUpdates.add(materialUpdate);
			//入库记录
			MaterialRecord materialRecord = new MaterialRecord();
			BeanUtils.copyProperties(materialInbound, materialRecord);
			materialRecord.setRecordType(MaterialRecordTypeEnum.IN.getCode());
			materialRecord.setDataSource(MaterialRecordSourceEnum.HAND.getCode());
			materialRecord.setCurrentStock(material.getStockQuantity() + materialInbound.getQuantity());
			materialRecords.add(materialRecord);
		});
		this.saveBatch(materialInbounds);
		if (CollectionUtil.isNotEmpty(materialUpdates)) {
			materialService.updateBatchById(materialUpdates);
		}
		if(CollectionUtil.isNotEmpty(materialRecords)){
			materialRecordService.saveBatch(materialRecords);
		}
		return true;
	}

    @Override
    public MaterialInboundModel getInboundNo() {
        MaterialInboundModel model = new MaterialInboundModel();
        model.setInboundNo(calInboundNo());
        model.setCreatorId(userApiService.getCurrentStaffNo());
        model.setCreateBy(userApiService.getCurrentStaffName());
        return model;
    }

    private String calInboundNo(){
		MaterialInbound materialInbound = this.getOne(Wrappers.<MaterialInbound>lambdaQuery().select(MaterialInbound::getInboundNo).orderByDesc(MaterialInbound::getInboundNo).last("limit 1"));
		if(ObjectUtil.isEmpty(materialInbound)){
			return formatToSixDigits(1);
		}else{
			return formatToSixDigits(convertToInteger(materialInbound.getInboundNo()) + 1);
		}
	}

	private LambdaQueryWrapper buildQuery(MaterialInboundPageParam param) {
		LambdaQueryWrapper<MaterialInbound> query = new LambdaQueryWrapper<>();
		// 根据入库单号筛选
		query.eq(ObjectUtil.isNotEmpty(param.getInboundNo()), MaterialInbound::getInboundNo, param.getInboundNo());
		//根据材料id
		query.eq(ObjectUtil.isNotEmpty(param.getMaterialId()), MaterialInbound::getMaterialId, param.getMaterialId());
		// 根据材料名称筛选
		query.like(ObjectUtil.isNotEmpty(param.getMaterialName()), MaterialInbound::getMaterialName, param.getMaterialName());
		//根据材料编码筛选
		query.eq(ObjectUtil.isNotEmpty(param.getMaterialCode()), MaterialInbound::getMaterialCode, param.getMaterialCode());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), MaterialInbound::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(MaterialInbound::getCreateTime);
		return query;
	}

	/***
	 * @Description 将整数转换成六位的字符串，默认补0
	 * @author huangyongtao
	 * @date 2025/9/23 16:35
	 * @param number
	 */
	private String formatToSixDigits(Integer number) {
		return String.format("%06d", number);
	}

	/***
	 * @Description 将补0的字符串，转换成整数
	 * @author huangyongtao
	 * @date 2025/9/23 16:36
	 * @param sixDigitString
	 */
	private Integer convertToInteger(String sixDigitString) {
		return Integer.parseInt(sixDigitString);
	}

	public static void main(String[] args) {
		String a = String.format("%06d", 10);
		System.out.println(a);
		System.out.println(Integer.parseInt(a));
	}

}
