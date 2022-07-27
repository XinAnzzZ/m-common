package com.yuhangma.common.core.exception;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2022/3/21
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
