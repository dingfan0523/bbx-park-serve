
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
import com.cgnpc.bbxpark.settings.domain.AwardRoman;
import com.cgnpc.bbxpark.settings.dto.param.AwardRomanParam;
import com.cgnpc.bbxpark.supplier.domain.SupplierGroupPerson;
import com.cgnpc.bbxpark.supplier.domain.SupplierPerson;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierPersonModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonParam;
import com.cgnpc.bbxpark.supplier.mapper.SupplierPersonRepository;
import com.cgnpc.bbxpark.supplier.service.ISupplierPersonService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/***
 * @Description 服务商人员服务实现
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Service("supplierPersonService")
public class SupplierPersonServiceImpl extends ServiceImpl<SupplierPersonRepository, SupplierPerson> implements ISupplierPersonService {

	/**
	 * 根据服务商人员标识获得服务商人员详情信息.
	 * @Param id 服务商人员标识
	 * @Return 服务商人员详情信息
	 */
	@Override
	public SupplierPersonModel detail(Long id) {
		SupplierPerson supplierPerson = this.getById(id);
		AssertUtils.notNull(supplierPerson, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(supplierPerson, SupplierPersonModel::new);
	}

	/**
	 * 获取服务商人员列表(分页).
	 * @Param param 服务商人员查询条件
	 * @Return 服务商人员信息列表（分页）
	 */
	@Override
	public IPage<SupplierPersonModel> page(SupplierPersonPageParam param) {
		IPage<SupplierPerson> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, SupplierPersonListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(),  BeanUtils.convertListTo(result.getRecords(), SupplierPersonModel::new));
	}

	/**
	 * 获取服务商人员列表.
	 * @Param param 服务商人员查询条件
	 * @Return 服务商人员信息列表
	 */
	@Override
	@SneakyThrows
	public List<SupplierPersonModel> list(SupplierPersonListParam param) {
		List<SupplierPerson> supplierPersons = this.list(buildQuery(param));
		return BeanUtils.convertListTo(supplierPersons, SupplierPersonModel::new);
	}

	/**
	 * 新增服务商人员.
	 * @Param param 服务商人员信息
	 * @Return 新增服务商人员是否成功
	 */
	@Override
	public Boolean add(SupplierPersonParam param) {
		SupplierPerson supplierPerson = BeanUtils.convertTo(param, SupplierPerson::new);
		supplierPerson.setId(null);
		return this.save(supplierPerson);
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBatch(List<SupplierPersonParam> params) {
        List<SupplierPerson> supplierPersons = BeanUtils.convertListTo(params, SupplierPerson::new);
        return this.saveBatch(supplierPersons);
    }

	/**
	 * 删除服务商人员.
	 * @Param id 服务商人员标识
	 * @Return 删除服务商人员是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		return this.removeById(id);
	}

	@Override
	public Boolean removeBySupplierId(Long supplierId) {
		return this.remove(new LambdaQueryWrapper<SupplierPerson>().eq(SupplierPerson::getSupplierId, supplierId));
	}

	/**
	 * 编辑服务商人员信息.
	 * @Param param 服务商人员信息
	 * @Return 编辑服务商人员是否成功
	 */
	@Override
	public Boolean edit(SupplierPersonParam param) {
		SupplierPerson supplierPerson = this.getById(param.getId());
		AssertUtils.notNull(supplierPerson, SystemResultCode.RESULT_DATA_NONE.message());
		SupplierPerson su = BeanUtils.convertTo(param, SupplierPerson::new);
		return this.updateById(su);
	}
	
	private LambdaQueryWrapper<SupplierPerson> buildQuery(SupplierPersonListParam param) {
		LambdaQueryWrapper<SupplierPerson> query = new LambdaQueryWrapper<>();
		// 根据实际业务添加查询条件
		query.eq(ObjectUtil.isNotEmpty(param.getSupplierId()), SupplierPerson::getSupplierId, param.getSupplierId());
		query.like(ObjectUtil.isNotEmpty(param.getName()),SupplierPerson::getName, param.getName());
		query.like(ObjectUtil.isNotEmpty(param.getPhone()),SupplierPerson::getPhone, param.getPhone());
        // 租户
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), SupplierPerson::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByAsc(SupplierPerson::getCreateTime);
		return query;
	}
}
