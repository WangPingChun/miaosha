package com.imooc.seckill.config;

import com.imooc.seckill.entity.User;
import com.imooc.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author : chris
 * 2018-07-28
 */
@Configuration
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;

    @Autowired
    public UserArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        final Class<?> type = methodParameter.getParameterType();
        return type == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        final HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        final HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        final String token = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        User user;
        if (StringUtils.isEmpty(token) || (user = userService.getByToken(response, token)) == null) {
            return null;
        }
        return user;
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            final Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                    .filter(cookie -> cookieNameToken.equals(cookie.getName()))
                    .findFirst();
            return cookieOptional.map(Cookie::getValue).orElse(null);
        }

        return null;
    }
}
