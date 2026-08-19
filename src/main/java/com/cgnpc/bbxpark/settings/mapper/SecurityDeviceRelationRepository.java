
package com.cgnpc.bbxpark.settings.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.settings.domain.SecurityDeviceRelation;
import com.cgnpc.bbxpark.settings.dto.model.SecurityDeviceRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 安全管理员设备关联数据操作接口
 * @author huangyongtao
 * @date 2025/8/1 11:29
 */
@Repository
public interface SecurityDeviceRelationRepository extends BaseMapper<SecurityDeviceRelation> {

    List<Long> queryAllDeviceIdByLoginUser(@Param("securityStaffid") String securityStaffid, @Param("tenantId") Long tenantId);

    List<SecurityDeviceRelationModel> findAllBySpaceIds(@Param("spaceIds") List<Long> spaceIds);

}
