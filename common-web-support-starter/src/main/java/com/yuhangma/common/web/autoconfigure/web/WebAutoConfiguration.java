package com.yuhangma.common.web.autoconfigure.web;

import com.yuhangma.common.web.converter.LocalDateTimeConverter;
import com.yuhangma.common.web.log.filter.WebHttpLogFilter;
import com.yuhangma.common.web.log.selector.DefaultWebLogSelector;
import com.yuhangma.common.web.log.selector.WebLogSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/6
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
public class WebAutoConfiguration {

    /**
     * 注册请求日志打印 filter bean
     */
    @Bean
    @ConditionalOnProperty(name = "web.http.log.enabled", havingValue = "true")
    public FilterRegistrationBean<WebHttpLogFilter> webHttpLogFilterBean(WebLogSelector logSelector) {
        final WebHttpLogFilter webHttpLogFilter = new WebHttpLogFilter(logSelector);
        final FilterRegistrationBean<WebHttpLogFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("webHttpLogFilter");
        filterRegistrationBean.setFilter(webHttpLogFilter);
        return filterRegistrationBean;
    }

    @Bean
    @ConditionalOnMissingBean(WebLogSelector.class)
    @ConditionalOnProperty(name = "web.http.log.enabled", havingValue = "true")
    public WebLogSelector webLogSelector() {
        return new DefaultWebLogSelector();
    }

    /**
     * http message 转换器配合（默认启用）：时间戳转换为 LocalDateTime
     */
    @Bean
    @ConditionalOnProperty(
            name = "web.http.message.converter.local-date-time.enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public LocalDateTimeConverter localDateTimeConverterBean() {
        return new LocalDateTimeConverter();
    }
}
