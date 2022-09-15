package com.letmeclean.service;

import com.letmeclean.dto.member.MemberDto;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
import com.letmeclean.dto.issuedticket.response.IssuedTicketDetailResponse;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
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
    public void signUp(MemberDto memberDto) {
        if (checkEmailDuplicated(memberDto.email())) {
            throw new LetMeCleanException(
                    ErrorCode.DUPLICATE_EMAIL_CONFLICT,
                    String.format("%s 는(은) 이미 존재하는 이메일입니다.", memberDto.email())
            );
        }
        if (checkNicknameDuplicated(memberDto.nickname())) {
            throw new LetMeCleanException(
                    ErrorCode.DUPLICATE_NICKNAME_CONFLICT,
                    String.format("%s 는(은) 이미 존재하는 닉네임입니다.", memberDto.nickname())
            );
        }
        if (!isValidPassword(memberDto.password())) {
            throw new LetMeCleanException(ErrorCode.INVALID_PASSWORD, String.format("해당 비밀번호 형식이 유효하지 않습니다."));
        }

        Member member = memberDto.toEntity(passwordEncoder.encode(memberDto.password()));
        memberRepository.save(member);
    }

    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", email)));
    }

    public List<PaymentDetailResponse> findPaymentList(String email, Pageable pageable) {
        return paymentRepository.findPaymentsByMemberEmail(email, pageable);
    }

    public List<IssuedTicketDetailResponse> findIssuedTicketList(String email, Pageable pageable) {
        return issuedTicketRepository.findIssuedTicketsByEmail(email, pageable);
    }

    @Transactional
    public MemberDto modifyMemberInfo(MemberDto memberDto) {
        Member member = findMember(memberDto.email());
        member.changeNickname(memberDto.nickname());
        member.changeTel(memberDto.tel());

        return MemberDto.fromEntity(member);
    }

    @Transactional
    public MemberDto modifyPassword(String email, String prevPassword, String newPassword) {
        Member member = findMember(email);
        String encodedPassword = passwordEncoder.encode(prevPassword);

        if (!passwordEncoder.matches(prevPassword, encodedPassword)) {
            throw new LetMeCleanException(ErrorCode.BAD_REQUEST_PREV_PASSWORD);
        }
        member.changePassword(passwordEncoder.encode(newPassword));

        return MemberDto.fromEntity(member);
    }
}
