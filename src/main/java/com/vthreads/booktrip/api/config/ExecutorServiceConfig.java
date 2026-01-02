package com.vthreads.booktrip.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.boot.thread.Threading;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceConfig.class);
    @Bean
    @ConditionalOnThreading(Threading.VIRTUAL)
    public ExecutorService virtualThreadExecutor(){
        logger.info("Virtual Thread Executor Selected");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    @ConditionalOnThreading(Threading.PLATFORM)
    public ExecutorService platformThreadExecutor(){
        logger.info("Platform Thread Executor Selected");
        return Executors.newFixedThreadPool(400);
    }
}
