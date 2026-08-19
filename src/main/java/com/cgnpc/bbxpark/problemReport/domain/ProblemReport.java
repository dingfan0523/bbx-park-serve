package com.cgnpc.bbxpark.problemReport.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @create zhaoshuo
 * @time 2025/3/21
 * @desc 报事报修实体
 */
@Data
@TableName("bbx_problem_report")
public class ProblemReport extends BaseExEntity implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -4928217012804918804L;


    /**
     * 问题需求描述
     */
    private String problemDesc;

    /**
     * 问题类型：1报事报修
     */
    private Integer problemType;

    /**
     * 问题状态（1.新产生，2.已查看，3.已确认）
     */
    private Integer status;

    /**
     * 空间位置id
     */
    private Long spaceId;

    /**
     * 空间位置名称
     */
    private String spaceName;

    /**
     * 问题联系人
     */
    private String problemContact;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 问题确认结果(1.转工单，2.无需处理)
     */
    private Integer problemResultType;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 工作评价
     */
    private String evaluation;

    /**
     * 评价人
     */
    private String evaluationUname;

    /**
     * 评价人工号
     */
    private String evaluationUstaffid;

    /**
     * 评价时间
     */
    private Date evaluationTime;

    /**
     * 关联工单id
     */
    private Long workOrderId;

    /**
     * 乐观锁
     */
    private String revision;
}
