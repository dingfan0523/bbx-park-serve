
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.RegionManage;
import com.cgnpc.bbxpark.settings.domain.RegionSpaceRelation;
import com.cgnpc.bbxpark.settings.dto.model.RegionManageModel;
import com.cgnpc.bbxpark.settings.dto.param.RegionManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.RegionManageParam;
import com.cgnpc.bbxpark.settings.mapper.RegionManageRepository;
import com.cgnpc.bbxpark.settings.service.IRegionManageService;
import com.cgnpc.bbxpark.settings.service.IRegionSpaceRelationService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Description 区域管理员管理服务实现
 * @author huangyongtao
 * @date 2025/3/11 11:26
 */
@Service("regionManageService")
public class RegionManageServiceImpl extends BaseServiceImpl<RegionManageRepository, RegionManage> implements IRegionManageService {

	@Autowired
	private IRegionSpaceRelationService regionSpaceRelationService;
    @Autowired
    private IUserApiService userApiService;

	/**
	 * 获取区域管理员管理列表(分页).
	 * @Param param 区域管理员管理查询条件
	 * @Return 区域管理员管理信息列表（分页）
	 */
	@Override
	public IPage<RegionManageModel> page(RegionManagePageParam param) {
		IPage<RegionManage> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<RegionManageModel> regionManageModels = BeanUtils.convertListTo(page.getRecords(), RegionManageModel::new);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), regionManageModels);
	}

	/**
	 * 获取区域管理员管理列表.
	 * @Param param 区域管理员管理查询条件
	 * @Return 区域管理员管理信息列表
	 */
	@Override
	@SneakyThrows
	public List<RegionManageModel> list(RegionManageListParam param) {
		List<RegionManage> attentionManages = this.list(buildQuery(BeanUtils.convertTo(param, RegionManagePageParam::new)));
		if (CollectionUtil.isEmpty(attentionManages)) {
			return Collections.emptyList();
		}
		return BeanUtils.convertListTo(attentionManages, RegionManageModel::new);
	}

	private LambdaQueryWrapper buildQuery(RegionManagePageParam param) {
		LambdaQueryWrapper<RegionManage> query = new LambdaQueryWrapper<>();
		//名称
		query.like(ObjectUtil.isNotEmpty(param.getRegionUname()), RegionManage::getRegionUname, param.getRegionUname());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), RegionManage::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(RegionManage::getCreateTime);
		return query;
	}

	/**
	 * 新增区域管理员管理.
	 * @Param param 区域管理员管理信息
	 * @Return 新增区域管理员管理是否成功
	 */
	@Override
	public Boolean add(RegionManageParam param) {
		RegionManage regionManage = BeanUtils.convertTo(param, RegionManage::new);
		regionManage.setId(null);
		return this.save(regionManage);
	}

	/**
	 * 批量新增区域管理员管理.
	 * @Param params 区域管理员管理信息列表
	 * @Return 批量新增区域管理员管理是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized Boolean addBatch(RegionManageParam params) {
		if (CollectionUtil.isEmpty(params.getUserIds())) {
			return true;
		}
//		List<RegionManage> regionManages = BeanUtils.convertListTo(params, RegionManage::new);
		List<RegionManage> regionManageOlds= this.list(buildQuery(new RegionManagePageParam()));
		if (regionManageOlds == null) {
			regionManageOlds = Collections.emptyList();
		}
		// 提取输入集合和数据库集合的 regionUid
		List<String> inputUids = params.getUserIds();
		List<String> dbUids = regionManageOlds.stream()
				.map(RegionManage::getRegionUid)
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
			LambdaQueryWrapper<RegionManage> removeQuery = new LambdaQueryWrapper<>();
			removeQuery.in(RegionManage::getRegionUid, toRemoveUids);
			this.remove(removeQuery);
		}

		// 执行新增操作
		if (CollectionUtil.isNotEmpty(toAddManages)) {
            List<UserInfoModel> toAddManageList = userApiService.getByStaffNos(toAddManages);
            List<RegionManage> list = toAddManageList.stream().map(user->{
                RegionManage manage = new RegionManage();
                manage.setRegionUid(user.getId());
                manage.setRegionStaffid(user.getStaffNo());
                manage.setRegionUname(user.getUserName());
                return manage;
            }).collect(Collectors.toList());
			this.saveBatch(list);
		}
		return true;
	}

	/**
	 * 删除区域管理员管理.
	 * @Param id 区域管理员管理标识
	 * @Return 删除区域管理员管理是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean remove(Long id) {
		RegionManage regionManage = this.getById(id);
		AssertUtils.notNull(regionManage, SystemResultCode.RESULT_DATA_NONE.message());
		this.removeById(id);
		regionSpaceRelationService.remove(Wrappers.<RegionSpaceRelation>lambdaQuery().eq(RegionSpaceRelation::getRegionId, id));
		return true;
	}
}
