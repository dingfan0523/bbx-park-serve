package com.cgnpc.bbxpark.workbench.cudauth.config;

import com.cgnpc.bbxpark.workbench.cudauth.CudPermissonInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author P629041
 * @Description 拦截器相关配置
 * @Date 9:07 2023/2/28
 * @Param
 * @return
 **/
@Configuration
public class CudPermissonConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private CudPermissonInterceptor cudPermissonInterceptor;

    /**
     * 权限排除路径
     **/
    @Value("${cud.authExcludeUrl:}")
    private List<String> excludeUrl = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //默认放行路径
        String[] excludePatterns = new String[]{
                "/login/**", "/error/**", "/**/*.png",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",
                "/api", "/api-docs", "/api-docs/**", "/doc.html/**",
        };
        //拦截器 通用权限拦截器 优先级排在认证拦截通过后再调用
        registry.addInterceptor(cudPermissonInterceptor)
                .addPathPatterns("/**")
                .order(2)
                .excludePathPatterns(excludeUrl)
                .excludePathPatterns(excludePatterns);
        super.addInterceptors(registry);
    }
}
