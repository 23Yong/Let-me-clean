package com.letmeclean.config;

import com.letmeclean.service.encryption.PasswordEncoder;
import com.letmeclean.service.encryption.SHA256EncryptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA256EncryptionService();
    }
}
