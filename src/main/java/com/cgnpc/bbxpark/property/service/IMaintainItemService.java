package com.cgnpc.bbxpark.property.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.property.domain.MaintainItem;
import com.cgnpc.bbxpark.property.dto.model.MaintainItemModel;
import com.cgnpc.bbxpark.property.dto.param.MaintainItemParam;

import java.util.List;

/***
 * @Description 维保项目服务接口
 * @author huangyongtao
 * @date 2025/10/16 15:15
 */
public interface IMaintainItemService extends IService<MaintainItem> {

	/**
	 * 获取维保项目列表.
	 * @Param param 维保项目查询条件
	 * @Return 维保项目信息列表
	 */
	List<MaintainItemModel> list(Long maintainId);

	/**
	 * 新增维保项目.
	 * @Param param 维保项目信息
	 * @Return 新增维保项目是否成功
	 */
	Boolean add(MaintainItemParam param);

	/**
	 * 批量新增维保项目.
	 * @Param params 维保项目信息列表
	 * @Return 批量新增维保项目是否成功
	 */
	Boolean addBatch(List<MaintainItemParam> params);

	/**
	 * 删除维保项目.
	 * @Param maintainId 维保项目标识
	 * @Return 删除维保项目是否成功
	 */
	Boolean remove(Long maintainId);

}