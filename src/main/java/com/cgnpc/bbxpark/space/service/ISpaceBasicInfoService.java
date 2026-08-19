
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.space.domain.SpaceBasicInfo;
import com.cgnpc.bbxpark.space.dto.model.SimpleSpaceBasicInfoModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceBasicInfoModel;
import com.cgnpc.bbxpark.space.dto.model.SpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceBasicInfoParam;

import java.util.List;


public interface ISpaceBasicInfoService extends IService<SpaceBasicInfo> {

    /**
     * 获取空间基础信息树.
     * @Return 空间基础信息树
     */
    List<SpaceTreeModel> tree(SpaceBasicInfoListParam param);

    /**
     * 获取空间信息列表
     * @param param 参数
     * @return 空间信息列表
     */
    List<SimpleSpaceBasicInfoModel> list(SpaceBasicInfoListParam param);

	/**
	 * 根据空间基础信息标识获得空间基础信息详情信息.
	 * @Param [id] 空间基础信息标识
	 * @Return 空间基础信息详情信息
	 */
	SpaceBasicInfoModel detail(Long id);

	/**
	 * 编辑空间基础信息信息.
	 * @Param param 空间基础信息信息
	 * @Return 编辑空间基础信息是否成功
	 */
	Boolean edit(SpaceBasicInfoParam param);
}
