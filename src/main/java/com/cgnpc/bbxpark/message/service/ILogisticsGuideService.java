
package com.cgnpc.bbxpark.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.common.web.CudPageDto;
import com.cgnpc.bbxpark.message.domain.LogisticsGuide;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuideListParam;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuidePageParam;
import com.cgnpc.bbxpark.message.dto.req.LogisticsGuideParam;
import com.cgnpc.bbxpark.message.dto.resp.AppLogisticsGuideDetailModel;
import com.cgnpc.bbxpark.message.dto.resp.AppLogisticsGuideModel;
import com.cgnpc.bbxpark.message.dto.resp.LogisticsGuideDetailModel;
import com.cgnpc.bbxpark.message.dto.resp.LogisticsGuideListModel;

import java.util.List;

/**
 * 后勤指南服务接口
 * @author dingfan
 * @date 2024/10/12 13:56
 */
public interface ILogisticsGuideService extends IService<LogisticsGuide> {
    /**
     * PC端-后勤指南分页列表
     *
     * @Param param 后勤指南分页查询条件
     * @Return 后勤指南信息列表（分页）
     */
    IPage<LogisticsGuideListModel> page(LogisticsGuidePageParam param);

    /**
     * PC端-后勤指南列表
     *
     * @Param param 后勤指南查询条件
     * @Return 后勤指南列表
     */
    List<LogisticsGuideListModel> list(LogisticsGuideListParam param);

    /**
     * PC端-后勤指南详情
     *
     * @Param [id] 后勤指南标识
     * @Return 后勤指南详情信息
     */
    LogisticsGuideDetailModel detail(Long id);

    /**
     * PC端-新增后勤指南.
     *
     * @Param param 后勤指南信息
     * @Return 新增后勤指南是否成功
     */
    Boolean add(LogisticsGuideParam param);

    /**
     * PC端-编辑后勤指南信息.
     *
     * @Param param 后勤指南信息
     * @Return 编辑后勤指南是否成功
     */
    Boolean edit(LogisticsGuideParam param);

    /**
     * PC端-删除后勤指南.
     *
     * @Param id 后勤指南标识
     * @Return 删除后勤指南是否成功
     */
    Boolean remove(Long id);

    /**
     * 移动端-后勤指南分页列表
     *
     * @Param param 后勤指南分页查询条件
     * @Return 后勤指南信息列表（分页）
     */
    IPage<AppLogisticsGuideModel> pageApp(CudPageDto param);

    /**
     * 移动端-后勤指南列表
     *
     * @Param param 后勤指南查询条件
     * @Return 后勤指南列表
     */
    List<AppLogisticsGuideModel> listApp();

    /**
     * 移动端-后勤指南详情
     *
     * @Param [id] 后勤指南标识
     * @Return 后勤指南详情信息
     */
    AppLogisticsGuideDetailModel detailApp(Long id);
}
