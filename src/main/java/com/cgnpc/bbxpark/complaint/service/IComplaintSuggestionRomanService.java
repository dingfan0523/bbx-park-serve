
package com.cgnpc.bbxpark.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestionRoman;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionRomanModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionRomanListParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionRomanParam;

import java.util.List;

/***
 * @Description 投诉建议流转表;服务接口
 * @author huangyongtao
 * @date 2024/7/12 14:17
 */
public interface IComplaintSuggestionRomanService extends IService<ComplaintSuggestionRoman> {

	/**
	 * 根据投诉建议流转表;标识获得投诉建议流转表;详情信息.
	 * @Param [id] 投诉建议流转表;标识
	 * @Return 投诉建议流转表;详情信息
	 */
	ComplaintSuggestionRomanModel detail(Long id);

	/**
	 * 获取投诉建议流转表;列表.
	 * @Param param 投诉建议流转表;查询条件
	 * @Return 投诉建议流转表;信息列表
	 */
	List<ComplaintSuggestionRomanModel> list(ComplaintSuggestionRomanListParam param);

	/**
	 * 新增投诉建议流转表;.
	 * @Param param 投诉建议流转表;信息
	 * @Return 新增投诉建议流转表;是否成功
	 */
	Boolean add(ComplaintSuggestionRomanParam param);

	/**
	 * 批量新增投诉建议流转表;.
	 * @Param params 投诉建议流转表;信息列表
	 * @Return 批量新增投诉建议流转表;是否成功
	 */
	Boolean addBatch(List<ComplaintSuggestionRomanParam> params);

	/**
	 * 删除投诉建议流转表;.
	 * @Param id 投诉建议流转表;标识
	 * @Return 删除投诉建议流转表;是否成功
	 */
	Boolean remove(Long id);

	/**
	 * 批量删除投诉建议流转表;.
	 * @Param ids 投诉建议流转表;标识列表
	 * @Return 批量删除投诉建议流转表;是否成功
	 */
	Boolean removeBatch(List<Long> ids);


}
