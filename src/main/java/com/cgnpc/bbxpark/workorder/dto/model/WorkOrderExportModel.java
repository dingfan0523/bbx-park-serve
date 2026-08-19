
package com.cgnpc.bbxpark.workorder.dto.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * @value 工单导出模型
 * @author huangyongtao
 * @date 2025/11/12 15:31
 */
@Data
public class WorkOrderExportModel implements Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单编码.")
    @ExcelProperty(value = "工单编号" , index = 0)
    private String code;

    @ApiModelProperty(value = "工单名称.")
    @ExcelProperty(value = "工单名称" , index = 1)
    private String name;

    @ApiModelProperty(value = "工单来源.")
    @ExcelIgnore
    private String source;

    @ExcelProperty(value = "工单来源" , index = 2)
    private String sourceDesc;

    /** 空间名称 */
    @ApiModelProperty(value = "空间位置")
    @ExcelProperty(value = "位置" , index = 3)
    private String spaceName ;

    @ApiModelProperty(value = "分配人.")
    @ExcelProperty(value = "分配人" , index = 4)
    private String allotUname;

    @ApiModelProperty(value = "分配人员工号")
    @ExcelIgnore
    private String allotUstaffid;

    @ApiModelProperty(value = "分配人员id")
    @ExcelIgnore
    private String allotUid;

    @ApiModelProperty(value = "创建时间.")
    @ExcelIgnore
    private Date createTime;

    @ApiModelProperty(value = "创建时间.")
    @ExcelProperty(value = "生成时间" , index = 5)
    private String createTimeStr;

    @ApiModelProperty(value = "处理人名称.")
    @ExcelProperty(value = "处理人" , index = 6)
    private String processedPersonName;

    @ApiModelProperty(value = "处理人工号")
    @ExcelIgnore
    private String processedPersonStaffid;

    @ApiModelProperty(value = "处理人id")
    @ExcelIgnore
    private String processedPersonId;

    @ApiModelProperty(value = "转派人名称.")
    @ExcelProperty(value = "是否转派" , index = 7)
    private String transferUname;

    @ApiModelProperty(value = "转派人工号.")
    @ExcelIgnore
    private String transferStaffid;

    @ApiModelProperty(value = "转派人id.")
    @ExcelIgnore
    private String transferUid;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）")
    @ExcelIgnore
    private Integer status;

    @ApiModelProperty(value = "工单状态（待分配：10；待处理：20；处理中：30；待审核：40；已完成：50）")
    @ExcelProperty(value = "工单状态" , index = 8)
    private String statusDesc;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    @ExcelIgnore
    private Integer dispatchType;

    @ApiModelProperty(value = "派单方式;10：分组人员抢单；20：组长派单；30：直接指派.")
    @ExcelProperty(value = "派单方式" , index = 9)
    private String dispatchTypeDesc;

}
