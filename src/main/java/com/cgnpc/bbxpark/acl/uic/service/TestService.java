package com.cgnpc.bbxpark.acl.uic.service;


import com.cgnpc.bbxpark.acl.iot.service.IotCapacityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class TestService {
    @Autowired
    private IDepartmentApiService departmentApiService;
    @Autowired
    private IUserApiService userApiService;
//    @Autowired
//    private ICudUserInfoService userInfoService;
//    @Autowired
//    private ICudDepartmentService departmentService;
//    @Autowired
//    private IDepartmentMemberService departmentMemberService;
    @Autowired
    private IRoleApiService roleApiService;
    @Autowired
    private IotCapacityService iotCapacityService;
    @Autowired
    private IUserPermissionApiService userPermissionApiService;
    @PostConstruct
    public void syncDepartment(){
//        List<UserPermissionModel> list = userPermissionApiService.getPermissionByStaffNo("P309150");
//        log.info("权限集合:{}",list);
//        List<UserInfoModel> staffs = userApiService.getStaffsByOrgId("50307683",1L,100L);
//        System.out.printf("sss");
//        OrgDepartmentNode dept = departmentApiService.getOrgByNo("00888888");
//        OrgDepartmentNode dept2 = departmentApiService.getOrgByNo("50307683");
//        roleApiService.findUserByRole("R_ZHGL");
//        //中广核一级
//        List<OrgDepartmentNode> departments = departmentApiService.getOrgTreeForOrgWidget("00888888");
//        System.out.println(departments);
//        //第二级
//        List<OrgDepartmentNode> departments2 = departmentApiService.getOrgTreeForOrgWidget("50259024");
//        saveDepartment(departments2);
//        //综合管理部
//        List<OrgDepartmentNode> departments3 = departmentApiService.getOrgTreeForOrgWidget("50307683");
//        saveDepartment(departments3);
//        int current = 1;
//        List<DepartStaffModel> users = userApiService.getStaffInfoByDeptInfoWithPager("50307683",current,20);
//        saveUsers(users);
    }

//    public void saveDepartment(List<OrgDepartmentNode> departs){
//        List<CudDepartment> list = BeanUtils.convertListTo(departs,CudDepartment::new);
//        departmentService.saveBatch(list);
//    }
//
//    public void saveUsers(List<DepartStaffModel> users){
//        List<CudUserInfo> list = BeanUtils.convertListTo(users,CudUserInfo::new);
//        userInfoService.saveBatch(list);
//        List<CudDepartment> departments = departmentService.list(Wrappers.emptyWrapper());
//        Map<String,Long> map = departments.stream().collect(Collectors.toMap(CudDepartment::getDeptNo,CudDepartment::getId,(v1, v2)->v1));
//
//        List<DepartmentMember> members = list.stream().map(user->{
//            DepartmentMember member = new DepartmentMember();
//            member.setUserId(user.getId());
//            member.setDepartmentId(map.getOrDefault(user.getDeptNo(),0L));
//            return member;
//        }).collect(Collectors.toList());
//        departmentMemberService.saveBatch(members);
//    }
}
