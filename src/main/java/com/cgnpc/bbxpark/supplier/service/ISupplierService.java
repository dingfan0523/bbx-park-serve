
package com.cgnpc.bbxpark.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.supplier.domain.Supplier;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierParam;

import java.util.List;

/***
 * @Description 服务商服务接口
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
public interface ISupplierService extends IService<Supplier> {

	/**
	 * 根据服务商标识获得服务商详情信息.
	 * @Param [id] 服务商标识
	 * @Return 服务商详情信息
	 */
	SupplierModel detail(Long id);

	/**
	 * 获取服务商列表(分页).
	 * @Param param 服务商查询条件
	 * @Return 服务商信息列表（分页）
	 */
	IPage<SupplierModel> page(SupplierPageParam param);

	/**
	 * 获取服务商列表.
	 * @Param param 服务商查询条件
	 * @Return 服务商信息列表
	 */
	List<SupplierModel> list(SupplierListParam param);

	/**
	 * 获取服务商列表.
	 * @Param param 服务商查询条件
	 * @Return 服务商信息列表
	 */
	List<SupplierModel> findList(SupplierListParam param);

	/**
	 * 新增服务商.
	 * @Param param 服务商信息
	 * @Return 新增服务商是否成功
	 */
	Boolean add(SupplierParam param);

	/**
	 * 删除服务商.
	 * @Param id 服务商标识
	 * @Return 删除服务商是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑服务商信息.
	 * @Param param 服务商信息
	 * @Return 编辑服务商是否成功
	 */
	Boolean edit(SupplierParam param);

}