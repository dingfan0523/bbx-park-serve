package com.cgnpc.dingtalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用途说明: 异步线程池 作用于发送消息通知，防止业务阻塞
 * 作者姓名: P633860
 * 创建时间: 2023/6/25
 */

@Configuration
@EnableAsync
@SuppressWarnings("all")
public class WarningThreadPoolConfig extends AsyncConfigurerSupport {

    @Bean("asyncExecutor")
    public AsyncTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(8);
        // 设置最大线程数
        executor.setMaxPoolSize(16);
        // 设置队列容量
        executor.setQueueCapacity(32);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("AsyncWarningMvc-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncTaskExecutor getAsyncExecutor() {
        return asyncExecutor();
    }

}
