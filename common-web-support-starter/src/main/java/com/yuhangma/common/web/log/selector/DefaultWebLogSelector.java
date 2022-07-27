package com.yuhangma.common.web.log.selector;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author Moore.Ma
 * @version 1.0
 * @since 2021/12/21
 */
public class DefaultWebLogSelector extends AbstractWebLogSelector {

    @Value("${web.log.selector.allow.uri.pattern:/**}")
    private List<String> allowUriPatterns;

    @Value("${web.log.selector.ignore.uri.pattern:}#{T(java.util.Collections).emptyList()}")
    private List<String> ignoreUriPatterns;

    @Value("${web.log.selector.allow.headers:}#{T(java.util.Collections).emptyList()}")
    private List<String> allowHeaders;

    @Override
    protected List<String> allowUriPattern() {
        return allowUriPatterns;
    }

    @Override
    protected List<String> ignoreUriPattern() {
        return ignoreUriPatterns;
    }

    @Override
    protected List<String> allowHeaders() {
        return allowHeaders;
    }
}
