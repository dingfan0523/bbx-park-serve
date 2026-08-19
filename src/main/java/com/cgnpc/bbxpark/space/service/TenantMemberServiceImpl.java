
package com.cgnpc.bbxpark.space.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.domain.TenantMember;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.model.TenantMemberModel;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberAddParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberPageParam;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberParam;
import com.cgnpc.bbxpark.space.mapper.TenantInfoRepository;
import com.cgnpc.bbxpark.space.mapper.TenantMemberRepository;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import com.cgnpc.pro.api.ICudUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TenantMemberServiceImpl extends BaseServiceImpl<TenantMemberRepository, TenantMember> implements ITenantMemberService {

    private static final Map<String, String> sexMap = new HashMap<>();
    /**
     * 注入repository.
     */
    @Autowired
    private TenantMemberRepository tenantMemberRepository;

//	@Autowired
//	private UserInfoRepository userInfoRepository;

    @Autowired
    private TenantInfoRepository tenantInfoRepository;

    @Autowired
    private ITenantInfoService tenantInfoService;

//    @Autowired
//	private DepartmentMemberRepository departmentMemberRepository;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private ICudUserService cudUserService;
//    @Autowired
//    private ICudUserInfoService cudUserInfoService;

    static {
        sexMap.put("0", "未知");
        sexMap.put("1", "男");
        sexMap.put("2", "女");
        sexMap.put("3", "保密");
        sexMap.put("未知", "0");
        sexMap.put("男", "1");
        sexMap.put("女", "2");
        sexMap.put("保密", "3");
    }

    /**
     * 根据租户成员标识获得租户成员详情信息.
     *
     * @Param [id] 租户成员标识
     * @Return 租户成员详情信息
     */
    @Override
    public TenantMemberDomain detail(Long id) {
        TenantMember tenantMember = this.getById(id);
        if (tenantMember != null) {
            TenantMemberDomain tenantMemberDomain = BeanUtils.convertTo(tenantMember, TenantMemberDomain::new);
            UserInfoModel userInfo = userApiService.getByStaffNo(tenantMemberDomain.getUserId());
//            CudUserInfoVO userInfo = cudUserService.getUsersInfo(tenantMemberDomain.getStaffNo());
//			UserInfo userInfo = userInfoRepository.get(tenantMemberDomain.getUserId());
//			tenantMemberDomain.setUserAvatar(userInfo.getAvatar());
            tenantMemberDomain.setUserName(userInfo.getUserName());
//			tenantMemberDomain.setNickName(userInfo.getNickName());
            return tenantMemberDomain;
        }
        return null;
    }


    /**
     * 获取租户成员列表(分页).
     *
     * @Param param 租户成员查询条件
     * @Return 租户成员信息列表（分页）
     */
    @Override
    public IPage<TenantMemberDomain> page(TenantMemberPageParam param) {
        IPage<TenantMember> tenantMemberResult = this.page(new Page<>(param.getCurrent(),param.getSize()),Wrappers.<TenantMember>lambdaQuery().eq(TenantMember::getTenantId, WebFrameworkUtils.getHeaderTenantId()));
		List<TenantMemberDomain> memberList = BeanUtils.convertListTo(tenantMemberResult.getRecords(),TenantMemberDomain::new);
        if (CollectionUtils.isNotEmpty(memberList)) {
			List<String> userIds = memberList.stream().map(TenantMemberDomain::getUserId).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(userIds)) {
                List<UserInfoModel> userInfos = userApiService.getByStaffNos(userIds);

				Map<String, UserInfoModel> userIdMap = userInfos.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo,
						Function.identity(),(v1,v2)->v2));
				for (TenantMemberDomain tenantMemberDomain : memberList) {
                    UserInfoModel userInfo = userIdMap.get(tenantMemberDomain.getUserId());
					if (userInfo != null) {
						tenantMemberDomain.setUserName(userInfo.getUserName());
                        tenantMemberDomain.setDepartmentId(userInfo.getDepartmentId());
                        tenantMemberDomain.setDepartmentName(userInfo.getDepartmentName());
					}

				}
			}
		}
		return ConvertUtil.pageConvert(tenantMemberResult,memberList);
    }

    /**
     * 获取租户成员列表.
     *
     * @Param param 租户成员查询条件
     * @Return 租户成员信息列表
     */
    @Override
    public IPage<TenantMemberDomain> listPage(TenantMemberListParam param) {
        IPage<TenantMemberModel> pageResult = tenantMemberRepository.listPage(new Page<>(param.getCurrent(),param.getSize()), param);
        List<TenantMemberDomain> tenantMemberDomains = BeanUtils.convertListTo(pageResult.getRecords(), TenantMemberDomain::new);
        if (CollectionUtils.isNotEmpty(tenantMemberDomains)) {
            List<String> userIds = tenantMemberDomains.stream().map(TenantMemberDomain::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userIds)) {
                //查找user信息
                List<UserInfoModel> userInfos = userApiService.getByStaffNos(userIds);
                Map<String, UserInfoModel> userIdMap = userInfos.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo, Function.identity(), (v1, v2) -> v2));
                //查找tenant信息
                List<Long> tenantIdList = tenantMemberDomains.stream().map(TenantMemberDomain::getTenantId).distinct().collect(Collectors.toList());
                List<TenantInfo> tenantInfos = tenantInfoRepository.selectList(Wrappers.<TenantInfo>lambdaQuery().in(TenantInfo::getId, tenantIdList));
                //tenantInfoRepository.selectList(new WhereClause().addIn(TenantInfoExt.C_ID, tenantIdList));
                if (tenantInfos == null) {
                    tenantInfos = new ArrayList<>();
                }
                Map<Long, TenantInfo> tenantInfoMap = tenantInfos.stream().collect(Collectors.toMap(TenantInfo::getId, Function.identity(), (v1, v2) -> v2));
                for (Iterator<TenantMemberDomain> iterator = tenantMemberDomains.iterator(); iterator.hasNext(); ) {
                    TenantMemberDomain tenantMemberDomain = iterator.next();
                    UserInfoModel userInfo = userIdMap.get(tenantMemberDomain.getUserId());
                    if (userInfo != null) {
                        tenantMemberDomain.setSexLabel(sexMap.get(userInfo.getSexDesc() != null ? userInfo.getSexDesc().toString() : ""));
                        tenantMemberDomain.setUserName(userInfo.getUserName());
                        tenantMemberDomain.setStaffid(userInfo.getStaffNo());
                        tenantMemberDomain.setDepartmentId(userInfo.getDepartmentId());
                        tenantMemberDomain.setDepartmentName(userInfo.getDepartmentName());
                    }
                    TenantInfo tenantInfo = tenantInfoMap.get(tenantMemberDomain.getTenantId());
                    if (tenantInfo != null) {
                        tenantMemberDomain.setTenantName(tenantInfo.getName());
                    } else {
                        // 禁用或者移除的租户，不返回
                        iterator.remove();
                    }
                }
            }
        }
        return ConvertUtil.pageConvert(pageResult, tenantMemberDomains);
    }

    /**
     * 获取租户成员列表.
     *
     * @Param param 租户成员查询条件
     * @Return 租户成员信息列表
     */
    @Override
    public List<TenantMemberDomain> list(TenantMemberListParam param) {
        TenantMember tenantMember = BeanUtils.convertTo(param, TenantMember::new);
        //todo:筛选
        List<TenantMember> tenantMemberList = this.list(Wrappers.<TenantMember>lambdaQuery()
                .eq(param.getTenantId() != null, TenantMember::getTenantId, param.getTenantId())
                .eq(param.getUserId() != null,TenantMember::getUserId,param.getUserId()));
        List<TenantMemberDomain> tenantMemberDomains = BeanUtils.convertListTo(tenantMemberList, TenantMemberDomain::new);
        if (CollectionUtils.isNotEmpty(tenantMemberDomains)) {
            List<String> userIds = tenantMemberDomains.stream().map(TenantMemberDomain::getUserId).collect(Collectors.toList());
//            List<String> staffNos = tenantMemberDomains.stream().map(TenantMemberDomain::getStaffNo).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userIds)) {
                //查找user信息

                List<UserInfoModel> userInfos = userApiService.getByStaffNos(userIds);
                Map<String, UserInfoModel> userIdMap = userInfos.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo, Function.identity(), (v1, v2) -> v2));
                //查找tenant信息
                List<Long> tenantIdList = tenantMemberDomains.stream().map(TenantMemberDomain::getTenantId).distinct().collect(Collectors.toList());
//				List<TenantInfo> tenantInfos = tenantInfoRepository.selectList(new WhereClause().addIn(TenantInfoExt.C_ID, tenantIdList).addEq(TenantInfoExt.C_STATUS, Status.enabled.getKey()));
                List<TenantInfo> tenantInfos = tenantInfoRepository.selectList(Wrappers.<TenantInfo>lambdaQuery().in(TenantInfo::getId, tenantIdList).eq(TenantInfo::getStatus, Status.enabled.getKey()));
                if (tenantInfos == null) {
                    tenantInfos = new ArrayList<>();
                }
                Map<Long, TenantInfo> tenantInfoMap = tenantInfos.stream().collect(Collectors.toMap(TenantInfo::getId, Function.identity(), (v1, v2) -> v2));
                for (Iterator<TenantMemberDomain> iterator = tenantMemberDomains.iterator(); iterator.hasNext(); ) {
                    TenantMemberDomain tenantMemberDomain = iterator.next();
                    UserInfoModel userInfo = userIdMap.get(tenantMemberDomain.getUserId());
                    if (userInfo != null) {
//						tenantMemberDomain.setUserAvatar(userInfo.getAvatar());
                        tenantMemberDomain.setUserName(userInfo.getUserName());
//						tenantMemberDomain.setNickName(userInfo.getNickName());
                    }
                    TenantInfo tenantInfo = tenantInfoMap.get(tenantMemberDomain.getTenantId());
                    if (tenantInfo != null) {
                        tenantMemberDomain.setTenantName(tenantInfo.getName());
                    } else {
                        // 禁用或者移除的租户，不返回
                        iterator.remove();
                    }
                }
            }
        }
        return tenantMemberDomains;
    }

    /**
     * 新增租户成员.
     *
     * @Param param 租户成员信息
     * @Return 新增租户成员是否成功
     */
    @Override
    public Boolean add(TenantMemberParam param) {
        TenantInfo tenantInfo = tenantInfoService.getById(param.getTenantId());
        AssertUtils.notNull(tenantInfo, SystemResultCode.RESULT_DATA_NONE);
//		UserInfo userInfo = userInfoRepository.get(param.getUserId());
        UserInfoModel userInfo = userApiService.getByStaffNo(param.getUserId());
        AssertUtils.notNull(userInfo, SystemResultCode.RESULT_DATA_NONE);
//		AssertUtils.isTrue(userInfo.getStatus() != 2, new CustomMessageResultCode(SystemResultCode.FAIL, "注销用户无法新增"));

        TenantMember tenantMember = BeanUtils.convertTo(param, TenantMember::new);
        tenantMemberRepository.insert(tenantMember);
        return true;
    }

    /**
     * 批量新增租户成员.
     *
     * @Param params 租户成员信息列表
     * @Return 批量新增租户成员是否成功
     */
    @Override
    public Boolean addBatch(TenantMemberAddParam param) {
        //租户校验
        TenantInfo tenantInfo = tenantInfoRepository.selectById(param.getTenantId());
        AssertUtils.notNull(tenantInfo, SystemResultCode.RESULT_DATA_NONE);

		List<String> userIds = param.getUserIds();
        List<TenantMember> tenantMembers = tenantMemberRepository.selectList(Wrappers.<TenantMember>lambdaQuery()
                .in(TenantMember::getUserId, userIds).eq(TenantMember::getTenantId, param.getTenantId()));
        // 获取已经存在的用户 ID 集合
		Set<String> existingUserIds = tenantMembers.stream().map(TenantMember::getUserId).collect(Collectors.toSet());
        // 过滤掉已经存在的用户 ID
		userIds = userIds.stream().filter(userId -> !existingUserIds.contains(userId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserInfoModel> users = userApiService.getByStaffNos(userIds);
            Map<String,String> userNameMap = CollectionUtils.isEmpty(users) ? new HashMap<>(4) :
                    users.stream().collect(Collectors.toMap(UserInfoModel::getStaffNo,UserInfoModel::getUserName,(v1,v2)->v2));

            List<TenantMember> tenantMemberList = userIds.stream().map(e -> {
                TenantMember tenantMember = new TenantMember();
                tenantMember.setTenantId(param.getTenantId());
                tenantMember.setStatus(0);
                tenantMember.setIdentity(2);
				tenantMember.setUserId(e);
                tenantMember.setStaffName(userNameMap.get(e));
                tenantMember.setCreateTime(new Date());
                tenantMember.setUpdateTime(new Date());
                return tenantMember;
            }).collect(Collectors.toList());
            this.saveBatch(tenantMemberList);
        }
        return true;
    }


    /**
     * 删除租户成员.
     *
     * @Param id 租户成员标识
     * @Return 删除租户成员是否成功
     */
    @Override
    public Boolean remove(Long id) {
        return this.removeById(id);
    }

    /**
     * 批量删除租户成员.
     *
     * @Param ids 租户成员标识列表
     * @Return 批量删除租户成员是否成功
     */
    @Override
    public Boolean removeBatch(List<Long> ids) {
        List<TenantMember> list = this.list(Wrappers.<TenantMember>lambdaQuery().in(TenantMember::getId, ids));
        this.removeByIds(list.stream().map(TenantMember::getId).collect(Collectors.toList()));
        // todo  同时删除  租户下 角色分配的当前人员信息
        // 1. 根据 ids 从 uic_tenant_member 找到 当前租户 和当前要删除的userId
        // 2. 根据 租户id 找到 当前租户的所有租户权限
        // 3. 根据 租户权限列表 移除 第一步查找到的人
        remoteRoleUser(list);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remoteRoleUser(List<TenantMember> list) {
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        for (TenantMember tenantMember : list) {
            Long tenantId = tenantMember.getTenantId();
            if (tenantId != null) {
                //todo:清理当前租户下多余(非当前租户成员)的用户-角色关联信息
//				tenantMemberRepository.removeNoMemberRoleUser(tenantId);
            }
        }
        return true;
    }

    /**
     * 编辑租户成员信息.
     *
     * @Param param 租户成员信息
     * @Return 编辑租户成员是否成功
     */
    @Override
    public Boolean edit(Long id, TenantMemberParam param) {
        TenantMember tenantMember = BeanUtils.convertTo(param, TenantMember::new);
        //所属分组不能编辑
        tenantMember.setTenantId(null);
//        tenantMember.setModifyUserNo(null);
        tenantMember.setCreateTime(new Date());
        tenantMember.setUpdateTime(null);
//        tenantMember.setCreateUserNo(null);
        return this.updateById(tenantMember);
    }


    /**
     * 批量启用租户成员信息.
     *
     * @Param ids 租户成员标识列表
     * @Return 批量启用租户成员是否成功
     */
    @Override
    public Boolean enableBatch(List<Long> ids) {
        List<TenantMember> tenantMemberList = ids.stream().map(e -> {
            TenantMember tenantMember = new TenantMember();
            tenantMember.setId(e);
            tenantMember.setStatus(Status.enabled.getKey());
            return tenantMember;
        }).collect(Collectors.toList());
        return this.updateBatchById(tenantMemberList);
    }

    /**
     * 批量禁用租户成员信息.
     *
     * @Param ids 租户成员标识列表
     * @Return 批量禁用租户成员是否成功
     */
    @Override
    public Boolean disableBatch(List<Long> ids) {
        List<TenantMember> tenantMemberList = ids.stream().map(e -> {
            TenantMember tenantMember = new TenantMember();
            tenantMember.setId(e);
            tenantMember.setStatus(Status.disabled.getKey());
            return tenantMember;
        }).collect(Collectors.toList());
        return this.updateBatchById(tenantMemberList);
    }

    @Override
    public Boolean assignAdmin(TenantMemberParam param) {
        return null;
    }
}
