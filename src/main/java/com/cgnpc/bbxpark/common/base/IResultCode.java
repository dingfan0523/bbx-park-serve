package com.cgnpc.bbxpark.common.base;

public interface IResultCode {
    /**
     * 获取消息
     *
     * @return
     */
    String getMessage();

    /**
     * 获取状态码
     *
     * @return
     */
    int getCode();
}
