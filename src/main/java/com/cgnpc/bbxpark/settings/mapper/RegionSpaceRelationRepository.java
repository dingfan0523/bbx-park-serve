
package com.cgnpc.bbxpark.settings.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.settings.domain.RegionSpaceRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 区域管理员空间关联数据操作接口
 * @author huangyongtao
 * @date 2025/3/11 11:22
 */
@Repository
public interface RegionSpaceRelationRepository extends BaseMapper<RegionSpaceRelation> {

    List<Long> queryAllSpaceIdByLoginUser(@Param("regionStaffid") String regionStaffid, @Param("tenantId") Long tenantId);
}
