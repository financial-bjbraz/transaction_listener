package br.com.bjbraz.tx.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class Config {

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); //default: 1
        executor.setMaxPoolSize(10); //default: Integer.MAX_VALUE
        executor.setQueueCapacity(20); // default: Integer.MAX_VALUE
        executor.setKeepAliveSeconds(120); // default: 60 seconds
        executor.initialize();
        return executor;
    }
}
