package com.cgnpc.framework.permission.holder;

import java.util.Optional;

/**
 * @Author P629041
 * @Description 记录当前sql 是否已被分页sql处理器处理
 * @Date 14:36 2024/8/26
 **/
public class DataSqlHandlerHolder {

    /**
     * 初始状态 未处理
     */
    public static final Integer INIT_STATUS = 0;

    /**
     * 已处理状态
     */
    public static final Integer HANDLER_STATUS = 1;

    /**
     * 未匹配上
     */
    public static final Integer NOT_MATCH_STATUS = 2;

    private static final InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<Integer>();

    /**
     * 设置上下文
     * @param handlerFlag 上下文参数
     */
    public static void setHandlerFlag(Integer handlerFlag) {
        threadLocal.set(handlerFlag);
    }

    /**
     * 获取上下文参数
     * @return UserTokenDto
     */
    public static Integer getHandlerFlag() {
        return Optional.ofNullable(threadLocal.get()).orElse(INIT_STATUS);
    }
    /**
     * 清除上下文参数
     */
    public static void remove(){
        threadLocal.remove();
    }
}
