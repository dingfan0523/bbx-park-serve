package com.cgnpc.bbxpark.tools.uiccapture;

import cn.hutool.json.JSONUtil;
import com.cgnpc.bbxpark.acl.uic.model.RoleModel;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.acl.uic.service.IRoleApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserApiService;
import com.cgnpc.bbxpark.acl.uic.service.IUserPermissionApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * 临时 UIC 数据采集器。仅在显式开启 bbx.uic-capture.enabled 时运行。
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "bbx.uic-capture.enabled", havingValue = "true")
public class UicDataCaptureRunner implements ApplicationListener<ApplicationReadyEvent> {

    private static final DateTimeFormatter FILE_TIME = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final DateTimeFormatter RECORD_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final List<String> DEFAULT_DEPARTMENT_IDS = Arrays.asList(
            "00888888", "50259024", "50808343", "50808638");
    private static final List<String> DEFAULT_USER_IDS = Arrays.asList(
            "P309147", "P309148", "P309149", "P309150", "P309151", "P309152");
    private static final List<String> DEFAULT_ROLE_CODES = Arrays.asList(
            "bbx_park_admin", "R_ZHGL", "default_admin_auth", "bbx_role_test");

    private final IDepartmentApiService departmentService;
    private final IUserApiService userService;
    private final IRoleApiService roleService;
    private final IUserPermissionApiService permissionService;
    private final long intervalMillis;
    private final Path outputDirectory;
    private final Sleeper sleeper;
    private final List<String> departmentIds;
    private final List<String> userIds;
    private final List<String> roleCodes;
    private final AtomicBoolean started = new AtomicBoolean(false);

    private BufferedWriter activeWriter;
    private long sequence;
    private long actualCallCount;

    @Autowired
    public UicDataCaptureRunner(IDepartmentApiService departmentService,
                                IUserApiService userService,
                                IRoleApiService roleService,
                                IUserPermissionApiService permissionService,
                                @Value("${bbx.uic-capture.interval-ms:2000}") long intervalMillis,
                                @Value("${bbx.uic-capture.output-dir:uic-capture}") String outputDirectory) {
        this(departmentService, userService, roleService, permissionService,
                intervalMillis, Paths.get(outputDirectory), Thread::sleep,
                DEFAULT_DEPARTMENT_IDS, DEFAULT_USER_IDS, DEFAULT_ROLE_CODES);
    }

    UicDataCaptureRunner(IDepartmentApiService departmentService,
                         IUserApiService userService,
                         IRoleApiService roleService,
                         IUserPermissionApiService permissionService,
                         long intervalMillis,
                         Path outputDirectory,
                         Sleeper sleeper,
                         List<String> departmentIds,
                         List<String> userIds,
                         List<String> roleCodes) {
        if (intervalMillis < 0) {
            throw new IllegalArgumentException("intervalMillis must not be negative");
        }
        this.departmentService = departmentService;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.intervalMillis = intervalMillis;
        this.outputDirectory = outputDirectory;
        this.sleeper = sleeper;
        this.departmentIds = new ArrayList<>(departmentIds);
        this.userIds = new ArrayList<>(userIds);
        this.roleCodes = new ArrayList<>(roleCodes);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (!started.compareAndSet(false, true)) {
            return;
        }
        try {
            Path outputFile = capture();
            log.info("UIC 数据采集完成，文件：{}", outputFile);
        } catch (Exception exception) {
            log.error("UIC 数据采集异常结束", exception);
        }
    }

    public synchronized Path capture() throws IOException {
        Path absoluteDirectory = outputDirectory.toAbsolutePath().normalize();
        Files.createDirectories(absoluteDirectory);
        Path outputFile = absoluteDirectory.resolve("uic-capture-" + FILE_TIME.format(LocalDateTime.now()) + ".jsonl");
        log.info("UIC 数据采集开始，真实接口调用间隔 {} ms，输出文件：{}", intervalMillis, outputFile);

        sequence = 0;
        actualCallCount = 0;
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            activeWriter = writer;
            captureAll();
        } finally {
            activeWriter = null;
        }
        return outputFile;
    }

    private void captureAll() {
        for (String departmentId : departmentIds) {
            invoke("IDepartmentApiService", "getOrgTreeForOrgWidget", "/hrcenter/getOrgTreeForOrgWidget",
                    params("orgId", departmentId), () -> departmentService.getOrgTreeForOrgWidget(departmentId));
            invoke("IDepartmentApiService", "getOrgByNo", "/hrcenter/getDeptInfo",
                    params("deptNo", departmentId), () -> departmentService.getOrgByNo(departmentId));
            invoke("IDepartmentApiService", "findSubDepartments", "/hrcenter/getOrgTreeForOrgWidget (本地转换)",
                    params("departmentId", departmentId), () -> departmentService.findSubDepartments(departmentId));
            invoke("IUserApiService", "getStaffsByOrgId", "ICudUserService#getStaffsByOrgId",
                    params("orgId", departmentId, "pageIndex", 1, "pageSize", 100, "keyword", ""),
                    () -> userService.getStaffsByOrgId(departmentId, 1, 100, ""));
            invoke("IUserApiService", "getOriginalStaffsByOrgId", "ICudUserService#getStaffsByOrgId",
                    params("orgId", departmentId, "pageIndex", 1, "pageSize", 100, "keyword", ""),
                    () -> userService.getOriginalStaffsByOrgId(departmentId, 1, 100, ""));
        }

        invoke("IDepartmentApiService", "getSecondOrgList", "/hrcenter/getOrgTreeForOrgWidget",
                Collections.emptyMap(), departmentService::getSecondOrgList);
        invoke("IDepartmentApiService", "getOrgsByOrgIds", "/hrcenter/getOrgsByOrgIds",
                params("deptIds", new LinkedHashSet<>(departmentIds)),
                () -> departmentService.getOrgsByOrgIds(new LinkedHashSet<>(departmentIds)));

        for (String userId : userIds) {
            invoke("IDepartmentApiService", "getOrgByStaffNo", "/hrcenter/getOrgByStaffNo",
                    params("staffNo", userId), () -> departmentService.getOrgByStaffNo(userId));
        }
        for (String userId : userIds) {
            invoke("IUserApiService", "detail", "ICudUserService#getUsersInfo",
                    params("staffNo", userId), () -> userService.detail(userId));
            invoke("IUserApiService", "getByStaffNo", "ICudUserService#getUsersInfo",
                    params("staffNo", userId), () -> userService.getByStaffNo(userId));
            invoke("IUserApiService", "getSecondDeptByStaffNo", "ICudUserService#getUsersInfo + /hrcenter/getOrgsByOrgIds",
                    params("staffNo", userId), () -> userService.getSecondDeptByStaffNo(userId));
        }
        invoke("IUserApiService", "getByStaffNos", "ICudUserService#getUserList",
                params("staffNos", userIds), () -> userService.getByStaffNos(new ArrayList<>(userIds)));

        List<RoleModel> roles = invoke("IRoleApiService", "findRoles", "/uauauth/role/getRoleByAppCode",
                Collections.emptyMap(), roleService::findRoles);
        if (roles != null && !roles.isEmpty() && roles.get(0) != null && roles.get(0).getRoleId() != null) {
            String roleId = roles.get(0).getRoleId();
            invoke("IRoleApiService", "getRoleById", "findRoles 返回结果本地筛选",
                    params("roleId", roleId), () -> roleService.getRoleById(roleId));
        }
        for (String userId : userIds) {
            invoke("IRoleApiService", "getRoleByStaffNo", "/uauauth/role/getRoleByLoginNameAndAppCode",
                    params("staffNo", userId), () -> roleService.getRoleByStaffNo(userId));
        }
        for (String roleCode : roleCodes) {
            invoke("IRoleApiService", "findUserByRole", "/uauauth/user/getUserByAppCodeAndRoleCode",
                    params("roleCode", roleCode), () -> roleService.findUserByRole(roleCode));
            invoke("IRoleApiService", "findStaffNoByRoleCode", "/uauauth/user/getUserByAppCodeAndRoleCode",
                    params("roleCode", roleCode), () -> roleService.findStaffNoByRoleCode(roleCode));
        }
        if (!userIds.isEmpty()) {
            String userId = userIds.get(0);
            for (String roleCode : roleCodes) {
                invoke("IRoleApiService", "hasRole", "/uauauth/role/getRoleByLoginNameAndAppCode",
                        params("staffNo", userId, "roleCode", roleCode),
                        () -> roleService.hasRole(userId, roleCode));
            }
        }
        for (String userId : userIds) {
            invoke("IUserPermissionApiService", "getPermissionByStaffNo",
                    "/uauauth/resource/getAuthMenuByAppCodeAndLoginName",
                    params("staffNo", userId), () -> permissionService.getPermissionByStaffNo(userId));
        }

        skip("IUserApiService", "getCurrentStaffNo", "依赖当前 HTTP 登录上下文，启动线程无法提供");
        skip("IUserApiService", "getCurrentStaffName", "依赖当前 HTTP 登录上下文，启动线程无法提供");
        skip("IUserApiService", "getCurrentUserInfo", "依赖当前 HTTP 登录上下文，启动线程无法提供");
        skip("IUserApiService", "parkAdmin", "依赖当前请求和租户信息，启动线程无法提供");
        skip("IRoleApiService", "getRoleByCurrent", "依赖当前 HTTP 登录上下文，启动线程无法提供");
        skip("IUserPermissionApiService", "getPermissionCodeByCurrent", "依赖当前 HTTP 登录上下文，启动线程无法提供");
        skip("IDepartmentApiService", "listOrgTree", "当前实现直接返回空集合，不会调用中台接口");
    }

    private <T> T invoke(String service, String method, String endpoint,
                         Map<String, Object> input, Supplier<T> action) {
        waitBeforeNextCall();
        actualCallCount++;
        long startedAt = System.currentTimeMillis();
        try {
            T output = action.get();
            writeRecord(record(service, method, endpoint, input, "SUCCESS",
                    output, System.currentTimeMillis() - startedAt, null));
            return output;
        } catch (RuntimeException exception) {
            writeRecord(record(service, method, endpoint, input, "ERROR",
                    null, System.currentTimeMillis() - startedAt, exception));
            log.warn("UIC 接口采集失败，继续执行：{}#{}，入参：{}", service, method, input, exception);
            return null;
        }
    }

    private void waitBeforeNextCall() {
        if (actualCallCount == 0) {
            return;
        }
        try {
            sleeper.sleep(intervalMillis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("UIC 数据采集等待被中断", exception);
        }
    }

    private void skip(String service, String method, String reason) {
        Map<String, Object> record = record(service, method, null, Collections.emptyMap(),
                "SKIPPED", null, 0L, null);
        record.put("reason", reason);
        writeRecord(record);
    }

    private Map<String, Object> record(String service, String method, String endpoint,
                                       Map<String, Object> input, String status, Object output,
                                       long durationMillis, RuntimeException error) {
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("sequence", ++sequence);
        record.put("timestamp", RECORD_TIME.format(LocalDateTime.now()));
        record.put("service", service);
        record.put("method", method);
        record.put("endpoint", endpoint);
        record.put("input", input);
        record.put("status", status);
        record.put("durationMs", durationMillis);
        record.put("output", output);
        if (error != null) {
            record.put("errorClass", error.getClass().getName());
            record.put("errorMessage", error.getMessage());
        }
        return record;
    }

    private void writeRecord(Map<String, Object> record) {
        try {
            activeWriter.write(toJson(record));
            activeWriter.newLine();
            activeWriter.flush();
        } catch (IOException exception) {
            throw new UncheckedIOException("写入 UIC 采集文件失败", exception);
        }
    }

    private String toJson(Map<String, Object> record) {
        try {
            return JSONUtil.toJsonStr(record);
        } catch (RuntimeException exception) {
            Map<String, Object> fallback = new LinkedHashMap<>(record);
            fallback.put("output", String.valueOf(record.get("output")));
            fallback.put("serializationError", exception.getClass().getName() + ": " + exception.getMessage());
            return JSONUtil.toJsonStr(fallback);
        }
    }

    private static Map<String, Object> params(Object... keyValues) {
        Map<String, Object> params = new LinkedHashMap<>();
        for (int index = 0; index < keyValues.length; index += 2) {
            params.put(String.valueOf(keyValues[index]), keyValues[index + 1]);
        }
        return params;
    }

    @FunctionalInterface
    interface Sleeper {
        void sleep(long millis) throws InterruptedException;
    }
}
