
package com.cgnpc.bbxpark.log.vo;

import com.cgnpc.bbxpark.log.enumeration.HttpMethod;
import com.cgnpc.bbxpark.log.matcher.RequestMatcher;
import lombok.Data;

@Data
public class ActionTypeConfigItem {

    private String actionPathPattern;

    private HttpMethod method;

    private String controllerMethodName;

    private String actionType;

    private RequestMatcher matcher;

}
