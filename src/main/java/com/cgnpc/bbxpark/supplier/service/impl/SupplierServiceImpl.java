
package com.cgnpc.bbxpark.supplier.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.settings.domain.AwardWinner;
import com.cgnpc.bbxpark.supplier.domain.Supplier;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupModel;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierModel;
import com.cgnpc.bbxpark.supplier.dto.param.*;
import com.cgnpc.bbxpark.supplier.mapper.SupplierRepository;
import com.cgnpc.bbxpark.supplier.service.ISupplierGroupService;
import com.cgnpc.bbxpark.supplier.service.ISupplierPersonService;
import com.cgnpc.bbxpark.supplier.service.ISupplierService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @Description 服务商服务实现
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierRepository, Supplier> implements ISupplierService {

	@Autowired
	private ISupplierGroupService supplierGroupService;

	@Autowired
	private ISupplierPersonService supplierPersonService;

	/**
	 * 根据服务商标识获得服务商详情信息.
	 * @Param id 服务商标识
	 * @Return 服务商详情信息
	 */
	@Override
	public SupplierModel detail(Long id) {
		Supplier supplier = this.getById(id);
		AssertUtils.notNull(supplier, SystemResultCode.RESULT_DATA_NONE.message());
        SupplierModel model = BeanUtils.convertTo(supplier, SupplierModel::new);
        handleSupplierPerson(model);
		return model;
	}

	/**
	 * 获取服务商列表(分页).
	 * @Param param 服务商查询条件
	 * @Return 服务商信息列表（分页）
	 */
	@Override
	public IPage<SupplierModel> page(SupplierPageParam param) {
		IPage<Supplier> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(BeanUtils.convertTo(param, SupplierListParam::new)));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(),  BeanUtils.convertListTo(result.getRecords(), SupplierModel::new));
	}

	/**
	 * 获取服务商列表.
	 * @Param param 服务商查询条件
	 * @Return 服务商信息列表
	 */
	@Override
	@SneakyThrows
	public List<SupplierModel> list(SupplierListParam param) {
		List<Supplier> suppliers = this.list(buildQuery(param));
		List<SupplierModel> supplierModels = BeanUtils.convertListTo(suppliers, SupplierModel::new);
		return supplierModels;
	}

	/**
	 * 获取服务商列表.
	 * @Param param 服务商查询条件
	 * @Return 服务商信息列表
	 */
	@Override
	@SneakyThrows
	public List<SupplierModel> findList(SupplierListParam param) {
		List<Supplier> suppliers = this.list(buildQuery(param));
		List<SupplierModel> supplierModels = BeanUtils.convertListTo(suppliers, SupplierModel::new);
		handleModels(supplierModels);
		return supplierModels;
	}

	/**
	 * 新增服务商.
	 * @Param param 服务商信息
	 * @Return 新增服务商是否成功
	 */
	@Override
	public Boolean add(SupplierParam param) {
		Supplier supplier = BeanUtils.convertTo(param, Supplier::new);
		supplier.setId(null);
        supplier.setPinyin(ObjectUtil.isEmpty(param.getName()) ?  null : PinyinUtil.getFirstLetter(param.getName(),""));
		return this.save(supplier);
	}

	/**
	 * 删除服务商.
	 * @Param id 服务商标识
	 * @Return 删除服务商是否成功
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean remove(Long id) {
		Supplier supplier = this.getById(id);
		AssertUtils.notNull(supplier, SystemResultCode.RESULT_DATA_NONE.message());
		this.removeById(id);
		supplierGroupService.removeBySupplierId(id);
		supplierPersonService.removeBySupplierId(id);
		return true;
	}

	/**
	 * 编辑服务商信息.
	 * @Param param 服务商信息
	 * @Return 编辑服务商是否成功
	 */
	@Override
	public Boolean edit(SupplierParam param) {
		Supplier supplier = this.getById(param.getId());
		AssertUtils.notNull(supplier, SystemResultCode.RESULT_DATA_NONE.message());
		Supplier su = BeanUtils.convertTo(param, Supplier::new);
        su.setPinyin(ObjectUtil.isEmpty(param.getName()) ?  null : PinyinUtil.getFirstLetter(param.getName(),""));
        this.updateById(su);
        if(CollectionUtil.isNotEmpty(param.getSupplierPersonParams())){
            supplierPersonService.removeBySupplierId(param.getId());
            supplierPersonService.addBatch(param.getSupplierPersonParams());
        }
        return true;
	}

	private void handleModels(List<SupplierModel> supplierModels) {
		if (CollectionUtil.isEmpty(supplierModels)) {
			return;
		}
		List<Long> ids = supplierModels.stream().map(SupplierModel::getId).collect(Collectors.toList());
		SupplierGroupListParam param = new SupplierGroupListParam();
		param.setSupplierIds(ids);
		List<SupplierGroupModel> groupModels = supplierGroupService.list(param);
		Map<Long, List<SupplierGroupModel>> groupMap = CollectionUtil.isEmpty(groupModels) ? new HashMap<>() : groupModels.stream().collect(Collectors.groupingBy(SupplierGroupModel::getSupplierId));
		supplierModels.forEach(f -> f.setSupplierGroupList(groupMap.get(f.getId())));
	}

    private void handleSupplierPerson(SupplierModel model){
        SupplierPersonListParam param = new SupplierPersonListParam();
        param.setSupplierId(model.getId());
        model.setSupplierPersonModels(supplierPersonService.list(param));
    }
	
	private LambdaQueryWrapper<Supplier> buildQuery(SupplierListParam param) {
		LambdaQueryWrapper<Supplier> query = new LambdaQueryWrapper<>();
		// 根据实际业务添加查询条件
		query.like(ObjectUtil.isNotEmpty(param.getName()), Supplier::getName, param.getName());
		query.eq(ObjectUtil.isNotEmpty(param.getType()), Supplier::getType, param.getType());
        // 租户
        query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), Supplier::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByAsc(Supplier::getPinyin);
		return query;
	}
}