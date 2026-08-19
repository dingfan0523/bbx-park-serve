package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.ConfigInfo;
import com.cgnpc.bbxpark.settings.dto.model.ConfigInfoModel;
import com.cgnpc.bbxpark.settings.dto.param.ConfigInfoParam;
import com.cgnpc.bbxpark.settings.dto.param.ConfigListParam;
import com.cgnpc.bbxpark.settings.dto.param.ConfigPageParam;

import java.util.List;


public interface IConfigInfoService extends IService<ConfigInfo> {

	/**
	 * 根据系统配置标识获得系统配置详情信息.
	 * @Param [id] 系统配置标识
	 * @Return 系统配置详情信息
	 */
	ConfigInfoModel detail(Long id);

	/**
	 * 获取系统配置列表(分页).
	 * @Param param 系统配置查询条件
	 * @Return 系统配置信息列表（分页）
	 */
	IPage<ConfigInfoModel> page(ConfigPageParam param);

	/**
	 * 获取系统配置列表.
	 * @Param param 系统配置查询条件
	 * @Return 系统配置信息列表
	 */
	List<ConfigInfoModel> list(ConfigListParam param);

	/**
	 * 批量新增系统配置.
	 * @Param params 系统配置信息列表
	 * @Return 批量新增系统配置是否成功
	 */
	Boolean addBatch(List<ConfigInfoParam> params);

	/**
	 * 删除系统配置.
	 * @Param id 系统配置标识
	 * @Return 删除系统配置是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除系统配置.
	 * @Param ids 系统配置标识列表
	 * @Return 批量删除系统配置是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/**
	 * 编辑系统配置信息.
	 * @Param param 系统配置信息
	 * @Return 编辑系统配置是否成功
	 */
	Boolean edit(ConfigInfoParam param);

	/**
	 * 批量启用系统配置信息.
	 * @Param ids 系统配置标识列表
	 * @Return 批量启用系统配置是否成功
	 */
	Boolean enableBatch(List<Long> ids);

	/**
	 * 批量禁用系统配置信息.
	 * @Param ids 系统配置标识列表
	 * @Return 批量禁用系统配置是否成功
	 */
	Boolean disableBatch(List<Long> ids);

	ConfigInfoModel getByCodeDetail(String code);
}
