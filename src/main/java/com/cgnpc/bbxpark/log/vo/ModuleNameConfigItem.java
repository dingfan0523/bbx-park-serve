
package com.cgnpc.bbxpark.log.vo;

import com.cgnpc.bbxpark.log.matcher.RequestMatcher;
import lombok.Data;

import java.util.List;

@Data
public class ModuleNameConfigItem {

    private String modulePathPattern;

    private String moduleName;

    private List<ChildrenActionType> childrenActionType;

    private RequestMatcher matcher;

}
