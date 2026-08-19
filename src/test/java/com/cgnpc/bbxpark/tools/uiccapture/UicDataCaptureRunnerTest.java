package com.cgnpc.bbxpark.tools.uiccapture;

import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserPermissionApiService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UicDataCaptureRunnerTest {

    @Test
    void writesJsonLinesWithTwoSecondSpacingAndContinuesAfterFailure() throws Exception {
        List<String> calls = new ArrayList<>();
        List<Long> waits = new ArrayList<>();
        IDepartmentApiService departmentService = proxy(IDepartmentApiService.class, calls, "getOrgByNo");
        IUserApiService userService = proxy(IUserApiService.class, calls, null);
        IRoleApiService roleService = proxy(IRoleApiService.class, calls, null);
        IUserPermissionApiService permissionService = proxy(IUserPermissionApiService.class, calls, null);
        Path outputDirectory = Files.createTempDirectory("uic-capture-test-");
        UicDataCaptureRunner runner = new UicDataCaptureRunner(
                departmentService, userService, roleService, permissionService,
                2000L, outputDirectory, waits::add,
                Collections.singletonList("00888888"),
                Collections.singletonList("P309147"),
                Collections.singletonList("R_ZHGL"));

        Path outputFile = runner.capture();
        List<String> lines = Files.readAllLines(outputFile);

        assertTrue(calls.indexOf("IDepartmentApiService#getOrgByNo")
                < calls.indexOf("IDepartmentApiService#findSubDepartments"));
        assertEquals(calls.size() - 1, waits.size());
        assertTrue(waits.stream().allMatch(wait -> wait == 2000L));
        assertTrue(lines.stream().anyMatch(line -> line.contains("\"status\":\"ERROR\"")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("\"service\":\"IDepartmentApiService\"")
                && line.contains("\"input\":") && line.contains("\"output\":")));
        assertTrue(lines.stream().anyMatch(line -> line.contains("\"method\":\"getPermissionByStaffNo\"")));

        Files.deleteIfExists(outputFile);
        Files.deleteIfExists(outputDirectory);
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, List<String> calls, String failingMethod) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            calls.add(type.getSimpleName() + "#" + method.getName());
            if (method.getName().equals(failingMethod)) {
                throw new IllegalStateException("remote failed");
            }
            if (List.class.isAssignableFrom(method.getReturnType())) {
                return Collections.emptyList();
            }
            if (java.util.Set.class.isAssignableFrom(method.getReturnType())) {
                return Collections.emptySet();
            }
            if (Boolean.class.equals(method.getReturnType()) || boolean.class.equals(method.getReturnType())) {
                return false;
            }
            return null;
        });
    }
}
