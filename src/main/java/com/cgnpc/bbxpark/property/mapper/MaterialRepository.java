package com.cgnpc.bbxpark.property.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.property.domain.Material;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * @Description 材料数据操作接口
 * @author huangyongtao
 * @date 2025/9/22 16:12
 */
@Repository
public interface MaterialRepository extends BaseMapper<Material> {

    /**
     * 材料大屏-获取材料的空间集合
     * @param tenantId
     * @return
     */
    List<Long> getSpaceIdList(@Param("tenantId") Long tenantId);

    /**
     * 材料大屏-材料区域统计
     * @param tenantId
     * @return
     */
    List<MaterialSpaceCountModel> getMaterialSpaceCount(@Param("tenantId") Long tenantId);

    /**
     * 材料大屏-材料类型统计
     * @param tenantId
     * @return
     */
    List<MaterialInventoryTypeModel> getMaterialTypeCount(@Param("tenantId") Long tenantId);

    /**
     * 材料大屏-材料库存健康度
     * @param tenantId
     * @return
     */
    List<MaterialInventoryHealthModel> getMaterialInventoryHealth(@Param("tenantId") Long tenantId);

    /**
     * 材料大屏-材料库周转率排行榜
     * @param tenantId
     * @return
     */
    List<MaterialTurnoverRankModel> getMaterialTurnoverRank(@Param("tenantId") Long tenantId, @Param("type") Long type);

    /**
     * 材料大屏-材料库使用率排行榜
     * @param tenantId
     * @return
     */
    List<MaterialUsageRankModel> getMaterialUsageRank(@Param("tenantId") Long tenantId, @Param("type") Long type);

    /**
     * 大屏-材料的告警率
     * @param tenantId
     * @return
     */
    Double getMaterialAlarmRate(@Param("tenantId") Long tenantId);

}
