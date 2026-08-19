
package com.cgnpc.bbxpark.complaint.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.ComplaintSuggestionRomanTypeEnum;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestion;
import com.cgnpc.bbxpark.complaint.domain.ComplaintSuggestionRoman;
import com.cgnpc.bbxpark.complaint.dto.model.ComplaintSuggestionRomanModel;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionRomanListParam;
import com.cgnpc.bbxpark.complaint.dto.param.ComplaintSuggestionRomanParam;
import com.cgnpc.bbxpark.complaint.mapper.ComplaintSuggestionRomanRepository;
import com.cgnpc.bbxpark.complaint.service.IComplaintSuggestionRomanService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @Description 投诉建议流转表;服务实现
 * @author huangyongtao
 * @date 2024/7/12 14:27
 */
@Service("complaintSuggestionRomanService")
public class ComplaintSuggestionRomanServiceImpl extends ServiceImpl<ComplaintSuggestionRomanRepository, ComplaintSuggestionRoman> implements IComplaintSuggestionRomanService {

	/**
	 * 根据投诉建议流转表;标识获得投诉建议流转表;详情信息.
	 * @Param [id] 投诉建议流转表;标识
	 * @Return 投诉建议流转表;详情信息
	 */
	@Override
	public ComplaintSuggestionRomanModel detail(Long id) {
		ComplaintSuggestionRoman complaintSuggestionRoman = this.getById(id);
		AssertUtils.notNull(complaintSuggestionRoman, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(complaintSuggestionRoman, ComplaintSuggestionRomanModel::new);
	}


	/**
	 * 获取投诉建议流转表;列表.
	 * @Param param 投诉建议流转表;查询条件
	 * @Return 投诉建议流转表;信息列表
	 */
	@Override
	@SneakyThrows
	public List<ComplaintSuggestionRomanModel> list(ComplaintSuggestionRomanListParam param) {
		param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
		LambdaQueryWrapper<ComplaintSuggestionRoman> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(ObjectUtil.isNotEmpty(param.getComplaintSuggestionId()), ComplaintSuggestionRoman::getComplaintSuggestionId, param.getComplaintSuggestionId())
				.eq(ObjectUtil.isNotEmpty(param.getTenantId()), ComplaintSuggestionRoman::getTenantId, param.getTenantId())
				.orderByAsc(ComplaintSuggestionRoman::getCreateTime);
		List<ComplaintSuggestionRoman> complaintSuggestionRomans = this.list(queryWrapper);
		if(CollectionUtil.isEmpty(complaintSuggestionRomans)){
			return new ArrayList<>();
		}
		return BeanUtils.convertListTo(complaintSuggestionRomans, ComplaintSuggestionRomanModel::new);
	}

	public List<ComplaintSuggestionRomanModel> handleRomans(Long complaintSuggestionId) {
		ComplaintSuggestionRomanListParam param = new ComplaintSuggestionRomanListParam();
		param.setComplaintSuggestionId(complaintSuggestionId);
		List<ComplaintSuggestionRomanModel> romans = this.list(param);
		if(CollectionUtil.isEmpty(romans)){
			return new ArrayList<>();
		}
		Map<Long, List<ComplaintSuggestionRomanModel>> romanMap = romans.stream().collect(Collectors.groupingBy(ComplaintSuggestionRomanModel::getPid));
		List<ComplaintSuggestionRomanModel> romanModels = new ArrayList<>();
//		for(int i = 0; i < romans.size(); i++){
//			if(ComplaintSuggestionRomanTypeEnum.REPLY.getCode().equals(romans.get(i).getRomanType())){
//				romanModels.add(romans.get(i));
//			}else{
//				romanModels.get(romanModels.size()-1).setAuditUname(romans.get(i).getRomanUname());
//				romanModels.get(romanModels.size()-1).setAuditRemark(romans.get(i).getRomanRemark());
//				romanModels.get(romanModels.size()-1).setAuditUid(romans.get(i).getRomanUid());
//				romanModels.get(romanModels.size()-1).setAuditStaffid(romans.get(i).getRomanStaffid());
//				romanModels.get(romanModels.size()-1).setAuditTime(romans.get(i).getRomanTime());
//			}
//		}
		romans.stream().forEach(roman ->{
			if(CollectionUtil.isNotEmpty(romanMap.get(roman.getId()))){
				roman.setAuditUname(romanMap.get(roman.getId()).get(0).getRomanUname());
				roman.setAuditRemark(romanMap.get(roman.getId()).get(0).getRomanRemark());
				roman.setAuditUid(romanMap.get(roman.getId()).get(0).getRomanUid());
				roman.setAuditStaffid(romanMap.get(roman.getId()).get(0).getRomanStaffid());
				roman.setAuditTime(romanMap.get(roman.getId()).get(0).getRomanTime());
			}
			if( Constant.SPACE_ROOT_ID.equals(roman.getPid())){
				romanModels.add(roman);
			}
		});
		return romanModels;
	}

	/**
	 * 新增投诉建议流转表;.
	 * @Param param 投诉建议流转表;信息
	 * @Return 新增投诉建议流转表;是否成功
	 */
	@Override
	public Boolean add(ComplaintSuggestionRomanParam param) {
		ComplaintSuggestionRoman complaintSuggestionRoman = BeanUtils.convertTo(param, ComplaintSuggestionRoman::new);
		complaintSuggestionRoman.setId(null);
		return this.save(complaintSuggestionRoman);
	}

	/***
	 * @Description 保存流转信息
	 * @author huangyongtao
	 * @date 2024/7/16 11:28
	 * @param complaintSuggestion
	 * @param type
	 */
	public Boolean addRoman(ComplaintSuggestion complaintSuggestion, String type, Long pid) {
		ComplaintSuggestionRoman complaintSuggestionRoman =new ComplaintSuggestionRoman();
		complaintSuggestionRoman.setId(null);
		complaintSuggestionRoman.setComplaintSuggestionId(complaintSuggestion.getId());
		complaintSuggestionRoman.setRomanType(type);
		complaintSuggestionRoman.setPid(pid);
		if(ComplaintSuggestionRomanTypeEnum.REPLY.getCode().equals(type)){
			complaintSuggestionRoman.setRomanRemark(complaintSuggestion.getReplyRemark());
			complaintSuggestionRoman.setRomanStaffid(complaintSuggestion.getReplyStaffid());
			complaintSuggestionRoman.setRomanUid(complaintSuggestion.getReplyUid());
			complaintSuggestionRoman.setRomanUname(complaintSuggestion.getReplyUname());
			complaintSuggestionRoman.setRomanTime(complaintSuggestion.getReplyDate());
		}else{
			complaintSuggestionRoman.setRomanRemark(complaintSuggestion.getAuditRemark());
			complaintSuggestionRoman.setRomanStaffid(complaintSuggestion.getAuditStaffid());
			complaintSuggestionRoman.setRomanUid(complaintSuggestion.getAuditUid());
			complaintSuggestionRoman.setRomanUname(complaintSuggestion.getAuditUname());
			complaintSuggestionRoman.setRomanTime(complaintSuggestion.getAuditTime());
		}
		return this.save(complaintSuggestionRoman);
	}

	/**
	 * 批量新增投诉建议流转表;.
	 * @Param params 投诉建议流转表;信息列表
	 * @Return 批量新增投诉建议流转表;是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<ComplaintSuggestionRomanParam> params) {
		List<ComplaintSuggestionRoman> complaintSuggestionRomans = BeanUtils.convertListTo(params, ComplaintSuggestionRoman::new);
		return this.saveBatch(complaintSuggestionRomans);
	}

	/**
	 * 删除投诉建议流转表;.
	 * @Param id 投诉建议流转表;标识
	 * @Return 删除投诉建议流转表;是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		ComplaintSuggestionRoman complaintSuggestionRoman = this.getById(id);
		AssertUtils.notNull(complaintSuggestionRoman, SystemResultCode.RESULT_DATA_NONE.message());
		return this.removeById(id);
	}

	/**
	 * 批量删除投诉建议流转表;.
	 * @Param ids 投诉建议流转表;标识列表
	 * @Return 批量删除投诉建议流转表;是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeBatch(List<Long> ids) {
		return this.removeBatch(ids);
	}

}
