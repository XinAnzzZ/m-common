package com.yuhangma.common.web.converter;

import com.yuhangma.common.core.util.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/13
 */
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        return DateUtils.toLocalDateTime(Long.parseLong(source));
    }
}
