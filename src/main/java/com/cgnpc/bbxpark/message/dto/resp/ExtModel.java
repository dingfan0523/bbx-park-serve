package com.cgnpc.bbxpark.message.dto.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 扩展内容模型
 * 不同的渠道扩展内容格式可能不一样
 * @author dingfan
 * @version 1.0
 * @date 2024/10/24 17:25
 */
@Data
public class ExtModel implements Serializable {
    private Set<CommonUser> userSet;
}
