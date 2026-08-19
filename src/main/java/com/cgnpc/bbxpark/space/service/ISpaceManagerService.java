
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.SpaceManager;
import com.cgnpc.bbxpark.space.dto.model.SpaceManagerModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerPageParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerParam;

import java.util.List;


public interface ISpaceManagerService extends IService<SpaceManager> {

	/**
	 * 根据空间责任人标识获得空间责任人详情信息.
	 * @Param [id] 空间责任人标识
	 * @Return 空间责任人详情信息
	 */
	SpaceManagerModel detail(Long id);

	/**
	 * 获取空间责任人列表(分页).
	 * @Param param 空间责任人查询条件
	 * @Return 空间责任人信息列表（分页）
	 */
	IPage<SpaceManagerModel> page(SpaceManagerPageParam param);

	/**
	 * 获取空间责任人列表.
	 * @Param param 空间责任人查询条件
	 * @Return 空间责任人信息列表
	 */
	List<SpaceManagerModel> list(SpaceManagerListParam param);

	/**
	 * 新增空间责任人.
	 * @Param param 空间责任人信息
	 * @Return 新增空间责任人是否成功
	 */
	Boolean add(SpaceManagerParam param);

	/**
	 * 编辑空间责任人信息.
	 * @Param param 空间责任人信息
	 * @Return 编辑空间责任人是否成功
	 */
	Boolean edit(SpaceManagerParam param);

	/**
	 * 删除空间责任人.
	 * @Param id 空间责任人标识
	 * @Return 删除空间责任人是否成功
	 */
	Boolean remove(Long id);
}
