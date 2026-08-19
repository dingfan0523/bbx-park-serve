
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.settings.domain.AwardWinner;
import com.cgnpc.bbxpark.settings.dto.model.AwardWinnerModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerListParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerPageParam;
import com.cgnpc.bbxpark.settings.dto.param.AwardWinnerParam;
import com.cgnpc.bbxpark.settings.mapper.AwardWinnerRepository;
import com.cgnpc.bbxpark.settings.service.IAwardWinnerService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.AwardSortRuleEnum;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/***
 * @Description 获奖人信息服务实现
 * @author huangyongtao
 * @date 2025/11/13 9:26
 */
@Service("awardWinnerService")
public class AwardWinnerServiceImpl extends ServiceImpl<AwardWinnerRepository, AwardWinner> implements IAwardWinnerService {

	/**
	 * 根据获奖人信息标识获得获奖人信息详情信息.
	 * @Param [id] 获奖人信息标识
	 * @Return 获奖人信息详情信息
	 */
	@Override
	public AwardWinnerModel detail(Long id) {
		AwardWinner awardWinner = this.getById(id);
		AssertUtils.notNull(awardWinner, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(awardWinner, AwardWinnerModel::new);
	}

	/**
	 * 获取获奖人信息列表(分页).
	 * @Param param 获奖人信息查询条件
	 * @Return 获奖人信息信息列表（分页）
	 */
	@Override
	public IPage<AwardWinnerModel> page(AwardWinnerPageParam param) {
		IPage<AwardWinner> result = this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(result.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		return ConvertUtil.pageConvert(result.getCurrent(), result.getTotal(), result.getSize(),  BeanUtils.convertListTo(result.getRecords(), AwardWinnerModel::new));
	}

	/**
	 * 获取获奖人信息列表.
	 * @Param param 获奖人信息查询条件
	 * @Return 获奖人信息信息列表
	 */
	@Override
	@SneakyThrows
	public List<AwardWinnerModel> list(AwardWinnerListParam param) {
		List<AwardWinner> maintainPlans = this.list(buildQuery(BeanUtils.convertTo(param, AwardWinnerPageParam::new)));
		return BeanUtils.convertListTo(maintainPlans, AwardWinnerModel::new);
	}

	/**
	 * 新增获奖人信息.
	 * @Param param 获奖人信息信息
	 * @Return 新增获奖人信息是否成功
	 */
	@Override
	public Boolean add(AwardWinnerParam param) {
		AwardWinner awardWinner = BeanUtils.convertTo(param, AwardWinner::new);
		awardWinner.setId(null);
		if(ObjectUtil.isNotEmpty(awardWinner.getWinnerName())){
			awardWinner.setWinnerNamePinyin(PinyinUtil.getFirstLetter(awardWinner.getWinnerName(),""));
		}
		return this.save(awardWinner);
	}

	/**
	 * 批量新增获奖人信息.
	 * @Param params 获奖人信息信息列表
	 * @Return 批量新增获奖人信息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<AwardWinnerParam> params, Long awardId) {
		List<AwardWinner> awardWinners = BeanUtils.convertListTo(params, AwardWinner::new);
		if(ObjectUtil.isNotEmpty(awardWinners)){
			awardWinners.forEach(awardWinner -> {
				AssertUtils.notNull(awardWinner.getWinnerName(), "获奖人姓名不能为空！");
				awardWinner.setWinnerNamePinyin(PinyinUtil.getFirstLetter(awardWinner.getWinnerName(),""));
				awardWinner.setAwardId(awardId);
			});
		}
		return this.saveBatch(awardWinners);
	}

	/**
	 * 删除获奖人信息.
	 * @Param id 获奖人信息标识
	 * @Return 删除获奖人信息是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		AwardWinner awardWinner = this.getById(id);
		AssertUtils.notNull(awardWinner, SystemResultCode.RESULT_DATA_NONE.message());
		awardWinner.setDeleted(Status.disabled.getKey());
		return this.updateById(awardWinner);
	}

	/**
	 * 批量删除获奖人信息.
	 * @Param ids 获奖人信息标识列表
	 * @Return 批量删除获奖人信息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeBatch(List<Long> ids) {
		return this.update(Wrappers.<AwardWinner>lambdaUpdate().set(AwardWinner::getDeleted, Status.disabled.getKey()).in(AwardWinner::getId, ids));
	}

	@Override
	public Boolean removeByAwardId(Long awardId) {
		return this.update(Wrappers.<AwardWinner>lambdaUpdate().set(AwardWinner::getDeleted, Status.disabled.getKey()).eq(AwardWinner::getAwardId, awardId));
	}

	/**
	 * 编辑获奖人信息信息.
	 * @Param param 获奖人信息信息
	 * @Return 编辑获奖人信息是否成功
	 */
	@Override
	public Boolean edit(AwardWinnerParam param) {
		AwardWinner awardWinner = this.getById(param.getId());
		AssertUtils.notNull(awardWinner, SystemResultCode.RESULT_DATA_NONE.message());
		AwardWinner editParam = BeanUtils.convertTo(param, AwardWinner::new);
        if(ObjectUtil.isNotEmpty(param.getWinnerName())){
            editParam.setWinnerNamePinyin(PinyinUtil.getFirstLetter(param.getWinnerName(),""));
        }
		return this.updateById(editParam);
	}

	private LambdaQueryWrapper<AwardWinner> buildQuery(AwardWinnerPageParam param) {
		LambdaQueryWrapper<AwardWinner> query = new LambdaQueryWrapper<>();
		// 根据名称筛选
		query.like(ObjectUtil.isNotEmpty(param.getWinnerName()), AwardWinner::getWinnerName, param.getWinnerName());
		query.eq(ObjectUtil.isNotEmpty(param.getAwardId()), AwardWinner::getAwardId, param.getAwardId());
		query.in(ObjectUtil.isNotEmpty(param.getAwardIds()), AwardWinner::getAwardId, param.getAwardIds());
		// 租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), AwardWinner::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		// 查询未删除的记录
		query.eq(AwardWinner::getDeleted, Status.enabled.getKey());
		if(AwardSortRuleEnum.SORTNUM.getCode().equals(param.getSortRule())){
			query.orderByAsc(AwardWinner::getSortNumber);
		}else if(AwardSortRuleEnum.PINYIN.getCode().equals(param.getSortRule())){
			query.orderByAsc(AwardWinner::getWinnerNamePinyin);
		}else{
			query.orderByDesc(AwardWinner::getCreateTime);
		}
		return query;
	}

	public static void main(String[] args) {
        System.out.println(PinyinUtil.getPinyin("333",""));
        System.out.println(PinyinUtil.getFirstLetter("哈哈",""));
	}
}
