package com.cgnpc.bbxpark.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cgnpc.cud.core.common.util.DateUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/******************************
 * 用途说明:公用实体
 * 作者姓名: P309150
 * 创建时间: 2025/11/17 13:43
 ******************************/
@Data
public class BaseExEntity implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Integer deleted = 1;

    @TableField(fill = FieldFill.INSERT)
    private Long tenantId;

    @TableField(fill = FieldFill.INSERT)
    private String creatorId;

//    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatorId;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public String getCreateTimeStr() {
        return this.createTime != null ? DateUtils.parseDateToStr("yyyy-MM-dd", this.createTime) : "";
    }

    public String getCreateDateTimeStr() {
        return this.createTime != null ? DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", this.createTime) : "";
    }

    public String getUpdateTimeStr() {
        return this.updateTime != null ? DateUtils.parseDateToStr("yyyy-MM-dd", this.updateTime) : "";
    }

    public String getUpdateDateTimeStr() {
        return this.updateTime != null ? DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", this.updateTime) : "";
    }
}
