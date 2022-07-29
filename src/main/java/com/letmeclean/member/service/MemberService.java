package com.letmeclean.member.service;

import com.letmeclean.member.domain.Member;
import com.letmeclean.member.domain.MemberRepository;
import com.letmeclean.member.dto.MemberRequest.SignUpRequestDto;
import com.letmeclean.payment.domain.Payment;
import com.letmeclean.payment.domain.PaymentRepository;
import com.letmeclean.global.exception.member.DuplicatedEmailException;
import com.letmeclean.global.exception.member.DuplicatedNicknameException;
import com.letmeclean.global.exception.member.InvalidPasswordException;
import com.letmeclean.global.utils.MatcherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    private final PasswordEncoder passwordEncoder;

    private boolean isValidPassword(String password) {
        return MatcherUtil.isMatch(password);
    }

    public boolean checkEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean checkNicknameDuplicated(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (checkEmailDuplicated(signUpRequestDto.getEmail())) {
            throw DuplicatedEmailException.getInstance();
        }
        if (checkNicknameDuplicated(signUpRequestDto.getNickname())) {
            throw DuplicatedNicknameException.getInstance();
        }
        if (!isValidPassword(signUpRequestDto.getPassword())) {
            throw InvalidPasswordException.getInstance();
        }

        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        Member member = signUpRequestDto.toEntity();
        memberRepository.save(member);
    }

    public List<Payment> findPaymentList(String email, Pageable pageable) {
        return paymentRepository.findByEmail(email, pageable);
    }
}
