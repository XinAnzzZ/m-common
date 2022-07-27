package com.yuhangma.common.web.autoconfigure.task;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 如果提供了线程相关的配置，则容器中会有一个 {@link TaskExecutionProperties} 类型的 bean，
 * 在 {@link BeanFactoryPostProcessor} 回调函数中用此实例来创建对应的线程池实例并注入到容器中。
 * <p>
 * 如果缺失配置，则配置两个默认线程池 bean，分别为 io 密集型线程池和 cpu 密集型线程池。
 *
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/13
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@EnableConfigurationProperties(TaskExecutionProperties.class)
@AutoConfigureBefore(org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.class)
public class TaskExecutionAutoConfiguration implements BeanFactoryAware, InitializingBean {

    /**
     * @return 创建一个适合 io 密集型任务的线程池
     */
    // @Bean
    // @Conditional(TaskAutoConfigurationCondition.class)
    // @ConditionalOnMissingBean(TaskExecutionProperties.class)
    // public ThreadPoolTaskExecutor ioTaskExecutor() {
    //     final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    //     taskExecutor.setCorePoolSize(TaskExecutionProperties.PROCESSORS_COUNT * 2);
    //     taskExecutor.setMaxPoolSize(TaskExecutionProperties.PROCESSORS_COUNT * 3);
    //     taskExecutor.setQueueCapacity(TaskExecutionProperties.DEFAULT_QUEUE_CAPACITY);
    //     taskExecutor.setKeepAliveSeconds(TaskExecutionProperties.DEFAULT_KEEP_ALIVE_SECONDS);
    //     taskExecutor.setThreadNamePrefix("io-task-thread-");
    //     return taskExecutor;
    // }
    //
    // /**
    //  * @return 创建一个适合 cpu 密集型任务的线程池
    //  */
    // @Bean
    // @ConditionalOnMissingBean(TaskExecutionProperties.class)
    // public ThreadPoolTaskExecutor cpuTaskExecutor() {
    //     final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    //     taskExecutor.setCorePoolSize(TaskExecutionProperties.PROCESSORS_COUNT + 1);
    //     taskExecutor.setMaxPoolSize(TaskExecutionProperties.PROCESSORS_COUNT * 2);
    //     taskExecutor.setQueueCapacity(TaskExecutionProperties.DEFAULT_QUEUE_CAPACITY);
    //     taskExecutor.setKeepAliveSeconds(TaskExecutionProperties.DEFAULT_KEEP_ALIVE_SECONDS);
    //     taskExecutor.setThreadNamePrefix("cpu-task-thread-");
    //     return taskExecutor;
    // }

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        final DefaultListableBeanFactory beanFactoryToUse = (DefaultListableBeanFactory) beanFactory;
        beanFactoryToUse.getBean(TaskExecutionProperties.class)
                .getPool().forEach((namespace, poolProps) -> {
                    final String beanName = namespace + "TaskExecutor";
                    final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
                    taskExecutor.setCorePoolSize(poolProps.getCoreSize());
                    taskExecutor.setMaxPoolSize(poolProps.getMaxSize());
                    taskExecutor.setQueueCapacity(poolProps.getQueueCapacity());
                    taskExecutor.setKeepAliveSeconds(poolProps.getKeepAliveSec());
                    taskExecutor.setThreadNamePrefix(poolProps.getThreadNamePrefix()
                            .orElse(namespace + "-task-thread-"));
                    beanFactoryToUse.registerSingleton(beanName, taskExecutor);
                });
    }
}
