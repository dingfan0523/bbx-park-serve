package com.cgnpc.bbxpark.ioc.service;

import com.cgnpc.bbxpark.ioc.dto.model.*;

import java.util.List;

public interface IScreenWaterService {

    /**
     * 能管宏观成效
     * @param year
     * @param month
     * @return
     */
    WaterOverviewModel getWaterOverview(Integer year,Integer month);

    /**
     * 用水趋势分析
     * @param year
     * @param month
     * @return
     */
    List<WaterTrendModel> getWaterTrend(Integer year, Integer month);

    /**
     * 区域能耗对比
     */
    List<WaterAreaCompareModel> getAreaCompare(Long id,Integer year,Integer month);

    List<WaterLossRateModel> getLossRate(Long branchId,Integer year,Integer month);
    /**
     * 人均用水趋势分析
     * @param year
     * @param month
     * @return
     */
    List<WaterPerCapitaTrendModel> getPerCapitaTrend(Integer year, Integer month);

    List<WaterRealtimeFlowRankModel> getRealtimeFlowRank(String type,Integer year,Integer month);
}
