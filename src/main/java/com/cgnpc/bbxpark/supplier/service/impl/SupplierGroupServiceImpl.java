
package com.cgnpc.bbxpark.supplier.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.supplier.domain.Supplier;
import com.cgnpc.bbxpark.supplier.domain.SupplierGroup;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupParam;
import com.cgnpc.bbxpark.supplier.mapper.SupplierGroupRepository;
import com.cgnpc.bbxpark.supplier.service.ISupplierGroupPersonService;
import com.cgnpc.bbxpark.supplier.service.ISupplierGroupService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 服务商分组服务实现
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Service("supplierGroupService")
public class SupplierGroupServiceImpl extends ServiceImpl<SupplierGroupRepository, SupplierGroup> implements ISupplierGroupService {

	@Autowired
	private ISupplierGroupPersonService supplierGroupPersonService;
	/**
	 * 根据服务商分组标识获得服务商分组详情信息.
	 * @Param id 服务商分组标识
	 * @Return 服务商分组详情信息
	 */
	@Override
	public SupplierGroupModel detail(Long id) {
		SupplierGroup supplierGroup = this.getById(id);
		AssertUtils.notNull(supplierGroup, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(supplierGroup, SupplierGroupModel::new);
	}

	/**
	 * 获取服务商分组列表(分页).
	 * @Param param 服务商分组查询条件
	 * @Return 服务商分组信息列表（分页）
	 */
	@Override
	public IPage<SupplierGroupModel> page(SupplierGroupPageParam param) {
		IPage<SupplierGroup> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, SupplierGroupListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(),  BeanUtils.convertListTo(result.getRecords(), SupplierGroupModel::new));
	}

	/**
	 * 获取服务商分组列表.
	 * @Param param 服务商分组查询条件
	 * @Return 服务商分组信息列表
	 */
	@Override
	@SneakyThrows
	public List<SupplierGroupModel> list(SupplierGroupListParam param) {
		List<SupplierGroup> supplierGroups = this.list(buildQuery(param));
		return BeanUtils.convertListTo(supplierGroups, SupplierGroupModel::new);
	}

	/**
	 * 新增服务商分组.
	 * @Param param 服务商分组信息
	 * @Return 新增服务商分组是否成功
	 */
	@Override
	public Boolean add(SupplierGroupParam param) {
		SupplierGroup supplierGroup = BeanUtils.convertTo(param, SupplierGroup::new);
		supplierGroup.setId(null);
		return this.save(supplierGroup);
	}

	/**
	 * 删除服务商分组.
	 * @Param id 服务商分组标识
	 * @Return 删除服务商分组是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean remove(Long id) {
		SupplierGroup supplierGroup = this.getById(id);
		AssertUtils.notNull(supplierGroup, SystemResultCode.RESULT_DATA_NONE.message());
		this.removeById(id);
		supplierGroupPersonService.removeBySupplierId(supplierGroup.getSupplierId());
		return true;
	}

	@Override
	public Boolean removeBySupplierId(Long supplierId) {
		this.remove(new LambdaQueryWrapper<SupplierGroup>().eq(SupplierGroup::getSupplierId, supplierId));
		supplierGroupPersonService.removeBySupplierId(supplierId);
		return true;
	}

	/**
	 * 编辑服务商分组信息.
	 * @Param param 服务商分组信息
	 * @Return 编辑服务商分组是否成功
	 */
	@Override
	public Boolean edit(SupplierGroupParam param) {
		SupplierGroup supplierGroup = this.getById(param.getId());
		AssertUtils.notNull(supplierGroup, SystemResultCode.RESULT_DATA_NONE.message());
		SupplierGroup su = BeanUtils.convertTo(param, SupplierGroup::new);
		return this.updateById(su);
	}
	
	private LambdaQueryWrapper<SupplierGroup> buildQuery(SupplierGroupListParam param) {
		LambdaQueryWrapper<SupplierGroup> query = new LambdaQueryWrapper<>();
		// 根据实际业务添加查询条件
		query.eq(ObjectUtil.isNotEmpty(param.getSupplierId()), SupplierGroup::getSupplierId, param.getSupplierId());
		query.like(ObjectUtil.isNotEmpty(param.getName()), SupplierGroup::getName, param.getName());
		query.in(CollectionUtil.isNotEmpty(param.getSupplierIds()), SupplierGroup::getSupplierId,param.getSupplierIds());
        // 租户
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), SupplierGroup::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByAsc(SupplierGroup::getCreateTime);
		return query;
	}
}
