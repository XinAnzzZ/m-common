package com.yuhangma.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.yuhangma.common.core.util.Asserts;
import com.yuhangma.common.core.util.EnumUtils;
import lombok.Setter;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/06
 */
public class CustomEnumDeserializer extends JsonDeserializer<Enum<?>> implements ContextualDeserializer {

    private final String[] props;

    @Setter
    private Class<Enum<?>> enumClass;

    public CustomEnumDeserializer(String... props) {
        Asserts.notEmpty(props);
        for (String prop : props) {
            Asserts.notBlank(prop);
        }
        this.props = props;
    }

    @Override
    public Enum<?> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();
        return EnumUtils.propOf(enumClass, text, props)
                .orElseGet(
                        () -> Stream.of(enumClass.getEnumConstants())
                                .filter(e -> e.name().equals(text))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                        String.format("无法将值 %s 转换成对应枚举类型 %s ", text, enumClass.getName())))
                );
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<?> createContextual(DeserializationContext ctx, BeanProperty property) {
        Class<?> rawClass = ctx.getContextualType().getRawClass();
        Asserts.isTrue(rawClass.isEnum());

        Class<Enum<?>> enumClass = (Class<Enum<?>>) rawClass;
        CustomEnumDeserializer clone = new CustomEnumDeserializer(props);
        clone.setEnumClass(enumClass);
        return clone;
    }
}
