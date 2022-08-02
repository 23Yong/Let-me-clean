package com.letmeclean.service;

import com.letmeclean.global.exception.AppException;
import com.letmeclean.member.dto.MemberRequest.SignUpRequestDto;
import com.letmeclean.member.domain.Member;
import com.letmeclean.member.domain.MemberRepository;
import com.letmeclean.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Spy
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @InjectMocks
    MemberService memberService;

    Member member;

    private SignUpRequestDto createMemberSignUpRequestDto() {
        return SignUpRequestDto.builder()
                .email("23Yong@test.com")
                .password("member!123")
                .username("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .build();
    }

    @BeforeEach
    void setUp() {
        String encodedPassword = passwordEncoder.encode("member!123");
        member = Member.builder()
                .email("23Yong@test.com")
                .password(encodedPassword)
                .name("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .build();
    }

    @DisplayName("멤버가 회원가입을 시도하면")
    @Nested
    class MemberSignUpTest {

        @DisplayName("회원가입에 성공한다.")
        @Test
        void signUpSuccess() {
            // given
            SignUpRequestDto signUpRequestDto = createMemberSignUpRequestDto();
            String rawPassword = signUpRequestDto.getPassword();

            given(memberRepository.save(any(Member.class)))
                    .willReturn(member);

            // when
            memberService.signUp(signUpRequestDto);

            // then
            then(memberRepository).should().existsByEmail(signUpRequestDto.getEmail());
            then(memberRepository).should().existsByNickname(signUpRequestDto.getNickname());
            then(memberRepository).should(times(1)).save(any(Member.class));

            assertThat(passwordEncoder.matches(rawPassword, member.getPassword())).isTrue();
        }


        @DisplayName("중복된 이메일로 인해 회원가입에 실패한다.")
        @Test
        void signUpFailedDuplicatedEmail() {
            // given
            SignUpRequestDto signUpRequestDto = createMemberSignUpRequestDto();

            when(memberRepository.existsByEmail(signUpRequestDto.getEmail()))
                    .thenReturn(true);

            // when, then
            assertAll(
                    () -> assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                            .isInstanceOf(AppException.class),
                    () -> then(memberRepository).should().existsByEmail(signUpRequestDto.getEmail()),
                    () -> then(memberRepository).should(never()).save(member)
            );
        }

        @DisplayName("중복된 닉네임으로 인해 회원가입에 실패한다.")
        @Test
        void signUpFailedDuplicatedNickname() {
            // given
            SignUpRequestDto signUpRequestDto = createMemberSignUpRequestDto();

            when(memberRepository.existsByNickname(signUpRequestDto.getNickname()))
                    .thenReturn(true);

            // when, then
            assertAll(
                    () -> assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                            .isInstanceOf(AppException.class),
                    () -> then(memberRepository).should().existsByNickname(signUpRequestDto.getNickname()),
                    () -> then(memberRepository).should(never()).save(member)
            );
        }
    }
}