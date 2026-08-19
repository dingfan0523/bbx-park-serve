
package com.cgnpc.bbxpark.settings.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.settings.domain.Award;
import com.cgnpc.bbxpark.settings.dto.model.AwardModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardHandleParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardListParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardPageParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardParam;

import java.util.List;

/***
 * @Description 评优评奖服务接口
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
public interface IAwardService extends IService<Award> {

	/**
	 * 根据评优评奖标识获得评优评奖详情信息.
	 * @Param [id] 评优评奖标识
	 * @Return 评优评奖详情信息
	 */
	AwardModel detail(Long id);

	/**
	 * 获取评优评奖列表(分页).
	 * @Param param 评优评奖查询条件
	 * @Return 评优评奖信息列表（分页）
	 */
	IPage<AwardModel> page(AwardPageParam param);

	/**
	 * 获取评优评奖列表.
	 * @Param param 评优评奖查询条件
	 * @Return 评优评奖信息列表
	 */
	List<AwardModel> list(AwardListParam param);

	/**
	 * 获取评优评奖展示中列表.
	 * @Param param 评优评奖查询条件
	 * @Return 评优评奖信息列表
	 */
	List<AwardModel> findDisplay(AwardListParam param);

	/**
	 * 新增评优评奖.
	 * @Param param 评优评奖信息
	 * @Return 新增评优评奖是否成功
	 */
	Long add(AwardParam param);


	/**
	 * 删除评优评奖.
	 * @Param id 评优评奖标识
	 * @Return 删除评优评奖是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 编辑评优评奖信息.
	 * @Param param 评优评奖信息
	 * @Return 编辑评优评奖是否成功
	 */
	Boolean edit(AwardParam param);

	/**
	 * 撤回评优评奖.
	 * @Param param 评优评奖标识
	 * @Return 启用评优评奖是否成功
	 */
	Boolean recall(AwardHandleParam param);


	/**
	 * 提交评优评奖.
	 * @Param param 评优评奖标识
	 * @Return 禁用评优评奖是否成功
	 */
	Boolean submit(AwardHandleParam param);

	/**
	 * 审批评优评奖.
	 * @Param param 评优评奖标识
	 * @Return 禁用评优评奖是否成功
	 */
	Boolean audit(AwardHandleParam param);

	/**
	 * 取消展示评优评奖.
	 * @Param param 评优评奖标识
	 * @Return 禁用评优评奖是否成功
	 */
	Boolean cancel(AwardHandleParam param);

	/**
	 * 执行评优评奖展示时间.
	 */
	void executeAwardDisplayTime();
}
