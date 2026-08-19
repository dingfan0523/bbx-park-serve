
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.AttentionManage;
import com.cgnpc.bbxpark.settings.dto.model.AttentionManageModel;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManageParam;

import java.util.List;

/***
 * @Description 关注人管理服务接口
 * @author huangyongtao
 * @date 2025/3/11 10:28
 */
public interface IAttentionManageService extends IService<AttentionManage> {

	/**
	 * 获取关注人管理列表(分页).
	 * @Param param 关注人管理查询条件
	 * @Return 关注人管理信息列表（分页）
	 */
	IPage<AttentionManageModel> page(AttentionManagePageParam param);

	/**
	 * 获取关注人管理列表.
	 * @Param param 关注人管理查询条件
	 * @Return 关注人管理信息列表
	 */
	List<AttentionManageModel> list(AttentionManageListParam param);

	/**
	 * 新增关注人管理.
	 * @Param param 关注人管理信息
	 * @Return 新增关注人管理是否成功
	 */
	Boolean add(AttentionManageParam param);

	/**
	 * 批量新增关注人管理.
	 * @Param params 关注人管理信息列表
	 * @Return 批量新增关注人管理是否成功
	 */
	Boolean addBatch(List<AttentionManageParam> params);

	/**
	 * 删除关注人管理.
	 * @Param id 关注人管理标识
	 * @Return 删除关注人管理是否成功
	 */
	Boolean remove(Long id);

	/***
	 * @Description 是否包含关注人
	 * @author huangyongtao
	 * @date 2025/3/31 15:59
	 * @param userId
	 */
	Boolean checkAttention(String userId);

}
