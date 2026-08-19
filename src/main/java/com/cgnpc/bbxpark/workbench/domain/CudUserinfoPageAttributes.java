package com.cgnpc.bbxpark.workbench.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "cud用户信息")
@TableName("cud_userinfo_page_attributes")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CudUserinfoPageAttributes extends Model<CudUserinfoPageAttributes> {

    @ApiModelProperty("主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "page请求ID", required = true)
    @NotBlank(message = "page请求ID不能为空")
    private String queryId;

    @ApiModelProperty("自定义字段")
    @TableField(exist = false)
    private List<String> queryFields;

    @ApiModelProperty("自定义字段")
    private String jsonFields;

    @ApiModelProperty("设置固定值")
    private Boolean isFixed;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("创建时间")
    private Date createDate;
}
