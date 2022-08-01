package com.letmeclean.payment.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.redis.paymentcache.PaymentCache;
import com.letmeclean.global.redis.paymentcache.RedisPaymentCacheRepository;
import com.letmeclean.global.utils.SecurityUtil;
import com.letmeclean.member.domain.Member;
import com.letmeclean.member.domain.MemberRepository;
import com.letmeclean.payment.api.KakaoPayClient;
import com.letmeclean.payment.api.KakaoPayProperties;
import com.letmeclean.payment.api.dto.request.KakaoPayApproveRequest;
import com.letmeclean.payment.api.dto.request.KakaoPayReadyRequest;
import com.letmeclean.payment.api.dto.response.KakaoPayApproveResponse;
import com.letmeclean.payment.api.dto.response.KakaoPayReadyResponse;
import com.letmeclean.payment.application.dto.PaymentReadyRequest;
import com.letmeclean.payment.domain.Payment;
import com.letmeclean.payment.domain.PaymentRepository;
import com.letmeclean.payment.domain.PaymentStatus;
import com.letmeclean.payment.service.dto.PaymentApproveDto;
import com.letmeclean.payment.service.dto.PaymentReadyDto;
import com.letmeclean.payment.service.interfaces.PaymentApiService;
import com.letmeclean.ticket.domain.Ticket;
import com.letmeclean.ticket.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoPayApiService implements PaymentApiService {

    private final KakaoPayProperties kakaoPayProperties;

    private final KakaoPayClient kakaoPayClient;

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final RedisPaymentCacheRepository paymentCacheRepository;

    @Override
    @Transactional
    public PaymentReadyDto ready(PaymentReadyRequest request) {
        String paymentNumber = UUID.randomUUID().toString();
        String email = SecurityUtil.getCurrentMemberEmail();
        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException());

        KakaoPayReadyRequest kakaoPayReadyRequest = new KakaoPayReadyRequest(
                paymentNumber,
                email,
                ticket.getName(),
                request.getTicketQuantity(),
                ticket.getPrice() * request.getTicketQuantity(),
                kakaoPayProperties.getApprovalUrl(),
                kakaoPayProperties.getCancelUrl(),
                kakaoPayProperties.getFailUrl()
        );

        KakaoPayReadyResponse response = kakaoPayClient.ready(kakaoPayProperties.getAuthorization(), kakaoPayReadyRequest);

        PaymentCache paymentCache = new PaymentCache(
                email,
                ticket.getName(),
                response.getTid(),
                ticket.getId(),
                paymentNumber
        );

        paymentCacheRepository.findByEmail(email)
                .ifPresentOrElse(
                        p -> ErrorCode.throwBadRequestPaymentReady(),
                        () -> paymentCacheRepository.save(paymentCache)
                );

        return PaymentReadyDto.kakaoToPaymentReadyDto(response);
    }

    @Override
    public PaymentApproveDto approve(String pgToken) {
        String email = SecurityUtil.getCurrentMemberEmail();
        PaymentCache paymentCache = paymentCacheRepository.findByEmail(email)
                .orElseThrow(() -> ErrorCode.throwBadRequestPaymentApprove());

        KakaoPayApproveRequest kakaoPayApproveRequest = new KakaoPayApproveRequest(
                paymentCache.getTid(),
                paymentCache.getPaymentNumber(),
                email,
                pgToken
        );

        KakaoPayApproveResponse response = kakaoPayClient.approve(kakaoPayProperties.getAuthorization(), kakaoPayApproveRequest);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> ErrorCode.throwMemberNotFound());

        Payment payment = new Payment(
                PaymentStatus.TICKET_PAY_COMPLETED,
                response.getTotalAmount(),
                member
        );
        paymentRepository.save(payment);
        paymentCacheRepository.delete(paymentCache);

        return new PaymentApproveDto(email, response);
    }
}
