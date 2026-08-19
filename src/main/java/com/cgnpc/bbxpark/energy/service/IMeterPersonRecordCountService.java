
package com.cgnpc.bbxpark.energy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.energy.domain.MeterPersonRecordCount;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordBranchCountModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordCountModel;
import com.cgnpc.bbxpark.energy.dto.model.MeterRecordSonBranchCountModel;
import com.cgnpc.bbxpark.energy.dto.param.MeterRecordCountParam;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/***
 * @Description 抄表人工抄表记录统计服务接口
 * @author huangyongtao
 * @date 2025/4/21 9:27
 */
public interface IMeterPersonRecordCountService extends IService<MeterPersonRecordCount> {

    /***
     * @Description 查询最新时间
     * @author huangyongtao
     * @date 2025/4/23 9:11
     * @param param
     */
    MeterRecordCountModel findNewTime(MeterRecordCountParam param);

    /***
     * @Description 能耗概览
     * @author huangyongtao
     * @date 2025/4/23 9:11
     * @param param
     */
    MeterRecordCountModel energyCount(MeterRecordCountParam param);

    /***
     * @Description 支路能耗趋势
     * @author huangyongtao
     * @date 2025/4/23 9:12
     * @param param
     */
    List<MeterRecordBranchCountModel> energyBranchCount(MeterRecordCountParam param);

    /**
     * 支路能耗趋势（指定时间和支路id集合）
     */
    List<MeterRecordBranchCountModel> energyBranchCount(MeterRecordCountParam param, Date date,List<Long> branchIds);

    /***
     * @Description 支路能耗趋势导出
     * @author huangyongtao
     * @date 2025/4/24 10:39
     * @param response
     * @param param
     */
    Boolean energyBranchCountExport(HttpServletResponse response, MeterRecordCountParam param);

    /***
     * @Description 支子路能耗统计
     * @author huangyongtao
     * @date 2025/4/23 9:12
     * @param param
     */
    List<MeterRecordSonBranchCountModel> energySonBranchCount(MeterRecordCountParam param);

    /***
     * @Description 移动端支子路能耗统计
     * @author huangyongtao
     * @date 2025/4/23 9:12
     * @param param
     */
    List<MeterRecordSonBranchCountModel> appEnergySonBranchCount(MeterRecordCountParam param);

    /***
     * @Description 子支路能耗统计导出
     * @author huangyongtao
     * @date 2025/4/24 10:39
     * @param response
     * @param param
     */
    Boolean energySonBranchCountExport(HttpServletResponse response, MeterRecordCountParam param);

    /***
     * @Description 生成人工抄表集抄数据
     * @author huangyongtao
     * @date 2025/4/22 9:30
     */
    Boolean executePersonReadingCount();
}
