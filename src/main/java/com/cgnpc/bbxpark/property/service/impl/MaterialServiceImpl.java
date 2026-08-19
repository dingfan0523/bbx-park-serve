package com.cgnpc.bbxpark.property.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.enums.MaterialStatusEnum;
import com.cgnpc.bbxpark.common.enums.MaterialTypeEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.property.domain.Material;
import com.cgnpc.bbxpark.property.dto.model.MaterialExportModel;
import com.cgnpc.bbxpark.property.dto.model.MaterialModel;
import com.cgnpc.bbxpark.property.dto.param.MaterialListParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialPageParam;
import com.cgnpc.bbxpark.property.dto.param.MaterialParam;
import com.cgnpc.bbxpark.property.mapper.MaterialRepository;
import com.cgnpc.bbxpark.property.service.IMaterialService;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 材料服务实现
 * @author huangyongtao
 * @date 2025/9/22 17:30
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialRepository, Material> implements IMaterialService {

	@Autowired
	private ITenantInfoService tenantInfoService;

	@Autowired
	private IUserApiService userApiService;
    @Autowired
    private IParkSpaceService parkSpaceService;

	/**
	 * 根据材料标识获得材料详情信息.
	 * @Param [id] 材料标识
	 * @Return 材料详情信息
	 */
	@Override
	public MaterialModel detail(Long id) {
		Material material = this.getById(id);
		AssertUtils.notNull(material, SystemResultCode.RESULT_DATA_NONE.message());
        MaterialModel model = BeanUtils.convertTo(material, MaterialModel::new);
        if(model.getSpaceId() != null){
            Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(Collections.singletonList(model.getSpaceId()),WebFrameworkUtils.getHeaderTenantId());
            model.setSpaceName(spaceMap.getOrDefault(model.getSpaceId(),new ParkSpaceFullModel()).getFullPath());
        }
        return model;
	}

	/**
	 * 获取材料列表(分页).
	 * @Param param 材料查询条件
	 * @Return 材料信息列表（分页）
	 */
	@Override
	public IPage<MaterialModel> page(MaterialPageParam param) {
		IPage<Material> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<MaterialModel> models = BeanUtils.convertListTo(result.getRecords(), MaterialModel::new);
        List<Long> spaceIds = models.stream().map(MaterialModel::getSpaceId).filter(Objects::nonNull).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(spaceIds)){
            Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds,WebFrameworkUtils.getHeaderTenantId());
            models.stream().filter(m->m.getSpaceId() != null && spaceMap.containsKey(m.getSpaceId()))
                    .forEach(m->m.setSpaceName(spaceMap.get(m.getSpaceId()).getFullPath()));
        }

		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}

	/**
	 * 获取材料列表.
	 * @Param param 材料查询条件
	 * @Return 材料信息列表
	 */
	@Override
	@SneakyThrows
	public List<MaterialModel> list(MaterialListParam param) {
		List<Material> materials = this.list(buildQuery(BeanUtils.convertTo(param, MaterialPageParam::new)));
        List<MaterialModel> list = BeanUtils.convertListTo(materials, MaterialModel::new);
        List<Long> spaceIds = list.stream().map(MaterialModel::getSpaceId).filter(Objects::nonNull).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(spaceIds)){
            Map<Long, ParkSpaceFullModel> spaceMap = parkSpaceService.findFullSpaceMap(spaceIds,WebFrameworkUtils.getHeaderTenantId());
            list.stream().filter(m->m.getSpaceId() != null && spaceMap.containsKey(m.getSpaceId()))
                    .forEach(m->m.setSpaceName(spaceMap.get(m.getSpaceId()).getFullPath()));
        }
        return list;
	}

	/**
	 * 新增材料.
	 * @Param param 材料信息
	 * @Return 新增材料是否成功
	 */
	@Override
	public synchronized Boolean add(MaterialParam param) {
		//校验属性的非空
		checkPublicProperty(param);
		//校验编码的唯一性
		checkMaterialCodeUnique(param.getMaterialCode(), null);
		Material material = BeanUtils.convertTo(param, Material::new);
		material.setId(null);
        material.setCreateBy(userApiService.getCurrentStaffName());
        material.setOriginalStock(material.getStockQuantity());
		// 根据库存数量和预警值设置库存状态
		handelStockStatus(material);
		return this.save(material);
	}

	/**
	 * 删除材料.
	 * @Param id 材料标识
	 * @Return 删除材料是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		Material material = this.getById(id);
		AssertUtils.notNull(material, SystemResultCode.RESULT_DATA_NONE.message());
		Material ma = new Material();
		ma.setId(id);
		ma.setDeleted(Delete.DELETED.getKey());
		return this.updateById(ma);
	}

	/**
	 * 编辑材料信息.
	 * @Param param 材料信息
	 * @Return 编辑材料是否成功
	 */
	@Override
	public Boolean edit(MaterialParam param) {
		Material material = this.getById(param.getId());
		AssertUtils.notNull(material, SystemResultCode.RESULT_DATA_NONE.message());
		//校验属性的非空
		checkPublicProperty(param);
		//校验编码的唯一性
		checkMaterialCodeUnique(param.getMaterialCode(), param.getId());
		Material editParam = BeanUtils.convertTo(param, Material::new);
		// 根据库存数量和预警值设置库存状态
		handelStockStatus(editParam);
		return this.updateById(editParam);
	}

	@Override
	public Boolean materialEasyExport(HttpServletResponse response, MaterialPageParam param) {
		//生成excel
//		TenantInfo tenantInfoModel = tenantInfoService.getById(WebFrameworkUtils.getHeaderTenantId());
		param.setSize(-1);
		param.setCurrent(1);
		String title = "器材耗材统计表";
		IPage<MaterialModel> pageResult =  this.page(param);
		List<MaterialExportModel> exportModels = new ArrayList<>();
		if(CollectionUtil.isNotEmpty(pageResult.getRecords())){
			pageResult.getRecords().forEach(model->{
				MaterialExportModel exportModel = new MaterialExportModel();
				BeanUtils.copyProperties(model, exportModel);
				exportModel.setMaterialTypeStr(MaterialTypeEnum.getName(model.getMaterialType()));
				exportModel.setStockStatusStr(MaterialStatusEnum.getName(model.getStockStatus()));
				exportModel.setCreatorUname(model.getCreateBy());
				exportModel.setCreateTimeStr(ObjectUtil.isEmpty(model.getCreateTime()) ? "" : DateUtils.format(model.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				exportModels.add(exportModel);
			});
		}
		ExcelExportUtils.exportExcel(response, title,exportModels, MaterialExportModel.class, title);
		return true;
	}

	private LambdaQueryWrapper buildQuery(MaterialPageParam param) {
		LambdaQueryWrapper<Material> query = new LambdaQueryWrapper<>();
		// 根据材料名称筛选
		query.like(ObjectUtil.isNotEmpty(param.getMaterialName()), Material::getMaterialName, param.getMaterialName());
		//根据材料编码筛选
		query.like(ObjectUtil.isNotEmpty(param.getMaterialCode()), Material::getMaterialCode, param.getMaterialCode());
		//根据材料类型筛选
		query.eq(ObjectUtil.isNotEmpty(param.getMaterialType()), Material::getMaterialType, param.getMaterialType());
		//根据库存状态筛选
		query.eq(ObjectUtil.isNotEmpty(param.getStockStatus()), Material::getStockStatus, param.getStockStatus());
		//排除已选择的材料id筛选
		query.notIn(CollectionUtil.isNotEmpty(param.getNoIds()), Material::getId, param.getNoIds());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), Material::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		// 查询未删除的设备
		query.eq(Material::getDeleted, Status.enabled.getKey());
		query.orderByDesc(Material::getCreateTime);
		return query;
	}

	private void checkPublicProperty(MaterialParam param) {
		AssertUtils.notNull(param.getMaterialName(), "材料名称不能为空");
		AssertUtils.notNull(param.getMaterialCode(), "材料编码不能为空");
		AssertUtils.notNull(param.getMaterialType(), "材料类型不能为空");
		AssertUtils.notNull(param.getStockQuantity(), "材料初始库存不能为空");
		AssertUtils.notNull(param.getStockWarning(), "材料库存预警值不能为空");
	}
	private void checkMaterialCodeUnique(String materialCode, Long id) {
		boolean flag = this.count(Wrappers.<Material>lambdaQuery().eq(Material::getMaterialCode, materialCode)
				      .eq(Material::getDeleted,Delete.NORMAL.getKey())
				      .ne(ObjectUtil.isNotEmpty(id), Material::getId, id)
				      .eq(Material::getTenantId, WebFrameworkUtils.getHeaderTenantId())) > 0;
		AssertUtils.isFalse(flag,  "材料编码已存在");
	}
	public void handelStockStatus(Material material) {
		if (material.getStockQuantity() <= 0) {
			material.setStockStatus(MaterialStatusEnum.NO.getCode());
		}else if (material.getStockQuantity() >= material.getStockWarning()) {
			material.setStockStatus(MaterialStatusEnum.ENOUGH.getCode());
		} else if (material.getStockQuantity() > 0 && material.getStockQuantity() < material.getStockWarning()) {
			material.setStockStatus(MaterialStatusEnum.SHORTAGE.getCode());
		}
	}
}
