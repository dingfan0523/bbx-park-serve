package com.cgnpc.bbxpark.config.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * kafkaMsg线程池
 *
 * @author gujun
 * @since 2022/11/4
 */
@Configuration
@Slf4j
public class KafkaThreadPoolConfig {



    /**
     * 创建表务业务线程池
     * 表务大部分操作属于IO密集型，core和maxCore可以稍微给大一点，避免线程阻塞，影响业务操作
     *
     * @return
     */
    @Bean
    public Executor kafkaMsgThreadPool(@Autowired SecurityManager securityManager) {
        int core = Runtime.getRuntime().availableProcessors() << 1;
        int queueSize = 127;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(core << 1);
        executor.setQueueCapacity(queueSize);
        executor.setThreadNamePrefix("kafkaMsg-Executor-");
        executor.setTaskDecorator(new taskDecorator(securityManager));
        executor.setBeanName("kafkaMsgThreadPool");
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        log.info("kafkaMsgThreadPool init ====> code:{},maxPool:{},queueSize:{},qps:{}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize(),
                queueSize,
                (executor.getCorePoolSize() + executor.getMaxPoolSize() + queueSize)
        );
        return executor;
    }

    /**
     * 报装ThreadLocal数据
     */
    static class taskDecorator implements TaskDecorator {
        private final SecurityManager securityManager;

        public taskDecorator(SecurityManager securityManager){
            this.securityManager = securityManager;
        }

        @Override
        public Runnable decorate(Runnable runnable) {
            // 获取主线程中的请求信息（我们的用户信息也放在里面）
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            return () -> {
                ThreadContext.bind(securityManager);
                try {
                    // 将主线程的请求信息，设置到子线程中
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    // 执行子线程，这一步不要忘了
                    runnable.run();
                } finally {
                    // 线程结束，清空这些信息，否则可能造成内存泄漏
                    RequestContextHolder.resetRequestAttributes();
                    ThreadContext.unbindSecurityManager();
                    ThreadContext.unbindSubject();
                }
            };
        }
    }
}
