package com.letmeclean.global.utils;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static String getCurrentMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 을(를) 찾을 수 없습니다.", authentication.getName()));
        }
        return authentication.getName();
    }
}
