package com.itjing.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: lijing
 * @Date: 2021年08月03日 12:42
 * @Description: 线程池配置
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
