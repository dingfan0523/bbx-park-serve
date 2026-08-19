
package com.cgnpc.bbxpark.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.space.domain.TenantMember;
import com.cgnpc.bbxpark.space.dto.model.TenantMemberModel;
import com.cgnpc.bbxpark.space.dto.param.TenantMemberListParam;
import org.apache.ibatis.annotations.Param;


public interface TenantMemberRepository extends BaseMapper<TenantMember> {
    IPage<TenantMemberModel> listPage(IPage<TenantMemberListParam> page, @Param("condition") TenantMemberListParam condition);
//	/**
//	 * 构造函数.
//	 */
//	public TenantMemberRepository() {
//
//		this.setNamespace("com.cgnpc.bbxpark.service.uic.tenantMember");
//	}
//
//	public int deleteByUserIdListAndTenantId(List<String> userIdList,
//											 Long tenantId
//	) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userIdList", userIdList);
//		params.put("tenantId", tenantId);
//		return getSqlSessionTemplate().delete(getNamespace() + "." + "deleteByUserIdListAndTenantId", params);
//	}
//
//	public IPage<TenantMemberModel>  listPage(PaginationEntity<TenantMemberDomain> pagination) {
//		int page = pagination.getPage() == null ? 1 : pagination.getPage() == 0 ? 1 : pagination.getPage(); // 默认为第一页
//		int size = pagination.getSize() == null ? Constants.DEFAULT_PAGE_SIZE : pagination.getSize(); // 默认每页15个
//
//		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
//		Map<Object, Object> param = pagination.getParams();
//		if (param != null) {
//			param.put("orderColumn", pagination.getOrderColumn());
//			param.put("orderTurn", pagination.getOrderTurn());
//		} else {
//			param = new HashMap<Object, Object>();
//			param.put("orderColumn", pagination.getOrderColumn());
//			param.put("orderTurn", pagination.getOrderTurn());
//		}
//		List<TenantMemberModel> resultList = getSqlSessionTemplate().selectList(getNamespace() + ".listPage" , param, rowBounds);
//		int total = getSqlSessionTemplate().selectOne(getNamespace() + ".listPageCount" , param);
//
//		IPage<TenantMemberModel> pagingResult = new IPage<TenantMemberModel>();
//		pagingResult.setPage(page);
//		pagingResult.setSize(size);
//		pagingResult.setTotal(total);
//		pagingResult.setResult(resultList);
//		return pagingResult;
//	}
//
//	public int removeNoMemberRoleUser(Long tenantId) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("tenantId", tenantId);
//		return getSqlSessionTemplate().delete(getNamespace() + "." + "removeNoMemberRoleUser", params);
//	}
}
