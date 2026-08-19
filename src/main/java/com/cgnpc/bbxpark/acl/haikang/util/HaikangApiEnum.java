package com.cgnpc.bbxpark.acl.haikang.util;
/**
 *     operationCode   操作编码
 *     operationName   操作名称
 *     version    版本
 *     uri   请求地址
 *     httpMethod   Http方法：POST,GET
 *     method  service中的实际方法名（设备服务和设备事件需要定义，透传和同步不需要）
 */
@SuppressWarnings("all")
public enum HaikangApiEnum {

    PLAYBACKURLS(
            "playbackURLs",
            "获取监控点回放取流URLv2",
            "v2",
            "/api/video/v2/cameras/playbackURLs",
            "POST",
            ""
    ),
    PLAYLIVEURLS(
            "playliveURLs",
            "获取监控点预览取流URL",
            "v1",
            "/api/video/v2/cameras/previewURLs",
            "POST",
            ""
    ),
    CAMERASEARCH(
            "cameraSearch",
            "查询监控点列表v2",
            "v2",
            "/api/resource/v2/camera/search",
            "POST",
            ""
    ),
    CAMERAONLINEGET(
            "cameraOnlineGet",
            "获取监控点在线状态",
            "v1",
            "/api/nms/v1/online/camera/get",
            "POST",
            ""
    ),


    // 事件订阅相关接口
    EVENT_SUBSCRIPT_VIEW(
            "eventSubscriptionView",
            "查询事件订阅信息",
            "v1.4.301",
            "/api/eventService/v1/eventSubscriptionView",
            "POST",
            ""
    ),

    EVENT_SUBSCRIPT_BY_EVENT_TYPE(
            "eventSubscriptionByEventTypes",
            "按事件类型订阅事件",
            "v1.4.301",
            "/api/eventService/v1/eventSubscriptionByEventTypes",
            "POST",
            ""
    ),

    EVENT_UNSUBSCRIPT_BY_EVENT_TYPE(
            "eventUnSubscriptionByEventTypes",
            "指定事件类型取消订阅",
            "v1.4.301",
            "/api/eventService/v1/eventUnSubscriptionByEventTypes",
            "POST",
            ""
    ),

    EVENT_SEARCH(
            "eventSearch",
            "获取联动事件列表",
            "v1.4.301",
            "/api/els/v1/events/search",
            "POST",
            ""
    ),

    DEVICE_RESOURCE_LIST(
            "deviceResourceList",
            "获取资源列表v2",
            "v1.4.301",
            "/api/irds/v2/deviceResource/resources",
            "POST",
            ""
    ),

    ;

    private String operationCode;

    private String operationName;

    private String version;

    private String uri;

    private String httpMethod;

    private String method;

    HaikangApiEnum(String operationCode, String operationName, String version, String uri, String httpMethod, String method) {
        this.operationCode = operationCode;
        this.operationName = operationName;
        this.version = version;
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.method = method;
    }

    public static HaikangApiEnum getInterfaceEnum(String operationCode) {
        for (HaikangApiEnum keyTopInterfaceEnum : HaikangApiEnum.values()){
            if(keyTopInterfaceEnum.operationCode.equals(operationCode)){
                return keyTopInterfaceEnum;
            }
        }
        return null;
    }


    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
}
