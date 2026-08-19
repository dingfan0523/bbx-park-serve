package com.cgnpc.framework.domain.enums;



import com.baomidou.mybatisplus.core.enums.IEnum;

import java.io.Serializable;


/******************************
 * 用途说明: 必须现在 IEnum 配置 该包扫描自动注入，查看文件 spring-mybatis.xml 参数 typeEnumsPackage
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
public enum TypeEnum implements IEnum {
    //禁用状态
    DISABLED(0, "禁用"),
    //可用状态
    NORMAL(1, "正常");

    private final int value;
    private final String desc;

    TypeEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    // Jackson 注解为 JsonValue 返回中文 json 描述
    public String getDesc() {
        return this.desc;
    }
}
