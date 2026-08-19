
package com.cgnpc.bbxpark.complaint.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestion;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionListParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionPageParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionParam;

import java.util.List;

/***
 * @Description 投诉建议主表;服务接口
 * @author huangyongtao
 * @date 2024/7/12 14:17
 */
public interface IComplaintSuggestionService extends IService<ComplaintSuggestion> {

	/**
	 * 查询投诉建议详情（包含流转信息）
	 * @Param [id] 投诉建议主表;标识
	 * @Return 投诉建议主表;详情信息
	 */
	ComplaintSuggestionModel detail(Long id);

	/**
	 * 查询投诉建议详情
	 * @Param [id] 投诉建议主表;标识
	 * @Return 投诉建议主表;详情信息
	 */
	ComplaintSuggestionModel get(Long id);

	/**
	 * 获取投诉建议主表;列表(分页).
	 * @Param param 投诉建议主表;查询条件
	 * @Return 投诉建议主表;信息列表（分页）
	 */
	IPage<ComplaintSuggestionModel> page(ComplaintSuggestionPageParam param);

	/**
	 * 获取投诉建议主表;列表.
	 * @Param param 投诉建议主表;查询条件
	 * @Return 投诉建议主表;信息列表
	 */
	List<ComplaintSuggestionModel> list(ComplaintSuggestionListParam param);

	/**
	 * 新增投诉建议主表;.
	 * @Param param 投诉建议主表;信息
	 * @Return 新增投诉建议主表;是否成功
	 */
	Boolean add(ComplaintSuggestionParam param);


	/**
	 * 删除投诉建议主表;.
	 * @Param id 投诉建议主表;标识
	 * @Return 删除投诉建议主表;是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除投诉建议主表;.
	 * @Param ids 投诉建议主表;标识列表
	 * @Return 批量删除投诉建议主表;是否成功
	 */
	Boolean removeBatch(List<Long> ids);

	/***
	 * @Description 分配投诉建议
	 * @author huangyongtao
	 * @date 2024/7/16 9:35
	 * @param param
	 */
	Boolean assignment(ComplaintSuggestionParam param);

	/***
	 * @Description 审核投诉建议
	 * @author huangyongtao
	 * @date 2024/7/16 9:35
	 * @param param
	 */
	Boolean audit(ComplaintSuggestionParam param);

	/***
	 * @Description 回复投诉建议
	 * @author huangyongtao
	 * @date 2024/7/16 9:35
	 * @param param
	 */
	Boolean reply(ComplaintSuggestionParam param);

	/***
	 * @Description 评价投诉建议
	 * @author huangyongtao
	 * @date 2024/7/16 9:35
	 * @param param
	 */
	Boolean comment(ComplaintSuggestionParam param);


}
