package com.cgnpc.bbxpark.ioc.websocket;

import cn.hutool.crypto.SecureUtil;
import com.cgnpc.mobile.model.UserTokenDto;
import com.cgnpc.mobile.service.IJwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Token验证器
 * 用于验证WebSocket连接的token有效性
 */
@Component
public class TokenValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);
    
    @Value("${websocket.token.secret:default_secret}")
    private String tokenSecret;
    @Value("${cud.dtalk.tokenSecret:AAQWEFSEFSDFSE2F}")
    private String tokenMD5Key;
    @Value("${cud.dtalk.tokenKey:cud_access_token}")
    private String tokenKey;
    @Autowired
    IJwtTokenService jwtTokenService;
    
    /**
     * 验证token是否有效
     * @param token 待验证的token
     * @param userId 用户ID
     * @return true表示token有效，false表示无效
     */
    public boolean validateToken(String token, String userId) {
        try {
            UserTokenDto userTokenVO = this.jwtTokenService.verifyToken(token, SecureUtil.md5(this.tokenMD5Key));
            return userTokenVO != null && userTokenVO.getUserID().equals(userId);
//            // 这里应该调用实际的token验证逻辑
//            // 目前使用简单的验证方式作为示例
//
//            if (token == null || token.isEmpty()) {
//                return false;
//            }
//
//            // 示例验证逻辑：检查是否为预设的有效token
//            // 实际项目中应该调用认证服务进行验证
//            if ("valid_token_for_testing".equals(token)) {
//                return true;
//            }
//
//            // 可以在这里调用DingTokenController的验证接口
//            // 或者直接验证JWT token等
//
//            logger.debug("Token验证通过 - userId: {}", userId);
//            return true;
            
        } catch (Exception e) {
            logger.error("Token验证异常 - userId: {}", userId, e);
            return false;
        }
    }
    
    /**
     * 生成测试用的临时token
     * 实际项目中应该由认证服务生成
     */
    public String generateTestToken(String userId) {
        // 简单的测试token生成逻辑
        return "valid_token_for_testing";
    }
}