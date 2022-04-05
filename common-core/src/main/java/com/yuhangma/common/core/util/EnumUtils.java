package com.yuhangma.common.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtils {

    /**
     * 根据传入的枚举属性和枚举值找到对应的枚举
     *
     * @formatter:off
     *
     * Example：
     * {@code
     *
     * // 枚举定义
     * enum Gender {
     *     int code;
     *     int msg;
     *
     *     FEMALE(0, "女"),
     *     MALE(1, "男"),
     *     UNKNOWN(-1, "未知");
     * }
     *
     * static void test() {
     *     Optional<Gender> gender = EnumUtils.propOf(Gender.class, "code", "0");
     *     assert gender.map(g -> g == Gender.FEMALE).orElse(false);
     * }
     *
     * }
     * @formatter:on
     * @param enumClass 枚举类
     * @param prop      枚举类的属性
     * @param propVal   枚举类属性对应的值
     * @param <T>       枚举泛型
     * @return 枚举
     * @throws IllegalArgumentException 如果传入的 {@code enumClass} 类型不是枚举，则抛出此异常
     */
    public static <T extends Enum<?>> Optional<T> propOf(Class<T> enumClass, String propVal, String prop) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(String.format("非法参数：%s 不是枚举类型！", enumClass.getName()));
        }
        if (StringUtils.isBlank(prop)) {
            throw new IllegalArgumentException("非法参数：枚举属性不能为空！");
        }
        if (StringUtils.isBlank(propVal)) {
            return Optional.empty();
        }
        final T[] enumConstants = enumClass.getEnumConstants();
        if (ArrayUtils.isEmpty(enumConstants)) {
            return Optional.empty();
        }
        try {
            for (T enumConstant : enumConstants) {
                PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(enumConstant, prop);
                if (propertyDescriptor == null || propertyDescriptor.getReadMethod() == null) {
                    continue;
                }
                final Method method = propertyDescriptor.getReadMethod();
                method.setAccessible(true);
                if (propVal.equals(method.invoke(enumConstant).toString())) {
                    return Optional.of(enumConstant);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(String.format("反序列化枚举异常，enumClass=%s，propVal=%s，props=%s",
                    enumClass.getName(), propVal, prop), e);
        }
        return Optional.empty();
    }

    public static <T extends Enum<?>> Optional<T> propOf(Class<T> enumClass, String propVal, String... props) {
        if (ArrayUtils.isEmpty(props)) {
            throw new IllegalArgumentException("props 不能为空！");
        }
        Stream.of(props).filter(StringUtils::isBlank).findFirst().ifPresent(prop -> {
            throw new IllegalArgumentException("props 不能包含空值！");
        });
        return Arrays.stream(props).map(prop -> EnumUtils.propOf(enumClass, propVal, prop))
                .filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    /**
     * 使用枚举的 code 属性值获取对应枚举实例，注意枚举类中必须要有 code 属性，若没有，必定返回 Optional.empty()
     *
     * @param enumClass 枚举类
     * @param codeVal   code 值
     * @param <T>       枚举泛型
     * @return 可选的枚举值，需要调用方自己处理 Optional.empty() 的情况
     * @throws IllegalArgumentException 如果传入的 {@code enumClass} 类型不是枚举，则抛出此异常
     */
    public static <T extends Enum<?>> Optional<T> codeOf(Class<T> enumClass, int codeVal)
            throws IllegalArgumentException {
        return propOf(enumClass, String.valueOf(codeVal), "code");
    }

    public static <T extends Enum<?>> T codeOf(Class<T> enumClass, int codeVal, Supplier<T> notFoundSupplier)
            throws IllegalArgumentException {
        return codeOf(enumClass, codeVal).orElseGet(notFoundSupplier);
    }

    public static <T extends Enum<?>> T codeOf(Class<T> enumClass, int codeVal, T notFoundVal)
            throws IllegalArgumentException {
        return codeOf(enumClass, codeVal).orElse(notFoundVal);
    }

    /**
     * 根据枚举值和枚举属性名称，获取枚举对应的值
     *
     * @param enumConstant 枚举值
     * @param props        属性名称
     * @param <E>          枚举泛型
     * @return 枚举对应的值
     */
    public static <E extends Enum<E>> int propVal(E enumConstant, String... props)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (ArrayUtils.isEmpty(props)) {
            throw new IllegalArgumentException("props 不能为空！");
        }
        Stream.of(props).filter(StringUtils::isBlank).findFirst().ifPresent(prop -> {
            throw new IllegalArgumentException("props 不能包含空值！");
        });
        for (String prop : props) {
            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(enumConstant, prop);
            if (propertyDescriptor != null && propertyDescriptor.getReadMethod() != null) {
                final Method method = propertyDescriptor.getReadMethod();
                method.setAccessible(true);
                return (int) method.invoke(enumConstant);
            }
        }
        throw new NoSuchElementException("无法找到对应的属性值！");
    }
}

