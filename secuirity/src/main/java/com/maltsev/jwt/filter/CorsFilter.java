package com.maltsev.jwt.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Component
public class CorsFilter extends ZuulFilter {
    static final String ALL_ORIGINS = "*";
    static final long SECONDS_IN_DAY = 24 * 60 * 60L;
    static final String ALLOW_METHODS = Stream.of(POST, GET, OPTIONS, DELETE, PUT, PATCH)
            .map(Enum::name)
            .collect(Collectors.joining(", "));
    static final String ALLOW_HEADERS = String.join(", ",
            ORIGIN,
            CONTENT_TYPE,
            ACCEPT,
            AUTHORIZATION,
            CACHE_CONTROL
    );

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ALL_ORIGINS);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, String.valueOf(SECONDS_IN_DAY));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOW_METHODS);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
