package com.cgnpc.bbxpark.workbench.controller;


//import com.cgnpc.cud.auth.util.ShiroUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.workbench.application.IUserApplication;
import com.cgnpc.cud.core.common.util.StringUtils;
import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.cud.core.domain.AjaxResult;
import com.cgnpc.cud.shiro.util.ShiroUtils;
import com.cgnpc.framework.domain.SysUser;
import com.cgnpc.framework.service.IUserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/******************************
 * 用途说明: 登录验证
 * 作者姓名: PXMWLIN
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Controller
public class LoginController extends BaseController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

//
//    @PostMapping({"/login/getUser"})
//    @ResponseBody
//    @CrossOrigin
//    public AjaxResult getUserDetail() {
//        return AjaxResult.success().put("user", "PXMWHEY");
//    }
//
//    @CrossOrigin
//    @PostMapping("/login")
//    @ResponseBody
//    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe)
//    {
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
//        Subject subject = SecurityUtils.getSubject();
//        try
//        {
//            subject.login(token);
//            boolean b=subject.isAuthenticated();
//            return success().put("sessionId",subject.getSession().getId()).put("user", ShiroUtils.getUser());
//            //return success().put("sessionId",subject.getSession().getId());
//        }
//        catch (AuthenticationException e)
//        {
//            String msg = "用户或密码错误";
//            if (StringUtils.isNotEmpty(e.getMessage()))
//            {
//                msg = e.getMessage();
//            }
//            return error(msg);
//        }
//    }
    @Autowired
    private IUserRepository userService;

    @Autowired
    private IUserApplication iUserApplication;

    /**********************************
    * 用途说明:
    * 参数说明 modelAndView
    * 参数说明 current
    * 返回值说明:
    ***********************************/
    @GetMapping("/local")
    public String index2(Model modelAndView, @RequestParam(value = "current",defaultValue = "1") Integer current) {
        // modelAndView.setViewName("index");
        IPage<SysUser> userPage= userService.page(new Page<SysUser>( current,2),null);
        modelAndView.addAttribute("userList", userPage);
        modelAndView.addAttribute("welcome","test");
        return "local";
    }


    //    @RequestMapping("/")
//    public ModelAndView index(ModelAndView modelAndView) {
//        modelAndView.setViewName("index");
//        modelAndView.addObject("userList", userService.selectList(null));
//        modelAndView.addObject("welcome","test");
//        return modelAndView;
//    }
//
//    @RequestMapping("/preSave")
//    public ModelAndView preSave(ModelAndView modelAndView, @RequestParam(value = "id", required = false) Long id) {
//        modelAndView.setViewName("save");
//        if (id != null) {
//            modelAndView.addObject("user", userService.getUser(id));
//        }
//        return modelAndView;
//    }
    /**********************************
    * 用途说明:
    * 参数说明 modelAndView
    * 参数说明 id
    * 返回值说明:
    ***********************************/
    @GetMapping("/preSave")
//    @UBA(operationDesc = "保存数据")
    public String preSave(Model modelAndView, @RequestParam(value = "id", required = false) Long id) {
        // modelAndView.setViewName("save");
        if (id != null) {
            SysUser user = iUserApplication.getUser(id);
            if (user != null) {
                modelAndView.addAttribute("user", user);
            }
        } else {
            SysUser user = new SysUser();
            modelAndView.addAttribute("user", user);
        }
        return "save";
    }

    /**********************************
    * 用途说明: 
    * 参数说明 user
    * 返回值说明: 
    ***********************************/
/* 数据库无用户相关表 cud_sys_user_t
    @ResponseBody
    @PostMapping("save")
    public Object save(SysUser user) {
        user.setType(TypeEnum.DISABLED);
        if (user.getId() == null) {
            iUserApplication. insertUser(user);
            return userService.save(user) ? success("添加成功") : error("添加失败");
        } else {
            return userService.updateById(user) ? success("修改成功") : error("修改失败");
        }
    }
*/

    /**********************************
    * 用途说明: 
    * 参数说明 id
    * 返回值说明: 
    ***********************************/
    @ResponseBody
    @PostMapping("/delete")
    public Object delete(@RequestParam(value = "id", required = false) Long id) {

        return userService.removeById(id) ? success("删除成功") : error("删除失败");
    }
    
    /**********************************
    * 用途说明: 
    * 参数说明 username
    * 参数说明 password
    * 参数说明 rememberMe
    * 返回值说明: 
    ***********************************/
    @CrossOrigin
    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            boolean b = subject.isAuthenticated();


//            String usersAccount = (String) SecurityUtils.getSubject().getPrincipal();

            Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
//            String jwt =  CryptoUtil.issueJwt(ShiroUtils.getShiroProperties().getJwtSecretKey()
//                    ,usersAccount
//                    ,usersAccount
//                    ,100000000L
//                    ,null
//                    ,null
//                    , SignatureAlgorithm.HS512);

            return success().put("user", ShiroUtils.getUser());
            // return success().put("sessionId",subject.getSession().getId());
        } catch (AuthenticationException e) {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }
    
    /**********************************
    * 用途说明: 
    * 参数说明 
    * 返回值说明: 
    ***********************************/
    @GetMapping("/unauth")
    @ResponseBody
    public AjaxResult unauth() {
        return AjaxResult.message("401", "未登录");
    }
}
