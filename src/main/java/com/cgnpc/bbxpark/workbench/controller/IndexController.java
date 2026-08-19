package com.cgnpc.bbxpark.workbench.controller;


//import com.cgnpc.cud.shiro.subject.AepPrincipal;

import com.cgnpc.cud.core.controller.BaseController;
import com.cgnpc.framework.config.CudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/******************************
 * 用途说明: 首页业务处理
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Controller
public class IndexController extends BaseController {

    @Autowired
    private CudConfig cudConfig;

    @Autowired
    private MessageSource messageSource;

    /**********************************
    * 用途说明: 系统首页
    * 参数说明 model
    * 返回值说明:
    ***********************************/
    @GetMapping("/index")
    @CrossOrigin
    public String index(Model model) {
        //String s=  MessageUtils.message("welcome","123");
        // 取身份信息
        //Account user = ShiroUtils.getUser();
//        AepPrincipal principal = (AepPrincipal) ServletUtils.getSession().getAttribute("userObject");
//
//        CreateLicense cLicense = new CreateLicense();
//        //获取参数
//        cLicense.setParam("createparam.properties");
//        //生成证书
//        cLicense.create();
        //Pac4jPrincipal p = SecurityUtils.getSubject().getPrincipals().oneByType(Pac4jPrincipal.class);
//        if(user==null){
//            SysUser   user2=new SysUser();
//            user2.setName(p.getName());
//            user=user2;
//        }
//        user.setUserName("admin");
//       // Dept dept=new Dept();
//        //user.setDept(dept);
//        // 根据用户id取出菜单
//        List<Menu> menus = new ArrayList<>();//menuService.selectMenusByUserId(user.getUserId());
        model.addAttribute("menus", null);
//        model.addAttribute("user", principal);
        model.addAttribute("copyrightYear", messageSource.getMessage("cud.license.error", null, LocaleContextHolder.getLocale()));
        return "index";
    }

    /**********************************
    * 用途说明: 系统介绍
    * 参数说明 model
    * 返回值说明:
    ***********************************/
    @GetMapping("/system/main")
    public String main(Model model) {
        model.addAttribute("version", cudConfig.getVersion());
        return "main";
    }

}
