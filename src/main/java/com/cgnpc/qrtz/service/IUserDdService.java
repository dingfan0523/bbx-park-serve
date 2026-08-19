package com.cgnpc.qrtz.service;

/**
 * <p>
 * 钉钉免登 服务类
 * </p>
 *
 * @author P627253 喻佩
 * @since 2022-11-01
 */
public interface IUserDdService {
    /**
     * 获取钉钉登录的token
     *
     * @return token
     */
    String getToken();

    /**
     * 获取钉钉的用户信息
     *
     * @param accessToken token
     * @param code        code
     * @return 用户信息
     */
    String getUserInfo(String accessToken, String code);

    /**
     * 获取用户信息
     *
     * @param accessToken token
     * @param userId      用户ID
     * @return
     */
    String get(String accessToken, String userId);

}
