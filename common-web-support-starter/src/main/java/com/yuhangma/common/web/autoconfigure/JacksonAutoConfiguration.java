package com.yuhangma.common.web.autoconfigure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.yuhangma.common.jackson.serializer.CustomEnumDeserializer;
import com.yuhangma.common.jackson.serializer.CustomEnumSerializer;
import com.yuhangma.common.web.properties.JacksonProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/6
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@ConditionalOnProperty(prefix = "jackson.config", value = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonAutoConfiguration {

    @Value("#{'${jackson.enum.props:code,value}'.split(',')}")
    private String[] enumProps;

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.deactivateDefaultTyping();
        objectMapper.setTimeZone(TimeZone.getDefault());
        objectMapper.registerModule(dateTimeModule());
        objectMapper.registerModule(enumModule(enumProps));
        // 支持 java8 特性，例如 optional 的序列化与反序列化
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

    public SimpleModule dateTimeModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        // module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
        // module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMAT_PATTERN)));
        return module;
    }

    public SimpleModule enumModule(String... enumProps) {
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Enum.class, new CustomEnumDeserializer(enumProps));
        simpleModule.addSerializer(Enum.class, new CustomEnumSerializer(enumProps));
        return simpleModule;
    }
}
