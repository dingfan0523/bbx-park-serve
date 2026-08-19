package com.cgnpc.bbxpark.space.service;

//import com.cgnpc.bbxpark.framework.commons.pagination.PaginationResult;
//import com.cgnpc.bbxpark.framework.core.orm.service.BaseService;
//import com.cgnpc.bbxpark.service.sys.param.TenantMemberAddParam;
//import com.cgnpc.bbxpark.service.sys.param.TenantMemberPageParam;
//import com.cgnpc.bbxpark.service.sys.param.TenantMemberParam;
//import com.cgnpc.bbxpark.service.uic.domain.TenantMemberDomain;
//import com.cgnpc.bbxpark.service.uic.entity.TenantMember;
//import com.cgnpc.bbxpark.service.uic.param.TenantMemberListParam;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.space.domain.TenantMember;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberAddParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberPageParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

public interface ITenantMemberService extends IBaseService<TenantMember> {

	/**
	 * 根据租户成员标识获得租户成员详情信息.
	 * @Param [id] 租户成员标识
	 * @Return 租户成员详情信息
	 */
	TenantMemberDomain detail(Long id);

	/**
	 * 获取租户成员列表(分页).
	 * @Param param 租户成员查询条件
	 * @Return 租户成员信息列表（分页）
	 */
	IPage<TenantMemberDomain> page(TenantMemberPageParam param);

	IPage<TenantMemberDomain> listPage(TenantMemberListParam param);
	/**
	 * 获取租户成员列表.
	 *
	 * @Param param 租户成员查询条件
	 * @Return 租户成员信息列表
	 */
	List<TenantMemberDomain> list(TenantMemberListParam param);

	/**
	 * 新增租户成员.
	 * @Param param 租户成员信息
	 * @Return 新增租户成员是否成功
	 */
	Boolean add(TenantMemberParam param);

	/**
	 * 批量新增租户成员.
	 * @Param params 租户成员信息列表
	 * @Return 批量新增租户成员是否成功
	 */
	Boolean addBatch(TenantMemberAddParam param);

	/**
	 * 删除租户成员.
	 * @Param id 租户成员标识
	 * @Return 删除租户成员是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除租户成员.
	 * @Param ids 租户成员标识列表
	 * @Return 批量删除租户成员是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	Boolean remoteRoleUser(List<TenantMember> list);
	/**
	 * 编辑租户成员信息.
	 * @Param param 租户成员信息
	 * @Return 编辑租户成员是否成功
	 */
	Boolean edit(Long id, TenantMemberParam param);

	/**
	 * 批量启用租户成员信息.
	 * @Param ids 租户成员标识列表
	 * @Return 批量启用租户成员是否成功
	 */
	Boolean enableBatch(List<Long> ids);

	/**
	 * 批量禁用租户成员信息.
	 * @Param ids 租户成员标识列表
	 * @Return 批量禁用租户成员是否成功
	 */
	Boolean disableBatch(List<Long> ids);

	Boolean assignAdmin(TenantMemberParam param);

}
