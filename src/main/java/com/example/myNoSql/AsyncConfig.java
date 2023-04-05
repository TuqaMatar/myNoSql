package com.example.myNoSql;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the initial number of threads
        executor.setMaxPoolSize(50); // Set the maximum number of threads in the pool
        executor.setQueueCapacity(100); // Set the queue capacity for tasks waiting to be executed
        executor.setThreadNamePrefix("MyExecutor-"); // Set the thread name prefix for easier identification
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // Set the policy for handling rejected tasks
        executor.initialize();
        return executor;
    }
}
