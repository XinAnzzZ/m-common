package com.yuhangma.common.core.model;

import lombok.Data;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/6
 */
@Data
public class JsonResult<T> {

    private int code;

    private String msg;

    private T data;

    public static final int SUCCESS_CODE = 200;

    public static final String SUCCESS_MSG = "success";

    public static <T> JsonResult<T> success(T data) {
        final JsonResult<T> result = new JsonResult<>();
        result.setCode(SUCCESS_CODE);
        result.setMsg(SUCCESS_MSG);
        result.setData(data);
        return result;
    }
}
