
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.AttentionManage;
import com.cgnpc.bbxpark.settings.dto.model.AttentionManageModel;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.AttentionManageParam;
import com.cgnpc.bbxpark.settings.mapper.AttentionManageRepository;
import com.cgnpc.bbxpark.settings.service.IAttentionManageService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Description 关注人管理服务实现
 * @author huangyongtao
 * @date 2025/3/11 10:33
 */
@Service("attentionManageService")
public class AttentionManageServiceImpl extends ServiceImpl<AttentionManageRepository, AttentionManage> implements IAttentionManageService {

    @Autowired
    private IUserApiService userApiService;

	/**
	 * 获取关注人管理列表(分页).
	 * @Param param 关注人管理查询条件
	 * @Return 关注人管理信息列表（分页）
	 */
	@Override
	public IPage<AttentionManageModel> page(AttentionManagePageParam param) {
		IPage<AttentionManage> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<AttentionManageModel> attentionManageModels = BeanUtils.convertListTo(page.getRecords(), AttentionManageModel::new);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), attentionManageModels);
	}

	/**
	 * 获取关注人管理列表.
	 * @Param param 关注人管理查询条件
	 * @Return 关注人管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<AttentionManageModel> list(AttentionManageListParam param) {
		List<AttentionManage> attentionManages = this.list(buildQuery(BeanUtils.convertTo(param, AttentionManagePageParam::new)));
		if (CollectionUtil.isEmpty(attentionManages)) {
			return Collections.emptyList();
		}
		return BeanUtils.convertListTo(attentionManages, AttentionManageModel::new);
	}

	private LambdaQueryWrapper buildQuery(AttentionManagePageParam param) {
		LambdaQueryWrapper<AttentionManage> query = new LambdaQueryWrapper<>();
		//名称
		query.like(ObjectUtil.isNotEmpty(param.getAttentionUname()), AttentionManage::getAttentionUname, param.getAttentionUname());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), AttentionManage::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(AttentionManage::getCreateTime);
		return query;
	}

	/**
	 * 新增关注人管理.
	 * @Param param 关注人管理信息
	 * @Return 新增关注人管理是否成功
	 */
	@Override
	public Boolean add(AttentionManageParam param) {
		AttentionManage attentionManage = BeanUtils.convertTo(param, AttentionManage::new);
		attentionManage.setId(null);
		return this.save(attentionManage);
	}

	/**
	 * 批量新增关注人管理.
	 * @Param params 关注人管理信息列表
	 * @Return 批量新增关注人管理是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized Boolean addBatch(List<AttentionManageParam> params) {
		if (CollectionUtil.isEmpty(params)) {
			return true;
		}
		List<AttentionManage> attentionManages = BeanUtils.convertListTo(params, AttentionManage::new);
		List<AttentionManage> attentionManageOlds= this.list(buildQuery(new AttentionManagePageParam()));
		if (attentionManageOlds == null) {
			attentionManageOlds = Collections.emptyList();
		}
		// 提取输入集合和数据库集合的 attentionUid
		List<String> inputUids = attentionManages.stream()
				.map(AttentionManage::getAttentionUid)
				.filter(ObjectUtil::isNotEmpty)
				.collect(Collectors.toList());
		List<String> dbUids = attentionManageOlds.stream()
				.map(AttentionManage::getAttentionUid)
				.filter(ObjectUtil::isNotEmpty)
				.collect(Collectors.toList());

		// 找出需要删除的数据（数据库中有但输入集合中没有）
		List<String> toRemoveUids = dbUids.stream()
				.filter(uid -> !inputUids.contains(uid))
				.collect(Collectors.toList());

        // 找出需要新增的数据（输入集合中有但数据库中没有）
        List<String> toAddManages = inputUids.stream()
                .filter(uid -> !dbUids.contains(uid))
                .collect(Collectors.toList());

		// 执行删除操作
		if (CollectionUtil.isNotEmpty(toRemoveUids)) {
			LambdaQueryWrapper<AttentionManage> removeQuery = new LambdaQueryWrapper<>();
			removeQuery.in(AttentionManage::getAttentionUid, toRemoveUids);
			this.remove(removeQuery);
		}

        // 执行新增操作
        if (CollectionUtil.isNotEmpty(toAddManages)) {
            List<UserInfoModel> toAddManageList = userApiService.getByStaffNos(toAddManages);
            List<AttentionManage> list = toAddManageList.stream().map(user->{
                AttentionManage manage = new AttentionManage();
                manage.setAttentionUid(user.getId());
                manage.setAttentionStaffid(user.getStaffNo());
                manage.setAttentionUname(user.getUserName());
                return manage;
            }).collect(Collectors.toList());
            this.saveBatch(list);
        }
		return true;
	}

	/**
	 * 删除关注人管理.
	 * @Param id 关注人管理标识
	 * @Return 删除关注人管理是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		AttentionManage attentionManage = this.getById(id);
		AssertUtils.notNull(attentionManage, SystemResultCode.RESULT_DATA_NONE.message());
		return this.removeById(id);
	}

	@Override
	public Boolean checkAttention(String userId) {
        LambdaQueryWrapper<AttentionManage> wrapper = Wrappers.<AttentionManage>lambdaQuery().eq(AttentionManage::getAttentionUid, userId);
        int count = this.count(wrapper);
        return count > 0 ? true : false;
    }
}
