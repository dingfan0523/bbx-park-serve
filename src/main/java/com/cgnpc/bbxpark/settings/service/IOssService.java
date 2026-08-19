package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.domain.Oss;
import com.cgnpc.bbxpark.settings.dto.model.OssModel;
import com.cgnpc.bbxpark.settings.dto.param.OssListParam;
import com.cgnpc.bbxpark.settings.dto.param.OssPageParam;
import com.cgnpc.bbxpark.settings.dto.param.OssParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/**
 * OSS对象存储服务接口
 */
public interface IOssService extends IBaseService<Oss> {

	/**
	 * 根据OSS对象存储标识获得OSS对象存储详情信息.
	 * @Param [ossId] OSS对象存储标识
	 * @Return OSS对象存储详情信息
	 */
	OssModel detail(Long ossId);

	/**
	 * 获取OSS对象存储列表(分页).
	 * @Param param OSS对象存储查询条件
	 * @Return OSS对象存储信息列表（分页）
	 */
	IPage<OssModel> page(OssPageParam param);

	/**
	 * 获取OSS对象存储列表.
	 * @Param param OSS对象存储查询条件
	 * @Return OSS对象存储信息列表
	 */
	List<OssModel> list(OssListParam param);

	/**
	 * 新增OSS对象存储.
	 * @Param param OSS对象存储信息
	 * @Return 新增OSS对象存储是否成功
	 */
	Boolean add(OssParam param);

	/**
	 * 批量新增OSS对象存储.
	 * @Param params OSS对象存储信息列表
	 * @Return 批量新增OSS对象存储是否成功
	 */
	Boolean addBatch(List<OssParam> params);

	/**
	 * 删除OSS对象存储.
	 * @Param ossId OSS对象存储标识
	 * @Return 删除OSS对象存储是否成功
	 */
	Boolean remove(Long ossId);

	/**
	 * 批量删除OSS对象存储.
	 * @Param ossIds OSS对象存储标识列表
	 * @Return 批量删除OSS对象存储是否成功
	 */
	Boolean removeBatch(List<Long> ossIds);

	/**
	 * 编辑OSS对象存储信息.
	 * @Param param OSS对象存储信息
	 * @Return 编辑OSS对象存储是否成功
	 */
	Boolean edit(Long ossId, OssParam param);

	/**
	 * 批量编辑OSS对象存储信息.
	 * @Param params OSS对象存储信息列表
	 * @Return 批量编辑OSS对象存储是否成功
	 */
	Boolean editBatch(List<OssParam> params);

	/**
	 * 启用OSS对象存储.
	 * @Param ossId OSS对象存储标识
	 * @Return 启用OSS对象存储是否成功
	 */
	Boolean enable(Long ossId);

	/**
	 * 批量启用OSS对象存储信息.
	 * @Param ossIds OSS对象存储标识列表
	 * @Return 批量启用OSS对象存储是否成功
	 */
	Boolean enableBatch(List<Long> ossIds);

	/**
	 * 禁用OSS对象存储.
	 * @Param ossId OSS对象存储标识
	 * @Return 禁用OSS对象存储是否成功
	 */
	Boolean disable(Long ossId);

	/**
	 * 批量禁用OSS对象存储信息.
	 * @Param ossIds OSS对象存储标识列表
	 * @Return 批量禁用OSS对象存储是否成功
	 */
	Boolean disableBatch(List<Long> ossIds);
}
