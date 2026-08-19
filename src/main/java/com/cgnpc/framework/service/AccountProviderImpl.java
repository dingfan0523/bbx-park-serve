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


import com.cgnpc.cud.shiro.domain.Account;
import com.cgnpc.cud.shiro.service.AccountProvider;
import com.cgnpc.framework.domain.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/******************************
 * 用途说明: 账号信息提供者实现
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Service
public class AccountProviderImpl implements AccountProvider {

//	@Autowired
//	private UserService userService;
//	@Autowired
//	private UserRoleService userRoleService;
	
	/**********************************
	* 用途说明: 
	* 参数说明 account
	* 返回值说明:
	***********************************/
	@Override
	public Account loadAccount(String account) throws AuthenticationException {
		SysUser user=new SysUser();
		user.setId(1L);
		user.setName(account);
//		UserEntity user = userService.getByAccount(account);
//		// 用户不存在
//		if(null == user){
//			throw new AuthenticationException("账号或密码错误");
//		}
//		// 对账号做检查
//		// 当账号异常，如账号被锁定、被禁用等等需要限制登陆，直接抛出AuthenticationException即可
//		if(UserEntity.USER_STATUS_LOCKED == user.getStatus()){
//			throw new AuthenticationException("账号已被锁定，请联系系统管理员");
//		}
		return user;
		//return null;
	}
	
	
	/**********************************
	* 用途说明: 加载用户持有的角色
	* 参数说明 account
	* 返回值说明:
	***********************************/
	@Override
	public Set<String> loadRoles(String account) {

		 return new HashSet<>(Arrays.asList("role_admin"));
	}
	
	
	/**********************************
	* 用途说明: 系统采用  基于角色的权限访问控制(RBAC)策略
	 * 所谓的权限通常可以理解为用户所能操作的资源，如（user:add、user:delete）
	 * 此方法未实现
	* 参数说明 account
	* 返回值说明:
	***********************************/
	@Override
	public Set<String> loadPermissions(String account) {
		return null;
	}
}