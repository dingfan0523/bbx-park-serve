
package com.cgnpc.bbxpark.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.supplier.domain.SupplierPerson;
import com.cgnpc.bbxpark.supplier.dto.model.SupplierPersonModel;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonListParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonPageParam;
import com.cgnpc.bbxpark.supplier.dto.param.SupplierPersonParam;

import java.util.List;

/***
 * @Description 服务商人员服务接口
 * @author huangyongtao
 * @date 2025/11/14 11:00
 */
public interface ISupplierPersonService extends IService<SupplierPerson> {

	/**
	 * 根据服务商人员标识获得服务商人员详情信息.
	 * @Param [id] 服务商人员标识
	 * @Return 服务商人员详情信息
	 */
	SupplierPersonModel detail(Long id);

	/**
	 * 获取服务商人员列表(分页).
	 * @Param param 服务商人员查询条件
	 * @Return 服务商人员信息列表（分页）
	 */
	IPage<SupplierPersonModel> page(SupplierPersonPageParam param);

	/**
	 * 获取服务商人员列表.
	 * @Param param 服务商人员查询条件
	 * @Return 服务商人员信息列表
	 */
	List<SupplierPersonModel> list(SupplierPersonListParam param);

	/**
	 * 新增服务商人员.
	 * @Param param 服务商人员信息
	 * @Return 新增服务商人员是否成功
	 */
	Boolean add(SupplierPersonParam param);

    /**
     * 批量新增服务商人员.
     * @Param param 服务商人员信息
     * @Return 新增服务商人员是否成功
     */
    Boolean addBatch(List<SupplierPersonParam> params);

    /**
	 * 删除服务商人员.
	 * @Param id 服务商人员标识
	 * @Return 删除服务商人员是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 删除服务商人员.
	 * @Param supplierId 服务商人员标识
	 * @Return 删除服务商人员是否成功
	 */
	Boolean removeBySupplierId(Long supplierId);

	/**
	 * 编辑服务商人员信息.
	 * @Param param 服务商人员信息
	 * @Return 编辑服务商人员是否成功
	 */
	Boolean edit(SupplierPersonParam param);
}