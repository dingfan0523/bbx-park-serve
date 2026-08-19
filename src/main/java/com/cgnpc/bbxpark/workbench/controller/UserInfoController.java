package com.cgnpc.bbxpark.workbench.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.cgnpc.bbxpark.workbench.domain.CudUserinfoPageAttributes;
import com.cgnpc.bbxpark.workbench.dto.CudUserinfoDto;
import com.cgnpc.bbxpark.workbench.service.CudUserinfoPageAttributesService;
import com.cgnpc.bbxpark.workbench.service.CudUserinfoService;
import com.cgnpc.cud.annotation.OperatorType;
import com.cgnpc.cud.annotation.UBA;
import com.cgnpc.cud.core.domain.AjaxResult;
import com.cgnpc.framework.dto.UserNamePhone;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.auth.config.ValidTokenUtil;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;
import com.cgnpc.pro.model.respvo.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/******************************
 * 用途说明: 用户相关  前端控制器
 * 作者姓名: PXMWRYA
 * 创建时间: 2019/07/19 09:14
 ******************************/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController extends QSCABaseController {

    @Autowired(required = false)
    private ICudUserService cudUserService;

    @Resource
    private ValidTokenUtil validTokenUtil;

    @Autowired
    private CudUserinfoService userinfoService;

    @Autowired
    private CudUserinfoPageAttributesService pageAttributesService;

    /**
     * 登录模式
     **/
    @Value("${cud.login-mode:auth2}")
    private String loginMode;

    public static final String PRO_LOGIN = "pro";

    /**********************************
    * 用途说明: 获取当前用户信息
    * 参数说明 request
    * 返回值说明:
    ***********************************/
    @PostMapping("getCurrentUser")
    @UBA(module = "用户模块",action = "获取当前用户",channel = OperatorType.Page)
    public AjaxResult getCurrentUser() {
        try {
            CudUserInfoVO vo = cudUserService.getUsersInfo(cudUserService.getUser());
            AjaxResult result = getObject(vo.getNowUserName());
            return (AjaxResult) userinfoService.getDepartmentInfo(result, vo);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }


    /**********************************
    * 用途说明: 获取多个人员信息
    * 参数说明 usersMap
    * 返回值说明:
    ***********************************/
    @PostMapping("getUsersInfo")
    @UBA(module = "用户模块",action = "获取多个人员信息",channel = OperatorType.Page)
    public AjaxResult getUsersInfo(@RequestBody Map usersMap) {
        List<UserModel> userModelList = new ArrayList<>();
        try {
            String userIds = MapUtil.getStr(usersMap,"userIds","");
            if (StrUtil.isNotBlank(userIds)) {
                userModelList = cudUserService.getUsersByUserCenter(userIds);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return getObject(userModelList);
    }


    /**********************************
     * 用途说明: 获得用户名和电话
     * 参数说明 userId
     * 返回值说明:
     ***********************************/
    @PostMapping("getUserNamePhone/{userId}")
    @UBA(module = "用户模块",action = "获得用户名和电话",channel = OperatorType.Button)
    public AjaxResult getUserNamePhone(@PathVariable String userId) {
        try {
            UserNamePhone userNamePhone = new UserNamePhone();
            String userPhone = cudUserService.getUserPhoneNum(userId);
            userNamePhone.setUserPhone(userPhone);
            userNamePhone.setUserName(cudUserService.getUserRealName());
            return getObject(userNamePhone);
        } catch (Exception e) {
            log.error(e.getMessage());
            return error(e.getMessage());
        }
    }

    /**
     * 用途说明: 统计在线用户
     * 作者姓名: P633860
     * 创建时间: 2024/3/5
     */
    @GetMapping("getOnlineUsers")
    @UBA(module = "用户模块",action = "统计在线用户",channel = OperatorType.Page)
    public AjaxResult getOnlineUsers() {
        return getObject(userinfoService.getOnlineUsers());
    }

    /**
     * 用途说明: 保存用户个性化设置
     * 作者姓名: P633860
     * 创建时间: 2024/6/11
     */
    @PostMapping("/theme/save")
    @UBA(module = "用户模块",action = "保存用户个性化设置")
    public AjaxResult setTheme(@RequestBody @Validated CudUserinfoDto dto) {
        String userId = cudUserService.getUser();
        dto.setUserId(userId);
        return getObject(userinfoService.setTheme(dto));
    }

    @GetMapping("/theme")
    @UBA(module = "用户模块",action = "用户个性化设置",channel = OperatorType.Page)
    public AjaxResult theme() {
        String userId = cudUserService.getUser();
        try {
            return getObject(userinfoService.getTheme(userId));
        } catch (Exception e) {
            return error("No data.");
        }
    }

    /**
     * 用途说明: 保存用户个性化设置-自定义字段
     * 作者姓名: P633860
     * 创建时间: 2024/7/8
     */
    @PostMapping("/saveFields")
    @UBA(module = "用户模块",action = "保存用户个性化设置-自定义字段",channel = OperatorType.Page)
    public AjaxResult saveFields(@RequestBody @Validated CudUserinfoPageAttributes attributes) {
        String userId = cudUserService.getUser();
        attributes.setUserId(userId);
        return getObject(pageAttributesService.setAttr(attributes));
    }

    @GetMapping("/getFields")
    @UBA(module = "用户模块",action = "用户个性化设置-自定义字段",channel = OperatorType.Page)
    public AjaxResult getFields(@RequestParam(defaultValue = "") String queryId) {
        String userId = cudUserService.getUser();
        return getObject(pageAttributesService.getAttr(userId,queryId));
    }

    @GetMapping("/getFieldsList")
    @UBA(module = "用户模块",action = "用户个性化设置-返回字段列表",channel = OperatorType.Page)
    public AjaxResult getFieldsList() {
        String userId = cudUserService.getUser();
        return getObject(pageAttributesService.getAttrList(userId));
    }

}
