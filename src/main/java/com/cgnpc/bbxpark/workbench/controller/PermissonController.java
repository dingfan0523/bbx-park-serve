package com.cgnpc.bbxpark.workbench.controller;

import com.cgnpc.bbxpark.workbench.application.IPermissonApplication;
import com.cgnpc.cud.annotation.OperatorType;
import com.cgnpc.cud.annotation.UBA;
import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.cud.core.domain.AjaxResult;
import com.cgnpc.cud.form.common.dto.form.req.ReqGridMenuDto;
import com.cgnpc.cud.form.manage.service.CudMenuManagerService;
import com.cgnpc.cud.shiro.util.ContextHolder;
import com.cgnpc.pro.model.respvo.Menu;
import com.cgnpc.pro.model.respvo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/******************************
 * 用途说明: uau权限 前端控制器
 * 作者姓名: PXMWRYA
 * 创建时间: 2019/07/19 09:14
 ******************************/
@RestController
@RequestMapping("/uau/perm")
public class PermissonController extends BaseController{

    @Autowired
    IPermissonApplication iPermissonApplication;

    @Autowired
    CudMenuManagerService cudMenuManagerService;
    /**********************************
     * 用途说明:获取用户的菜单权限
     * 参数说明
     * 返回值说明:
     ***********************************/
    @PostMapping("/getMenuPermission")
    @UBA(module = "权限模块",action = "调用UAU获取菜单资源",channel = OperatorType.Page)
    public AjaxResult getMenuPermission(){
        try{
            Set<Menu> menuSet = iPermissonApplication.selectMenus(ContextHolder.getPrincipalName());
            return getObject(menuSet);
        }catch (Exception e){
            return error(e.getMessage());
        }
    }

    /**********************************
     * 用途说明:调用获取业务菜单获取菜单资源
     * 参数说明
     * 返回值说明:
     ***********************************/
    @PostMapping("/getBusiMenuPermission")
    @UBA(module = "权限模块",action = "调用业务菜单配置,获取菜单资源",channel = OperatorType.Page)
    public AjaxResult getBusiMenuPermission(){
        try{
            ReqGridMenuDto reqGridMenuDto = new ReqGridMenuDto();
            Set<String> menuSet = iPermissonApplication.selectPermissions(ContextHolder.getPrincipalName());
            reqGridMenuDto.setMenuCodeList(menuSet.stream().collect(Collectors.toList()));
            List<Map<String, Object>>  maps = cudMenuManagerService.getFormMenuTree(reqGridMenuDto);
            return getObject(maps);
        }catch (Exception e){
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**********************************
     * 用途说明:获取用户的其他权限
     * 参数说明
     * 返回值说明:
     ***********************************/
    @PostMapping("/getPermissions")
    @UBA(module = "权限模块",action = "调用UAU获取资源",channel = OperatorType.Page)
    public AjaxResult getPermissions(){
        try{
            Set<String> permSet = iPermissonApplication.selectPermissions(ContextHolder.getPrincipalName());
            return getObject(permSet);
        }catch (Exception e){
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**********************************
     * 用途说明:获取用户的其他权限
     * 参数说明
     * 返回值说明:
     ***********************************/
    @PostMapping("/queryUserRoles")
    @UBA(module = "权限模块",action = "获取用户角色",channel = OperatorType.Page)
    public AjaxResult queryUserRoles(){
        try{
            List<RoleVO> roleVOList = iPermissonApplication.selectRoleDetail(ContextHolder.getPrincipalName());
            return getObject(roleVOList);
        }catch (Exception e){
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

}