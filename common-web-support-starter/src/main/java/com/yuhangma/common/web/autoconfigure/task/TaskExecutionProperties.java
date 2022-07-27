package com.yuhangma.common.web.autoconfigure.task;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * spring.task.execution[io].core-size = 5
 * spring.task.execution[io].max-size = 5
 * spring.task.execution[io].queue-capacity = 5
 * spring.task.execution[io].keep-alive-ms = 5
 * spring.task.execution[io].thread-name-prefix = 5
 *
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/13
 */
@Data
@ConfigurationProperties(prefix = "spring.task.execution")
public class TaskExecutionProperties implements InitializingBean {

    /**
     * 处理器核心数
     */
    public static final int PROCESSORS_COUNT = Runtime.getRuntime().availableProcessors();

    public static final int DEFAULT_QUEUE_CAPACITY = 4096;

    public static final int DEFAULT_KEEP_ALIVE_SECONDS = 60;

    private Map<String, PoolProperties> pool = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(pool);
    }

    @Data
    public static class PoolProperties {

        private int coreSize;

        private int maxSize;

        private int queueCapacity;

        private int keepAliveSec = DEFAULT_KEEP_ALIVE_SECONDS;

        private Optional<String> threadNamePrefix = Optional.empty();

    }

}
