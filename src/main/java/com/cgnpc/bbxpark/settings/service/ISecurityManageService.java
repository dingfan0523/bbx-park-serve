
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.domain.SecurityManage;
import com.cgnpc.bbxpark.settings.dto.model.SecurityManageModel;
import com.cgnpc.bbxpark.settings.dto.param.SecurityManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.SecurityManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.SecurityManageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description 安全管理员服务接口
 * @author huangyongtao
 * @date 2025/8/1 11:32
 */
public interface ISecurityManageService extends IBaseService<SecurityManage> {

	/**
	 * 获取安全管理员列表(分页).
	 * @Param param 安全管理员查询条件
	 * @Return 安全管理员信息列表（分页）
	 */
    IPage<SecurityManageModel> page(SecurityManagePageParam param);

	/**
	 * 获取安全管理员列表.
	 * @Param param 安全管理员查询条件
	 * @Return 安全管理员信息列表
	 */
	List<SecurityManageModel> list(SecurityManageListParam param);

	/**
	 * 新增安全管理员.
	 * @Param param 安全管理员信息
	 * @Return 新增安全管理员是否成功
	 */
	Boolean add(SecurityManageParam param);

	/**
	 * 批量新增安全管理员.
	 * @Param params 安全管理员信息列表
	 * @Return 批量新增安全管理员是否成功
	 */
	Boolean addBatch(SecurityManageParam param);

	/**
	 * 删除安全管理员.
	 * @Param id 安全管理员标识
	 * @Return 删除安全管理员是否成功
	 */
	Boolean remove(Long id);

}
