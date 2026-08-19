package com.cgnpc.bbxpark.message.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangyongtao
 * @Description 消息中心的用户信息
 * @date 2024/10/28 17:21
 */
@Data
public class MessageUserInfoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户标识.")
    private String id;

    @ApiModelProperty(value = "邮箱地址.")
    private String email;

    @ApiModelProperty(value = "移动电话.")
    private String mobile;

    @ApiModelProperty(value = "用户昵称.")
    private String nickName;

    @ApiModelProperty(value = "性别，0未知1男2女3保密.")
    private Integer sex;

    private String sexDesc;

    @ApiModelProperty(value = "工号.")
    private String staffid;

    @ApiModelProperty(value = "第三方用户id")
    private String thirdUserId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
