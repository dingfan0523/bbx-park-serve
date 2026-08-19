
package com.cgnpc.bbxpark.space.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.Status;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.space.domain.ParkSpace;
import com.cgnpc.bbxpark.space.domain.TenantMemberDomain;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFullModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceTreeModel;
import com.cgnpc.bbxpark.space.dto.model.TenantInfoModel;
import com.cgnpc.bbxpark.space.dto.param.*;
import com.cgnpc.bbxpark.space.mapper.ParkSpaceRepository;
import com.cgnpc.bbxpark.space.service.IParkSpaceService;
import com.cgnpc.bbxpark.space.service.ITenantInfoService;
import com.cgnpc.bbxpark.space.service.ITenantMemberService;
import com.cgnpc.cud.core.service.BaseServiceImpl;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ParkSpaceServiceImpl extends BaseServiceImpl<ParkSpaceRepository, ParkSpace> implements IParkSpaceService {

//    @Autowired
//    private TenantInfoFeignClient tenantInfoFeignClient;
//    @Autowired
//    private UserInfoFeignClient userInfoFeignClient;
//    @Autowired
//    private TenantMemberFeignClient tenantMemberFeignClient;
//    @Autowired
//    private UserSpaceServiceImpl userSpaceService;
//    @Autowired
//    private OrganizationInfoFeignClient organizationInfoFeignClient;
//    @Autowired
//    private DepartmentInfoFeignClient departmentInfoFeignClient;
    @Autowired
    private ICudUserService cudUserService;
    @Autowired
    private ITenantMemberService tenantMemberService;
    @Autowired
    private ITenantInfoService tenantInfoService;
    @Autowired
    private IDepartmentApiService departmentApiService;
    @Autowired
    private IUserApiService userApiService;
    @Autowired
    private IRoleApiService roleApiService;
    @Value("${bbx.role.system:}")
    private String systemRoleCode;

    @Override
    public List<ParkSpaceTreeModel> listByParentId(ParkSpaceListParam param) {
       return findTreeList(param);
    }

    /**
     * 获取园区空间树
     * 平台管理员可查全部
     * 园区管理员可查自己园区
     *
     * @param param 参数
     * @return 园区树
     */
    @Override
    public List<ParkSpaceTreeModel> findTreeList(ParkSpaceListParam param) {
        //权限校验
//        UserInfoModel userInfo = getUser();
        CudUserInfoVO userInfo = cudUserService.getUsersInfo(cudUserService.getUser());
        boolean isSystem = verifySystemRole(userInfo);
        AssertUtils.isTrue(isSystem || verifyTenantAdmin(findTenantIdByUserId()), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        //查询所有空间集合
        return buildTreeList(isSystem, WebFrameworkUtils.getHeaderTenantId(),param.getParentId(), param.getSpaceName());
    }

    /**
     * 获取园区空间树
     * 平台管理员可查全部
     * 否则可查自己园区
     *
     * @param param 参数
     * @return 园区树
     */
    @Override
    public List<ParkSpaceTreeModel> tree(ParkSpaceListParam param) {
        //权限校验
//        UserInfoModel userInfo = getUser();
        CudUserInfoVO userInfo = cudUserService.getUsersInfo(cudUserService.getUser());
        boolean isSystem = verifySystemRole(userInfo);
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        AssertUtils.isTrue(isSystem || tenantId != null, SystemResultCode.PERMISSION_UNAUTHORISE.message());
        //查询所有空间集合
        return buildTreeList(isSystem, tenantId,param.getParentId(), param.getSpaceName(),param.getSpaceIdList());
    }

    @Override
    public Map<Long, ParkSpaceFullModel> findFullSpaceMap(List<Long> idList, Long tenantId) {
        //查询空间集合
        List<ParkSpace> list = list(Wrappers.<ParkSpace>lambdaQuery().eq(tenantId != null, ParkSpace::getTenantId, tenantId).eq(ParkSpace::getSpaceStatus, Status.enabled.getKey()));
        return getFullParkSpaceMap(idList, list);
    }

    @Override
    public List<Long> findChildrenIdList(Long id, Long tenantId) {
        List<ParkSpaceTreeModel> treeList = buildTreeList(tenantId == null, tenantId,null, null);
        Set<Long> spaceIdList = new HashSet<>();
        treeList.forEach(node -> findChildrenId(node, id, spaceIdList));
        return new ArrayList<>(spaceIdList);
    }

    @Override
    public List<Long> findChildrenIdList(List<Long> ids, Long tenantId) {
        List<ParkSpaceTreeModel> treeList = buildTreeList(tenantId == null, tenantId,null, null);
        Set<Long> spaceIdList = new HashSet<>();
        ids.forEach(id -> treeList.forEach(node -> findChildrenId(node, id, spaceIdList)));
        return new ArrayList<>(spaceIdList);
    }

    @Override
    public List<ParkSpaceTreeModel> buildTree(Long tenantId){
        return buildTreeList(tenantId == null, tenantId,null, null);
    }

    @Override
    public List<Long> findChildrenIdList(List<Long> ids, List<ParkSpaceTreeModel> treeList) {
        Set<Long> spaceIdList = new HashSet<>();
        ids.forEach(id -> treeList.forEach(node -> findChildrenId(node, id, spaceIdList)));
        return new ArrayList<>(spaceIdList);
    }

    /**
     * 根据园区ID和空间编码校验唯一性
     *
     * @param param 校验参数
     * @return 空间编码是否存在
     */
    @Override
    public Boolean checkOnlyByParkIdAndCode(ParkSpaceCheckCodeParam param) {
        return verifyCode(param.getTenantId(), param.getId(), param.getSpaceCode());
    }

    /**
     * 根据园区ID和空间编码校验唯一性
     *
     * @param param 校验参数
     * @return 空间编码是否存在
     */
    @Override
    public Boolean checkOnlyByParentIdAndName(ParkSpaceCheckNameParam param) {
        return verifyName(param.getParentSpaceId(), param.getId(), param.getSpaceName());
    }

    /**
     * 新增园区空间列.
     *
     * @Param param 园区空间列信息
     * @Return 新增园区空间列是否成功
     */
    @Override
    public ParkSpaceModel add(ParkSpaceParam param) {
        //判断是否拥有管理员权限
        AssertUtils.isTrue(verifySystemRole(getUser()) || verifyTenantAdmin(findTenantIdByUserId()), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        verifyBeforeSave(param);

        ParkSpace parkSpaceInfo = BeanUtils.convertTo(param, ParkSpace::new);
        parkSpaceInfo.setId(null);
        parkSpaceInfo.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        parkSpaceInfo.setSpaceStatus(Status.enabled.getKey());
        save(parkSpaceInfo);
        //包装返回,以便导入时使用
        ParkSpaceModel model = new ParkSpaceModel();
        BeanUtils.copyProperties(parkSpaceInfo, model);
        return model;
    }

    /**
     * 编辑园区空间列信息.
     *
     * @Param param 园区空间列信息
     * @Return 编辑园区空间列是否成功
     */
    @Override
    public Boolean edit(ParkSpaceParam param) {
        verifyBeforeSave(param);
        ParkSpace parkSpace = getById(param.getId());
        AssertUtils.notNull(parkSpace, SystemResultCode.RESULT_DATA_NONE.message());
        //判断是否拥有操作权限
        AssertUtils.isTrue(verifyOperaRole(parkSpace.getTenantId(), getUser(), findTenantIdByUserId()), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        //以下为不可编辑字段
        param.setId(param.getId());
        param.setTenantId(parkSpace.getTenantId());
        ParkSpace editParam = BeanUtils.convertTo(param, ParkSpace::new);
        return updateById(editParam);
    }

    /**
     * 删除园区空间列.
     *
     * @Param id 园区空间列标识
     * @Return 删除园区空间列是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remove(Long id) {
        ParkSpace parkSpace = getById(id);
        AssertUtils.notNull(parkSpace, SystemResultCode.RESULT_DATA_NONE.message());
        //判断是否拥有操作权限
        AssertUtils.isTrue(verifyOperaRole(parkSpace.getTenantId(), getUser(), findTenantIdByUserId()), SystemResultCode.PERMISSION_UNAUTHORISE.message());
        //校验是否存在下级空间
        AssertUtils.isFalse(count(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId, id).eq(ParkSpace::getSpaceStatus, Status.enabled.getKey())) > 0, "存在下级空间,无法删除");
        //删除人员空间分配数据
//        userSpaceService.removeBySpaceIds(Collections.singletonList(id));
        //判断是否分配了设备
        parkSpace.setSpaceStatus((int) Status.disabled.getKey());
        return updateById(parkSpace);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addParkAndCreateSpace(TenantInfoParam param) {
        AssertUtils.isTrue(verifySystemRole(null),"非管理员角色无权操作");
        // 创建园区信息
        TenantInfoModel tenantInfoModel = tenantInfoService.add(param);
        AssertUtils.notNull(tenantInfoModel, "租户信息创建失败");
        // 获取组织信息
//        List<OrganizationInfoDomain> organizationInfoDomains = organizationInfoFeignClient.list(new OrganizationInfoParam()).getBody().getResult();
//        AssertUtils.notNull(organizationInfoDomains, "组织信息为空");
//        List<OrgDeptTreeNode> deptTreeNodes = departmentInfoFeignClient.listOrgTree(organizationInfoDomains.get(0).getId()).getBody().getResult();
//        AssertUtils.notNull(deptTreeNodes, "部门树为空");
//
//        // 提取所有部门的ID
//        List<Long> deptIds = deptTreeNodes.stream()
//                .flatMap(this::extractDeptIds)
//                .collect(Collectors.toList());
//        TenantAssignOrgDeptParam tenantAssignOrgDeptParam = new TenantAssignOrgDeptParam();
//        tenantAssignOrgDeptParam.setDeptIdList(deptIds);
//        tenantAssignOrgDeptParam.setTenantId(tenantInfoModel.getId());
//        tenantAssignOrgDeptParam.setOrgIdList(Collections.singletonList(organizationInfoDomains.get(0).getId()));
//        tenantInfoFeignClient.assignOrgDept(tenantAssignOrgDeptParam);
//        //todo:需要调整
//        List<CudDepartment> departments = cudDepartmentService.list(Wrappers.emptyWrapper());
//        List<Long> deptIds = departments.stream().map(CudDepartment::getId).collect(Collectors.toList());
//        TenantAssignOrgDeptParam tenantAssignOrgDeptParam = new TenantAssignOrgDeptParam();
//        tenantAssignOrgDeptParam.setDeptIdList(deptIds);
//        tenantAssignOrgDeptParam.setTenantId(tenantInfoModel.getId());
////        tenantAssignOrgDeptParam.setOrgIdList(Collections.singletonList(organizationInfoDomains.get(0).getId()));
//        tenantInfoService.assignOrgDept(tenantAssignOrgDeptParam);

        ParkSpaceParam parkSpaceParam = new ParkSpaceParam();
        parkSpaceParam.setSpaceName(tenantInfoModel.getName());
        parkSpaceParam.setSpaceCode(tenantInfoModel.getCode());
        parkSpaceParam.setSpaceDesc(tenantInfoModel.getIntro());
        parkSpaceParam.setParentSpaceId(0L);
        parkSpaceParam.setTenantId(tenantInfoModel.getId());
        return this.add(parkSpaceParam).getId() != null;
    }
//
//    private Stream<Long> extractDeptIds(OrgDeptTreeNode node) {
//        Stream<Long> currentDeptId = "dept".equals(node.getType()) ? Stream.of(node.getDeptId()) : Stream.empty();
//        Stream<Long> childrenDeptIds = node.getChildren() == null ? Stream.empty() : node.getChildren().stream().flatMap(this::extractDeptIds);
//        return Stream.concat(currentDeptId, childrenDeptIds);
//    }

    /**
     * 根据园区id获取园区集合
     *
     * @param tenantId 租户/园区id
     * @return 园区集合
     */
    @Override
    public List<ParkSpaceModel> findParkSpaceList(Long tenantId) {
        //查询所有空间集合
        List<ParkSpace> list = list(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getSpaceStatus, Status.enabled.getKey()));
        return BeanUtils.convertListTo(list, ParkSpaceModel::new);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editParkAndCreateAspace(TenantInfoParam param) {
        AssertUtils.isTrue(verifySystemRole(null),"非管理员角色无权操作");
        boolean result = tenantInfoService.edit(param.getId(), param);
        if (!result) {
            return false;
        }
        ParkSpace parkSpace = this.getOne(new LambdaQueryWrapper<ParkSpace>().eq(ParkSpace::getTenantId, param.getId()).eq(ParkSpace::getParentSpaceId, 0L));
        if (ObjectUtil.isNotEmpty(parkSpace)) {
            parkSpace.setSpaceName(param.getName());
            parkSpace.setSpaceCode(param.getCode());
            this.updateById(parkSpace);
        }
        return true;
    }

    /**
     * 构建空间树列表
     *
     * @param isSystem  是否系统管理员,系统管理员可查全部
     * @param tenantId  租户id,非系统管理员仅查园区下
     * @param spaceName 空间名称
     * @return 空间树
     */
    private List<ParkSpaceTreeModel> buildTreeList(boolean isSystem, Long tenantId,Long parentId,  String spaceName) {
        return buildTreeList(isSystem, tenantId,parentId, spaceName,new ArrayList<>());
    }

    /**
     * 构建空间树列表
     *
     * @param isSystem  是否系统管理员,系统管理员可查全部
     * @param tenantId  租户id,非系统管理员仅查园区下
     * @param spaceName 空间名称
     * @return 空间树
     */
    private List<ParkSpaceTreeModel> buildTreeList(boolean isSystem, Long tenantId,Long parentId, String spaceName, List<Long> spaceIds) {
        //查询所有空间集合
        List<ParkSpace> list = list(Wrappers.<ParkSpace>lambdaQuery().eq(!isSystem, ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getSpaceStatus, Status.enabled.getKey())
                .eq(parentId != null,ParkSpace::getParentSpaceId,parentId)
                .in(!CollectionUtils.isEmpty(spaceIds), ParkSpace::getId, spaceIds)
                .like(StringUtils.isNotEmpty(spaceName), ParkSpace::getSpaceName, spaceName)
                .last("ORDER BY IF(isnull(order_code),1,0),order_code, update_time DESC"));
        if (StringUtils.isEmpty(spaceName) && CollectionUtils.isEmpty(spaceIds) && parentId == null) {
            return list.stream().filter(parkSpace -> parkSpace.getParentSpaceId().equals(0L))
                    .map(parkSpace -> covertNode(parkSpace, list)).collect(Collectors.toList());
        }
        //搜索时直接平铺,不需要多层级
        List<ParkSpaceTreeModel> modeList = list.stream().map(parkSpace -> {
            ParkSpaceTreeModel model = new ParkSpaceTreeModel();
            BeanUtils.copyProperties(parkSpace, model);
            return model;
        }).collect(Collectors.toList());
        childrenHandle(modeList,isSystem,tenantId);
        return modeList;
    }

    private void childrenHandle(List<ParkSpaceTreeModel> list,boolean isSystem,Long tenantId){
        Set<Long> parentIds = this.getBaseMapper().selectObjs(new QueryWrapper<ParkSpace>().select("DISTINCT parent_space_id"))
                .stream().filter(Objects::nonNull).map(obj->Long.valueOf(obj.toString())).collect(Collectors.toSet());
        list.stream().filter(l->parentIds.contains(l.getId())).forEach(l->l.setHasChildren(true));
    }

    /**
     * 将空间信息转换为树形结构数据
     */
    private ParkSpaceTreeModel covertNode(ParkSpace parkSpace, List<ParkSpace> list) {
        ParkSpaceTreeModel node = new ParkSpaceTreeModel();
        BeanUtils.copyProperties(parkSpace, node);
        List<ParkSpaceTreeModel> children = list.stream()
                .filter(parkSpace1 -> parkSpace1.getParentSpaceId().equals(parkSpace.getId()))
                .map(parkSpace1 -> covertNode(parkSpace1, list)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

    /**
     * 将空间信息转换为树形结构数据
     */
    private void covertNode2(ParkSpace parkSpace, List<ParkSpace> list, List<Long> idList, Map<Long, ParkSpaceFullModel> map) {
        list.stream().filter(parkSpace1 -> parkSpace1.getParentSpaceId().equals(parkSpace.getId())).forEach(parkSpace1 -> {
            parkSpace1.setSpaceName(parkSpace.getSpaceName() + "," + parkSpace1.getSpaceName());
            if (idList.contains(parkSpace1.getId())) {
                BeanUtils.convertTo(parkSpace, ParkSpaceFullModel::new);
            }
            covertNode2(parkSpace1, list, idList, map);
        });
    }

    /**
     * 查询指定节点下的所有子节点id
     */
    private static boolean findChildrenId(ParkSpaceTreeModel node, Long targetId, Set<Long> result) {
        if (node == null) {
            return false;
        }
        // 如果当前节点是目标节点，则添加其ID到结果列表中
        if (node.getId().equals(targetId)) {
            addNodeAndChildren(node,result);
            return true; // 表示找到了目标节点
        }
        // 如果当前节点不是目标节点，则递归查找其子节点
        for (ParkSpaceTreeModel child : node.getChildren()) {
            if (findChildrenId(child, targetId, result)) {
                return true; // 如果在子树中找到了目标节点，则返回true
            }
        }
        return false; // 如果当前节点及其所有子节点都不是目标节点，则返回false
    }

    /**
     * 添加节点id及其所有子节点id到结果集合中
     * @param node 空间节点
     * @param result 结果集
     */
    private static void addNodeAndChildren(ParkSpaceTreeModel node,Set<Long> result){
        if(node == null){
            return;
        }
        result.add(node.getId());
        for (ParkSpaceTreeModel child:node.getChildren()){
            addNodeAndChildren(child,result);
        }
    }

    /**
     * 校验空间编码是否可用
     * 同一园区下编码不可重复
     *
     * @param tenantId  园区/租户id
     * @param id        空间id
     * @param spaceCode 空间编码
     * @return 是否可用
     */
    private boolean verifyCode(Long tenantId, Long id, String spaceCode) {
        return count(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getTenantId, tenantId)
                .eq(ParkSpace::getSpaceCode, spaceCode)
                .eq(ParkSpace::getSpaceStatus, Status.enabled.getKey())
                //如果id不为空,那么说明是编辑,编辑时仅校验空间编码是否被园区下其他空间使用
                .ne(id != null, ParkSpace::getId, id)) > 0;
    }

    /**
     * 校验同一层级空间名称是否可用
     * 同一层级下名称不可重复
     *
     * @param parentId  父级id
     * @param id        空间id
     * @param spaceName 空间名称
     * @return 是否可用
     */
    private boolean verifyName(Long parentId, Long id, String spaceName) {
        return count(Wrappers.<ParkSpace>lambdaQuery().eq(ParkSpace::getParentSpaceId, parentId)
                .eq(ParkSpace::getSpaceName, spaceName)
                .eq(ParkSpace::getSpaceStatus, Status.enabled.getKey())
                //如果id不为空,那么说明是编辑,需排除自身
                .ne(id != null, ParkSpace::getId, id)) > 0;
    }

    /**
     * 保存前置校验
     * 校验编码和名称是否
     */
    private void verifyBeforeSave(ParkSpaceParam param) {
        //同一层级校验名称重复
        AssertUtils.isFalse(verifyName(param.getParentSpaceId(), param.getId(), param.getSpaceName()), "空间名称重复");
        //同一园区校验编码重复
        AssertUtils.isFalse(verifyCode(param.getTenantId(), param.getId(), param.getSpaceCode()), "空间编码重复");
        //非最上级空间,需判断上级空间是否存在
        if (param.getParentSpaceId() != 0) {
            ParkSpace parkSpace = getById(param.getParentSpaceId());
            AssertUtils.isFalse(parkSpace == null, "父级空间不存在");
            param.setParentSpaceCode(parkSpace.getSpaceCode());
            param.setTenantId(parkSpace.getTenantId());
        }
    }

    /**
     * 校验用户是否为系统管理员角色
     *
     * @return 是否为系统管理员角色
     */
    private boolean verifySystemRole(CudUserInfoVO userInfo) {
        List<RoleModel> roles = roleApiService.getRoleByCurrent();
        List<String> roleCodes = roles.stream().map(RoleModel::getRoleCode).collect(Collectors.toList());
        return roleCodes.contains(systemRoleCode);
    }

    /**
     * 校验是否拥有租户管理员权限
     *
     * @return 是否拥有权限
     */
    private boolean verifyTenantAdmin(List<Long> tenantIdList) {
        return !CollectionUtils.isEmpty(tenantIdList);
    }

    /**
     * 校验是否拥有操作权限
     * 平台管理员不做限制,园区管理员只能操作园区内空间数据
     *
     * @return 是否拥有操作权限
     */
    private boolean verifyOperaRole(Long tenantId, CudUserInfoVO userInfo, List<Long> tenantIdList) {
        //平台管理员或该园区的园区管理员
//        return Constant.SYSTEM_ACCOUNT.equals(userInfo.getUserAccount()) || tenantIdList.contains(tenantId);
        return verifySystemRole(userInfo) || tenantIdList.contains(tenantId);
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户信息
     */
    private CudUserInfoVO getUser() {
//        return Objects.requireNonNull(userInfoFeignClient.detail(WebFrameworkUtils.getHeaderUserId()).getBody()).getResult();
        return cudUserService.getUsersInfo(cudUserService.getUser());
    }

    /**
     * 查询用户拥有的租户管理员id集合
     *
     * @return 租户id集合
     */
    public List<Long> findTenantIdByUserId() {
        String userId = userApiService.getCurrentStaffNo();
        Long tenantId = WebFrameworkUtils.getHeaderTenantId();
        if (userId == null) {
            return Collections.emptyList();
        }
        TenantMemberListParam param = new TenantMemberListParam();
        param.setUserId(userId);
        param.setTenantId(tenantId);
        List<TenantMemberDomain> list = tenantMemberService.list(param);
        return list.stream().filter(domain -> domain.getIdentity() != null && domain.getIdentity() == 1).map(TenantMemberDomain::getTenantId).collect(Collectors.toList());
    }

    private Map<Long, ParkSpaceFullModel> getFullParkSpaceMap(List<Long> idList, List<ParkSpace> allList) {
        Map<Long, ParkSpaceFullModel> resultMap = new HashMap<>(16);

        for (Long id : idList) {
            ParkSpace space = findParkSpaceById(allList, id);
            if (space != null) {
                StringBuilder path = new StringBuilder();
                StringBuilder idPath = new StringBuilder();
                recursiveGetParentPath(space, allList, path, idPath);
                ParkSpaceFullModel model = BeanUtils.convertTo(space, ParkSpaceFullModel::new);
                model.setFullPath(path.delete(path.lastIndexOf("/"), path.length()).toString());
                model.setIdFullPath(idPath.delete(idPath.lastIndexOf("-"), idPath.length()).toString());
                resultMap.put(id, model);
            }
        }

        return resultMap;
    }

    private ParkSpace findParkSpaceById(List<ParkSpace> allList, Long id) {
        for (ParkSpace space : allList) {
            if (space.getId().equals(id)) {
                return space;
            }
        }
        return null;
    }

    private void recursiveGetParentPath(ParkSpace space, List<ParkSpace> allList, StringBuilder path, StringBuilder idPath) {
        if (space == null) {
            return;
        }
        path.insert(0, space.getSpaceName() + "/");
        idPath.insert(0, space.getId() + "-");
        ParkSpace parentSpace = findParkSpaceById(allList, space.getParentSpaceId());
        recursiveGetParentPath(parentSpace, allList, path, idPath);
    }
}
