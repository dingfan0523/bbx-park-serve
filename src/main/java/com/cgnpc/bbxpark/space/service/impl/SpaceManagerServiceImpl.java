
package com.cgnpc.bbxpark.space.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.SpaceManager;
import com.cgnpc.bbxpark.space.dto.model.SpaceManagerModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerListParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerPageParam;
import com.cgnpc.bbxpark.space.dto.param.SpaceManagerParam;
import com.cgnpc.bbxpark.space.mapper.SpaceManagerRepository;
import com.cgnpc.bbxpark.space.service.ISpaceManagerService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("spaceManagerService")
public class SpaceManagerServiceImpl extends ServiceImpl<SpaceManagerRepository, SpaceManager> implements ISpaceManagerService {
	@Resource
	private IUserApiService userApiService;

	/**
	 * 根据空间责任人标识获得空间责任人详情信息.
	 * @Param [id] 空间责任人标识
	 * @Return 空间责任人详情信息
	 */
	@Override
	public SpaceManagerModel detail(Long id) {
		SpaceManager spaceManager = this.getById(id);
		AssertUtils.notNull(spaceManager, SystemResultCode.RESULT_DATA_NONE.message());
		return BeanUtils.convertTo(spaceManager, SpaceManagerModel::new);
	}

	/**
	 * 获取空间责任人列表(分页).
	 * @Param param 空间责任人查询条件
	 * @Return 空间责任人信息列表（分页）
	 */
	@Override
	public IPage<SpaceManagerModel> page(SpaceManagerPageParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		IPage<SpaceManager> page = page(new Page<>(param.getCurrent(),param.getSize()), Wrappers.<SpaceManager>lambdaQuery()
				.eq(tenantId != null,SpaceManager::getTenantId,tenantId)
                .eq(param.getSpaceId() != null,SpaceManager::getSpaceId,param.getSpaceId())
                .orderByDesc(SpaceManager::getCreateTime));
		if(CollectionUtils.isEmpty(page.getRecords())){
			return ConvertUtil.pageEmptyConvert(page.getCurrent(),page.getSize());
		}
		List<SpaceManagerModel> list = BeanUtils.convertListTo(page.getRecords(), SpaceManagerModel::new);
		return ConvertUtil.pageConvert(page.getCurrent(),page.getTotal(),page.getSize(),list);
	}

	/**
	 * 获取空间责任人列表.
	 * @Param param 空间责任人查询条件
	 * @Return 空间责任人信息列表
	 */
	@Override
	@SneakyThrows
	public List<SpaceManagerModel> list(SpaceManagerListParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<SpaceManager> list = this.list(Wrappers.<SpaceManager>lambdaQuery().eq(param.getSpaceId() != null,SpaceManager::getSpaceId, param.getSpaceId())
                .eq(tenantId != null,SpaceManager::getTenantId, tenantId).orderByDesc(SpaceManager::getCreateTime));
		if(CollectionUtils.isEmpty(list)){
			return Collections.emptyList();
		}
		return BeanUtils.convertListTo(list, SpaceManagerModel::new);
	}

	/**
	 * 新增空间责任人.
	 * @Param param 空间责任人信息
	 * @Return 新增空间责任人是否成功
	 */
	@Override
	public Boolean add(SpaceManagerParam param) {
		//校验类型是否存在
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		List<SpaceManager> list = this.list(Wrappers.<SpaceManager>lambdaQuery().eq(tenantId != null,SpaceManager::getTenantId, tenantId)
                .eq(SpaceManager::getType, param.getType()).eq(SpaceManager::getSpaceId, param.getSpaceId()));
		AssertUtils.isTrue(list.isEmpty(), "该类型责任人已存在");

		SpaceManager spaceManager = BeanUtils.convertTo(param, SpaceManager::new);
		spaceManager.setId(null);
		//用户信息
		UserInfoModel userInfo = userApiService.getByStaffNo(param.getUserId());
		spaceManager.setName(userInfo.getUserName());
		spaceManager.setStaffid(userInfo.getStaffid());
		spaceManager.setPhone(userInfo.getMobile());
		return save(spaceManager);
	}

	/**
	 * 编辑空间责任人信息.
	 * @Param param 空间责任人信息
	 * @Return 编辑空间责任人是否成功
	 */
	@Override
	public Boolean edit(SpaceManagerParam param) {
		Long tenantId = WebFrameworkUtils.getHeaderTenantId();
		SpaceManager spaceManager = this.getById(param.getId());
		AssertUtils.notNull(spaceManager, SystemResultCode.RESULT_DATA_NONE.message());

		//校验类型是否存在
		List<SpaceManager> list = this.list(Wrappers.<SpaceManager>lambdaQuery().eq(tenantId != null,SpaceManager::getTenantId, tenantId).eq(SpaceManager::getType, param.getType()).eq(SpaceManager::getSpaceId, param.getSpaceId()).ne(SpaceManager::getId, param.getId()));
		AssertUtils.isTrue(list.isEmpty(), "该类型责任人已存在");

		BeanUtils.copyProperties(param, spaceManager);
		//用户信息
		UserInfoModel userInfo = userApiService.getByStaffNo(param.getUserId());
		spaceManager.setName(userInfo.getUserName());
		spaceManager.setStaffid(userInfo.getStaffid());
		spaceManager.setPhone(userInfo.getMobile());
		return updateById(spaceManager);
	}

	/**
	 * 删除空间责任人.
	 * @Param id 空间责任人标识
	 * @Return 删除空间责任人是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		SpaceManager spaceManager = this.getById(id);
		AssertUtils.notNull(spaceManager, SystemResultCode.RESULT_DATA_NONE.message());
		return this.removeById(id);
	}
}
