package com.cgnpc.dingtalk.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WorkNotice {

    private String msgType;

    private String title;

    private String url;

    private String content;
}
