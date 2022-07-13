package com.letmeclean.service;

import com.letmeclean.common.utils.MatcherUtil;
import com.letmeclean.domain.cleaner.Cleaner;
import com.letmeclean.domain.cleaner.CleanerRepository;
import com.letmeclean.exception.member.DuplicatedEmailException;
import com.letmeclean.exception.member.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.letmeclean.controller.dto.cleaner.CleanerRequest.*;

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
            throw DuplicatedEmailException.getInstance();
        }
        if (!isValidPassword(signUpRequestDto.getPassword())) {
            throw InvalidPasswordException.getInstance();
        }

        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        Cleaner cleaner = signUpRequestDto.toEntity();
        cleanerRepository.save(cleaner);
    }
}
