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

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

import static com.letmeclean.member.dto.MemberRequest.*;
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

    static ValidatorFactory validatorFactory;
    static Validator validator;

    private SignUpRequestDto createMemberSignUpRequestDto() {
        return SignUpRequestDto.builder()
                .email("23Yong@test.com")
                .password("member!123")
                .username("홍길동")
                .nickname("ImGilDong")
                .tel("010-1234-1234")
                .build();
    }

    @BeforeAll
    static void setValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
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

        @DisplayName("빈칸으로 입력해 회원가입에 실패한다.")
        @Test
        void signUpFailEmpty() {
            // given
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                    .email(" ")
                    .password(" ")
                    .nickname(" ")
                    .username(" ")
                    .tel(" ")
                    .build();
            Set<ConstraintViolation<SignUpRequestDto>> constraintViolations = validator.validate(signUpRequestDto);

            // when, then
            assertThat(constraintViolations.size()).isEqualTo(5);
        }
    }

    @DisplayName("멤버가 사용자의 정보(닉네임, 전화번호) 변경을 시도하면")
    @Nested
    class MemberEditInfoTest {

        @DisplayName("변경에 성공한다.")
        @Test
        void editInfoSuccess() {
            // given
            String email = "23Yong@test.com";
            EditInfoRequestDto infoRequestDto = new EditInfoRequestDto(
                    "Changed Nickname",
                    "010-9876-0987"
            );
            when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

            // when
            memberService.editMemberInfo(email, infoRequestDto);

            // then
            assertAll(
                    () -> assertThat(member.getNickname()).isEqualTo("Changed Nickname"),
                    () -> assertThat(member.getTel()).isEqualTo("010-9876-0987")
            );
        }

        @DisplayName("아무것도 입력하지 않아 실패한다.")
        @Test
        void editInfoFailEmpty() {
            // given
            EditInfoRequestDto infoRequestDto = new EditInfoRequestDto(
                    " ",
                    " "
            );

            Set<ConstraintViolation<EditInfoRequestDto>> constraintViolations = validator.validate(infoRequestDto);

            // when, then
            assertThat(constraintViolations.size()).isEqualTo(2);
        }
    }

    @DisplayName("멤버가 비밀번호 변경을 시도하면")
    @Nested
    class MemberEditPasswordTest {
        @DisplayName("변경에 성공한다.")
        @Test
        void editPasswordSuccess() {
            // given
            String email = "23Yong@test.com";
            String newPassword = "newPassword";
            EditPasswordRequestDto editPasswordRequestDto = new EditPasswordRequestDto(
                    member.getPassword(),
                    newPassword
            );

            when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

            // when
            memberService.editPassword(email, editPasswordRequestDto);

            // then
            assertAll(
                    () -> then(passwordEncoder).should().encode(newPassword),
                    () -> passwordEncoder.matches(newPassword, member.getPassword())
            );
        }

        @DisplayName("아무것도 입력하지 않아 실패한다.")
        @Test
        void editInfoFailEmpty() {
            // given
            EditPasswordRequestDto editPasswordRequestDto = new EditPasswordRequestDto(
                    member.getPassword(),
                    " "
            );

            Set<ConstraintViolation<EditPasswordRequestDto>> constraintViolations = validator.validate(editPasswordRequestDto);

            // when, then
            assertThat(constraintViolations.size()).isEqualTo(1);
        }
    }
}