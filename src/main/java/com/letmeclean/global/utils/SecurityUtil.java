package com.letmeclean.global.utils;

import com.letmeclean.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static String getCurrentMemberEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            ErrorCode.throwMemberNotFound();
        }
        return authentication.getName();
    }
}
