package com.cgnpc.dingtalk.util;

import com.google.common.collect.Lists;
import feign.RequestLine;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.List;


@SuppressWarnings("all")
public class PathMatchingResourcePatternResolverUtil {


    public static List<?> getRequestLine(String basePackage) throws Exception{

        List<String> requestLineList = Lists.newArrayList();
        //获取指定路径表的全部类
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String pattern = resolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
        //获取包下面指定的类资源
        org.springframework.core.io.Resource[] resources = resolver.getResources(pattern);
        MetadataReaderFactory factory = new CachingMetadataReaderFactory(resolver);
        for (Resource resource: resources) {
            //用来读取类的信息
            MetadataReader reader = factory.getMetadataReader(resource);
            //扫描到的class
            String className = reader.getClassMetadata().getClassName();
            Class<?> clazzName = Class.forName(className);
            for (Method method : clazzName.getMethods()) {
                RequestLine requestLine = method.getAnnotation(RequestLine.class);
                String value = requestLine.value().replace("POST ","").replace("GET ","");
                if (!requestLineList.contains(value)){
                    requestLineList.add(value);
                }
            }
        }

        return requestLineList;
    }

    public static List<?> getApiRequests(){
        try {
            return PathMatchingResourcePatternResolverUtil.getRequestLine("com.cgnpc.psc.client.feign");
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }
}
