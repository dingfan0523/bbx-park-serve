package com.cgnpc.bbxpark.common.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.cud.core.dto.BaseDto;


public class CudResult<T> extends BaseDto {

    private String code;
    private String msg;
    private T data;
    private Long total;

    public CudResult() {
    }

    public CudResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CudResult<T> success() {
        CudResult<T> ret = new CudResult<T>();
        ret.setCode("0");
        ret.setMsg("操作成功");
        return ret;
    }

    public static <T> CudResult<T> success(T data, String message) {
        CudResult<T> ret = success();
        ret.setMsg(message);
        ret.setData(data);
        pageResult(data,ret);
        return ret;
    }

    public static <T> CudResult<T> success(T data) {
        CudResult<T> ret = success();
        pageResult(data, ret);
        return ret;
    }

    private static <T> void pageResult(T data, CudResult<T> ret) {
        if (data != null && data instanceof IPage) {
            IPage page = (IPage) data;
            ret.setData((T) page.getRecords());;
            ret.setTotal(page.getTotal());
        } else {
            ret.setData(data);
        }
    }

    public static <T> CudResult<T> successMessage(String message) {
        CudResult<T> ret = success();
        ret.setMsg(message);
        return ret;
    }

    public static <T> CudResult<T> error() {
        CudResult<T> ret = new CudResult<T>();
        ret.setCode("1");
        ret.setMsg("操作失败");
        return ret;
    }

    public static <T> CudResult<T> error(T data, String message) {
        CudResult<T> ret = error();
        ret.setMsg(message);
        ret.setData(data);
        return ret;
    }

    public static <T> CudResult<T> errorData(T data) {
        CudResult<T> ret = error();
        ret.setData(data);
        return ret;
    }

    public static <T> CudResult<T> errorMessage(String message) {
        CudResult<T> ret = error();
        ret.setMsg(message);
        return ret;
    }

    public String getCode() {
        return this.code;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    protected void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    protected void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
