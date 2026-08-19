
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.AwardWinner;
import com.cgnpc.bbxpark.settings.dto.model.AwardWinnerModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerListParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerPageParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerParam;

import java.util.List;

/***
 * @Description 获奖人信息服务接口
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
public interface IAwardWinnerService extends IService<AwardWinner> {

	/**
	 * 根据获奖人信息标识获得获奖人信息详情信息.
	 * @Param [id] 获奖人信息标识
	 * @Return 获奖人信息详情信息
	 */
	AwardWinnerModel detail(Long id);

	/**
	 * 获取获奖人信息列表(分页).
	 * @Param param 获奖人信息查询条件
	 * @Return 获奖人信息信息列表（分页）
	 */
	IPage<AwardWinnerModel> page(AwardWinnerPageParam param);

	/**
	 * 获取获奖人信息列表.
	 * @Param param 获奖人信息查询条件
	 * @Return 获奖人信息信息列表
	 */
	List<AwardWinnerModel> list(AwardWinnerListParam param);

	/**
	 * 新增获奖人信息.
	 * @Param param 获奖人信息信息
	 * @Return 新增获奖人信息是否成功
	 */
	Boolean add(AwardWinnerParam param);

	/**
	 * 批量新增获奖人信息.
	 * @Param params 获奖人信息信息列表
	 * @Return 批量新增获奖人信息是否成功
	 */
	Boolean addBatch(List<AwardWinnerParam> params, Long awardId);

	/**
	 * 删除获奖人信息.
	 * @Param id 获奖人信息标识
	 * @Return 删除获奖人信息是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除获奖人信息.
	 * @Param ids 获奖人信息标识列表
	 * @Return 批量删除获奖人信息是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/**
	 * 删除获奖人信息
	 *
	 * @Param awardId 评优评奖标识
	 * @Return 删除评优评奖流程是否成功
	 */
	Boolean removeByAwardId(Long awardId);

	/**
	 * 编辑获奖人信息信息.
	 * @Param param 获奖人信息信息
	 * @Return 编辑获奖人信息是否成功
	 */
	Boolean edit(AwardWinnerParam param);
}
