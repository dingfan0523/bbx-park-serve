package com.cgnpc.bbxpark.log.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.cgnpc.bbxpark.log.matcher.AntPathRequestMatcher;
import com.cgnpc.bbxpark.log.vo.ActionTypeConfigItem;
import com.cgnpc.bbxpark.log.vo.ChildrenActionType;
import com.cgnpc.bbxpark.log.vo.ModuleNameConfigItem;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
@Configuration
@ConfigurationProperties(prefix = "matrix.operate-log")
@Data
public class OperateLogConfig implements InitializingBean {

    List<ModuleNameConfigItem> moduleNameConfig = Collections.EMPTY_LIST;

    List<ActionTypeConfigItem> actionTypeConfig = Collections.EMPTY_LIST;

    @Override
    public void afterPropertiesSet() throws Exception {

        //初始化moduleConfig的matcher
        if (CollUtil.isNotEmpty(moduleNameConfig)) {
            for (ModuleNameConfigItem moduleNameConfigItem : moduleNameConfig) {
                moduleNameConfigItem.setMatcher(new AntPathRequestMatcher((moduleNameConfigItem.getModulePathPattern())));
                if (CollUtil.isNotEmpty(moduleNameConfigItem.getChildrenActionType())) {
                    for (ChildrenActionType childrenActionType : moduleNameConfigItem.getChildrenActionType()) {
                        childrenActionType.setMatcher(new AntPathRequestMatcher(childrenActionType.getChildPath(),
                                childrenActionType.getMethod().toString()
                        ));
                    }
                }
            }
        }

        //初始化modulePatternConfig的matcher
        if (CollUtil.isNotEmpty(actionTypeConfig)) {
            for (ActionTypeConfigItem actionTypeConfigItem : actionTypeConfig) {
                if (StrUtil.isNotBlank(actionTypeConfigItem.getActionPathPattern())) {
                    actionTypeConfigItem.setMatcher(new AntPathRequestMatcher(actionTypeConfigItem.getActionPathPattern(),
                            actionTypeConfigItem.getMethod().toString()
                    ));
                }
            }
        }

    }
}
