package com.cgnpc.bbxpark.ioc.dto.model;

import lombok.Data;

import java.util.List;

/**
 * 今日站点信息模型
 */
@Data
public class TrafficSiteInfoModel {
    /**
     * 调度电话
     */
    private String dispatchPhone;

    /**
     * 站点列表
     */
    private List<SiteDetail> sites;

    /**
     * 站点详情
     */
    @Data
    public static class SiteDetail {
        /**
         * 线路（含该站点）
         */
        private String lineName;

        /**
         * 发车时间
         */
        private String departureTime;

        /**
         * 站点类型（起始站、过路站、终点站）
         */
        private String siteType;
    }
}