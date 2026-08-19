package com.cgnpc.bbxpark.problemReport.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修处理记录
 */
@Data
@TableName("bbx_problem_handle_record")
public class ProblemHandleRecord extends BaseExEntity  implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;

    /**
     * 关联的报事报修id
     */
    private Long problemId;

    /**
     * 环节（1新产生，2已查看，3已确认）
     */
    private Integer link;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人工号
     */
    private String operatorStaffid;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 确认结果(1.转工单，2.无需处理)
     */
    private Integer resultType;

    /**
     * 处理人id
     */
    private String handleUid;

    /**
     * 处理人姓名
     */
    private String handleUname;

    /**
     * 处理人工号
     */
    private String handleStaffid;

    /**
     * 原因
     */
    private String cause;

    /**
     * 备注
     */
    private String descr;


    /**
     * 处理人联系方式
     */
    private String handleUmobile;
}
