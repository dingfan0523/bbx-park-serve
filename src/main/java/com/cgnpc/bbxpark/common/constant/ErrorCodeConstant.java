package com.cgnpc.bbxpark.common.constant;

/**
 * 平台公共错误信息定义
 */
public final class ErrorCodeConstant {

    /**
     * 私有构造函数.
     */
    private ErrorCodeConstant() {

    }

    /**
     * 返回成功.
     */
    public static final int    SUCCESS                                = 200;

    /**
     * 服务内部异常.
     */
    public static final int    ERRORCODE_SERVICE_INNER                = 201;

    /**
     * 服务不可用.
     */
    public static final int    ERRORCODE_UNAVAILABLE_SERVICE          = 202;

    /**
     * 非法请求.
     */
    public static final int    ERRORCODE_REQUEST_INVALID              = 203;

    /**
     * 请求过于频繁.
     */
    public static final int    ERRORCODE_REQUEST_LIMITED              = 204;

    /**
     * 请求超时.
     */
    public static final int    ERRORCODE_REQUEST_TIMEOUT              = 205;

    /**
     * 数据不合法.
     */
    public static final int    ERRORCODE_DATA_INVALID                 = 206;

    /**
     * 权限异常.
     */
    public static final int    ERRORCODE_INSUFFICIENT_PERMISSION     = 207;

    /**
     * 数据不存在.
     */
    public static final int    ERRORCODE_DATA_NOTEXISTS               = 208;

    /**
     * 数据已存在.
     */
    public static final int    ERRORCODE_DATA_EXISTS              = 209;

    /**
     * 返回成功.
     */
    public static final String SUCCESS_MSG                            = "操作成功";

    /**
     * 服务端业务处理内部异常.
     */
    public static final String ERRORCODE_SERVICE_INNER_MSG            = "服务内部异常";

    /**
     * 服务不可用引发的异常.
     */
    public static final String ERRORCODE_UNAVAILABLE_SERVICE_MSG      = "服务不可用";

    /**
     * 当前服务请求被服务端判断非法.
     */
    public static final String ERRORCODE_REQUEST_INVALID_MSG          = "非法请求";

    /**
     * 求触发流控，服务器拒绝响应.
     */
    public static final String ERRORCODE_REQUEST_LIMITED_MSG          = "请求过于频繁";

    /**
     * 服务端响应超时，请求被中断.
     */
    public static final String ERRORCODE_REQUEST_TIMEOUT_MSG          = "请求超时";

    /**
     * 数据验证错误，格式错误，参数验证不合法等.
     */
    public static final String ERRORCODE_DATA_INVALID_MSG             = "数据不合法";

    /**
     * 未授权或权限不足.
     */
    public static final String ERRORCODE_INSUFFICIENT_PERMISSION_MSG = "权限不足";

    /**
     * 根据传入的数据没有获取的结果等.
     */
    public static final String ERRORCODE_DATA_NOTEXISTS_MSG            = "数据不存在";

    /**
     * 根据传入的数据已存在，重复.
     */
    public static final String ERRORCODE_DATA_EXISTS_MSG            = "数据已存在";
}
