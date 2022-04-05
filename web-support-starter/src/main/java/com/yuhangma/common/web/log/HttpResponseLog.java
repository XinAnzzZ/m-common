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
public class HttpResponseLog {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String msg;

    /**
     * 响应地址
     */
    private String url;

    /**
     * 响应头
     */
    @Singular("header")
    private List<String> headers = new ArrayList<>();

    /**
     * 响应体
     */
    private String body;

    /**
     * 耗时(ms)
     */
    private long tookMs;

    /**
     * 异常
     */
    private String errorMsg;

    @Override
    public String toString() {
        if (errorMsg == null) {
            String headers = "[\n\t\t" + String.join(",\n\t\t", this.headers) + "\n\t]";
            return "\n<--response start\n{" +
                    "\n\tcode: " + code + "," +
                    "\n\tmsg: " + msg + "," +
                    "\n\turl: " + url + "," +
                    "\n\theaders: " + headers + "," +
                    (body != null ? "\n\tbody: " + body.replaceAll("\\n", "") + "," : "") +
                    "\n\ttookMs: " + tookMs + "ms" +
                    "\n}\n<--response end";
        } else {
            return "\n<--response start\n{" +
                    "\n\turl: " + url + "," +
                    "\n\terrorMsg: " + errorMsg +
                    "\n}\n<--response end";
        }
    }
}
