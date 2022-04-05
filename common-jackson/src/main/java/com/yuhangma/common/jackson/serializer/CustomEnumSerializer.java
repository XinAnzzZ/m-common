package com.yuhangma.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yuhangma.common.core.util.Asserts;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/06
 */
public class CustomEnumSerializer extends JsonSerializer<Enum> {

    private final String[] props;

    public CustomEnumSerializer(String... props) {
        Asserts.notEmpty(props);
        for (String prop : props) {
            Asserts.notBlank(prop);
        }
        this.props = props;
    }

    @Override
    public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        try {
            for (String prop : props) {
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(value, prop);
                if (pd != null && pd.getReadMethod() != null) {
                    Method method = pd.getReadMethod();
                    method.setAccessible(true);
                    gen.writeObject(method.invoke(value));
                    return;
                }
            }
            gen.writeString(value.name());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format("序列化枚举异常，enumVal=%s，props=%s", value, Arrays.toString(props)), e);
        }
    }
}
