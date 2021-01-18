package com.maltsev.jwt.resolver;

import com.maltsev.jwt.UserAccount;
import com.maltsev.jwt.exception.AccountNotSetException;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@AllArgsConstructor
public class TestAuthenticationArgumentResolver extends AccountHandlerMethodArgumentResolver {
    private UserAccount account;

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        if (Objects.isNull(account)) {
            throw new AccountNotSetException("Endpoint requires authentication and User Account is not set");
        }
        return account;
    }
}
