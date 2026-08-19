
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.SpaceImage;
import com.cgnpc.bbxpark.space.dto.model.SpaceImageModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceImageListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceImageParam;

import java.util.List;


public interface ISpaceImageService extends IService<SpaceImage> {

	/**
	 * 根据空间图片标识获得空间图片详情信息.
	 * @Param [id] 空间图片标识
	 * @Return 空间图片详情信息
	 */
	SpaceImageModel detail(Long id);

	/**
	 * 获取空间图片列表.
	 * @Param param 空间图片查询条件
	 * @Return 空间图片信息列表
	 */
	List<SpaceImageModel> list(SpaceImageListParam param);

	/**
	 * 新增空间图片.
	 * @Param param 空间图片信息
	 * @Return 新增空间图片是否成功
	 */
	Boolean add(SpaceImageParam param);

	/**
	 * 编辑空间图片信息.
	 * @Param param 空间图片信息
	 * @Return 编辑空间图片是否成功
	 */
	Boolean edit(SpaceImageParam param);

	/**
	 * 删除空间图片.
	 * @Param id 空间图片标识
	 * @Return 删除空间图片是否成功
	 */
	Boolean remove(Long id);
}
