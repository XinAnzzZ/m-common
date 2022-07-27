package com.yuhangma.common.web.log.selector;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志选择器：选择想要打印的日志
 *
 可以通过往 IOC 中注入一个该实例来指定需要打印的日志
 * Example：
 *
 * public class MyLogSelector implements WebLogSelector {
 *
 *     public boolean selectHeader(String headerName) {
 *         return ture;
 *     }
 *
 *
 *     public boolean selectHeader(String uri) {
 *         //  只打印 uri 中包含 /test 的请求日志
 *         return uri.contains("/test");
 *     }
 * }
 *
 * 此选择器可打印所有 header
 *
 * @see com.yuhangma.common.web.log.selector.AbstractWebLogSelector
 * @see com.yuhangma.common.web.log.selector.DefaultWebLogSelector
 *
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/1
 */
public interface WebLogSelector {

    /**
     * 是否选择该 header，默认不打印任何 header
     *
     * @param headerName the header's name
     * @return 是否选择该 header
     */
    default boolean selectHeader(String headerName) {
        return false;
    }

    /**
     * 是否处理该请求的日志打印，默认全部打印
     *
     * @param request 请求对象
     * @return 是否打印改请求
     */
    default boolean selectRequest(HttpServletRequest request) {
        return true;
    }
}
