
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.Award;
import com.cgnpc.bbxpark.settings.dto.model.AwardModel;
import com.cgnpc.bbxpark.settings.dto.model.AwardWinnerModel;
import com.cgnpc.bbxpark.settings.dto.param.*;
import com.cgnpc.bbxpark.settings.mapper.AwardRepository;
import com.cgnpc.bbxpark.settings.service.IAwardRomanService;
import com.cgnpc.bbxpark.settings.service.IAwardService;
import com.cgnpc.bbxpark.settings.service.IAwardWinnerService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.AwardRomanStatusEnum;
import com.cgnpc.bbxpark.common.enums.AwardSortRuleEnum;
import com.cgnpc.bbxpark.common.enums.AwardStatusEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @Description 评优评奖服务实现
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Service("awardService")
public class AwardServiceImpl extends ServiceImpl<AwardRepository, Award> implements IAwardService {

	@Autowired
	private IAwardWinnerService awardWinnerService;

	@Autowired
	private IAwardRomanService awardRomanService;

	@Autowired
	private IUserApiService userApiService;

	/**
	 * 根据评优评奖标识获得评优评奖详情信息.
	 * @Param [id] 评优评奖标识
	 * @Return 评优评奖详情信息
	 */
	@Override
	public AwardModel detail(Long id) {
		Award award = this.getById(id);
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AwardModel model= BeanUtils.convertTo(award, AwardModel::new);
		model.setAwardRomanModels(awardRomanService.list(id));
        AwardWinnerListParam awardWinnerParam = new AwardWinnerListParam();
        awardWinnerParam.setAwardId(id);
        model.setAwardWinnerModel(awardWinnerService.list(awardWinnerParam));
		return model;
	}

	/**
	 * 获取评优评奖列表(分页).
	 * @Param param 评优评奖查询条件
	 * @Return 评优评奖信息列表（分页）
	 */
	@Override
	public IPage<AwardModel> page(AwardPageParam param) {
		buildRole(param);
		IPage<Award> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<AwardModel> models = BeanUtils.convertListTo(result.getRecords(), AwardModel::new);
		handlePermission(models);
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(), models);
	}


	/**
	 * 获取评优评奖列表.
	 * @Param param 评优评奖查询条件
	 * @Return 评优评奖信息列表
	 */
	@Override
	@SneakyThrows
	public List<AwardModel> list(AwardListParam param) {
		List<Award> maintainPlans = this.list(buildQuery(BeanUtils.convertTo(param, AwardPageParam::new)));
		return BeanUtils.convertListTo(maintainPlans, AwardModel::new);
	}

	@Override
	public List<AwardModel> findDisplay(AwardListParam param) {
		param.setStatus(AwardStatusEnum.ONDISPLAY.getCode());
		List<Award> maintainPlans = this.list(buildQuery(BeanUtils.convertTo(param, AwardPageParam::new)));
		List<AwardModel> awardModels = BeanUtils.convertListTo(maintainPlans, AwardModel::new);
		handleDisplay(awardModels);
		return awardModels;
	}


	/**
	 * 新增评优评奖.
	 * @Param param 评优评奖信息
	 * @Return 新增评优评奖是否成功
	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Long add(AwardParam param) {
		Award award = BeanUtils.convertTo(param, Award::new);
		award.setId(null);
		if(ObjectUtil.isNotEmpty(param.getAuditUid())){
			UserInfoModel userInfoModel = getUser(param.getAuditUid());
			award.setAuditUname(userInfoModel.getUserName());
			award.setAuditStaffid(userInfoModel.getStaffid());
		}
		award.setStatus(AwardStatusEnum.SUBMIT.getCode());
		this.save(award);
		if(CollectionUtil.isNotEmpty(param.getAwardWinnerParams())){
			awardWinnerService.addBatch(param.getAwardWinnerParams(), award.getId());
		}

		return award.getId();
	}

	/**
	 * 删除评优评奖.
	 * @Param id 评优评奖标识
	 * @Return 删除评优评奖是否成功
	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Boolean remove(Long id) {
		Award award = this.getById(id);
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isTrue(WebFrameworkUtils.getHeaderUserId().equals(award.getCreatorId()),"非发起人，无权限");
		award.setDeleted(Status.disabled.getKey());
		this.updateById(award);
		awardWinnerService.removeByAwardId(id);
		awardRomanService.removeByAwardId(id);
		return true;
	}

	/**
	 * 编辑评优评奖信息.
	 * @Param param 评优评奖信息
	 * @Return 编辑评优评奖是否成功
	 */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Boolean edit(AwardParam param) {
		Award award = this.getById(param.getId());
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isTrue(AwardStatusEnum.SUBMIT.getCode().equals(award.getStatus()),"状态发生变化，请确认");
		AssertUtils.isTrue(WebFrameworkUtils.getHeaderUserId().equals(award.getCreatorId()),"非发起人，无权限");
		Award editParam = BeanUtils.convertTo(param, Award::new);
		this.updateById(editParam);
        if(CollectionUtil.isNotEmpty(param.getAwardWinnerParams())){
            awardWinnerService.removeByAwardId(award.getId());
            awardWinnerService.addBatch(param.getAwardWinnerParams(), award.getId());
        }
        return true;
    }


	/**
	 * 撤回评优评奖.
	 * @Param id 评优评奖标识
	 * @Return 启用评优评奖是否成功
	 */
	@Override
	public Boolean recall(AwardHandleParam param) {
		Award award = this.getById(param.getId());
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isFalse(Arrays.asList(AwardStatusEnum.ONDISPLAY.getCode(), AwardStatusEnum.ENDDISPLAY.getCode()).contains(award.getStatus()),"状态发生变化，请确认");
		AssertUtils.isTrue(WebFrameworkUtils.getHeaderUserId().equals(award.getCreatorId()),"非发起人，无权限");
		award.setStatus(AwardStatusEnum.SUBMIT.getCode());
		this.updateById(award);
		saveRoman(param, "撤回人", AwardRomanStatusEnum.RECALL.getCode());
		return true;
	}
	/**
	 * 禁用评优评奖.
	 * @Param id 评优评奖标识
	 * @Return 禁用评优评奖是否成功
	 */
	@Override
	public Boolean submit(AwardHandleParam param) {
		Award award = this.getById(param.getId());
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isTrue(AwardStatusEnum.SUBMIT.getCode().equals(award.getStatus()),"状态发生变化，请确认");
		AssertUtils.isTrue(WebFrameworkUtils.getHeaderUserId().equals(award.getCreatorId()),"非发起人，无权限");
		if(ObjectUtil.isNotEmpty(award.getAuditUid())){
			award.setStatus(AwardStatusEnum.AUDIT.getCode());
		}else{
			if(award.getDisplayStartTime().after(new Date())){
				award.setStatus(AwardStatusEnum.DISPLAY.getCode());
			} else if(award.getDisplayStartTime().before(new Date()) && award.getDisplayEndTime().after(new Date())){
				award.setStatus(AwardStatusEnum.ONDISPLAY.getCode());
			}if(award.getDisplayEndTime().before(new Date())){
				award.setStatus(AwardStatusEnum.ENDDISPLAY.getCode());
			}
		}
		this.updateById(award);
		saveRoman(param, "提交人", AwardRomanStatusEnum.SUBMIT.getCode());
		return true;
	}

	@Override
	public Boolean audit(AwardHandleParam param) {
		Award award = this.getById(param.getId());
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isTrue(AwardStatusEnum.AUDIT.getCode().equals(award.getStatus()),"状态发生变化，请确认");
		AssertUtils.isTrue(WebFrameworkUtils.getHeaderUserId().equals(award.getAuditUid()),"非审批人，无权限");
        if(Status.disabled.getKey().equals(param.getOperatorResult())){
            award.setStatus(AwardStatusEnum.SUBMIT.getCode());
        }else if(award.getDisplayStartTime().after(new Date())){
			award.setStatus(AwardStatusEnum.DISPLAY.getCode());
		} else if(award.getDisplayStartTime().before(new Date()) && award.getDisplayEndTime().after(new Date())){
			award.setStatus(AwardStatusEnum.ONDISPLAY.getCode());
		}if(award.getDisplayEndTime().before(new Date())){
			award.setStatus(AwardStatusEnum.ENDDISPLAY.getCode());
		}
		this.updateById(award);
		saveRoman(param, "审批人", AwardRomanStatusEnum.AUDIT.getCode());
		return true;
	}

	@Override
	public Boolean cancel(AwardHandleParam param) {
		Award award = this.getById(param.getId());
		AssertUtils.notNull(award, SystemResultCode.RESULT_DATA_NONE.message());
		AssertUtils.isTrue(AwardStatusEnum.ONDISPLAY.getCode().equals(award.getStatus()),"状态发生变化，请确认");
		AssertUtils.isTrue(WebFrameworkUtils.getHeaderUserId().equals(award.getCreatorId()),"非发起人，无权限");
		award.setStatus(AwardStatusEnum.ENDDISPLAY.getCode());
		this.updateById(award);
		saveRoman(param, "操作人", AwardRomanStatusEnum.ENDDISPLAY.getCode());
		return true;
	}

	@Override
	public void executeAwardDisplayTime() {
		List<Award> awards = this.list(Wrappers.<Award>lambdaQuery()
				.in(Award::getStatus, Arrays.asList(AwardStatusEnum.DISPLAY.getCode(), AwardStatusEnum.ONDISPLAY.getCode()))
				.eq(Award::getDeleted, Status.enabled.getKey()));
		if (CollectionUtil.isEmpty(awards)){
			return;
		}
		awards.forEach(award -> {
			String operator = StringUtils.EMPTY;
			Integer romanStatus = null;
			if(award.getDisplayStartTime().before(new Date()) && award.getDisplayEndTime().after(new Date())){
				award.setStatus(AwardStatusEnum.ONDISPLAY.getCode());
				operator = "系统自动开始展示";
				romanStatus = AwardRomanStatusEnum.ONDISPLAY.getCode();
			}if(award.getDisplayEndTime().before(new Date())){
				award.setStatus(AwardStatusEnum.ENDDISPLAY.getCode());
				operator = "系统自动结束展示";
				romanStatus = AwardRomanStatusEnum.ENDDISPLAY.getCode();
			}
			this.update(Wrappers.<Award>lambdaUpdate()
					.eq(Award::getId, award.getId())
					.set(Award::getUpdateTime, new Date())
					.set(Award::getStatus, award.getStatus()));
			AwardRomanParam awardRomanParam = new AwardRomanParam();
			awardRomanParam.setAwardId(award.getId());
			awardRomanParam.setStatus(romanStatus);
			awardRomanParam.setOperatorName(operator);
			awardRomanParam.setOperator("操作人");
			awardRomanParam.setTenantId(award.getTenantId());
			awardRomanService.add(awardRomanParam);
		});
	}

	private void saveRoman(AwardHandleParam param, String operator, Integer status){
		UserInfoModel userInfo = getUser(WebFrameworkUtils.getHeaderUserId());
		AwardRomanParam awardRomanParam = new AwardRomanParam();
		awardRomanParam.setAwardId(param.getId());
		awardRomanParam.setStatus(status);
		awardRomanParam.setOperatorId(userInfo.getId());
		awardRomanParam.setOperatorName(userInfo.getUserName());
		awardRomanParam.setOperatorStaffid(userInfo.getStaffid());
		awardRomanParam.setOperator(operator);
		awardRomanParam.setRemark(param.getRemark());
		awardRomanParam.setOperatorResult(param.getOperatorResult());
		awardRomanService.add(awardRomanParam);
	}
	/***
	 * @Description 获取用户信息
	 * @author huangyongtao
	 * @date 2024/8/2 10:34
	 * @param userId
	 */
	private UserInfoModel getUser(String userId) {
		return userApiService.detail(userId);
	}

	private void buildRole(AwardPageParam param) {
        Boolean admin = userApiService.parkAdmin();
        if (admin){
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        } else {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            param.setUserId(WebFrameworkUtils.getHeaderUserId());
        }
	}

	private void handlePermission(List<AwardModel> models){
		String userId = WebFrameworkUtils.getHeaderUserId();
		if(CollectionUtil.isNotEmpty(models)){
			for (AwardModel model : models) {
				if(Objects.equals(model.getCreatorId(), userId)){
					model.setDeleteFlag(true);
					if(AwardStatusEnum.ONDISPLAY.getCode().equals(model.getStatus())){
						model.setCancelFlag(true);
					}
					if(AwardStatusEnum.SUBMIT.getCode().equals(model.getStatus())){
						model.setSubmitFlag(true);
						model.setEditFlag(true);
					}
					if(!Arrays.asList(AwardStatusEnum.ONDISPLAY.getCode(), AwardStatusEnum.ENDDISPLAY.getCode()).contains(model.getStatus())){
						model.setRecallFlag(true);
					}
				}
				if(AwardStatusEnum.AUDIT.getCode().equals(model.getStatus()) && Objects.equals(model.getAuditUid(), userId) ){
					model.setAuditFlag(true);
				}
			}
		}
	}

	private void handleDisplay(List<AwardModel> models) {
		if(CollectionUtil.isEmpty(models)){
			return;
		}
		List<Long> ids = models.stream().map(AwardModel::getId).collect(Collectors.toList());
		AwardWinnerListParam param = new AwardWinnerListParam();
		param.setAwardIds(ids);
		List<AwardWinnerModel> awardWinners = awardWinnerService.list(param);
		Map<Long, List<AwardWinnerModel>> map = awardWinners.stream().collect(Collectors.groupingBy(AwardWinnerModel::getAwardId));
		models.stream().filter(m->map.containsKey(m.getId())).forEach(model -> {
			List<AwardWinnerModel> winners = map.get(model.getId());
			if(AwardSortRuleEnum.SORTNUM.getCode().equals(model.getSortRule())){
				winners.sort(Comparator.comparingInt(AwardWinnerModel::getSortNumber));
			}else{
				winners.sort(Comparator.comparing(AwardWinnerModel::getWinnerNamePinyin));
			}
			model.setAwardWinnerModel(winners);
		});
	}
	private LambdaQueryWrapper<Award> buildQuery(AwardPageParam param) {
		LambdaQueryWrapper<Award> query = new LambdaQueryWrapper<>();
		// 根据名称筛选
		query.like(ObjectUtil.isNotEmpty(param.getAwardName()), Award::getAwardName, param.getAwardName());
		// 根据状态筛选
		query.eq(ObjectUtil.isNotEmpty(param.getStatus()), Award::getStatus, param.getStatus());
		// 根据开始时间筛选
		query.ge(ObjectUtil.isNotEmpty(param.getCreateTimeStart()), Award::getCreateTime, param.getCreateTimeStart());
		// 根据结束时间筛选
		query.ne(ObjectUtil.isNotEmpty(param.getCreateTimeEnd()), Award::getCreateTime, param.getCreateTimeEnd());
		// 当userId不为空时，添加条件：userId要么是审核人ID，要么是创建人ID
		if(ObjectUtil.isNotEmpty(param.getUserId())){
			query.and(wrapper -> wrapper.eq(Award::getAuditUid, param.getUserId())
					.or()
					.eq(Award::getCreatorId, param.getUserId()));
		}
		// 租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), Award::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		// 查询未删除的记录
		query.eq(Award::getDeleted, Status.enabled.getKey());
		query.orderByDesc(Award::getCreateTime);
		return query;
	}

}
