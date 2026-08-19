package com.cgnpc.framework.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cgnpc.cud.core.common.util.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * 用途说明: 解决jackson配置失败问题
 * 创建时间: 2023/09/18
 * @author P629988
 */
@Configuration
public class FastJsonMessageConvertConfig implements WebMvcConfigurer {

    /**
     * @Author P629041
     * @Description 全局fastJson 序列化规则
     * @Date 10:24 2024/7/17
     **/
    static {

        // 全局修改日期格式，默认为false
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteDateUseDateFormat.getMask();

        // 消除循环引用
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();

        //开启SageMode
        ParserConfig.getGlobalInstance().setSafeMode(true);
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverters() {
        //设置时区
        JSON.defaultTimeZone = TimeZone.getTimeZone("GMT+8");
        //设置请求头
        List<MediaType> mediaTypes = new ArrayList<>(16);
        mediaTypes.add(MediaType.valueOf("application/json;charset=UTF-8"));
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        //设置格式化规则
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect);
        fastJsonConfig.setDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        //设置字符格式
        fastJsonHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        return fastJsonHttpMessageConverter;
    }

    /**
     * 保证StringHttpMessageConverter在FastJsonHttpMessageConverter前被调用
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        converters.add(converter);
        converters.add(fastJsonHttpMessageConverters());
    }
}