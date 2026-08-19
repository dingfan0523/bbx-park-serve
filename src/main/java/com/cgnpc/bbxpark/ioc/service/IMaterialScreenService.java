package com.cgnpc.bbxpark.ioc.service;


import com.cgnpc.bbxpark.ioc.dto.model.*;

import java.util.List;

/**
 * 大屏材料统计接口
 */
public interface IMaterialScreenService {

    /**
     * 库存分析
     * @return
     */
    MaterialInventoryAnalysisModel getMaterialInventory(Long type);

    /**
     * 库存健康度
     * @return
     */
    List<MaterialInventoryHealthModel> getMaterialInventoryHealth();

    /**
     * 库存周转
     * @return
     */
    List<MaterialTurnoverModel> getMaterialTurnover();

    /**
     * sku关联分析
     * @return
     */
    List<MaterialWorkUseModel> getMaterialWorkUse();

    /**
     * 智能分析建议
     * @return
     */
    List<MaterialSmartSuggestModel> getMaterialSmartSuggest();


    /**
     * 耗材周转排行榜
     * @return
     */
    List<MaterialTurnoverRankModel> getMaterialTurnoverRank(Long type);

    /**
     * 器材使用排行榜
     * @return
     */
    List<MaterialUsageRankModel> getMaterialUsageRank(Long type);


    /**
     * 材料空间统计
     * @return
     */
    List<MaterialSpaceCountModel> getMaterialSpaceCount(String sslcCode);

    /**
     * 材料空间楼层高亮展示列表
     * @return
     */
    List<SpaceViewModel> getMaterialSpaceView(String sslcCode);

    /**
     * 大屏-材料的告警率
     * @return
     */
    Double getMaterialAlarmRate();
}
