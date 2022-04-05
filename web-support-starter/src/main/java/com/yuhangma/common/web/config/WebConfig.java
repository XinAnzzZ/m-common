package com.yuhangma.common.web.config;

import com.yuhangma.common.web.converter.factory.CustomEnumConverterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("#{'${jackson.enum.props:code,value}'.split(',')}")
    private String[] enumProps;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CustomEnumConverterFactory(enumProps));
    }
}
