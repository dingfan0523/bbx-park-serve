package com.cgnpc.bbxpark.message.dto.resp;

import lombok.Data;

/**
 * 钉钉工作通知内容模型
 *
 * @author dingfan
 * @version 1.0
 * @date 2024/10/25 14:19
 */
@Data
public class DingDingWorkNoticeExtModel extends ExtModel {
    /**
     * 链接文字
     */
    private String singleTitle;
    /**
     * 链接url
     */
    private String singleUrl;
}
