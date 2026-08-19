package com.cgnpc.bbxpark.acl.haikang.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : yzguf
 * @description : 海康返回结果
 * @Version 1.0
 * @date : 2023/10/17
 */
@Data
public class HkResult implements Serializable {
    private static final long serialVersionUID = 3187018432235655092L;
    private String code;
    private String msg;
    private Object data;
    private Boolean success;


    public boolean isSuccess(){
        return "0".equals(code);
    }
}
