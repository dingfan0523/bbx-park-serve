
package com.cgnpc.bbxpark.log.vo;

import com.cgnpc.bbxpark.log.enumeration.HttpMethod;
import com.cgnpc.bbxpark.log.matcher.RequestMatcher;
import lombok.Data;

@Data
public class ChildrenActionType {

    /**
     * childPath的pattern要符合父url的pattern
     */
    private String childPath;

    private String actionType;

    private HttpMethod method;

    private RequestMatcher matcher;
}
