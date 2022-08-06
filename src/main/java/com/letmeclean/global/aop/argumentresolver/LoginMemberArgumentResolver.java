package com.letmeclean.global.aop.argumentresolver;

import com.letmeclean.global.aop.CurrentEmail;
import com.letmeclean.global.utils.SecurityUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasCurrentEmailAnnotation = parameter.hasParameterAnnotation(CurrentEmail.class);
        boolean hasStringType = String.class.isAssignableFrom(parameter.getParameterType());

        return hasCurrentEmailAnnotation && hasStringType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return SecurityUtil.getCurrentMemberEmail();
    }
}
