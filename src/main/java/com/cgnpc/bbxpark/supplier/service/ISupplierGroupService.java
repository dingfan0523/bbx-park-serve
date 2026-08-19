
package com.cgnpc.bbxpark.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.supplier.domain.SupplierGroup;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupParam;

import java.util.List;

/***
 * @Description 服务商分组服务接口
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
public interface ISupplierGroupService extends IService<SupplierGroup> {

	/**
	 * 根据服务商分组标识获得服务商分组详情信息.
	 * @Param [id] 服务商分组标识
	 * @Return 服务商分组详情信息
	 */
	SupplierGroupModel detail(Long id);

	/**
	 * 获取服务商分组列表(分页).
	 * @Param param 服务商分组查询条件
	 * @Return 服务商分组信息列表（分页）
	 */
	IPage<SupplierGroupModel> page(SupplierGroupPageParam param);

	/**
	 * 获取服务商分组列表.
	 * @Param param 服务商分组查询条件
	 * @Return 服务商分组信息列表
	 */
	List<SupplierGroupModel> list(SupplierGroupListParam param);

	/**
	 * 新增服务商分组.
	 * @Param param 服务商分组信息
	 * @Return 新增服务商分组是否成功
	 */
	Boolean add(SupplierGroupParam param);

	/**
	 * 删除服务商分组.
	 * @Param id 服务商分组标识
	 * @Return 删除服务商分组是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 删除服务商人员.
	 * @Param supplierId 服务商人员标识
	 * @Return 删除服务商人员是否成功
	 */
	Boolean removeBySupplierId(Long supplierId);

	/**
	 * 编辑服务商分组信息.
	 * @Param param 服务商分组信息
	 * @Return 编辑服务商分组是否成功
	 */
	Boolean edit(SupplierGroupParam param);

}