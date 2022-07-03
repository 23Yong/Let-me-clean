package com.letmeclean.service;

import com.letmeclean.controller.dto.request.member.MemberRequest.SignUpRequestDto;
import com.letmeclean.controller.dto.response.member.MemberResponse.SignUpResponseDto;
import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberRepository;
import com.letmeclean.domain.member.MemberStatus;
import com.letmeclean.exception.member.DuplicatedEmailException;
import com.letmeclean.exception.member.DuplicatedNicknameException;
import com.letmeclean.service.converter.SignUpDtoConverter;
import com.letmeclean.service.encryption.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    SignUpDtoConverter signUpDtoConverter;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    Member member;

    final String hashedPassword = "hashedPassword";

    private SignUpRequestDto createSignUpRequestDto() {
        return SignUpRequestDto.builder()
                .email("23Yong@test.com")
                .password("qkrwjddyd!123")
                .confirmPassword("qkrwjddyd!123")
                .name("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .build();
    }

    private SignUpResponseDto createSignUpResponseDto() {
        return SignUpResponseDto.builder()
                .email("23Yong@test.com")
                .name("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .status(MemberStatus.DEFAULT)
                .build();
    }

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("23Yong@test.com")
                .password(hashedPassword)
                .name("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .build();
    }

    @DisplayName("회원가입에 성공한다.")
    @Test
    void signUpSuccess() {
        // given
        SignUpRequestDto signUpRequestDto = createSignUpRequestDto();
        SignUpResponseDto signUpResponseDto = createSignUpResponseDto();

        given(memberRepository.save(any(Member.class)))
                        .willReturn(member);
        given(passwordEncoder.encrypt(signUpRequestDto.getPassword()))
                        .willReturn(hashedPassword);
        given(signUpDtoConverter.convertSignUpRequestToMember(any(SignUpRequestDto.class), eq(hashedPassword)))
                        .willReturn(member);
        given(signUpDtoConverter.convertMemberToSignUpResponse(any(Member.class)))
                .willReturn(signUpResponseDto);

        // when
        SignUpResponseDto result = memberService.signUp(signUpRequestDto);

        // then
        then(memberRepository).should().save(member);
        then(passwordEncoder).should().encrypt(signUpRequestDto.getPassword());
        then(signUpDtoConverter).should().convertSignUpRequestToMember(signUpRequestDto, hashedPassword);
        then(signUpDtoConverter).should().convertMemberToSignUpResponse(member);

        assertThat(result.getEmail()).isEqualTo(signUpResponseDto.getEmail());
        assertThat(result.getName()).isEqualTo(signUpResponseDto.getName());
        assertThat(result.getNickname()).isEqualTo(signUpResponseDto.getNickname());
        assertThat(result.getTel()).isEqualTo(signUpResponseDto.getTel());
        assertThat(result.getStatus()).isEqualTo(signUpResponseDto.getStatus());
    }

    @DisplayName("중복된 이메일로 인해 회원가입에 실패한다.")
    @Test
    void signUpFailedDuplicatedEmail() {
        // given
        SignUpRequestDto signUpRequestDto = createSignUpRequestDto();

        when(memberRepository.existsByEmail(signUpRequestDto.getEmail()))
                .thenReturn(true);

        // when, then
        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(DuplicatedEmailException.class);

        then(memberRepository).should().existsByEmail(signUpRequestDto.getEmail());
        then(memberRepository).should(never()).save(member);
    }

    @DisplayName("중복된 닉네임으로 인해 회원가입에 실패한다.")
    @Test
    void signUpFailedDuplicatedNickname() {
        // given
        SignUpRequestDto signUpRequestDto = createSignUpRequestDto();

        when(memberRepository.existsByNickname(signUpRequestDto.getNickname()))
                .thenReturn(true);

        // when, then
        assertThatThrownBy(() -> memberService.signUp(signUpRequestDto))
                .isInstanceOf(DuplicatedNicknameException.class);

        then(memberRepository).should().existsByNickname(signUpRequestDto.getNickname());
        then(memberRepository).should(never()).save(member);
    }
}