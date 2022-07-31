package com.letmeclean.cleaner.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.utils.MatcherUtil;
import com.letmeclean.cleaner.domain.Cleaner;
import com.letmeclean.cleaner.domain.CleanerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.letmeclean.cleaner.dto.request.CleanerRequest.*;

@RequiredArgsConstructor
@Service
public class CleanerService {

    private final CleanerRepository cleanerRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean isValidPassword(String password) {
        return MatcherUtil.isMatch(password);
    }

    public boolean checkEmailDuplicated(String email) {
        return cleanerRepository.existsByEmail(email);
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (checkEmailDuplicated(signUpRequestDto.getEmail())) {
            ErrorCode.throwDuplicateEmailConflict();
        }
        if (!isValidPassword(signUpRequestDto.getPassword())) {
            ErrorCode.throwInvalidPassword();
        }

        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        Cleaner cleaner = signUpRequestDto.toEntity();
        cleanerRepository.save(cleaner);
    }
}
