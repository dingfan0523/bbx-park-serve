package com.cgnpc.framework.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.cud.shiro.domain.Account;
import com.cgnpc.framework.domain.enums.TypeEnum;

/******************************
 * 用途说明: 系统用户表 实体类
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@TableName("cud_sys_user_t")
public class SysUser implements Serializable, Account {

    private static final long serialVersionUID = -8924832687376053347L;
    /**
     * 用户ID
     */
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 用户名
     */
    // 这样可以注入 LIKE 查询 @TableField(condition = SqlCondition.LIKE)
    private String name;
    /**
     * 通用枚举测试
     */
    private TypeEnum type;
    /**
     * 用户年龄
     */
    private Integer age;
    /**
     * 自定义填充的创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 这样可以注入更新数据库时间 , update = "now()")// 该注解插入忽略验证，自动填充
    private Date ctime;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public Date getCtime() {
        if(ctime==null){
            return null;
        }
        else{
         return    (Date)ctime.clone();
        }
        //return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime == null? null: (Date) ctime.clone();
    }

    @Override
    public String getAccount() {
        return this.name;
    }

    @Override
    public String getPassword() {
        return "319e69997d1d4d59787de9cc081c370c";
    }
}
