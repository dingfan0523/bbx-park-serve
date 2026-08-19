package com.cgnpc.bbxpark.localauth.service;

import com.cgnpc.bbxpark.localauth.config.LocalAuthProperties;
import com.cgnpc.pro.model.respvo.Menu;
import com.cgnpc.pro.model.respvo.RoleVO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalPermissionServiceTest {

    @Test
    void returnsHomepageMenusRolesAndPermissions() {
        LocalAuthProperties properties = new LocalAuthProperties();
        LocalPermissionService service = new LocalPermissionService(properties);

        Set<Menu> menus = service.selectMenus("LOCAL001");
        Set<String> permissions = service.selectPermissions("LOCAL001");
        List<RoleVO> roles = service.selectRoleDetail("LOCAL001");

        assertEquals(14, menus.size());
        assertTrue(menus.stream().anyMatch(menu -> "office".equals(menu.getMenuCode())));
        assertEquals(15, permissions.size());
        assertTrue(permissions.contains("systemManage"));
        assertEquals("default_admin_auth", roles.get(0).getRoleCode());
        assertEquals("本地管理员", roles.get(0).getRoleName());
    }
}
