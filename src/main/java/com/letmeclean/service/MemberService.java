package com.letmeclean.service;

import com.letmeclean.common.utils.SecurityUtil;
import com.letmeclean.domain.member.Member;
import com.letmeclean.domain.member.MemberRepository;
import com.letmeclean.controller.dto.member.request.MemberRequest.SignUpRequestDto;
import com.letmeclean.domain.payment.Payment;
import com.letmeclean.domain.payment.PaymentRepository;
import com.letmeclean.exception.member.DuplicatedEmailException;
import com.letmeclean.exception.member.DuplicatedNicknameException;
import com.letmeclean.exception.member.InvalidPasswordException;
import com.letmeclean.common.utils.MatcherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
