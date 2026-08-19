package com.cgnpc.bbxpark.common.base;

public class CustomMessageResultCode implements IResultCode{
    private String message;

    private int code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getCode() {
        return code;
    }

    public CustomMessageResultCode(IResultCode resultCode, String message){
        this.code=resultCode.getCode();
        this.message=message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
