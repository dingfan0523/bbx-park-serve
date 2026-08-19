
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.space.domain.TenantDomain;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModel;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModelExt;
import com.cgnpc.bbxpark.space.dto.param.TenantInfoListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantInfoParam;
import com.cgnpc.bbxpark.space.dto.param.TenantPageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

public interface ITenantInfoService extends IBaseService<TenantInfo> {

	/**
	 * 根据租户信息标识获得租户信息详情信息.
	 * @Param [id] 租户信息标识
	 * @Return 租户信息详情信息
	 */
	TenantDomain detail(Long id);

	/**
	 * 获取租户信息列表(分页).
	 * @Param param 租户信息查询条件
	 * @Return 租户信息列表（分页）
	 */
	IPage<TenantInfoModelExt> pageFullInfo(TenantPageParam param);
	/**
	 * 获取租户信息列表(分页).
	 * @Param param 租户信息查询条件
	 * @Return 租户信息列表（分页）
	 */
    IPage<TenantInfoModel> page(TenantPageParam param);

	/**
	 * 获取租户信息列表.
	 * @Param param 租户信息查询条件
	 * @Return 租户信息列表
	 */
	List<TenantInfoModel> list(TenantInfoListParam param);

	/**
	 * 新增租户信息.
	 * @Param param 租户信息
	 * @Return 新增租户信息是否成功
	 */
	TenantInfoModel add(TenantInfoParam param);

	/**
	 * 删除租户信息.
	 * @Param id 租户信息标识
	 * @Return 删除租户信息是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑租户信息.
	 * @Param param 租户信息
	 * @Return 编辑租户信息是否成功
	 */
	Boolean edit(Long id, TenantInfoParam param);

	/**
	 * 启用租户信息.
	 * @Param id 租户信息标识
	 * @Return 启用租户信息是否成功
	 */
	Boolean enable(Long id);

	/**
	 * 禁用租户信息.
	 * @Param id 租户信息标识
	 * @Return 禁用租户信息是否成功
	 */
	Boolean disable(Long id);

//	Boolean assignOrgDept(TenantAssignOrgDeptParam param);
//
//	Boolean assignPerm(TenantAssignPermParam param);
//
//	Boolean assignMenu(TenantAssignMenuParam param);
}
