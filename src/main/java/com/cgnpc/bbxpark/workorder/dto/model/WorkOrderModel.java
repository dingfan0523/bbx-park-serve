
package com.cgnpc.bbxpark.workorder.dto.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.cgnpc.bbxpark.device.dto.model.AlarmInfoModel;
import com.cgnpc.bbxpark.problemReport.model.ProblemReportModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工单主业务数据模型
 */
@Data
public class WorkOrderModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键.")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "工单编码.")
    @ExcelProperty(value = "工单编号" , index = 0)
    private String code;

    @ApiModelProperty(value = "工单名称.")
    @ExcelProperty(value = "工单名称" , index = 1)
    private String name;

    @ApiModelProperty(value = "工单类型(报修工单:repair).")
    @ExcelIgnore
    private String type;

    @ApiModelProperty(value = "工单来源（人工上报：person；告警触发：alarm）.")
    @ExcelIgnore
    private String source;

    @ExcelProperty(value = "工单来源" , index = 1)
    private String sourceDesc;

    @ApiModelProperty(value = "工单描述.")
    @ExcelIgnore
    private String remark;

    @ApiModelProperty(value = "问题图片集合.")
    @ExcelIgnore
    private List<String> problemPictureUrlList;

    @ApiModelProperty(value = "处理人名称.")
    @ExcelProperty(value = "处理人" , index = 5)
    private String processedPersonName;

    @ApiModelProperty(value = "处理人工号")
    @ExcelIgnore
    private String processedPersonStaffid;

    @ApiModelProperty(value = "处理人id")
    @ExcelIgnore
    private String processedPersonId;

    @ApiModelProperty(value = "处理人电话")
    @ExcelIgnore
    private String processedPhone;

    @ApiModelProperty(value = "处理图片集合.")
    @ExcelIgnore
    private List<String> processedPictureUrlList;

    @ApiModelProperty(value = "处理描述.")
    @ExcelIgnore
    private String processedDesc;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）")
    @ExcelIgnore
    private Integer status;

    @ExcelProperty(value = "工单状态" , index = 6)
    private String statusDesc;

    @ApiModelProperty(value = "分配人.")
    @ExcelProperty(value = "分配人" , index = 3)
    private String allotUname;

    @ApiModelProperty(value = "分配人员工号")
    @ExcelIgnore
    private String allotUstaffid;

    @ApiModelProperty(value = "分配人员id")
    @ExcelIgnore
    private String allotUid;

    @ApiModelProperty(value = "满意度.")
    @ExcelIgnore
    private Integer satisfaction;

    @ApiModelProperty(value = "评价.")
    @ExcelIgnore
    private String evaluateContent;


    @ApiModelProperty(value = "租户id.")
    @ExcelIgnore
    private Long tenantId;

    @ApiModelProperty(value = "删除状态(1->未删;0->已删).")
    @ExcelIgnore
    private Boolean deleted;

    @ApiModelProperty(value = "当前登录人工号")
    @ExcelIgnore
    private String nowUserId;

    @ApiModelProperty(value = "创建时间.")
    @ExcelIgnore
    private Date createTime;

    @ApiModelProperty(value = "创建时间.")
    @ExcelProperty(value = "生成时间" , index = 4)
    private String createTimeStr;

    @ApiModelProperty(value = "更新人工号.")
    @ExcelIgnore
    private String updatorId;

    @ApiModelProperty(value = "更新时间.")
    @ExcelIgnore
    private Date updateTime;

    /** 工单结束时间 */
    @ExcelIgnore
    private Date endTime;

    @ApiModelProperty(value = "操作的值.")
    @ExcelIgnore
    private String operatorValue;

    @ApiModelProperty(value = "冗余字段4（操作的值）.")
    @ExcelIgnore
    private String redundancyFour;

    @ApiModelProperty(value = "创建人名称")
    @ExcelIgnore
    private String createBy;

    @ApiModelProperty(value = "创建人工号")
    @ExcelIgnore
    private String creatorId;

    @ApiModelProperty(value = "设备集合.")
    @ExcelIgnore
    private List<WorkOrderDeviceModel> workOrderDeviceModels;

    @ApiModelProperty(value = "流程集合.")
    @ExcelIgnore
    private List<WorkOrderRomanModel> workOrderRomanModels;

    @ApiModelProperty(value = "超时时间.")
    @ExcelIgnore
    private Date outTime;

    @ApiModelProperty(value = "超时状态（1->是;0->否）")
    @ExcelIgnore
    private Integer outStatus;

    @ApiModelProperty(value = "超时原因（1：工单生成为节假日；2：表有故障，等待 报修；3：个人原因（请假））")
    @ExcelIgnore
    private Integer outReason;

    @ApiModelProperty(value = "关联原始信息-报事报修")
    @ExcelIgnore
    private ProblemReportModel problemReportModel;

    /** 空间名称 */
    @ApiModelProperty(value = "空间位置")
    @ExcelProperty(value = "位置" , index = 2)
    private String spaceName;

    @ApiModelProperty(value = "关联原始信息-告警详情")
    @ExcelIgnore
    private AlarmInfoModel alarmInfoModel;

    /** 评价人 */
    @ApiModelProperty(value = "评价人")
    @ExcelIgnore
    private String evaluateUname;
    /** 评价时间 */
    @ApiModelProperty(value = "评价时间")
    @ExcelIgnore
    private Date evaluateTime;
    /** 评价人工号 */
    @ApiModelProperty(value = "评价人工号")
    @ExcelIgnore
    private String evaluateUstaffid;

    @ApiModelProperty(value = "处理结果(1已解决，2确认存在异常，需要管理员介入)")
    @ExcelIgnore
    private Integer handleResult;

    @ApiModelProperty(value = "处理方式")
    @ExcelIgnore
    private String processMode;

    @ApiModelProperty(value = "工单计划详情")
    @ExcelIgnore
    private WorkPlanDetailModel workPlanDetailModel;

    @ApiModelProperty(value = "当前人是否是工单处理人true-是 false-不是")
    @ExcelIgnore
    private Boolean flag = false;

    @ApiModelProperty(value = "转派人名称.")
    @ExcelIgnore
    private String transferUname;

    @ApiModelProperty(value = "转派人工号.")
    @ExcelIgnore
    private String transferStaffid;

    @ApiModelProperty(value = "转派人id.")
    @ExcelIgnore
    private String transferUid;

    @ApiModelProperty(value = "审核人名称.")
    @ExcelIgnore
    private String auditUname;

    @ApiModelProperty(value = "审核人工号.")
    @ExcelIgnore
    private String auditStaffid;

    @ApiModelProperty(value = "审核人id.")
    @ExcelIgnore
    private String auditUid;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    @ExcelIgnore
    private Integer dispatchType;


    @ApiModelProperty(value = "分配权限")
    @ExcelIgnore
    private Boolean allotFlag = false;

    @ApiModelProperty(value = "抢单权限")
    @ExcelIgnore
    private Boolean grabFlag = false;

    @ApiModelProperty(value = "接受权限")
    @ExcelIgnore
    private Boolean acceptFlag = false;

    @ApiModelProperty(value = "转派处理权限")
    @ExcelIgnore
    private Boolean transferHandleFlag = false;

    @ApiModelProperty(value = "转派审核权限")
    @ExcelIgnore
    private Boolean transferAuditFlag = false;

    @ApiModelProperty(value = "处理权限")
    @ExcelIgnore
    private Boolean handleFlag = false;

    @ApiModelProperty(value = "审核权限")
    @ExcelIgnore
    private Boolean auditFlag = false;

    @ApiModelProperty(value = "评价列表")
    @ExcelIgnore
    private List<WorkEvaluateModel> evaluateModels;

    @ApiModelProperty(value = "材料列表")
    @ExcelIgnore
    private List<WorkMaterialModel> workMaterialModels;

    @ApiModelProperty(value = "任务组列表")
    @ExcelIgnore
    private List<WorkTaskGroupModel> workTaskGroupModels;
}
