package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.pro.api.IPermissionService;
import com.cgnpc.pro.model.respvo.Menu;
import com.cgnpc.pro.model.respvo.RoleDetailVO;
import com.cgnpc.pro.model.respvo.RoleVO;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service("cudPermissionService")
@Primary
@Profile("local-auth")
public class LocalPermissionService implements IPermissionService {

    private static final String PARENT_ID = "4ba36188ea3b46209ee9b8427dedbc57";

    private static final String[][] MENUS = {
            {"03d7aa1c85274d44bd548d40307233a4", "bbx_zhct", "智慧餐厅"},
            {"bdaa782a981f42ef92d886c07ef1216e", "bbx_log_mgr", "日志管理"},
            {"6ce0fd290a0441538ed51745b06fd569", "bbx_msg_center", "消息中心"},
            {"b3214e609576466e952f25e9ee1f96b2", "office", "首页"},
            {"5763e5b6dad64faa9e17943fcc9c16f1", "bbx_zhas", "智慧安消"},
            {"5b5e0eba8b3341e783192dafecb140fc", "bbx_zhng", "智慧能管"},
            {"938be9784dbd4f8b85aa35e4253c2358", "bbx_zhwy", "智慧物业"},
            {"b0ffd9b9bf8f49efb9cda366629b8207", "dict_manage", "字典管理"},
            {"94688c37768b41bfa2b408e4df44fe29", "bbx_notice_mgr", "通知管理"},
            {"83092af0c6fa417ba81cdceab7ebee22", "bbx_property_mgr", "物业管理"},
            {"c60040a44ef642e2bfb3f261d572938e", "bbx_zhkj", "智慧空间"},
            {"8ce1449e0901449eb80b3c728ec02c5c", "bbx_hqgl", "后勤管理"},
            {"9f59716e94554ee19a4c4c3f8084138a", "bbx_yqgl", "园区管理"},
            {"a13f0e46b320423d9a27315e7a97178d", "bbx_zhhy", "智慧会议"}
    };

    private final LocalAuthProperties properties;

    public LocalPermissionService(LocalAuthProperties properties) {
        this.properties = properties;
    }

    static Set<String> permissionCodes() {
        LinkedHashSet<String> codes = new LinkedHashSet<>();
        codes.addAll(Arrays.asList(
                "bbx_zhng", "dict_manage", "bbx_log_mgr", "systemManage", "bbx_zhas",
                "bbx_msg_center", "bbx_notice_mgr", "bbx_zhct", "bbx_zhhy", "bbx_yqgl",
                "office", "bbx_hqgl", "bbx_zhwy", "bbx_zhkj", "bbx_property_mgr"));
        return codes;
    }

    @Override
    public Set<Menu> selectMenus(String userId, String... args) {
        Set<Menu> menus = new LinkedHashSet<>();
        for (String[] definition : MENUS) {
            Menu menu = new Menu();
            menu.setMenuId(definition[0]);
            menu.setMenuCode(definition[1]);
            menu.setMenuName(definition[2]);
            menu.setParentId(PARENT_ID);
            menu.setOrderNum("1");
            menu.setMenuType("001");
            menu.setChildren(new ArrayList<>());
            menus.add(menu);
        }
        return menus;
    }

    @Override
    public Set<String> selectRoles(String userId, String... args) {
        return Collections.singleton(properties.getRoleCode());
    }

    @Override
    public List<RoleVO> selectRoleDetail(String userId, String... args) {
        RoleVO role = new RoleVO();
        role.setRoleId("local-default-admin-auth");
        role.setRoleCode(properties.getRoleCode());
        role.setRoleName(properties.getRoleName());
        role.setRoleDesc("本地联调角色");
        return Collections.singletonList(role);
    }

    @Override
    public Set<String> selectPermissions(String userId, String... args) {
        return permissionCodes();
    }

    @Override
    public RoleDetailVO queryUserAndDeptByRoleId(String roleId, String... args) {
        return null;
    }
}
