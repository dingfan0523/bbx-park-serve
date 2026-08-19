
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.settings.domain.SecurityDeviceRelation;
import com.cgnpc.bbxpark.settings.domain.SecurityManage;
import com.cgnpc.bbxpark.settings.dto.model.SecurityManageModel;
import com.cgnpc.bbxpark.settings.dto.param.SecurityManageListParam;
import com.cgnpc.bbxpark.settings.dto.param.SecurityManagePageParam;
import com.cgnpc.bbxpark.settings.dto.param.SecurityManageParam;
import com.cgnpc.bbxpark.settings.mapper.SecurityManageRepository;
import com.cgnpc.bbxpark.settings.service.ISecurityDeviceRelationService;
import com.cgnpc.bbxpark.settings.service.ISecurityManageService;
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
 * @Description 安全管理员服务实现
 * @author huangyongtao
 * @date 2025/8/1 11:35
 */
@Service("securityManageService")
public class SecurityManageServiceImpl extends BaseServiceImpl<SecurityManageRepository, SecurityManage> implements ISecurityManageService {

	@Autowired
	private ISecurityDeviceRelationService securityDeviceRelationService;
    @Autowired
    private IUserApiService userApiService;
	/**
	 * 获取安全管理员列表(分页).
	 * @Param param 安全管理员查询条件
	 * @Return 安全管理员信息列表（分页）
	 */
	@Override
	public IPage<SecurityManageModel> page(SecurityManagePageParam param) {
		IPage<SecurityManage> page =  this.page(new Page<>(param.getCurrent(), param.getSize()), buildQuery(param));
		if (CollectionUtil.isEmpty(page.getRecords())) {
			return ConvertUtil.pageEmptyConvert(param.getCurrent(), param.getSize());
		}
		List<SecurityManageModel> securityManageModels = BeanUtils.convertListTo(page.getRecords(), SecurityManageModel::new);
		return ConvertUtil.pageConvert(page.getCurrent(), page.getTotal(), page.getSize(), securityManageModels);
	}

	/**
	 * 获取安全管理员列表.
	 * @Param param 安全管理员查询条件
	 * @Return 安全管理员信息列表
	 */
	@Override
	@SneakyThrows
	public List<SecurityManageModel> list(SecurityManageListParam param) {
		List<SecurityManage> securityManage = this.list(buildQuery(BeanUtils.convertTo(param, SecurityManagePageParam::new)));
		if (CollectionUtil.isEmpty(securityManage)) {
			return Collections.emptyList();
		}
		return BeanUtils.convertListTo(securityManage, SecurityManageModel::new);
	}

	private LambdaQueryWrapper buildQuery(SecurityManagePageParam param) {
		LambdaQueryWrapper<SecurityManage> query = new LambdaQueryWrapper<>();
		//名称
		query.like(ObjectUtil.isNotEmpty(param.getSecurityUname()), SecurityManage::getSecurityUname, param.getSecurityUname());
		//租户
		query.eq(ObjectUtil.isNotEmpty(WebFrameworkUtils.getHeaderTenantId()), SecurityManage::getTenantId, WebFrameworkUtils.getHeaderTenantId());
		query.orderByDesc(SecurityManage::getCreateTime);
		return query;
	}

	/**
	 * 新增安全管理员.
	 * @Param param 安全管理员信息
	 * @Return 新增安全管理员是否成功
	 */
	@Override
	public Boolean add(SecurityManageParam param) {
		SecurityManage securityManage = BeanUtils.convertTo(param, SecurityManage::new);
		securityManage.setId(null);
		return this.save(securityManage);
	}

	/**
	 * 批量新增安全管理员.
	 * @Param params 安全管理员信息列表
	 * @Return 批量新增安全管理员是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized Boolean addBatch(SecurityManageParam params) {
		if (CollectionUtil.isEmpty(params.getUserIds())) {
			return true;
		}
		List<SecurityManage> regionManageOlds= this.list(buildQuery(new SecurityManagePageParam()));
		if (regionManageOlds == null) {
			regionManageOlds = Collections.emptyList();
		}
		// 提取输入集合和数据库集合的 regionUid
		List<String> inputUids = params.getUserIds();
		List<String> dbUids = regionManageOlds.stream()
				.map(SecurityManage::getSecurityUid)
				.filter(ObjectUtil::isNotEmpty)
				.collect(Collectors.toList());

		// 找出需要删除的数据（数据库中有但输入集合中没有）
		List<String> toRemoveUids = dbUids.stream()
				.filter(uid -> !inputUids.contains(uid))
				.collect(Collectors.toList());

		// 找出需要新增的数据（输入集合中有但数据库中没有）
		List<String> toAddManages = inputUids.stream()
				.filter(id -> !dbUids.contains(id))
				.collect(Collectors.toList());

		// 执行删除操作
		if (CollectionUtil.isNotEmpty(toRemoveUids)) {
			LambdaQueryWrapper<SecurityManage> removeQuery = new LambdaQueryWrapper<>();
			removeQuery.in(SecurityManage::getSecurityUid, toRemoveUids);
			this.remove(removeQuery);
		}

		// 执行新增操作
		if (CollectionUtil.isNotEmpty(toAddManages)) {
            List<UserInfoModel> toAddManageList = userApiService.getByStaffNos(toAddManages);
            List<SecurityManage> list = toAddManageList.stream().map(user->{
                SecurityManage manage = new SecurityManage();
                manage.setSecurityUid(user.getId());
                manage.setSecurityStaffid(user.getStaffNo());
                manage.setSecurityUname(user.getUserName());
                return manage;
            }).collect(Collectors.toList());
			this.saveBatch(list);
		}
		return true;
	}

	/**
	 * 删除安全管理员.
	 * @Param id 安全管理员标识
	 * @Return 删除安全管理员是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean remove(Long id) {
		SecurityManage securityManage = this.getById(id);
		AssertUtils.notNull(securityManage, SystemResultCode.RESULT_DATA_NONE.message());
		this.removeById(id);
		securityDeviceRelationService.remove(Wrappers.<SecurityDeviceRelation>lambdaQuery().in(SecurityDeviceRelation::getSecurityManageId, id));
		return true;
	}
}
