package com.cgnpc.framework.permission.application;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cgnpc.framework.permission.config.DataPermissionConfiguration;
import com.cgnpc.bbxpark.workbench.application.IPermissonApplication;
import com.cgnpc.bbxpark.workbench.cudauth.utils.UserPermissionUtils;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.model.respvo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 租户隔离应用类
 * @Author P629041
 * @Date 2024/8/26 9:20
 */
@Slf4j
@Component
public class DataPermissionApplication {


    @Autowired
    private IPermissonApplication iPermissonApplication;

    @Autowired
    private ICudUserService cudUserService;

    /**
     * 登录模式
     **/
    @Value("${cud.login-mode:auth2}")
    private String loginMode;

    @Autowired
    private DataPermissionConfiguration dataPermissionConfiguration;

    /**
     * 用户角色缓存key
     */
    public final static String cacheKey = ":userRoles:";

    /**
     * 解耦版本标识
     */
    public static final String PRO_LOGIN = "pro";

    /**
     * 内网版本标识
     */
    public static final String AEP_LOGIN = "aep";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_CREATE_USER_NO = "CREATE_USER_NO";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_CREATE_USER = "CREATE_USER";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_CREATE_BY = "CREATE_BY";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_CREATE_USER_NAME = "CREATE_USER_NAME";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_MODIFY_USER_NO = "MODIFY_USER_NO";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_UPDATE_USER = "UPDATE_USER";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_UPDATE_BY = "UPDATE_BY";

    /**
     * 重写SQL 条件字段
     */
    public final String DATA_FIELD_MODIFY_USER_NAME = "MODIFY_USER_NAME";

    /**
     * 重写SQL 条件字段 标识公共数据字段
     */
    public final String DATA_FIELD_PUBLIC_FLAG = "PUBLIC_FLAG";

    /**
     * PUBLIC_FLAG 公共数据字段值
     */
    public final Integer PUBLIC_FLAG_DEFAULT_VALUE = 1;

    /**
     * 默认SQL 模板
     */
    public final String DEFAULT_TEMPLATE = "select * from ({}) scope where scope.{} = '{}'";

    /**
     * 默认SQL 模板
     */
    public final String UPDATE_DEFAULT_TEMPLATE = "{} and {} = '{}'";

    /**
     * 携带公共数据标识SQL 模板
     */
    public final String PUBLIC_FLAG_TEMPLATE = "select * from ({}) scope where scope.{} = '{}' or scope.{} = {}";

    /**
     * @Author P629041
     * @Description 获取当前用户角色
     * @Date 9:25 2024/8/26
     * @Param [userId]
     * @return java.util.Set<java.lang.String>
     **/
    private Set<String> getCacheUserRoles(){
        Set<String> roleSetResult = new HashSet<>(16);
        String userId = cudUserService.getUser();
        //确认登录模式
        String key = PRO_LOGIN.equals(loginMode) ? PRO_LOGIN : AEP_LOGIN;
        roleSetResult = UserPermissionUtils.cacheGet(key + cacheKey + userId);
        if (CollUtil.isEmpty(roleSetResult)) {
            List<RoleVO> roleVOList = iPermissonApplication.selectRoleDetail(userId);
            if (CollUtil.isNotEmpty(roleVOList)) {
                roleSetResult = roleVOList.stream().map(RoleVO::getRoleCode).collect(Collectors.toSet());
            }
        }
        return roleSetResult;
    }

    /**
     * @return java.lang.Boolean
     * @Author P629041
     * @Description 校验SQL 是否需要拦截重写
     * @Date 15:53 2024/8/29
     * @Param [sqlId sql唯一标识, sqlType sql类型]
     **/
    public Boolean validSqlInterceptor(String sqlId, SqlCommandType sqlType) {
        if (!dataPermissionConfiguration.isEnable()) {
            return false;
        }
        Boolean result = false;
        Boolean interceptorSqlFlag = false;
        String adminRoleCode = dataPermissionConfiguration.getAdminRoleCode();
        List<String> selectSqlList = dataPermissionConfiguration.getSelectSqlList();
        List<String> updateSqlList = dataPermissionConfiguration.getUpdateSqlList();

        //判断当前SELECT 查询sql是否需要拦截
        if (SqlCommandType.SELECT.equals(sqlType) && CollUtil.isNotEmpty(selectSqlList) && selectSqlList.contains(sqlId)) {
            interceptorSqlFlag = true;
        }
        //判断当前UPDATE 修改sql是否需要拦截
        if (SqlCommandType.UPDATE.equals(sqlType) && CollUtil.isNotEmpty(updateSqlList) && updateSqlList.contains(sqlId)) {
            interceptorSqlFlag = true;
        }

        if (interceptorSqlFlag) {
            //判断当前用户是否有管理员用户角色
            Set<String> cacheUserRoles = getCacheUserRoles();
            if (CollUtil.isNotEmpty(cacheUserRoles) && !cacheUserRoles.contains(adminRoleCode)) {
                result = true;
            }
        }
        return result;
    }

/**
 * @Author P629041
 * @Description 拦截后重写SQL
 * @Date 15:54 2024/8/29
 * @Param [sql, sqlType]
 * @return java.lang.String
 **/
    public String prepareHandlerSql(String sql,SqlCommandType sqlType) {
        String nowUser = cudUserService.getNowUser();
        String user = cudUserService.getUser();
        if (StrUtil.isBlank(nowUser) || StrUtil.isBlank(user)){
            log.error("用户信息异常！中止SQL 重写：user:{},nowUser:{}",user,nowUser);
            return sql;
        }

        /*
         * 查询SELECT SQL 处理
         * 此处逻辑为：如果查询SQL中包含CREATE_USER_NO字段 则将原SQL作为数据源嵌套一层条件，将数据创建人CREATE_USER_NO 字段作为查询条件 兼容其它字段
         * 请遵循数据库规范创建公共字段
         */
        if (SqlCommandType.SELECT.equals(sqlType)) {
            //存在 PUBLIC_FLAG 标识使用 PUBLIC_FLAG标识模板
            String templateStr = sql.contains(DATA_FIELD_PUBLIC_FLAG) ? PUBLIC_FLAG_TEMPLATE : DEFAULT_TEMPLATE;
            //存在 CREATE_USER_NO 以CREATE_USER_NO 字段作为筛选条件
            if (sql.contains(DATA_FIELD_CREATE_USER_NO) || sql.contains(DATA_FIELD_CREATE_USER_NO.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(templateStr,
                        sql, DATA_FIELD_CREATE_USER_NO, user, DATA_FIELD_PUBLIC_FLAG,PUBLIC_FLAG_DEFAULT_VALUE);
            }
            //存在 CREATE_USER_NAME 以CREATE_USER_NAME 字段作为筛选条件
            if (sql.contains(DATA_FIELD_CREATE_USER_NAME) || sql.contains(DATA_FIELD_CREATE_USER_NAME.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(templateStr,
                        sql, DATA_FIELD_CREATE_USER_NAME, nowUser, DATA_FIELD_PUBLIC_FLAG,PUBLIC_FLAG_DEFAULT_VALUE);
            }
            //兼容 create_user 字段
            if (sql.contains(DATA_FIELD_CREATE_USER) || sql.contains(DATA_FIELD_CREATE_USER.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(templateStr,
                        sql, DATA_FIELD_CREATE_USER, nowUser,DATA_FIELD_PUBLIC_FLAG,PUBLIC_FLAG_DEFAULT_VALUE);
            }
            //兼容 create_by 字段
            if (sql.contains(DATA_FIELD_CREATE_BY) || sql.contains(DATA_FIELD_CREATE_BY.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(templateStr,
                        sql, DATA_FIELD_CREATE_BY, user,DATA_FIELD_PUBLIC_FLAG,PUBLIC_FLAG_DEFAULT_VALUE);
            }
            log.info("重写sql中不包含{}、{}、{}、{}，取消重写！",
                    DATA_FIELD_CREATE_USER_NO, DATA_FIELD_CREATE_USER_NAME,DATA_FIELD_CREATE_USER,DATA_FIELD_CREATE_BY);
        }

        /*
         * 修改UPDATE SQL 处理
         * 此处逻辑为：如果修改SQL中包含MODIFY_USER_NO字段 则使用配套的 CREATE_USER_NO 字段作为修改条件 兼容其它字段
         * 请遵循数据库规范创建公共字段 且字段需要成对出现
         */
        if (SqlCommandType.UPDATE.equals(sqlType)) {
            //存在 MODIFY_USER_NO 则以 CREATE_USER_NO字段作为修改条件
            if (sql.contains(DATA_FIELD_MODIFY_USER_NO) || sql.contains(DATA_FIELD_MODIFY_USER_NO.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(UPDATE_DEFAULT_TEMPLATE,
                        sql, DATA_FIELD_CREATE_USER_NO, user);
            }
            //存在 MODIFY_USER_NAME 以CREATE_USER_NAME 字段作为修改条件
            if (sql.contains(DATA_FIELD_MODIFY_USER_NAME) || sql.contains(DATA_FIELD_MODIFY_USER_NAME.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(UPDATE_DEFAULT_TEMPLATE,
                        sql, DATA_FIELD_CREATE_USER_NAME, nowUser);
            }
            //兼容 UPDATE_USER 字段
            if (sql.contains(DATA_FIELD_UPDATE_USER) || sql.contains(DATA_FIELD_UPDATE_USER.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(UPDATE_DEFAULT_TEMPLATE,
                        sql, DATA_FIELD_CREATE_USER, nowUser);
            }
            //兼容 UPDATE_BY 字段
            if (sql.contains(DATA_FIELD_UPDATE_BY) || sql.contains(DATA_FIELD_UPDATE_BY.toLowerCase(Locale.ENGLISH))) {
                return StrUtil.format(UPDATE_DEFAULT_TEMPLATE,
                        sql, DATA_FIELD_CREATE_BY, user);
            }
        }

        return sql;
    }

}
