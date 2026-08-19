package com.cgnpc.bbxpark.config.eventbus;

import lombok.Data;

import java.io.Serializable;

/**
 * 审批完成事件
 * @author dingfan
 * @version 1.0
 * @date 2025/8/5 11:18
 */
@Data
public class ApprovalCompletedEvent implements Serializable {
    /**
     * 业务ID
     */
    private Long businessId;
    /**
     * 审批结果：true-全部通过，false-有拒绝
     */
    private Boolean approved;
    /**
     * 备注
     */
    private String remark;
}
