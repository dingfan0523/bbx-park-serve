package com.cgnpc.dingtalk.controller;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.cgnpc.cud.core.domain.AjaxResult;
import com.cgnpc.mobile.model.UserDto;
import com.cgnpc.mobile.model.UserTokenDto;
import com.cgnpc.mobile.service.IJwtTokenService;
import com.cgnpc.mobile.service.IUserService;
import com.cgnpc.mobile.utils.MobileUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import static com.cgnpc.cud.core.domain.AjaxResult.error;

@RestController
@Slf4j
@RequestMapping("/api/ding")
@Profile({"dev","test","local","front","zhang"})
public class DingTokenController {

    @Value("${cud.dtalk.tokenSecret:AAQWEFSEFSDFSE2F}")
    private  String tokenMD5Key;

    @Autowired
    IUserService userService;

    @Autowired
    IJwtTokenService jwtTokenService;

    @PostMapping("/getToken")
    @ResponseBody
    @Profile({"dev","test","local"})
    public AjaxResult getToken(@RequestParam("userNo") String userNo) {
        try {
            UserDto uds = userService.getLoginUser(userNo);
            if (uds == null) {
                return AjaxResult.message("40011", userNo + "信息未录入系统或者黄页数据有重复信息");
            }
            UserTokenDto userTokenDto = jwtTokenService.getDefaultPayloadDto(uds.getUserId(), uds.getPhone(), uds.getUserName(), true);
            //保存用户信息，方便后期调用
            uds.setUserId(MobileUserUtils.getTokenOrCurrentUserId());
            userTokenDto.setUserDto(uds);
            String token = jwtTokenService.generateToken(JSONUtil.toJsonStr(userTokenDto), SecureUtil.md5(tokenMD5Key));
            AjaxResult result = AjaxResult.success().put("token", token);
            result.put("token", token);
            result.put("user", uds);
            return result;
        } catch (Exception e) {
            log.error("获取token失败：", e);
            return error("获取token失败！");
        }
    }

}
