package com.cgnpc.bbxpark.log.service.impl;


import com.cgnpc.bbxpark.settings.dto.param.SystemRunningParam;
import com.cgnpc.bbxpark.log.service.ISystemRunningService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EDZ
 */
@Service
@Slf4j
public class SystemRunningImpl implements ISystemRunningService {
//    @Autowired
//    private SystemLogRepository systemLogRepository;

    @Override
    public Map<String, Object> selectSystemRunningLog(SystemRunningParam param) {
        List<Map<String, Object>> returnData = new ArrayList<>();
        Map<String, Object> dataMap = new HashMap<>(16);
//        String url = "http://10.8.99.11:9200/bbx-log-2024.07/_search";
//
//        if (param.getTimeList() != null && param.getTimeList().size() > 0) {
//            String timData = returnTime(param.getTimeList().get(0));
//            if (!StrUtil.isBlank(timData)) {
//                url = "http://10.8.99.11:9200/bbx-log-" + timData + "/_search";
//            }
//        }
//        StringBuilder sqlbuffer = new StringBuilder();
//        sqlbuffer.append("{\"from\":" + param.getPageNumber() + ",\"query\":{\"bool\":{\"must\":[");
//        if (param.getTimeList() != null && param.getTimeList().size() > 0) {
//            sqlbuffer.append("{\"range\":{\"log_timestamp.keyword\":{\"from\":\"" + DateUtils.formatYMD(DateUtils.format(param.getTimeList().get(0))) + " 00:00:00\",\"to\":\"" + DateUtils.formatYMD(DateUtils.format(param.getTimeList().get(1))) + " 23:59:59\"}}},");
//        }
//        sqlbuffer.append("{\"bool\":{\"should\":[");
//        List<Map<String, Object>> data = systemLogRepository.queryBbxLogKeyInfoData(param);
//        if (data != null && !data.isEmpty()) {
//            int size = data.size();
//            int counter = 0;
//            for (Map<String, Object> item : data) {
//                if (counter == size - 1) {
//                    sqlbuffer.append("{\"wildcard\":{\"log_content\":\"*" + item.get("key_name") + "*\"}}");
//                } else {
//                    sqlbuffer.append("{\"wildcard\":{\"log_content\":\"*" + item.get("key_name") + "*\"}},");
//                }
//                counter++;
//            }
//        }
//        sqlbuffer.append("],\"minimum_should_match\":1}}");
//        if (!StrUtil.isBlank(param.getAbnormalDescription())) {
//            sqlbuffer.append(",{\"wildcard\":{\"log_content\":\"*" + param.getAbnormalDescription() + "*\"}}");
//        }
//        sqlbuffer.append("]}},\"sort\":[{\"@timestamp\":{\"order\":\"desc\",\"unmapped_type\":\"boolean\"}}],\"size\":" + param.getPageSize() + "}");
//
//
//        String returnString = null;
//        try {
//            returnString = httpMethod2(url, sqlbuffer.toString(), "POST");
//        } catch (IOException e) {
//            log.error("SystemRunningImpl#selectSystemRunningLog error,错误信息:\n{}", Throwables.getStackTraceAsString(e));
//        }
//
//        log.error("系统日志查询 返回content = 【{}】 ", returnString);
//        JSONObject returnObject = JSON.parseObject(returnString);
//        JSONObject hitsjson = returnObject.getJSONObject("hits");
//        JSONObject total = hitsjson.getJSONObject("total");
//        JSONArray hitsarray = hitsjson.getJSONArray("hits");
//        Long totalValue = total.getLong("value");
//
//        log.error("系统日志查询 totalValue = {} ", totalValue);
//        dataMap.put("totalSize", totalValue);
//        for (int index = 0; index < hitsarray.size(); index++) {
//            JSONObject hitsdata = hitsarray.getJSONObject(index);
//            JSONObject source = hitsdata.getJSONObject("_source");
//            Map<String, Object> map = source.toJavaObject(Map.class);
//            // 截取从"Exception"
//            String logContent = map.get("log_content").toString();
//            for (Map<String, Object> excelData : data) {
//                String subExc = substringFromExceptionToDot(logContent, MapUtils.getString(excelData, "key_name"));
                //            String subExc = substringFromExceptionToDot(logContent);
//                Map<String, Object> oneData = systemLogRepository.queryBbxLogKeyInfoDataOne(subExc);
//                if (oneData != null && !oneData.isEmpty()) {
//                    oneData.put("errMsg", map.get("log_content"));
//                    oneData.put("errTime", map.get("log_timestamp"));
//                    oneData.put("errIp", map.get("log_ip"));
//                    returnData.add(oneData);
//                    break;
//                }
//            }
//        }
        dataMap.put("result",returnData);
        return dataMap;
    }

    private static String returnTime(String time) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = dateTime.format(outputFormatter);
        String formattedDateStr = formattedDate.replace('-', '.');
        return formattedDateStr;
    }

    private static String substringFromExceptionToDot(String str, String toFind) {
        String extractedString = "";
        // 使用contains()方法检查str是否包含toFind
        // 如果你还想截取这个子字符串（尽管在这个场景下它可能不是必需的）
        // 你可以使用indexOf()和substring()方法
        int startIndex = str.indexOf(toFind);
        if (startIndex != -1) {
            // 注意：因为我们要截取整个子字符串，所以endIndex是startIndex加上子字符串的长度
            int endIndex = startIndex + toFind.length();
            extractedString = str.substring(startIndex, endIndex);
            // 但在这个特定的情况下，我们其实不需要截取，因为子字符串就是我们要找的整个字符串
        }
        return extractedString;
//        int startIndex = logContent.indexOf("Exception");
//        int endIndex = logContent.lastIndexOf('.', startIndex - 1);
//        // 返回从"Exception"到第一个"."之间的字符串（不包括"."）
//        return logContent.substring(endIndex + 1, startIndex);
    }

    private String httpMethod2(String url, String msg, String infoType) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        String encoding = DatatypeConverter.printBase64Binary("elastic:Elastic_Mon#2021".getBytes("UTF-8"));
        httpPost.setHeader("Authorization", "Basic " + encoding);
        StringEntity entity = new StringEntity(msg, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(10000).setSocketTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String respText = "";
        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity2 = response.getEntity();
            if (entity2 != null) {
                respText = EntityUtils.toString(entity2, "UTF-8");
            }
        } finally {
            httpclient.close();
        }

        return respText;

    }
}
