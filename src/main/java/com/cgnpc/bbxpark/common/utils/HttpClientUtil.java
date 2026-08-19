package com.cgnpc.bbxpark.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class HttpClientUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 发送 POST 请求并将响应的 result 数组转换为 List<T>
     *
     * @param url      请求地址（如果为空则使用默认地址）
     * @param jsonBody 请求体 JSON 字符串（如果为空则使用默认时间范围）
     * @param clazz    目标实体类的 Class 对象（用于反序列化 list 中的元素类型）
     * @param <T>      实体类型
     * @return 解析后的 List<T>，出错时返回空列表（可改为抛出异常）
     */
    public static <T> List<T> postForList(String url, String jsonBody, Class<T> clazz) {
        // 默认值处理（生产环境建议去掉默认逻辑，由调用方显式传入）
        String finalUrl = (url == null || url.trim().isEmpty())
                ? "https://cnbap.gnpjvc.cgnpc.com.cn/sdata/rest/service/dataapi/rest/6433832857565504"
                : url;
        String finalBody = (jsonBody == null || jsonBody.trim().isEmpty())
                ? "{ \"start_time\": \"2026-05-01\", \"end_time\": \"2026-08-30\" }"
                : jsonBody;

        // 创建忽略 SSL 证书的 HttpClient（仅测试环境使用）
        try (CloseableHttpClient httpClient = createIgnoreSslHttpClient()) {
            HttpPost httpPost = new HttpPost(finalUrl);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");

            StringEntity entity = new StringEntity(finalBody, "UTF-8");
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (statusCode != 200) {
                    log.error("请求失败，状态码: {}, 响应内容: {}", statusCode, responseBody);
                    return Collections.emptyList();
                }

                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode resultNode = rootNode.get("result");

                if (resultNode != null && resultNode.isArray()) {
                    // 关键：将 JSON 数组转换为 List<T>
                    return objectMapper.convertValue(resultNode,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
                } else {
                    log.warn("响应中 result 节点不存在或不是数组，原始响应: {}", responseBody);
                    return Collections.emptyList();
                }
            }
        } catch (Exception e) {
            log.error("调用 POST 接口异常, url={}, body={}", finalUrl, finalBody, e);
            return Collections.emptyList();
        }
    }

    /**
     * 创建忽略 SSL 证书验证的 HttpClient（仅供内部测试使用）
     */
    private static CloseableHttpClient createIgnoreSslHttpClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true) // 信任所有证书
                .build();
        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier((hostname, session) -> true) // 忽略主机名验证
                .build();
    }
}
