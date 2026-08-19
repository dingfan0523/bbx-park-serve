
package com.cgnpc.bbxpark.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.supplier.domain.SupplierGroupPerson;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierGroupPersonModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierGroupPersonParam;

import java.util.List;

/***
 * @Description 服务商分组人员服务接口
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
public interface ISupplierGroupPersonService extends IService<SupplierGroupPerson> {

	/**
	 * 根据服务商分组人员标识获得服务商分组人员详情信息.
	 * @Param [id] 服务商分组人员标识
	 * @Return 服务商分组人员详情信息
	 */
	SupplierGroupPersonModel detail(Long id);

	/**
	 * 获取服务商分组人员列表(分页).
	 * @Param param 服务商分组人员查询条件
	 * @Return 服务商分组人员信息列表（分页）
	 */
	IPage<SupplierGroupPersonModel> page(SupplierGroupPersonPageParam param);

	/**
	 * 获取服务商分组人员列表.
	 * @Param param 服务商分组人员查询条件
	 * @Return 服务商分组人员信息列表
	 */
	List<SupplierGroupPersonModel> list(SupplierGroupPersonListParam param);

	/**
	 * 新增服务商分组人员.
	 * @Param param 服务商分组人员信息
	 * @Return 新增服务商分组人员是否成功
	 */
	Boolean add(SupplierGroupPersonParam param);

	/**
	 * 删除服务商分组人员.
	 * @Param id 服务商分组人员标识
	 * @Return 删除服务商分组人员是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 删除服务商人员.
	 * @Param supplierId 服务商人员标识
	 * @Return 删除服务商人员是否成功
	 */
	Boolean removeBySupplierId(Long supplierId);

	/**
	 * 编辑服务商分组人员信息.
	 * @Param param 服务商分组人员信息
	 * @Return 编辑服务商分组人员是否成功
	 */
	Boolean edit(SupplierGroupPersonParam param);

}