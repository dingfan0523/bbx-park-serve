package com.cgnpc.bbxpark.common.enums;


import com.cgnpc.bbxpark.common.constant.ErrorCodeConstant;

/**
 * 平台公共错误信息定义
 */
public enum ErrorCode {

    /**
     * 错误码规范：
     * 1、统一错误，平台错误，定义为isp，错误码从200起编制；
     * 2、业务错误，子系统错误，定义为isv，错误码从500起编制；
     * 3、isv必须指定isp父错误编码;
     */

    /**
     * 返回成功.
     */
    SUCCESS(ErrorCodeConstant.SUCCESS, "Success.", ErrorCodeConstant.SUCCESS_MSG),
    /**
     * 请求失败
     */
    FAIL(-1,"Failed.","操作失败"),
    /**
     * 服务内部异常.
     */
    SERVICE_INNER(ErrorCodeConstant.ERRORCODE_SERVICE_INNER, "系统开小差了，请稍候再试",
        ErrorCodeConstant.ERRORCODE_SERVICE_INNER_MSG),
    /**
     * 服务不可用.
     */
    UNAVAILABLE_SERVICE(ErrorCodeConstant.ERRORCODE_UNAVAILABLE_SERVICE, "Service Currently Unavailable.",
        ErrorCodeConstant.ERRORCODE_UNAVAILABLE_SERVICE_MSG),
    /**
     * 非法请求.
     */
    REQUEST_INVALID(ErrorCodeConstant.ERRORCODE_REQUEST_INVALID, "Invalid Request.",
        ErrorCodeConstant.ERRORCODE_REQUEST_INVALID_MSG),
    /**
     * 请求过于频繁.
     */
    REQUEST_LIMITED(ErrorCodeConstant.ERRORCODE_REQUEST_LIMITED, "Service Call Limited.",
        ErrorCodeConstant.ERRORCODE_REQUEST_LIMITED_MSG),
    /**
     * 请求超时.
     */
    REQUEST_TIMEOUT(ErrorCodeConstant.ERRORCODE_REQUEST_TIMEOUT, "Request Timeout.",
        ErrorCodeConstant.ERRORCODE_REQUEST_TIMEOUT_MSG),
    /**
     * 数据不合法.
     */
    DATA_INVALID(ErrorCodeConstant.ERRORCODE_DATA_INVALID, "Invalid Data.",
        ErrorCodeConstant.ERRORCODE_DATA_INVALID_MSG),
    /**
     * 未授权或权限不足.
     */
    INSUFFICIENT_PERMISSION_MSG(ErrorCodeConstant.ERRORCODE_INSUFFICIENT_PERMISSION, "Insufficient Permissions.",
        ErrorCodeConstant.ERRORCODE_INSUFFICIENT_PERMISSION_MSG),

    /**
     * 数据不存在.
     */
    DATA_NOTEXISTS(ErrorCodeConstant.ERRORCODE_DATA_NOTEXISTS, "Data Not Exists.",
                 ErrorCodeConstant.ERRORCODE_DATA_NOTEXISTS_MSG),

    /**
     * 数据已存在.
     */
    DATA_EXISTS(ErrorCodeConstant.ERRORCODE_DATA_EXISTS, "Data Is Exists.",
                   ErrorCodeConstant.ERRORCODE_DATA_EXISTS_MSG);

    /**
     * 错误编码.
     */
    private int    code;
    /**
     * 英文错误信息.
     */
    private String msg;
    /**
     * 中文错误信息.
     */
    private String msgCn;

    /**
     * 私有构造函数.
     *
     * @param code
     *            错误码.
     * @param msg
     *            错误消息.
     * @param msgCn
     *            中文错误消息.
     */
    ErrorCode(int code, String msg, String msgCn) {
        this.code = code;
        this.msg = msg;
        this.msgCn = msgCn;
    }

    ErrorCode() {
    }

    public static ErrorCode valueOfCode(int code) {
        for (ErrorCode obj : ErrorCode.values()) {
            if (java.util.Objects.equals(obj.code, code)) {
                return obj;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgCn() {
        return msgCn;
    }
}
