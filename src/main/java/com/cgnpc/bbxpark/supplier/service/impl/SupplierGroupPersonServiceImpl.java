
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
import com.cgnpc.bbxpark.supplier.domain.SupplierGroup;
import com.cgnpc.bbxpark.supplier.domain.SupplierGroupPerson;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupPersonModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonParam;
import com.cgnpc.bbxpark.supplier.mapper.SupplierGroupPersonRepository;
import com.cgnpc.bbxpark.supplier.service.ISupplierGroupPersonService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

/***
 * @Description 服务商分组人员服务实现
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Service("supplierGroupPersonService")
public class SupplierGroupPersonServiceImpl extends ServiceImpl<SupplierGroupPersonRepository, SupplierGroupPerson> implements ISupplierGroupPersonService {

	/**
	 * 根据服务商分组人员标识获得服务商分组人员详情信息.
	 * @Param id 服务商分组人员标识
	 * @Return 服务商分组人员详情信息
	 */
	@Override
	public SupplierGroupPersonModel detail(Long id) {
		SupplierGroupPerson supplierGroupPerson = this.getById(id);
		AssertUtils.notNull(supplierGroupPerson, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(supplierGroupPerson, SupplierGroupPersonModel::new);
	}

	/**
	 * 获取服务商分组人员列表(分页).
	 * @Param param 服务商分组人员查询条件
	 * @Return 服务商分组人员信息列表（分页）
	 */
	@Override
	public IPage<SupplierGroupPersonModel> page(SupplierGroupPersonPageParam param) {
		IPage<SupplierGroupPerson> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, SupplierGroupPersonListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(),  BeanUtils.convertListTo(result.getRecords(), SupplierGroupPersonModel::new));
	}

	/**
	 * 获取服务商分组人员列表.
	 * @Param param 服务商分组人员查询条件
	 * @Return 服务商分组人员信息列表
	 */
	@Override
	@SneakyThrows
	public List<SupplierGroupPersonModel> list(SupplierGroupPersonListParam param) {
		List<SupplierGroupPerson> supplierGroupPersons = this.list(buildQuery(param));
		return BeanUtils.convertListTo(supplierGroupPersons, SupplierGroupPersonModel::new);
	}

	/**
	 * 新增服务商分组人员.
	 * @Param param 服务商分组人员信息
	 * @Return 新增服务商分组人员是否成功
	 */
	@Override
	public Boolean add(SupplierGroupPersonParam param) {
		SupplierGroupPerson supplierGroupPerson = BeanUtils.convertTo(param, SupplierGroupPerson::new);
		supplierGroupPerson.setId(null);
		return this.save(supplierGroupPerson);
	}

	/**
	 * 删除服务商分组人员.
	 * @Param id 服务商分组人员标识
	 * @Return 删除服务商分组人员是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		return this.removeById(id);
	}

	@Override
	public Boolean removeBySupplierId(Long supplierId) {
		return this.remove(new LambdaQueryWrapper<SupplierGroupPerson>().eq(SupplierGroupPerson::getSupplierId, supplierId));
	}

	/**
	 * 编辑服务商分组人员信息.
	 * @Param param 服务商分组人员信息
	 * @Return 编辑服务商分组人员是否成功
	 */
	@Override
	public Boolean edit(SupplierGroupPersonParam param) {
		SupplierGroupPerson supplierGroupPerson = this.getById(param.getId());
		AssertUtils.notNull(supplierGroupPerson, SystemResultCode.RESULT_DATA_NONE.message());
		SupplierGroupPerson su = BeanUtils.convertTo(param, SupplierGroupPerson::new);
		return this.updateById(su);
	}
	
	private LambdaQueryWrapper<SupplierGroupPerson> buildQuery(SupplierGroupPersonListParam param) {
		LambdaQueryWrapper<SupplierGroupPerson> query = new LambdaQueryWrapper<>();
		// 根据实际业务添加查询条件
		query.eq(ObjectUtil.isNotEmpty(param.getSupplierGroupId()), SupplierGroupPerson::getSupplierGroupId, param.getSupplierGroupId());
        query.eq(ObjectUtil.isNotEmpty(param.getSupplierId()), SupplierGroupPerson::getSupplierId, param.getSupplierId());
		query.like(ObjectUtil.isNotEmpty(param.getName()), SupplierGroupPerson::getName, param.getName());
		query.like(ObjectUtil.isNotEmpty(param.getPhone()), SupplierGroupPerson::getPhone, param.getPhone());
        // 租户
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), SupplierGroupPerson::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByAsc(SupplierGroupPerson::getCreateTime);
		return query;
	}
}
