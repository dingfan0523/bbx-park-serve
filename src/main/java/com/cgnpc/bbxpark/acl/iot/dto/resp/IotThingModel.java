/*
 * +----------------------------------------------------------------------
 * | Copyright (c)  2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「」相关版权
 * +----------------------------------------------------------------------
 * | Author: 
 * +----------------------------------------------------------------------
 */
package com.cgnpc.bbxpark.acl.iot.dto.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sjg
 */

@Data
//@Builder
@NoArgsConstructor
public class IotThingModel implements Serializable {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("产品key")
    private String productKey;
    @ApiModelProperty("模型")
    private Model model;


    @Data
    public static class Model {
        private List<Property> properties;
        private List<Service> services;
        private List<Event> events;

        public Map<String, Service> serviceMap() {
            if (services == null) {
                return new HashMap<>();
            }
            return services.stream().collect(Collectors.toMap(Service::getIdentifier, s -> s));
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Property {
        private String identifier;
        private DataType dataType;
        private String name;
        private String accessMode = "rw";
        // 描述
        private String description;
        // 单位
        private String unit;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Parameter {
        private String identifier;
        private DataType dataType;
        private String name;
        private Boolean required = false;
    }

    @Data
    public static class Service {
        private String identifier;
        private String requestMethod;
        private String requestUrl;
        private List<Parameter> inputData;
        private List<Parameter> outputData;
        private String name;
    }

    @Data
    public static class Event {
        private String identifier;
        private List<Parameter> outputData;
        private String name;
    }

    @Data
    public static class DataType {
        private String type;
        private Object specs;
        public <T> Object parse(T value) {
            if (value == null) {
                return null;
            }

            String val = value.toString();
            type = type.toLowerCase();
            switch (type) {
                case "bool":
                case "enum":
                    return val;
                case "int":
                    return Integer.parseInt(val);
                default:
                    return val;
            }

        }
    }
}
