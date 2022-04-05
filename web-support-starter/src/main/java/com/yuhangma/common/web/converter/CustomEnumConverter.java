package com.yuhangma.common.web.converter;

import com.yuhangma.common.core.util.Asserts;
import com.yuhangma.common.core.util.EnumUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * 将枚举属性值转换为枚举值
 *
 * @formatter:off
 *
 * Example:
 *
 * enum Gender {
 *     MALE(0);
 *
 *     int code;
 * }
 *
 * 无论传入的值是 0，还是 “MALE”，都将转换为 Gender.MALE
 *
 * @formatter:on
 *
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/9/8 11:38
 */
public class CustomEnumConverter<T extends Enum<T>> implements Converter<String, T> {

    private final String[] props;

    private final Class<T> targetType;

    public CustomEnumConverter(String[] props, Class<T> targetType) {
        Asserts.isFalse(ArrayUtils.isEmpty(props));
        Stream.of(props).filter(StringUtils::isBlank).findFirst().ifPresent(prop -> {
            throw new IllegalArgumentException("The props can not contains null!");
        });
        this.props = props;
        this.targetType = targetType;
    }

    @Override
    public T convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        return EnumUtils.propOf(targetType, source, props).orElseGet(() ->
                // 如果属性未匹配上，则使用枚举名称进行匹配
                Arrays.stream(targetType.getEnumConstants())
                        .filter(e -> e.name().equals(source)).findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(String.format("无法将值 %s 转换成对应枚举类型 %s ", source, targetType.getName())))
        );
    }
}
