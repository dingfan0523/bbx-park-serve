/*
 * Copyright 2017-2018 the original author(https://github.com/wj596)
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package com.cgnpc.framework.service;

import com.cgnpc.cud.shiro.handler.PasswordHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/******************************
 * 用途说明: 密码错误次数超限处理器实现集成自PasswordHandler 此处演示锁定用户
 * 作者姓名: pxmwlin
 * 创建时间: 2019/8/26 9:20
 ******************************/
@Service
public class PasswordHandlerImpl implements PasswordHandler {

    //logger 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordHandlerImpl.class);

//    @Autowired
//    private UserService userService;

    /**********************************
    * 用途说明: 锁定账户
    * 参数说明 account
    * 返回值说明:
    ***********************************/
    @Override
    public void handle(String account) {
        //锁定账号
        // userService.updateStatus(account, UserEntity.USER_STATUS_LOCKED);
        LOGGER.warn("账号：" + account + "密码错误超过5次，已锁定");
    }
}