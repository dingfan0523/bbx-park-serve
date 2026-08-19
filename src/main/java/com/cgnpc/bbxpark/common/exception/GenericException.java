package com.cgnpc.bbxpark.common.exception;


import com.cgnpc.bbxpark.common.base.CustomMessageResultCode;
import com.cgnpc.bbxpark.common.base.IResultCode;
import com.cgnpc.bbxpark.common.constant.SystemResultCode;
import com.cgnpc.bbxpark.common.enums.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class GenericException extends RuntimeException {

    /**
     * .
     */
    private static final long serialVersionUID = 7815426752583648734L;
    /**
     * errcode.
     */
    @Getter
    private int subMsgCode;                             // Exception不能有子类，异常类型用ErrorCode表示，以便保持兼容。
    /**
     * ErrorCode.
     */
    @Getter
    private ErrorCode errorCode;                              // Exception不能有子类，异常类型用ErrorCode表示，以便保持兼容。

    @Getter
    private String subMsg;
    /**
     * .
     */
    public GenericException() {
        super();
    }

    /**
     *
     * @param resultCode 业务异常：业务方自己定义
     * @param errorCode  外层异常：提供几个通用的定义
     */
    public GenericException(IResultCode resultCode, ErrorCode errorCode){
        super(resultCode.getMessage());
        this.subMsgCode = resultCode.getCode();
        this.subMsg = resultCode.getMessage();
        this.errorCode=errorCode;
    }

    public GenericException(Integer subMsgCode, String subMsg, ErrorCode errorCode){
        super(subMsg);
        this.subMsgCode = subMsgCode;
        this.subMsg = subMsg;
        this.errorCode = errorCode;
    }

    /**
     * .
     *
     * @param cause
     *            .
     */
    public GenericException(IResultCode resultCode,ErrorCode errorCode,Throwable cause) {
        super(resultCode.getMessage(),cause);
        this.subMsgCode = resultCode.getCode();
        this.subMsg = resultCode.getMessage();
        this.errorCode=errorCode;
    }

    public GenericException(Integer subMsgCode, String subMsg, ErrorCode errorCode,Throwable cause){
        super(subMsg,cause);
        this.subMsgCode = subMsgCode;
        this.subMsg = subMsg;
        this.errorCode = errorCode;
    }

    public static GenericException success(IResultCode resultCode){
        return new GenericException(resultCode,ErrorCode.SUCCESS);
    }

    public static GenericException success(IResultCode resultCode,Throwable cause){
        return new GenericException(resultCode,ErrorCode.SUCCESS,cause);
    }

    public static GenericException fail(IResultCode resultCode){
        return new GenericException(resultCode,ErrorCode.FAIL);
    }

    public static GenericException fail(IResultCode resultCode, Object[] msgParams){
        String message = doFormat(resultCode.getCode(), resultCode.getMessage(), msgParams);
        return GenericException.fail(new CustomMessageResultCode(resultCode, message));
    }

    public static GenericException fail(IResultCode resultCode,Throwable cause){
        return new GenericException(resultCode,ErrorCode.FAIL,cause);
    }

    public static GenericException fail(String message){
        if (message == null) {
            message = "";
        }
        return GenericException.fail(new CustomMessageResultCode(SystemResultCode.FAIL, message));
    }

    public static GenericException fail(String message,Throwable cause){
        return GenericException.fail(new CustomMessageResultCode(SystemResultCode.FAIL, message),cause);
    }

    public static String doFormat(int code, String messagePattern, Object... params) {
        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);
        int i = 0;
        int j;
        int l;
        for (l = 0; l < params.length; l++) {
            j = messagePattern.indexOf("{}", i);
            if (j == -1) {
                log.error("[doFormat][参数过多：错误码({})|错误内容({})|参数({})", code, messagePattern, params);
                if (i == 0) {
                    return messagePattern;
                } else {
                    sbuf.append(messagePattern.substring(i));
                    return sbuf.toString();
                }
            } else {
                sbuf.append(messagePattern, i, j);
                sbuf.append(params[l]);
                i = j + 2;
            }
        }
        if (messagePattern.indexOf("{}", i) != -1) {
            log.error("[doFormat][参数过少：错误码({})|错误内容({})|参数({})", code, messagePattern, params);
        }
        sbuf.append(messagePattern.substring(i));
        return sbuf.toString();
    }


}
