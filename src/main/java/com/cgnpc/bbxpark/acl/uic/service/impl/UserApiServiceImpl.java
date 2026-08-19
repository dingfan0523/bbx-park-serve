package com.cgnpc.bbxpark.acl.uic.service.impl;

import cn.com.cgnpc.aep.bizcenter.usercenter.api.ImsUserCenterService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.acl.uic.model.StaffModel;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.acl.uic.utils.HttpUtil;
import com.cgnpc.bbxpark.common.utils.*;
import com.cgnpc.bbxpark.space.domain.TenantMember;
import com.cgnpc.bbxpark.space.dto.model.UserInfoModel;
import com.cgnpc.bbxpark.space.mapper.TenantMemberRepository;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.model.resdto.HrcenterDto;
import com.cgnpc.pro.model.respvo.CudUserCenterVO;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class UserApiServiceImpl implements IUserApiService {
    @Autowired(required = false)
    private ICudUserService cudUserService;
    @Autowired
    private HttpUtil httpUtil;

    private ImsUserCenterService userCenterService;

    @Autowired
    private TenantMemberRepository tenantMemberRepository;

    @Autowired
    private IDepartmentApiService departmentApiService;

    @Value("${hrcenter.department.cangnan:10010}")
    private String parentDepartmentId;

    /**
     * 组织部门级别id
     */
    private  final static String deptRankId = "001020";

    @Override
    public String getCurrentStaffNo() {
        return cudUserService.getUser();
    }

    @Override
    public String getCurrentStaffName() {
        return cudUserService.getNowUser(cudUserService.getUser());
    }

    @Override
    public UserInfoModel getCurrentUserInfo() {
        return convertToModel(cudUserService.getUsersInfo(getCurrentStaffNo()));
    }

    @Override
    public UserInfoModel getByStaffNo(String staffNo) {
        return convertToModel(cudUserService.getUsersInfo(staffNo));
    }


    public UserInfoModel detail(String staffNo){
        CudUserInfoVO usersInfo = cudUserService.getUsersInfo(staffNo);
        return convertToModel(usersInfo);
    }


    @Override
    public List<UserInfoModel> getByStaffNos(List<String> staffNos) {
        if(CollectionUtils.isEmpty(staffNos)){
            return Collections.emptyList();
        }
        HrcenterDto dto = new HrcenterDto();
        dto.setUserId(String.join(",",staffNos));
        return convertListToModel(cudUserService.getUserList(dto));
    }

    @Override
    public List<UserInfoModel> getStaffsByOrgId(String orgId, Integer pageIndex, Integer pageSize,String keyword) {
        List<StaffModel> list = getOriginalStaffsByOrgId(orgId,pageIndex,pageSize,keyword);
        return list.stream().map(vo->{
            UserInfoModel model = new UserInfoModel();
            model.setId(vo.getEmpId());
            model.setUserId(vo.getEmpId());
            model.setStaffid(vo.getEmpId());
            model.setStaffNo(vo.getEmpId());
            model.setUserName(vo.getEmpName());
            model.setSexDesc(vo.getStaffSex());
            model.setDepartmentId(vo.getWorkDeptId());
            model.setDepartmentName(vo.getWorkDeptName());
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public List<StaffModel> getOriginalStaffsByOrgId(String orgId, Integer pageIndex, Integer pageSize, String keyword) {
        HrcenterDto dto = new HrcenterDto();
        dto.setOrgId(orgId);
        dto.setKeyword(keyword);
        dto.setPageNum(pageIndex);
        dto.setPageSize(pageSize);
        List<CudUserCenterVO> list = cudUserService.getStaffsByOrgId(dto);
        if(CollectionUtils.isEmpty(list) || pageIndex < 1 || pageSize < 1){
            return Collections.emptyList();
        }
        int formIndex = (pageIndex - 1) * pageSize;
        if(formIndex >= list.size()){
            return Collections.emptyList();
        }
        int toIndex = Math.min(formIndex + pageSize,list.size());
        List<CudUserCenterVO> users = new ArrayList<>(list.subList(formIndex,toIndex));
        return JSONUtil.toList(JSONUtil.parseArray(users), StaffModel.class);
    }

    @Override
    public Boolean parkAdmin() {
        Integer count = tenantMemberRepository.selectCount(Wrappers.<TenantMember>lambdaQuery().eq(TenantMember::getTenantId,
                        WebFrameworkUtils.getHeaderTenantId())
                .eq(TenantMember::getUserId, WebFrameworkUtils.getHeaderUserId())
                .eq(TenantMember::getIdentity, 1));
        return count > 0;
    }

    @Override
    public UserInfoModel getSecondDeptByStaffNo(String staffNo) {
        UserInfoModel user = this.getByStaffNo(staffNo);
        //二级部门
        if(ObjectUtil.isNotEmpty(user.getDepartmentIdPath())){
            findSecondDepart(user);
        }
        return user;
    }

    private void findSecondDepart(UserInfoModel user){
        List<String> ids = Arrays.stream(user.getDepartmentIdPath().split("\\\\")).collect(Collectors.toList());
        List<OrgDepartmentNode> orgDepartmentNodes = departmentApiService.getOrgsByOrgIds(new HashSet<>(ids));
        if(CollectionUtil.isEmpty(orgDepartmentNodes)){
            return;
        }
        Map<String, OrgDepartmentNode> orgDepartMap = orgDepartmentNodes.stream().sorted(Comparator.comparing(OrgDepartmentNode::getDeptNo))
                .collect(Collectors.toMap(OrgDepartmentNode::getDeptNo, Function.identity(),(k1,k2)->k1, LinkedHashMap::new));
        for (String id : ids){
            OrgDepartmentNode node = orgDepartMap.get(id);
            if(deptRankId.equals(node.getDeptRank())){
                user.setDepartmentSecondId(node.getDeptNo());
                user.setDepartmentSecondName(node.getDeptName());
                break;
            }
        }
    }

    /**
     * 用户信息转换
     * @param vo cud用户vo
     * @return 用户model
     */
    private UserInfoModel convertToModel(CudUserInfoVO vo){
        UserInfoModel model = new UserInfoModel();
        model.setId(vo.getUserId());
        model.setUserId(vo.getUserId());
        model.setStaffid(vo.getUserId());
        model.setStaffNo(vo.getUserId());
        model.setSexDesc(vo.getUserSex());
        model.setUserName(StringUtils.isEmpty(vo.getNowUserName()) ? vo.getUserName() : vo.getNowUserName());
        model.setMobile(vo.getCellphoneNo());
        model.setTelephone(vo.getTelephoneNo());
        model.setDepartmentId(vo.getWorkDeptId());
        model.setDepartmentName(vo.getWorkDeptName());
        model.setDepartmentIdPath(vo.getUserDeptIdPath());
        model.setDepartmentNamePath(vo.getUserDeptNamePath());
        return model;
    }

    /**
     * 用户信息转换
     * @param voList cud用户vo集合
     * @return 用户model集合
     */
    private List<UserInfoModel> convertListToModel(List<CudUserInfoVO> voList){
        return voList.stream().map(this::convertToModel).collect(Collectors.toList());
    }
}
