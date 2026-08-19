
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.AwardRoman;
import com.cgnpc.bbxpark.settings.dto.model.AwardRomanModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardRomanParam;

import java.util.List;

/***
 * @Description 评优评奖流程服务接口
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
public interface IAwardRomanService extends IService<AwardRoman> {

	/**
	 * 根据评优评奖流程标识获得评优评奖流程详情信息.
	 *
	 * @Param [id] 评优评奖流程标识
	 * @Return 评优评奖流程详情信息
	 */
	AwardRomanModel detail(Long id);

	/**
	 * 获取评优评奖流程列表.
	 *
	 * @Param param 评优评奖流程查询条件
	 * @Return 评优评奖流程信息列表
	 */
	List<AwardRomanModel> list(Long awardId);

	/**
	 * 新增评优评奖流程.
	 *
	 * @Param param 评优评奖流程信息
	 * @Return 新增评优评奖流程是否成功
	 */
	Boolean add(AwardRomanParam param);

	/**
	 * 批量新增评优评奖流程.
	 *
	 * @Param params 评优评奖流程信息列表
	 * @Return 批量新增评优评奖流程是否成功
	 */
	Boolean addBatch(List<AwardRomanParam> params);

	/**
	 * 删除评优评奖流程.
	 *
	 * @Param awardId 评优评奖标识
	 * @Return 删除评优评奖流程是否成功
	 */
	Boolean removeByAwardId(Long awardId);
}
