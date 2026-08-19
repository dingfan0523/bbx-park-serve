
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.settings.domain.RegionManage;
import com.cgnpc.bbxpark.settings.dto.model.RegionManageModel;
import com.cgnpc.bbxpark.settings.dto.param.RegionManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionManageParam;
import com.cgnpc.cud.core.service.IBaseService;

import java.util.List;

/***
 * @Description 区域管理员管理服务接口
 * @author huangyongtao
 * @date 2025/3/11 11:25
 */
public interface IRegionManageService extends IBaseService<RegionManage> {


	/**
	 * 获取区域管理员管理列表(分页).
	 * @Param param 区域管理员管理查询条件
	 * @Return 区域管理员管理信息列表（分页）
	 */
	IPage<RegionManageModel> page(RegionManagePageParam param);

	/**
	 * 获取区域管理员管理列表.
	 * @Param param 区域管理员管理查询条件
	 * @Return 区域管理员管理信息列表
	 */
	List<RegionManageModel> list(RegionManageListParam param);

	/**
	 * 新增区域管理员管理.
	 * @Param param 区域管理员管理信息
	 * @Return 新增区域管理员管理是否成功
	 */
	Boolean add(RegionManageParam param);

	/**
	 * 批量新增区域管理员管理.
	 * @Param params 区域管理员管理信息列表
	 * @Return 批量新增区域管理员管理是否成功
	 */
	Boolean addBatch(RegionManageParam params);

	/**
	 * 删除区域管理员管理.
	 * @Param id 区域管理员管理标识
	 * @Return 删除区域管理员管理是否成功
	 */
	Boolean remove(Long id);

}
