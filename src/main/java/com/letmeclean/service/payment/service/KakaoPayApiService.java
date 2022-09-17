package com.letmeclean.service.payment.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.exception.LetMeCleanException;
import com.letmeclean.global.redis.paymentcache.PaymentCache;
import com.letmeclean.global.redis.paymentcache.RedisPaymentCacheRepository;
import com.letmeclean.api.KakaoPayProperties;
import com.letmeclean.api.PayClient;
import com.letmeclean.dto.payment.api.request.KakaoPayApproveRequest;
import com.letmeclean.dto.payment.api.request.KakaoPayReadyRequest;
import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;
import com.letmeclean.dto.payment.api.response.KakaoPayReadyResponse;
import com.letmeclean.dto.payment.request.PaymentReadyRequest;
import com.letmeclean.dto.payment.api.dto.PaymentApproveDto;
import com.letmeclean.dto.payment.api.dto.PaymentReadyDto;
import com.letmeclean.model.issuedticket.IssuedTicketRepository;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.service.payment.service.interfaces.PaymentApiService;
import com.letmeclean.model.ticket.Ticket;
import com.letmeclean.model.ticket.TicketRepository;
import com.letmeclean.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.letmeclean.dto.ticket.request.TicketRequest.*;

@RequiredArgsConstructor
@Service
public class KakaoPayApiService implements PaymentApiService {

    private final KakaoPayProperties kakaoPayProperties;

    private final PayClient kakaoPayClient;

    private final TicketService ticketService;

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final RedisPaymentCacheRepository paymentCacheRepository;

    @Override
    @Transactional
    public PaymentReadyDto ready(String email, PaymentReadyRequest request) {
        String paymentNumber = UUID.randomUUID().toString();
        if (!memberRepository.existsByEmail(email)) {
            throw new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", email));
        }
        Ticket ticket = ticketRepository.findById(request.ticketId())
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.TICKET_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", request.ticketId())));

        KakaoPayReadyRequest kakaoPayReadyRequest = KakaoPayReadyRequest.of(
                paymentNumber,
                email,
                ticket.getName(),
                request.ticketQuantity(),
                ticket.getPrice() * request.ticketQuantity(),
                kakaoPayProperties.getApprovalUrl(),
                kakaoPayProperties.getCancelUrl(),
                kakaoPayProperties.getFailUrl()
        );

        KakaoPayReadyResponse response = kakaoPayClient.ready(kakaoPayProperties.getAuthorization(), kakaoPayReadyRequest);

        PaymentCache paymentCache = PaymentCache.of(
                email,
                ticket.getName(),
                response.getTid(),
                ticket.getId(),
                paymentNumber
        );

        paymentCacheRepository.findByEmail(email)
                .ifPresentOrElse(
                        p -> {
                            throw new LetMeCleanException(ErrorCode.BAD_REQUEST_PAYMENT_READY);
                        },
                        () -> paymentCacheRepository.save(paymentCache)
                );

        return PaymentReadyDto.kakaoToPaymentReadyDto(response);
    }

    @Override
    @Transactional
    public PaymentApproveDto approve(String email, String pgToken) {
        PaymentCache paymentCache = paymentCacheRepository.findByEmail(email)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.BAD_REQUEST_PAYMENT_APPROVE));

        KakaoPayApproveRequest kakaoPayApproveRequest = new KakaoPayApproveRequest(
                paymentCache.getTid(),
                paymentCache.getPaymentNumber(),
                email,
                pgToken
        );

        KakaoPayApproveResponse response = kakaoPayClient.approve(kakaoPayProperties.getAuthorization(), kakaoPayApproveRequest);

        TicketSoldRequestDto ticketSoldRequestDto = new TicketSoldRequestDto(email, response.getQuantity(), response.getTotalAmount(), paymentCache.getTicketId());
        ticketService.sold(ticketSoldRequestDto);

        paymentCacheRepository.delete(paymentCache);

        return PaymentApproveDto.of(email, response);
    }
}
