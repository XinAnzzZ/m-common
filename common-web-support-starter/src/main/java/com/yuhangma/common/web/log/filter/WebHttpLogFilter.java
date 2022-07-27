package com.yuhangma.common.web.log.filter;

import com.yuhangma.common.web.log.HttpRequestLog;
import com.yuhangma.common.web.log.HttpResponseLog;
import com.yuhangma.common.web.log.request.ReadRepeatableRequestWrapper;
import com.yuhangma.common.web.log.selector.WebLogSelector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * web http 请求响应日志打印 filter
 *
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/11/29
 */
@Slf4j
public class WebHttpLogFilter extends OncePerRequestFilter {

    private final WebLogSelector logSelector;

    public WebHttpLogFilter(WebLogSelector logSelector) {
        this.logSelector = logSelector;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (!logSelector.selectRequest(request) || isMultipartRequest(request)) {
            chain.doFilter(request, response);
            return;
        }
        // 替换请求响应对象
        if (!(request instanceof ReadRepeatableRequestWrapper)) {
            request = new ReadRepeatableRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }

        // 打印日志
        doLog(request, response, chain);
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return (contentType != null && StringUtils.startsWithIgnoreCase(contentType, "multipart"));
    }

    private void doLog(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logRequest(request);
        long startNs = System.nanoTime();
        chain.doFilter(request, response);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
        logResponse(request, response, tookMs);
        // 将 body 复制回 response
        try {
            ((ContentCachingResponseWrapper) response).copyBodyToResponse();
        } catch (IOException e) {
            log.error(String.format("复制流到 response 失败: %s", request.getRequestURI()), e);
        }
    }

    private void logRequest(HttpServletRequest request) {
        ReadRepeatableRequestWrapper requestToUse = WebUtils
                .getNativeRequest(request, ReadRepeatableRequestWrapper.class);
        if (requestToUse == null) {
            return;
        }
        final HttpRequestLog.HttpRequestLogBuilder logBuilder = HttpRequestLog.builder()
                .protocol(requestToUse.getProtocol())
                .url(getUrl(requestToUse))
                .method(requestToUse.getMethod());
        final List<String> headers = Collections.list(requestToUse.getHeaderNames()).stream()
                // 筛选出需要打印的 header
                .filter(logSelector::selectHeader)
                .map(headerName -> String.format("%s: %s", headerName, requestToUse.getHeader(headerName)))
                .collect(Collectors.toList());
        logBuilder.headers(headers);
        // append body
        try {
            String body = new String(requestToUse.getCachedContent(), StandardCharsets.UTF_8);
            logBuilder.body(body);
        } catch (Exception e) {
            logBuilder.body("读取body时发生异常");
        }
        log.info(logBuilder.build().toString());
    }

    @SneakyThrows
    private void logResponse(HttpServletRequest request, HttpServletResponse response, long tookMs) {
        if (!(response instanceof ContentCachingResponseWrapper)) {
            return;
        }
        final ContentCachingResponseWrapper responseToUse = (ContentCachingResponseWrapper) response;
        HttpResponseLog.HttpResponseLogBuilder logBuilder = HttpResponseLog.builder()
                .code(responseToUse.getStatus())
                .url(getUrl(request))
                .tookMs(tookMs);
        final List<String> headers = responseToUse.getHeaderNames().stream()
                .map(responseToUse::getHeader)
                .collect(Collectors.toList());
        logBuilder.headers(headers);
        // body
        try {
            final String body = new String(responseToUse.getContentAsByteArray(), StandardCharsets.UTF_8);
            logBuilder.body(body);
        } catch (Exception e) {
            log.error(String.format("读取 response body 失败: %s", request.getRequestURI()), e);
            logBuilder.body("读取response body失败");
        }
        log.info(logBuilder.build().toString());
    }

    private String getUrl(HttpServletRequest request) {
        String query = request.getQueryString();
        String url = request.getRequestURL().toString();
        return query == null ? url : url + "?" + query;
    }

}
