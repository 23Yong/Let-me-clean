package com.letmeclean.service;

import com.letmeclean.domain.cleaner.Cleaner;
import com.letmeclean.domain.cleaner.CleanerRepository;
import com.letmeclean.exception.member.DuplicatedEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.letmeclean.controller.dto.cleaner.CleanerRequest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CleanerServiceTest {

    @Mock
    CleanerRepository cleanerRepository;

    @Spy
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @InjectMocks
    CleanerService cleanerService;

    Cleaner cleaner;

    private SignUpRequestDto createSignUpRequestDto() {
        return SignUpRequestDto.builder()
                .email("23Yong@cleaner.com")
                .password("cleaner123!")
                .username("Hong Gil Dong")
                .tel("010-1234-5678")
                .build();
    }

    @BeforeEach
    void setUp() {
        String encodedPassword = passwordEncoder.encode("cleaner123!");
        cleaner = Cleaner.builder()
                .email("23Yong@cleaner.com")
                .password(encodedPassword)
                .username("Hong Gil Dong")
                .tel("010-1234-5678")
                .build();
    }

    @DisplayName("클리너가 회원가입을 시도하면")
    @Nested
    class CleanerSignUpTest {

        @DisplayName("회원가입에 성공한다.")
        @Test
        void signUpSuccess() {
            // given
            SignUpRequestDto signUpRequestDto = createSignUpRequestDto();

            String rawPassword = signUpRequestDto.getPassword();

            given(cleanerRepository.save(any(Cleaner.class)))
                    .willReturn(cleaner);

            // when
            cleanerService.signUp(signUpRequestDto);

            // then
            then(cleanerRepository).should().save(any(Cleaner.class));
            then(cleanerRepository).should().existsByEmail(signUpRequestDto.getEmail());

            assertThat(passwordEncoder.matches(rawPassword, cleaner.getPassword())).isTrue();
        }

        @DisplayName("중복된 이메일로 인해 회원가입에 실패한다.")
        @Test
        void signUpFailedDuplicatedEmail() {
            // given
            SignUpRequestDto signUpRequestDto = createSignUpRequestDto();

            given(cleanerRepository.existsByEmail(signUpRequestDto.getEmail()))
                    .willReturn(true);

            // when, then
            assertAll(
                    () -> assertThatThrownBy(() -> cleanerService.signUp(signUpRequestDto))
                            .isInstanceOf(DuplicatedEmailException.class),
                    () -> then(cleanerRepository).should().existsByEmail(signUpRequestDto.getEmail()),
                    () ->then(cleanerRepository).should(never()).save(any(Cleaner.class))
            );
        }
    }
}