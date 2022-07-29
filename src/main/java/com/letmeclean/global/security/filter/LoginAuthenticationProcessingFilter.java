package com.letmeclean.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letmeclean.global.security.jwt.LoginAuthenticationSuccessHandler;
import com.letmeclean.global.utils.RequestWrapper;
import com.letmeclean.global.security.jwt.LoginAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.letmeclean.member.dto.MemberRequest.*;

@Component
public class LoginAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public LoginAuthenticationProcessingFilter(LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler,
                                               LoginAuthenticationFailureHandler loginAuthenticationFailureHandler) {
        super("/login");
        objectMapper = new ObjectMapper();
        setAuthenticationSuccessHandler(loginAuthenticationSuccessHandler);
        setAuthenticationFailureHandler(loginAuthenticationFailureHandler);
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        LoginRequestDto loginRequestDto = objectMapper.readValue(requestWrapper.getReader(), LoginRequestDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
