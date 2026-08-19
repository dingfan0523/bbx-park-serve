package com.cgnpc.bbxpark.config.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author gujun
 * @since 2024-10-22
 */
@Configuration
public class EventBusConfiguration {


    @Bean("asyncEventBusExecutor")
    public Executor asyncEventBusExecutor() {

        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(corePoolSize * 2);
        //配置队列大小
        executor.setQueueCapacity(9999);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-event-");

        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator(new TaskDecorator() {
            @Override
            public Runnable decorate(Runnable runnable) {
                // 获取主线程中的请求信息（我们的用户信息也放在里面）
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                return () -> {
                    try {
                        // 将主线程的请求信息，设置到子线程中
                        RequestContextHolder.setRequestAttributes(requestAttributes);
                        // 执行子线程，这一步不要忘了
                        runnable.run();
                    } finally {
                        // 线程结束，清空这些信息，否则可能造成内存泄漏
                        RequestContextHolder.resetRequestAttributes();
                    }
                };
            }
        });
        //执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * AsyncEventBus注册
     */
    @Bean
    public AsyncEventBus asyncEventBus(@Qualifier("asyncEventBusExecutor") Executor asyncEventBusExecutor) {
        return new AsyncEventBus(asyncEventBusExecutor);
    }

    @Bean(name = "syncEventBus")
    public EventBus eventBus() {
        return new EventBus("syncEventBus");
    }
}
