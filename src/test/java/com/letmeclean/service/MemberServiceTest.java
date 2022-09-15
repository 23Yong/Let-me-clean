package com.letmeclean.service;

import com.letmeclean.dto.member.MemberDto;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @InjectMocks
    MemberService memberService;

    Member member;

    @DisplayName("멤버가 회원가입을 시도하면")
    @Nested
    class MemberSignUpTest {

        @DisplayName("회원가입에 성공한다.")
        @Test
        void 회원가입_성공() {
            // given
            MemberDto memberDto = createMemberDto();
            Member memberEntity = memberDto.toEntity();

            given(memberRepository.existsByEmail(memberDto.email())).willReturn(false);
            given(memberRepository.existsByNickname(memberDto.nickname())).willReturn(false);
            given(memberRepository.save(any(Member.class))).willReturn(memberEntity);
            given(passwordEncoder.encode(memberDto.password())).willReturn(anyString());

            // when
            memberService.signUp(memberDto);

            // then
            then(memberRepository).should().existsByEmail(anyString());
            then(memberRepository).should().existsByNickname(anyString());
            then(passwordEncoder).should().encode(anyString());
            then(memberRepository).should().save(any(Member.class));
        }

        @DisplayName("중복된 이메일로 인해 회원가입에 실패한다.")
        @Test
        void 중복된_이메일로_회원가입_실패() {
            // given
            MemberDto memberDto = createMemberDto();

            given(memberRepository.existsByEmail(anyString())).willReturn(true);

            // when, then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> memberService.signUp(memberDto));
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_EMAIL_CONFLICT);

            then(memberRepository).should().existsByEmail(anyString());
        }

        @DisplayName("중복된 닉네임으로 인해 회원가입에 실패한다.")
        @Test
        void 중복된_닉네임으로_회원가입_실패() {
            // given
            MemberDto memberDto = createMemberDto();

            given(memberRepository.existsByNickname(anyString())).willReturn(true);

            // when, then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> memberService.signUp(memberDto));
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_NICKNAME_CONFLICT);

            then(memberRepository).should().existsByNickname(anyString());
        }
    }

    @DisplayName("멤버가 사용자의 정보(닉네임, 전화번호) 변경을 시도하면")
    @Nested
    class MemberEditInfoTest {

        @DisplayName("변경에 성공한다.")
        @Test
        void 회원정보변경에_성공() {
            // given
            MemberDto memberDto = MemberDto.of("23Yong@email.com", "", "username", "changedNickname", "010-1111-1234");
            Member memberEntity = memberDto.toEntity();

            when(memberRepository.findByEmail(memberDto.email())).thenReturn(Optional.of(memberEntity));

            // when
            MemberDto changedMemberDto = memberService.modifyMemberInfo(memberDto);

            // then
            then(memberRepository).should().findByEmail(memberDto.email());

            assertThat(changedMemberDto.nickname()).isEqualTo("changedNickname");
            assertThat(changedMemberDto.tel()).isEqualTo("010-1111-1234");
        }

        @DisplayName("회원정보를 찾을 수 없어 변경에 실패한다.")
        @Test
        void 회원정보_찾을수없어_변경에_실패() {
            // given
            MemberDto memberDto = createMemberDto();

            given(memberRepository.findByEmail(memberDto.email())).willReturn(Optional.empty());

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> memberService.modifyMemberInfo(memberDto));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            then(memberRepository).should().findByEmail(memberDto.email());
        }
    }

    @DisplayName("멤버가 비밀번호 변경을 시도하면")
    @Nested
    class MemberEditPasswordTest {

        @DisplayName("변경에 성공한다.")
        @Test
        void 비밀번호_변경에_성공() {
            // given
            String email = "23Yong@email.com";
            String prevPassword = "prevPassword123!";
            String newPassword = "newPassword123!";
            String encodedPassword = "encodedPrevPassword";
            String newEncodedPassword = "encodedNewPassword";

            Member memberEntity = Member.of(email, prevPassword, "username", "nickname", "010-1234-1234");

            given(memberRepository.findByEmail(email)).willReturn(Optional.of(memberEntity));
            given(passwordEncoder.encode(prevPassword)).willReturn(encodedPassword);
            given(passwordEncoder.matches(prevPassword, encodedPassword)).willReturn(true);
            given(passwordEncoder.encode(newPassword)).willReturn(newEncodedPassword);

            // when
            MemberDto memberDto = memberService.modifyPassword(email, prevPassword, newPassword);

            // then
            assertThat(memberDto.password()).isEqualTo(newEncodedPassword);

            then(memberRepository).should().findByEmail(email);
            then(passwordEncoder).should().encode(prevPassword);
            then(passwordEncoder).should().matches(prevPassword, encodedPassword);
        }

        @DisplayName("변경에 성공한다.")
        @Test
        void 회원을_찾을수_없어_비밀번호_변경에_실패() {
            // given
            String email = "23Yong@email.com";
            String prevPassword = "prevPassword123!";
            String newPassword = "newPassword123!";

            given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

            // when & then
            LetMeCleanException e = Assertions.assertThrows(LetMeCleanException.class, () -> memberService.modifyPassword(email, prevPassword, newPassword));

            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
            then(memberRepository).should().findByEmail(email);
            then(passwordEncoder).shouldHaveNoInteractions();
        }
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                1L,
                "23Yong@email.com",
                "thisIsTestPw123!@",
                "23Yong",
                "Nickname",
                "010-1234-1234"
        );
    }
}