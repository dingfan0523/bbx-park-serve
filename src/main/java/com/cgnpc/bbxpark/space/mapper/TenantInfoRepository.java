
package com.cgnpc.bbxpark.space.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.space.domain.TenantInfo;
import com.cgnpc.bbxpark.space.dto.param.TenantPageParam;
import org.apache.ibatis.annotations.Param;
// @Repository
public interface TenantInfoRepository extends BaseMapper<TenantInfo> {
    IPage<TenantInfo> pageFullInfo(IPage<TenantPageParam> page, @Param("condition") TenantPageParam condition);

//	public <T> IPage<T> pageFullInfo(PaginationEntity<TenantPageParam> pagination) {
//		int page = pagination.getPage() == null ? 1 : pagination.getPage() == 0 ? 1 : pagination.getPage(); // 默认为第一页
//		int size = pagination.getSize() == null ? Constants.DEFAULT_PAGE_SIZE : pagination.getSize(); // 默认每页15个
//
//		RowBounds rowBounds = new RowBounds((page - 1) * size, size);
//
//		Map<Object, Object> param = pagination.getParams();
//		if (param != null) {
//			param.put("orderColumn", pagination.getOrderColumn());
//			param.put("orderTurn", pagination.getOrderTurn());
//		} else {
//			param = new HashMap<Object, Object>();
//			param.put("orderColumn", pagination.getOrderColumn());
//			param.put("orderTurn", pagination.getOrderTurn());
//		}
//
//		List<T> resultList = getSqlSessionTemplate().selectList(getNamespace() + ".pageFullInfo" , param, rowBounds);
//		int total = getSqlSessionTemplate().selectOne(getNamespace() + ".countPageFullInfo" , param);
//
//		IPage<T> pagingResult = new IPage<T>();
//		pagingResult.setPage(page);
//		pagingResult.setSize(size);
//		pagingResult.setTotal(total);
//		pagingResult.setResult(resultList);
//		return pagingResult;
//	}
}
