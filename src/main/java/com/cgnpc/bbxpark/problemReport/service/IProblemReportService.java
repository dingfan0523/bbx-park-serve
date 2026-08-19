package com.cgnpc.bbxpark.problemReport.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cgnpc.bbxpark.meeting.dto.model.ImportReturnModel;
import com.cgnpc.bbxpark.problemReport.domain.ProblemReport;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemReportQueryParam;
import com.cgnpc.bbxpark.problemReport.param.ProblemValidationParam;
import com.cgnpc.bbxpark.workorder.dto.model.WorkOrderRomanModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修服务接口
 */
public interface IProblemReportService extends IService<ProblemReport> {
    /**
     * 保存报事报修
     * @param param 报事报修参数
     * @return 是否成功
     */
    ProblemReportModel save(ProblemReportParam param);

    /**
     * 问题确认
     * @param param 问题确认参数
     * @return 是否成功
     */
    Boolean validationProblem(ProblemValidationParam param);

    /**
     * 分页查询报事报修记录
     * @param param 查询参数
     * @return 报事报修记录列表
     */
    IPage<ProblemReportModel> page(ProblemReportQueryParam param);

    /**
     * 查询报事报修详情
     * @param param
     * @return
     */
    ProblemReportModel detail(ProblemReportQueryParam param);

    /**
     * 报事报修评价
     * @param param
     * @return
     */
    Boolean review(ProblemValidationParam param);

    /**
     * 报事报修上报历史
     * @param param
     * @return
     */
    IPage<ProblemReportModel> reportHistory(ProblemReportQueryParam param);

     List<WorkOrderRomanModel> workOrderProcess(ProblemValidationParam param);

    ProblemReportModel appDetail(ProblemReportQueryParam problemReportQueryParam);

    /**
     * 获取今日的设备报事报修集合
     * @return
     */
    Set<Long> getTodayDeviceIdList();

    /**
     * 上传产品设备文件
     * @param file 产品文件数据
     * @return
     */
    ImportReturnModel importFile(MultipartFile file);
}
