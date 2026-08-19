package com.cgnpc.bbxpark.acl.uic.controller;

import com.cgnpc.cud.annotation.OperatorType;
import com.cgnpc.cud.annotation.UBA;
import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.cud.core.domain.AjaxResult;
import com.cgnpc.framework.deptinfo.CurrentDept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/******************************
 * 用途说明: 用户相关  前端控制器
 * 作者姓名: PXMWRYA
 * 创建时间: 2019/07/19 09:14
 ******************************/
@RestController
@RequestMapping("/dept")
public class DeptInfoController extends BaseController {

    @Autowired
    CurrentDept currentDept;

   
    /**********************************
    * 用途说明: 获取多个人员信息
    * 参数说明 usersMap
    * 返回值说明:
    ***********************************/
    @PostMapping("getDeptInfo")
    @UBA(module = "用户模块",action = "获取部门信息",channel = OperatorType.Button)
    public AjaxResult getUsersInfo(@RequestBody Map deptMap) {
        try {
            String deptId = deptMap.get("deptId").toString();
            Map usersMap = currentDept.getDeptInfo(deptId);
            return getObject(usersMap);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }
    
    /**********************************
     * 用途说明: 获取个人公司信息
     * 参数说明 usersMap
     * 返回值说明:
     ***********************************/
     @PostMapping("getStaffCompanyInfo")
     @UBA(module = "用户模块",action = "获取部门信息",channel = OperatorType.Button)
     public AjaxResult getStaffCompanyInfo(@RequestBody Map deptMap) {
         try {
             String userIds = deptMap.get("userIds").toString();
             String [] data = userIds.split(";");
             List<Map> res = new ArrayList<>();
             for (int i = 0; i < data.length; i++) {
            	 Map usersMap = currentDept.getStaffCompanyInfo(data[i]);
            	 res.add(usersMap);
             }
             
             return getObject(res);
         } catch (Exception e) {
             return error(e.getMessage());
         }
     }
}