package com.yuhangma.common.web.log.selector;

import org.springframework.http.HttpMethod;
import org.springframework.util.RouteMatcher;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/23
 */
public abstract class AbstractWebLogSelector implements WebLogSelector {

    protected final RouteMatcher routeMatcher;

    public AbstractWebLogSelector() {
        this.routeMatcher = new PathPatternRouteMatcher();
    }

    @Override
    public boolean selectRequest(HttpServletRequest request) {
        final RouteMatcher.Route route = routeMatcher.parseRoute(request.getRequestURI());
        final HttpMethod method = HttpMethod.resolve(request.getMethod());
        final boolean allowUri = allowUriPattern().stream().anyMatch(pattern -> routeMatcher.match(pattern, route))
                && ignoreUriPattern().stream().noneMatch(pattern -> routeMatcher.match(pattern, route));
        final boolean allowMethod = allowMethod().contains(method) && !ignoreMethod().contains(method);
        return allowUri && allowMethod;
    }

    protected List<String> allowUriPattern() {
        final List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/**");
        return urlPatterns;
    }

    protected List<String> ignoreUriPattern() {
        return Collections.emptyList();
    }

    protected List<HttpMethod> allowMethod() {
        return new ArrayList<>(Arrays.asList(HttpMethod.values()));
    }

    protected List<HttpMethod> ignoreMethod() {
        return Collections.emptyList();
    }

    @Override
    public boolean selectHeader(String headerName) {
        return allowHeaders().stream().anyMatch(header -> header.equalsIgnoreCase(headerName));
    }

    protected List<String> allowHeaders() {
        return Collections.emptyList();
    }

}
