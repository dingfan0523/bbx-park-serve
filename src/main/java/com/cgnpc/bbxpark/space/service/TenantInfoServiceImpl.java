package com.cgnpc.bbxpark.space.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.base.CustomMessageResultCode;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.ConvertUtil;
import com.cgnpc.bbxpark.space.domain.TenantDomain;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.domain.TenantMember;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModel;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModelExt;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.TenantInfoListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantInfoParam;
import com.cgnpc.bbxpark.space.dto.param.TenantPageParam;
import com.cgnpc.bbxpark.space.mapper.TenantInfoRepository;
import com.cgnpc.bbxpark.space.mapper.TenantMemberRepository;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import com.cgnpc.pro.api.ICudUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class TenantInfoServiceImpl extends BaseServiceImpl<TenantInfoRepository, TenantInfo> implements ITenantInfoService {
	private static final Logger log = LoggerFactory.getLogger(TenantInfoServiceImpl.class);
	/**
     * 注入repository.
     */
//	@Autowired
//	private TenantInfoRepository tenantInfoRepository;
//
//	@Autowired
//	private TenantMemberRepository tenantMemberRepository;
//
//	@Autowired
//	private IPositionInfoService iPositionInfoService;
//
//	@Autowired
//	private PositionInfoRepository positionInfoRepository;
//
//	@Autowired
//	private TenantOrganizationService tenantOrganizationService;
//
//	@Autowired
//	private TenantDepartmentService tenantDepartmentService;
//
//	@Autowired
//	private TenantPermissionService tenantPermissionService;
//
//	@Autowired
//	private TenantMenuService tenantMenuService;
//
//	@Autowired
//	private PermissionRepository permissionRepository;
//
//	@Autowired
//	private UserInfoServiceImpl userInfoService;
//
//	@Autowired
//	private PermissionServiceImpl permissionService;
//	@Lazy
//	@Autowired
//	private IMenuInfoService menuInfoService;
    @Autowired
    private TenantInfoRepository tenantInfoRepository;
    @Autowired
    private TenantMemberRepository tenantMemberRepository;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private ICudUserService cudUserService;
    @Autowired
    private IRoleApiService roleApiService;
    @Value("${bbx.role.system:}")
    private String systemRoleCode;

	/**
	 * 根据租户信息标识获得租户信息详情信息.
	 * @Param [id] 租户信息标识
	 * @Return 租户信息详情信息
	 */
	@Override
	public TenantDomain detail(Long id) {
		TenantInfo tenantInfo = this.getById(id);
		TenantDomain tenantDomain = BeanUtils.convertTo(tenantInfo, TenantDomain::new);

		List<TenantMember> tenantMembers = tenantMemberRepository.selectList(Wrappers.<TenantMember>lambdaQuery()
                .eq(TenantMember::getTenantId,id).eq(TenantMember::getIdentity,1));
		List<String> staffNos = tenantMembers.stream().map(TenantMember::getUserId).collect(Collectors.toList());
		tenantDomain.setStaffNos(staffNos);
		if (CollectionUtils.isEmpty(staffNos)) {
			tenantDomain.setAdminUserList(new ArrayList<>());
		} else {
            List<UserInfoModel> staffs = userApiService.getByStaffNos(staffNos);
//            List<CudUserInfo> staffs = cudUserInfoService.list(Wrappers.<CudUserInfo>lambdaQuery().in(CudUserInfo::getId,adminIdList));
            List<UserInfoModel> convert = staffs.stream().map(staff->{
                UserInfoModel model = new UserInfoModel();
                model.setStaffid(staff.getStaffNo());
                model.setUserName(staff.getUserName());
                model.setUserAccount(staff.getStaffNo());
                model.setSexDesc(staff.getSexDesc());
                model.setNamePinyin(staff.getNamePinyin());
                return model;
            }).collect(Collectors.toList());
			tenantDomain.setAdminUserList(convert);
		}
		return tenantDomain;
	}
	/**
	 * 获取租户信息列表(分页).
	 *
	 * @Param param 租户信息查询条件
	 * @Return 租户信息列表（分页）
	 */
	@Override
	public IPage<TenantInfoModelExt> pageFullInfo(TenantPageParam param) {
        UserInfoModel userInfo = userApiService.getCurrentUserInfo();
        if(userInfo != null && !verifySystemRole(userInfo)){
			// 不是超级管理员
//			param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            param.setLoginStaffNo(userInfo.getId());
		}
//		PaginationEntity<TenantPageParam> pageEntity = new PaginationEntity(param);
//		pageEntity.setOrderColumn(TenantInfoExt.C_CREATE_TIME);
//		pageEntity.setOrderTurn("DESC");
//		IPage<TenantInfoModelExt> convert = tenantInfoRepository.pageFullInfo(pageEntity).convert(TenantInfoModelExt.class);
//		// 拿到所有租户id
//		List<TenantInfoModelExt> list = convert.getResult();
        IPage<TenantInfo> tenantPage = tenantInfoRepository.pageFullInfo(new Page<>(param.getCurrent(),param.getSize()),param);

		if(CollectionUtils.isEmpty(tenantPage.getRecords())){
			return ConvertUtil.pageEmptyConvert(param.getCurrent(),param.getSize());
		}
        List<TenantInfoModelExt> list = BeanUtils.convertListTo(tenantPage.getRecords(),TenantInfoModelExt::new);
        IPage<TenantInfoModelExt> convert = ConvertUtil.pageConvert(tenantPage,list);

		List<Long> tenantIdList  = list.stream().map(TenantInfoModel::getId).collect(Collectors.toList());
		// 查找列表租户的管理员
		List<TenantMember> tenantMemberList = tenantMemberRepository.selectList(Wrappers.<TenantMember>lambdaQuery().in(TenantMember::getTenantId,tenantIdList).eq(TenantMember::getIdentity,1));
        //tenantMemberRepository.selectList(new WhereClause().addIn("tenant_id", tenantIdList).addEq("identity",1));
        if(tenantMemberList.isEmpty()){
			return convert;
		}
        List<String> staffNos = tenantMemberList.stream().map(TenantMember::getUserId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(staffNos)){
            return convert;
        }
//		List<String> userIdList = tenantMemberList.stream().map(e->e.getUserId()).collect(Collectors.toList());
//		if(userIdList.isEmpty()){
//			return convert;
//		}
//		List<String> columnsList = new ArrayList<>();
//		columnsList.add(UserInfoExt.C_ID);
//		columnsList.add(UserInfoExt.C_USER_NAME);
//		columnsList.add(UserInfoExt.C_NICK_NAME);
//		List<UserInfo> userInfoList = userInfoService.selectList(columnsList, new WhereClause().addIn(UserInfoExt.C_ID, userIdList));
//        List<CudUserInfo> staffs = cudUserInfoService.list(Wrappers.<CudUserInfo>lambdaQuery().in(CudUserInfo::getId,adminIdList));
        List<UserInfoModel> staffs = userApiService.getByStaffNos(staffNos);
        if(!CollectionUtils.isEmpty(staffs)){
			buildManagerInfo(list, tenantMemberList, staffs);
		}
		return convert;
	}

	private void buildManagerInfo(List<TenantInfoModelExt> list, List<TenantMember> tenantMemberList, List<UserInfoModel> userInfoList){
        Map<String,UserInfoModel> userMap = userInfoList.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo,u->u));
		Map<Long,List<String>> tenantMap = tenantMemberList.stream().collect(Collectors.groupingBy(TenantMember::getTenantId,Collectors.mapping(TenantMember::getUserId,Collectors.toList())));
        for(TenantInfoModelExt ext : list){
            if(tenantMap.containsKey(ext.getId())){
                List<String> userIds = tenantMap.get(ext.getId());
                List<TenantInfoModelExt.Manager> managers = userIds.stream().filter(userMap::containsKey).map(userId->{
                    TenantInfoModelExt.Manager manager = new TenantInfoModelExt.Manager();
                    UserInfoModel userInfo = userMap.get(userId);
                    manager.setManagerStaffNo(userInfo.getStaffNo());
                    manager.setManagerUserId(userInfo.getStaffNo());
                    manager.setManagerUserName(userInfo.getUserName());
                    return manager;
                }).collect(Collectors.toList());
                ext.setManagers(managers);
            }
		}
	}
//	private UserInfo getUserInfo(List<UserInfo> userInfoList, String userId){
//		for(UserInfo ext : userInfoList){
//			if(ext.getId().equals(userId))
//			return ext;
//		}
//		return null;
//	}
	/**
	 * 获取租户信息列表(分页).
	 *
	 * @Param param 租户信息查询条件
	 * @Return 租户信息列表（分页）
	 */
	@Override
	public IPage<TenantInfoModel> page(TenantPageParam param) {
        //todo:筛选
        IPage<TenantInfo> result = this.page(new Page<>(param.getCurrent(),param.getSize()),Wrappers.<TenantInfo>lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCode()),TenantInfo::getCode,param.getCode()));
        List<TenantInfoModel> list = BeanUtils.convertListTo(result.getRecords(),TenantInfoModel::new);
        return ConvertUtil.pageConvert(result,list);
	}

	/**
	 * 获取租户信息列表.
	 * @Param param 租户信息查询条件
	 * @Return 租户信息列表
	 */
	@Override
	public List<TenantInfoModel> list(TenantInfoListParam param) {
		TenantInfo tenantInfo = BeanUtils.convertTo(param, TenantInfo::new);
        //todo:筛选
        List<TenantInfo> list = this.list(Wrappers.<TenantInfo>lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getCode()),TenantInfo::getCode,param.getCode()));
        return BeanUtils.convertListTo(list,TenantInfoModel::new);
	}

	/**
	 * 新增租户信息.
	 * @Param param 租户信息
	 * @Return 新增租户信息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public TenantInfoModel add(TenantInfoParam param) {
		checkCodeUnique(param.getCode());
		checkNameUnique(param.getName());
		TenantInfo tenantInfo = BeanUtils.convertTo(param, TenantInfo::new);
        tenantInfo.setCreatorId(userApiService.getCurrentStaffNo());
        tenantInfo.setCreateTime(new Date());
        this.save(tenantInfo);
        if(!CollectionUtils.isEmpty(param.getAdminUserIdList())){
            assignAdmin(param.getAdminUserIdList(), tenantInfo.getId());
        }
		return BeanUtils.convertTo(tenantInfo,TenantInfoModel::new);
	}

	private void assignAdmin(List<String> adminIds, Long tenantId) {
        //删除租户下的租户管理员
        tenantMemberRepository.delete(Wrappers.<TenantMember>lambdaQuery()
                .eq(TenantMember::getTenantId,tenantId).eq(TenantMember::getIdentity,1));
        //删除租户下的成员信息
        tenantMemberRepository.delete(Wrappers.<TenantMember>lambdaQuery()
                .in(TenantMember::getUserId,adminIds).eq(TenantMember::getTenantId,tenantId));

		List<TenantMember> tenantMemberList = Optional.ofNullable(adminIds)
				.orElse(new ArrayList<>())
				.stream()
				.map(e -> {
					TenantMember tenantMember = new TenantMember();
					tenantMember.setTenantId(tenantId);
					tenantMember.setUserId(e);
					tenantMember.setStatus(0);
					tenantMember.setIdentity(1);
					return tenantMember;
				})
				.collect(Collectors.toList());
        tenantMemberList.forEach(tenantMember -> tenantMemberRepository.insert(tenantMember));
	}

	private void checkCodeUnique(String code) {
		List<TenantInfo> tenantInfos = this.list(Wrappers.<TenantInfo>lambdaQuery().eq(TenantInfo::getCode,code));

		AssertUtils.isTrue(CollectionUtils.isEmpty(tenantInfos),
				new CustomMessageResultCode(SystemResultCode.DATA_ALREADY_EXISTED, "编码已存在"));
	}

	private void checkNameUnique(String name) {
		List<TenantInfo> tenantInfos = this.list(Wrappers.<TenantInfo>lambdaQuery().eq(TenantInfo::getName,name));
		AssertUtils.isTrue(CollectionUtils.isEmpty(tenantInfos),
				new CustomMessageResultCode(SystemResultCode.DATA_ALREADY_EXISTED, "名称已存在"));
	}

	/**
	 * 删除租户信息.
	 * @Param id 租户信息标识
	 * @Return 删除租户信息是否成功
	 */
	@Override
	public Boolean remove(Long id) {
		//存在租户成员则不允许删除
		List<TenantMember> tenantMembers = tenantMemberRepository.selectList(Wrappers.<TenantMember>lambdaQuery().eq(TenantMember::getTenantId,id).ne(TenantMember::getIdentity,1));
		AssertUtils.isEmpty(tenantMembers, SystemResultCode.TENANT_HAS_MEMBER_CAN_NOT_CANCEL);
		//删除对应的职位信息
	/*	List<PositionInfo> positionInfos = positionInfoRepository.selectList(new WhereClause().addEq(PositionInfoExt.C_TENANT_ID, id));
		List<Long> positionIdList = positionInfos.stream().map(PositionInfo::getId).collect(Collectors.toList());
		iPositionInfoService.removeBatch(positionIdList);
		*/
        return this.removeById(id);
	}

	/**
	 * 编辑租户信息.
	 *
	 * @Param param 租户信息
	 * @Return 编辑租户信息是否成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean edit(Long id, TenantInfoParam param) {
		TenantInfo tenant = this.getById(param.getId());
		TenantInfo tenantInfo = BeanUtils.convertTo(param, TenantInfo::new);
		if (StringUtils.isNotBlank(tenantInfo.getCode()) && !tenant.getCode().equals(tenantInfo.getCode())) {
			checkCodeUnique(tenantInfo.getCode());
		}
		if (StringUtils.isNotBlank(tenantInfo.getName()) && !tenant.getName().equals(tenantInfo.getName())) {
			checkNameUnique(tenantInfo.getName());
		}
        tenantInfo.setUpdatorId(userApiService.getCurrentStaffNo());
        tenantInfo.setUpdateTime(new Date());
        this.updateById(tenantInfo);
        if(!CollectionUtils.isEmpty(param.getAdminUserIdList())){
            assignAdmin(param.getAdminUserIdList(), tenantInfo.getId());
        }
		return true;
	}

	/**
	 * 启用租户信息.
	 * @Param id 租户信息标识
	 * @Return 启用租户信息是否成功
	 */
	@Override
	public Boolean enable(Long id) {
		TenantInfo tenantInfo = new TenantInfo();
		tenantInfo.setId(id);
		tenantInfo.setStatus(Status.enabled.getKey());
        return this.updateById(tenantInfo);
	}

	/**
	 * 禁用租户信息.
	 * @Param id 租户信息标识
	 * @Return 禁用租户信息是否成功
	 */
	@Override
	public Boolean disable(Long id) {
		TenantInfo tenantInfo = new TenantInfo();
		tenantInfo.setId(id);
		tenantInfo.setStatus(Status.disabled.getKey());
        return this.updateById(tenantInfo);
	}

//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean assignOrgDept(TenantAssignOrgDeptParam param) {
//
//		AssertUtils.notNull(param.getTenantId(), "tenantId不能为空");
//		/**
//		 * 1、写入部门id
//		 */
//		tenantDepartmentService.remove(new LambdaQueryWrapper<TenantDepartment>().eq(TenantDepartment::getTenantId,
//				param.getTenantId()
//		));
//		if (!CollectionUtils.isEmpty(param.getDeptIdList())) {
//			List<TenantDepartment> collect = param.getDeptIdList().stream().map(e -> {
//                TenantDepartment tenantDepartmentVO = new TenantDepartment();
//				tenantDepartmentVO.setTenantId(param.getTenantId());
//				tenantDepartmentVO.setDepartmentId(e);
//				return tenantDepartmentVO;
//			}).collect(Collectors.toList());
//			if (!CollectionUtils.isEmpty(collect)) {
//				tenantDepartmentService.saveBatch(collect);
//			}
//		}
//
//
//		/**
//		 * 2、写入组织id
//		 */
//		tenantOrganizationService.remove(new LambdaQueryWrapper<TenantOrganizationDO>().eq(TenantOrganizationDO::getTenantId,
//				param.getTenantId()
//		));
//		if (CollectionUtils.isNotEmpty(param.getOrgIdList())) {
//			List<TenantOrganizationModel> collect = param.getOrgIdList().stream().map(e -> {
//				TenantOrganizationModel tenantOrganizationVO = new TenantOrganizationModel();
//				tenantOrganizationVO.setTenantId(param.getTenantId());
//				tenantOrganizationVO.setOrganizationId(e);
//				return tenantOrganizationVO;
//			}).collect(Collectors.toList());
//			if (CollectionUtils.isNotEmpty(collect)) {
//				tenantOrganizationService.addBatch(collect);
//			}
//		}
//
//		return true;
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public Boolean assignPerm(TenantAssignPermParam param) {
//		AssertUtils.notNull(param.getTenantId(), "tenantId不能为空");
//
//
//		if (CollectionUtils.isNotEmpty(param.getPermIdList())) {
//			/**
//			 * 分配租户权限关联
//			 */
//			tenantPermissionService.remove(new LambdaQueryWrapper<TenantPermissionDO>().eq(TenantPermissionDO::getTenantId,
//					param.getTenantId()
//			));
//
//			List<TenantPermissionModel> collect = param.getPermIdList().stream().map(e -> {
//				TenantPermissionModel tenantPermissionVO = new TenantPermissionModel();
//				tenantPermissionVO.setTenantId(param.getTenantId());
//				tenantPermissionVO.setPermissionId(e);
//				return tenantPermissionVO;
//			}).collect(Collectors.toList());
//			if (CollectionUtils.isNotEmpty(collect)) {
//				tenantPermissionService.addBatch(collect);
//			}
//
//			/**
//			 * 分配租户菜单关联
//			 */
//			tenantMenuService.remove(new LambdaQueryWrapper<TenantMenuDO>().eq(TenantMenuDO::getTenantId,
//					param.getTenantId()
//			));
//			List<Permission> allPerm = permissionRepository.select();
//			List<TenantMenuModel> tenantMenuList = Optional.ofNullable(allPerm).orElse(new ArrayList<>()).stream()
//					.filter(e -> param.getPermIdList().contains(e.getId()))
//					.map(e -> {
//						TenantMenuModel tenantMenuVO = new TenantMenuModel();
//						tenantMenuVO.setMenuId(e.getMenuId());
//						tenantMenuVO.setTenantId(param.getTenantId());
//						return tenantMenuVO;
//					})
//					.collect(Collectors.toList());
//			if (CollectionUtils.isNotEmpty(tenantMenuList)) {
//				tenantMenuService.addBatch(tenantMenuList);
//			}
//		}
//
//		return true;
//	}
//
//	@Override
//	public Boolean assignMenu(TenantAssignMenuParam param) {
//		AssertUtils.notNull(param.getTenantId(), "tenantId不能为空");
//
////		tenantMenuService.remove(new LambdaQueryWrapper<TenantMenuDO>().eq(TenantMenuDO::getTenantId,
////				param.getTenantId()
////		));
////		tenantPermissionService.remove(new LambdaQueryWrapper<TenantPermissionDO>().eq(TenantPermissionDO::getTenantId,
////				param.getTenantId()
////		));
//		List<String> columnsList = new ArrayList<>();
//		columnsList.add(MenuInfoExt.C_ID);
//		columnsList.add(MenuInfoExt.C_SYSTEM_ID);
//		columnsList.add(MenuInfoExt.C_TENANT_ID);
//		ListResult<MenuInfo> menuInfos = menuInfoService.selectList(columnsList,
//				new WhereClause().addEq(MenuInfoExt.C_SYSTEM_ID, 1));
//
//		List<Long> menuIds = menuInfos.stream().map(MenuInfo::getId).collect(Collectors.toList());
//		if(menuIds.isEmpty()){
//			menuIds.add(-1L);
//		}
//		LambdaQueryWrapper<TenantMenuDO> query = new LambdaQueryWrapper();
//		query.eq(TenantMenuDO::getTenantId, param.getTenantId());
//		query.in(TenantMenuDO::getMenuId, menuIds);
//		List<TenantMenuDO> tenantMenuDOList = tenantMenuService.list(query);
//		log.info("获取到要删除的tenantMenuList = {}", JSON.toJSONString(tenantMenuDOList.stream().map(TenantMenuDO::getId).collect(Collectors.toList())));
//		if (CollectionUtils.isNotEmpty(tenantMenuDOList)) {
//			List<Long> tenantMenuIdList = tenantMenuDOList.stream().map(TenantMenuDO::getId).collect(Collectors.toList());
//			List<Long> menuIdList = tenantMenuDOList.stream().map(TenantMenuDO::getMenuId).collect(Collectors.toList());
//			tenantMenuService.remove(new LambdaQueryWrapper<TenantMenuDO>().eq(TenantMenuDO::getTenantId,
//					param.getTenantId()
//			).in(TenantMenuDO::getId, tenantMenuIdList));
//
//			List<Permission> permissionList = permissionRepository.selectList(new WhereClause().addIn(PermissionExt.C_MENU_ID,
//					menuIdList
//			));
//			List<Long> preIds = permissionList.stream().map(Permission::getId).collect(Collectors.toList());
//			tenantPermissionService.remove(new LambdaQueryWrapper<TenantPermissionDO>().eq(TenantPermissionDO::getTenantId,
//					param.getTenantId()
//			).in(TenantPermissionDO::getId, preIds));
//		}
//
//
//		if (CollectionUtils.isNotEmpty(param.getMenuIdList())) {
//
//			/**
//			 * 分配租户菜单关联
//			 */
//			List<TenantMenuModel> tenantMenu = param.getMenuIdList().stream().map(e -> {
//				TenantMenuModel tenantMenuModel = new TenantMenuModel();
//				tenantMenuModel.setMenuId(e);
//				tenantMenuModel.setTenantId(param.getTenantId());
//				return tenantMenuModel;
//			}).collect(Collectors.toList());
//			tenantMenuService.addBatch(tenantMenu);
//
//			/**
//			 * 分配租户权限关联
//			 */
//
//
//			List<Permission> permissions = permissionRepository.selectList(new WhereClause().addIn(PermissionExt.C_MENU_ID,
//					param.getMenuIdList()
//			));
//
//			if (CollectionUtils.isEmpty(permissions)) {
//				return true;
//			}
//			List<TenantPermissionModel> collect = permissions.stream().map(e -> {
//				TenantPermissionModel tenantPermissionModel = new TenantPermissionModel();
//				tenantPermissionModel.setPermissionId(e.getId());
//				tenantPermissionModel.setTenantId(param.getTenantId());
//				return tenantPermissionModel;
//			}).collect(Collectors.toList());
//			tenantPermissionService.addBatch(collect);
//		}
//
//		return true;
//	}

    /**
     * 校验用户是否为系统管理员角色
     *
     * @return 是否为系统管理员角色
     */
    private boolean verifySystemRole(UserInfoModel userInfo) {
        List<RoleModel> roles = roleApiService.getRoleByCurrent();
        List<String> roleCodes = roles.stream().map(RoleModel::getRoleCode).collect(Collectors.toList());
        return roleCodes.contains(systemRoleCode);
    }
}
