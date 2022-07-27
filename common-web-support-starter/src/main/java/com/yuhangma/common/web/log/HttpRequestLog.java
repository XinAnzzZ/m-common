package com.yuhangma.common.web.log;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/06
 */
@Getter
@Builder
public class HttpRequestLog {

    /**
     * 协议
     */
    private String protocol;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求头
     */
    @Singular("header")
    private List<String> headers = new ArrayList<>();

    /**
     * 请求体
     */
    private String body;

    @Override
    public String toString() {
        String headers = "[\n\t\t" + String.join(",\n\t\t", this.headers) + "\n\t]";
        return "\n-->request start\n{" +
                "\n\turl: " + url + "," +
                "\n\tmethod: " + method + "," +
                "\n\theaders: " + headers + "," +
                (body != null ? "\n\tbody: " + body.replaceAll("\\n", "") + "," : "") +
                "\n\tprotocol: " + protocol +
                "\n}\n-->request end";
    }
}
