package com.cgnpc.framework.permission.interceptor;

import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.cgnpc.framework.permission.application.DataPermissionApplication;
import com.cgnpc.framework.permission.holder.DataSqlHandlerHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;

/**
 * @Author P629041
 * @Description 租户隔离sql拦截器
 * @Date 15:10 2024/8/23
 **/
@Slf4j
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
})
public class DataPermissionSqlInterceptor extends AbstractSqlParserHandler implements Interceptor {

    /**
     * 需要拦截的sql
     */
    @Autowired
    private DataPermissionApplication dataPermissionApplication;


    /**
     * 拦截sql
     *
     * @param invocation
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        // 通过MetaObject访问对象的属性
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());

        // 先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        // id为执行的mapper方法的全路径名，如com.cq.UserMapper.insertUser， 便于后续使用反射
        String id = mappedStatement.getId();
        log.info("id:{}",id);
        // 数据库连接信息
        BoundSql boundSql = statementHandler.getBoundSql();
        // 获取到原始sql语句
        String sql = boundSql.getSql();
        // 判断是否为 SELECT 或者 UPDATE
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.SELECT != sqlCommandType && SqlCommandType.UPDATE != sqlCommandType) {
            return invocation.proceed();
        }
        //是否已被分页sql拦截器重写
        if (!DataSqlHandlerHolder.INIT_STATUS.equals(DataSqlHandlerHolder.getHandlerFlag())){
            return invocation.proceed();
        }
        //当前sql是否需要拦截
        if (dataPermissionApplication.validSqlInterceptor(id,sqlCommandType)) {
            log.info("DataPermissionSqlInterceptor rewrite SQL");
            // 重写sql
            String permissionSql = dataPermissionApplication.prepareHandlerSql(sql,sqlCommandType);
            //通过反射修改sql
            Field field = boundSql.getClass().getDeclaredField("sql");
            field.setAccessible(true);
            field.set(boundSql, permissionSql);
        }
        return invocation.proceed();
    }

}
