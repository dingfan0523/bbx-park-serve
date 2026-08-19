
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.settings.domain.AwardRoman;
import com.cgnpc.bbxpark.settings.dto.model.AwardRomanModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardRomanParam;
import com.cgnpc.bbxpark.settings.mapper.AwardRomanRepository;
import com.cgnpc.bbxpark.settings.service.IAwardRomanService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/***
 * @Description 评优评奖流程服务实现
 * @author huangyongtao
 * @date 2025/11/12 16:41
 */
@Service("awardRomanService")
public class AwardRomanServiceImpl extends ServiceImpl<AwardRomanRepository, AwardRoman> implements IAwardRomanService {

	/**
	 * 根据评优评奖流程标识获得评优评奖流程详情信息.
	 * @Param [id] 评优评奖流程标识
	 * @Return 评优评奖流程详情信息
	 */
	@Override
	public AwardRomanModel detail(Long id) {
		AwardRoman awardRoman = this.getById(id);
		AssertUtils.notNull(awardRoman, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(awardRoman, AwardRomanModel::new);
	}

	/**
	 * 获取评优评奖流程列表.
	 * @Param param 评优评奖流程查询条件
	 * @Return 评优评奖流程信息列表
	 */
	@Override
	@SneakyThrows
	public List<AwardRomanModel> list(Long awardId) {
        val awardRomans = this.list(Wrappers.<AwardRoman>lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(awardId), AwardRoman::getAwardId, awardId)
                .eq(AwardRoman::getDeleted, Status.enabled.getKey())
                .eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), AwardRoman::getTenantId, WebFrameworkUtils.getHeaderTenantId())
                .orderByAsc(AwardRoman::getCreateTime));
		return BeanUtils.convertListTo(awardRomans, AwardRomanModel::new);
	}

	/**
	 * 新增评优评奖流程.
	 * @Param param AwardId
	 * @Return 新增评优评奖流程是否成功
	 */
	@Override
	public Boolean add(AwardRomanParam param) {
		AwardRoman awardRoman = BeanUtils.convertTo(param, AwardRoman::new);
		awardRoman.setId(null);
		return this.save(awardRoman);
	}

	/**
	 * 批量新增评优评奖流程.
	 * @Param params 评优评奖流程信息列表
	 * @Return 批量新增评优评奖流程是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addBatch(List<AwardRomanParam> params) {
		List<AwardRoman> awardRomans = BeanUtils.convertListTo(params, AwardRoman::new);
		return this.saveBatch(awardRomans);
	}

	/**
	 * 删除评优评奖流程.
	 * @Param id 评优评奖流程标识
	 * @Return 删除评优评奖流程是否成功
	 */
	@Override
	public Boolean removeByAwardId(Long awardId) {
		return this.update(Wrappers.<AwardRoman>lambdaUpdate().set(AwardRoman::getDeleted,
                Status.disabled.getKey()).eq(AwardRoman::getAwardId, awardId));
	}

}
