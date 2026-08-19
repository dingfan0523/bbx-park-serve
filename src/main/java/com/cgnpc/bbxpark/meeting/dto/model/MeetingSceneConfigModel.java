package com.cgnpc.bbxpark.meeting.dto.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 会议场景配置入参模型
 * @author dingfan
 * @version 1.0
 * @date 2025/1/7 15:05
 */
@Data
public class MeetingSceneConfigModel {
    @ApiModelProperty(value = "设备id")
    private Long deviceId;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "物模型标识")
    private String identifier;
    @ApiModelProperty(value = "物模型名称")
    private String name;
    @ApiModelProperty(value = "物模型参数集合")
    private List<ThingModelParam> modelParamList;

    public static class ThingModelParam{
        @ApiModelProperty(value = "参数")
        private String identifier;
        @ApiModelProperty(value = "参数名称")
        private String name;
        @ApiModelProperty(value = "参数值")
        private String value;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
