package com.yuhangma.common.web.converter.factory;

import com.yuhangma.common.core.util.Asserts;
import com.yuhangma.common.web.converter.CustomEnumConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.stream.Stream;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/9/8
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomEnumConverterFactory implements ConverterFactory<String, Enum> {

    private final String[] props;

    public CustomEnumConverterFactory(String... props) {
        Asserts.isFalse(ArrayUtils.isEmpty(props));
        Stream.of(props).filter(StringUtils::isBlank).findFirst().ifPresent(prop -> {
            throw new IllegalArgumentException("The props can not contains null!");
        });
        this.props = props;
    }

    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new CustomEnumConverter<>(props, targetType);
    }
}
