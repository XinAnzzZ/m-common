package com.yuhangma.common.core.model;

import lombok.Data;

import java.util.Optional;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/6
 */
@Data
public class JsonResult<T> {

    private int code;

    private String msg;

    private Optional<T> data = Optional.empty();

    public static final int SUCCESS_CODE = 200;

    public static final String SUCCESS_MSG = "success";

    public static <T> JsonResult<T> success() {
        final JsonResult<T> result = new JsonResult<>();
        result.setCode(SUCCESS_CODE);
        result.setMsg(SUCCESS_MSG);
        return result;
    }

    public static <T> JsonResult<T> success(T data) {
        final JsonResult<T> result = new JsonResult<>();
        result.setCode(SUCCESS_CODE);
        result.setMsg(SUCCESS_MSG);
        result.setData(Optional.of(data));
        return result;
    }

    public static <T> JsonResult<T> fail() {
        final JsonResult<T> result = new JsonResult<>();
        result.setCode(1000);
        result.setMsg("服务异常");
        return result;
    }

    public static <T> JsonResult<T> fail(String msg) {
        final JsonResult<T> result = new JsonResult<>();
        result.setCode(1000);
        result.setMsg(msg);
        return result;
    }

    public static <T> JsonResult<T> fail(Integer code, String msg) {
        final JsonResult<T> result = new JsonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
