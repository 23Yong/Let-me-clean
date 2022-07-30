package com.letmeclean.global.config;

import com.letmeclean.global.security.filter.JwtFilter;
import com.letmeclean.global.security.jwt.JwtAccessDeniedHandler;
import com.letmeclean.global.security.jwt.JwtAuthenticationEntryPoint;
import com.letmeclean.global.security.jwt.LogoutSuccessHandler;
import com.letmeclean.global.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(
                            "/login",
                            "/reissue",
                            "/members", "/member-emails/**", "/member-nicknames/**",
                            "/cleaners", "/cleaner-emails/**").permitAll()
                    .antMatchers("/payments").hasRole("MEMBER")
                    .antMatchers("/tickets").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .logoutSuccessHandler(logoutSuccessHandler)
                );
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        // JWT 를 통해 Authentication / Authorization 수행
        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
