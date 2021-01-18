package com.maltsev.jwt.resolver;

import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.maltsev.jwt.constants.SecurityConstants.AUTHORIZATION;
import static com.maltsev.jwt.constants.SecurityConstants.BEARER;

public class AccountHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(Auth.class)
                && methodParameter.getParameterType().equals(UserAccount.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        if (supportsParameter(methodParameter)) {
            if (isAuthNotRequired(methodParameter) && Objects.isNull(webRequest.getUserPrincipal())) return null;
            UserAccount account = convert(webRequest);
            return account;
        }
        return WebArgumentResolver.UNRESOLVED;
    }

    private boolean isAuthNotRequired(MethodParameter methodParameter) {
        return !Objects.requireNonNull(methodParameter.getParameterAnnotation(Auth.class)).required();
    }

    private UserAccount convert(NativeWebRequest webRequest) {
        Authentication authentication = (Authentication) webRequest.getUserPrincipal();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserAccount.NotPermitted();
        }
        User principal = (User) authentication.getPrincipal();

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new UserAccount(principal.getUsername(),
                Objects.requireNonNull(StringUtils.removeStart(webRequest.getHeader(AUTHORIZATION), BEARER)).trim());
    }


}
