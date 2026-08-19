package com.cgnpc.bbxpark.message.dto.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfan
 * @version 1.0
 * @date 2024/10/29 9:05
 */
@Data
public class CommonUser implements Serializable {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户钉钉id
     */
    private String thirdUserId;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户手机号
     */
    private String telephone;
}
