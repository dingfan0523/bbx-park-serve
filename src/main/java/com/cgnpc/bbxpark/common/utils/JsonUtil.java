package com.cgnpc.bbxpark.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * Json工具类
 * @author dingfan
 * @date 2024/09/02
 */
public class JsonUtil {
    private JsonUtil(){}

    /**
     * list转换为json字符串
     * @param list list
     * @return json字符串
     */
    public static String convertListToJsonStr(List<?> list){
        if(CollectionUtils.isEmpty(list)){
            return new JSONArray().toString();
        }
        return JSON.toJSONString(list);
    }

    /**
     * jsonArray字符串转换为list
     * @param jsonArrStr jsonArray字符串
     * @return list
     */
    public static List<String> convertJsonArrStrToList(String jsonArrStr){
        return convertJsonArrStrToList(jsonArrStr,String.class);
    }

    public static <T> List<T> convertJsonArrStrToList(String jsonArrStr,Class<T> clazz){
        String str = "[]";
        if(StringUtils.isEmpty(jsonArrStr) || str.equals(jsonArrStr)){
            return Collections.emptyList();
        }
        return JSON.parseArray(jsonArrStr,clazz);
    }
}
