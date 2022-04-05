package com.yuhangma.common.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Asserts {

    public static <T> void notEmpty(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("数组至少应该包含一个元素！");
        }
    }

    public static <T extends CharSequence> void notBlank(T text) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("字符串不能为空！");
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("表达式的结果必须为 TRUE！");
        }
    }

    public static void isFalse(boolean expression) {
        if (expression) {
            throw new IllegalArgumentException("表达式的结果必须为 FALSE！");
        }
    }
}
