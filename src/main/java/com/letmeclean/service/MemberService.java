package com.letmeclean.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
import com.letmeclean.dto.issuedticket.response.IssuedTicketDetailResponse;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.dto.member.request.MemberRequest.EditInfoRequestDto;
import com.letmeclean.dto.member.request.MemberRequest.EditPasswordRequestDto;
import com.letmeclean.dto.member.request.MemberRequest.SignUpRequestDto;
import com.letmeclean.model.payment.PaymentRepository;
import com.letmeclean.global.utils.MatcherUtil;
import com.letmeclean.dto.payment.response.PaymentDetailResponse;
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
    private final IssuedTicketRepository issuedTicketRepository;

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
            ErrorCode.throwDuplicateEmailConflict();
        }
        if (checkNicknameDuplicated(signUpRequestDto.getNickname())) {
            ErrorCode.throwDuplicateNicknameConflict();
        }
        if (!isValidPassword(signUpRequestDto.getPassword())) {
            ErrorCode.throwInvalidPassword();
        }

        signUpRequestDto.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));

        Member member = signUpRequestDto.toEntity();
        memberRepository.save(member);
    }

    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> ErrorCode.throwMemberNotFound());
    }

    public List<PaymentDetailResponse> findPaymentList(String email, Pageable pageable) {
        return paymentRepository.findPaymentsByMemberEmail(email, pageable);
    }

    public List<IssuedTicketDetailResponse> findIssuedTicketList(String email, Pageable pageable) {
        return issuedTicketRepository.findIssuedTicketsByEmail(email, pageable);
    }

    @Transactional
    public void editMemberInfo(String email, EditInfoRequestDto editInfoRequestDto) {
        Member member = findMember(email);
        member.changeNickname(editInfoRequestDto.getNickname());
        member.changeTel(editInfoRequestDto.getTel());
    }

    @Transactional
    public void editPassword(String email, EditPasswordRequestDto editPasswordRequestDto) {
        Member member = findMember(email);
        String prevPassword = editPasswordRequestDto.getPrevPassword();
        String newPassword = editPasswordRequestDto.getNewPassword();
        String encodedPassword = passwordEncoder.encode(prevPassword);

        if (passwordEncoder.matches(prevPassword, encodedPassword)) {
            member.changePassword(passwordEncoder.encode(newPassword));
        } else {
            ErrorCode.throwBadRequestPrevPassword();
        }
    }
}