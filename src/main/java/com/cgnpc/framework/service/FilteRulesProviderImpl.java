package com.cgnpc.framework.service;

import com.cgnpc.cud.shiro.domain.RolePermRule;
import com.cgnpc.cud.shiro.service.ShiroFilterRulesProvider;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/******************************
 * 用途说明: 动态过滤规则提供者实现
 * 作者姓名: pxmwlin
 * 创建时间: 2019/11/20 14:39
 ******************************/
@Service
public class FilteRulesProviderImpl implements ShiroFilterRulesProvider{
	
//	@Autowired
//	private JdbcEnhance jdbcEnhance;
	
	/**********************************
	* 用途说明: 加载基于角色/资源的过滤规则
	 * <br>大部分系统的安全体系都是RBAC(基于角色的权限访问控制)授权模型。
	 * <br>即：用户--角色--资源(URL),对应关系可配并且存储在数据库中。
	 * <br>此方法提供的数据为：RolePermRule{url资源地址、needRoles需要的角色列表}
	 * <br>在shiro中生成的过滤器链为：url=roles[角色1、角色2、角色n]
	 * <br>当用户持有[角色1、角色2、角色n]中的任何一个角色，则给予访问，否则不予访问
	 *
	 * <br>权限指用户能操作资源的统称、角色则说权限的集合。
	 * <br>权限授权模型直接表示为：用户--资源(URL)。
	 * <br>此方法提供的数据格为：PermRule{url资源地址、needPerms需要的权限列表}
	 * <br>在shiro中生成的过滤器链为：url=perms[权限编码1、权限编码2、权限编码n]
	 * <br>当用户持有[权限编码1、权限编码2、权限编码n]中的任何一个权限，则给予访问，否则不予访问
	* 参数说明
	* 返回值说明:
	***********************************/
	@Override
	public List<RolePermRule> loadRolePermRules(){
//		 List<RolePermRule> rolePermRules = this.jdbcEnhance
//				 	.selector()
//				 	.SELECT("URL,GROUP_CONCAT(T.ROLE_ID ORDER BY R.URL) NEED_ROLES")
//					.FROM("T_ROLE_RESOURCE T")
//					.JOIN("T_RESOURCE R ON T.RESOURCE_ID = R.ID")
//					.GROUP_BY("R.URL")
//					.entityClass(RolePermRule.class)
//					.list();
		//return rolePermRules;
		List<RolePermRule> hmacRules = Lists.newLinkedList();
		return hmacRules;
	}
	
	/**********************************
	* 用途说明: HMAC数字签名鉴权过滤规则配置
	* 参数说明 
	* 返回值说明: 
	***********************************/
	@Override
	public List<RolePermRule> loadHmacRules() {
		/// HMAC数字签名鉴权过滤规则配置
		/// 为了展示数据原貌使用了编码配置
		/// 真实场景中可以通过界面配置，比如做一个"接口管理"功能
		/// 或者放到"资源管理"中和菜单、按钮一块管理
		/// 删除操作需要通过HMAC数字摘要认证并且具有"role_admin"角色
		RolePermRule hmacRule1 = new RolePermRule();
		hmacRule1.setUrl("/hmac_api/delete");
		hmacRule1.setNeedRoles("role_admin");
		/// 其余操作需要通过HMAC数字摘要认证
		RolePermRule hmacRule2 = new RolePermRule();
		hmacRule2.setUrl("/hmac_api/**");
		List<RolePermRule> hmacRules = Lists.newLinkedList();
		hmacRules.add(hmacRule1);
		hmacRules.add(hmacRule2);
		return hmacRules;
	}

	/**********************************
	* 用途说明:JWT令牌鉴权过滤规则配置
	* 参数说明
	* 返回值说明:
	***********************************/
	@Override
	public List<RolePermRule> loadJwtRules() {
		/// JWT令牌鉴权过滤规则配置
		/// 为了展示数据原貌使用了编码配置
		/// 真实场景中可以通过界面配置，比如做一个"接口管理"功能
		/// 或者放到"资源管理"中和菜单、按钮一块管理
		
		/// 删除操作需要通过JWT令牌认证并且具有"role_admin"角色
		RolePermRule jwtRule1 = new RolePermRule();
		jwtRule1.setUrl("/jwt_api/delete");
		jwtRule1.setNeedRoles("role_admin");
		/// 其余操作需要通过JWT令牌认证
//		RolePermRule jwtRule2 = new RolePermRule();
//		jwtRule2.setUrl("/tool/**");
//		RolePermRule jwtRule3 = new RolePermRule();
//		jwtRule3.setUrl("/module/**");
		List<RolePermRule> jwtRules = Lists.newLinkedList();
		jwtRules.add(jwtRule1);
		//jwtRules.add(jwtRule2);
		//jwtRules.add(jwtRule3);
		return jwtRules;
	}

	/**********************************
	* 用途说明:AEP令牌鉴权过滤规则配置
	* 参数说明
	* 返回值说明:
	***********************************/
	@Override
	public List<RolePermRule> loadAepRules() {
		/// AEP令牌鉴权过滤规则配置
		/// 为了展示数据原貌使用了编码配置
		/// 真实场景中可以通过界面配置，比如做一个"接口管理"功能
		/// 或者放到"资源管理"中和菜单、按钮一块管理

		/// 删除操作需要通过AEP令牌认证并且具有"role_admin"角色
		RolePermRule aepRule1 = new RolePermRule();
		aepRule1.setUrl("/aep_api/delete");
		aepRule1.setNeedRoles("role_admin");
		/// 其余操作需要通过JWT令牌认证
		RolePermRule aepRule2 = new RolePermRule();
		aepRule2.setUrl("/tool/**");
		RolePermRule aepRule3 = new RolePermRule();
		aepRule3.setUrl("/module/person/**");
		List<RolePermRule> jwtRules = Lists.newLinkedList();
		jwtRules.add(aepRule1);
		jwtRules.add(aepRule2);
		jwtRules.add(aepRule3);
		return jwtRules;
	}

//	@Override
//	public List<CustomAuthorizeRule> loadCustomRules() {
//		return null;
//	}
	
}