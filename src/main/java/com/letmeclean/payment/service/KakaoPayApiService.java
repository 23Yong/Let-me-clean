package com.letmeclean.payment.service;

import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.redis.paymentcache.PaymentCache;
import com.letmeclean.global.redis.paymentcache.RedisPaymentCacheRepository;
import com.letmeclean.global.utils.SecurityUtil;
import com.letmeclean.payment.api.KakaoPayClient;
import com.letmeclean.payment.api.KakaoPayProperties;
import com.letmeclean.payment.api.PayClient;
import com.letmeclean.payment.api.dto.request.KakaoPayApproveRequest;
import com.letmeclean.payment.api.dto.request.KakaoPayReadyRequest;
import com.letmeclean.payment.api.dto.response.KakaoPayApproveResponse;
import com.letmeclean.payment.api.dto.response.KakaoPayReadyResponse;
import com.letmeclean.payment.application.dto.PaymentReadyRequest;
import com.letmeclean.payment.service.dto.PaymentApproveDto;
import com.letmeclean.payment.service.dto.PaymentReadyDto;
import com.letmeclean.payment.service.interfaces.PaymentApiService;
import com.letmeclean.ticket.domain.Ticket;
import com.letmeclean.ticket.domain.TicketRepository;
import com.letmeclean.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.letmeclean.ticket.dto.request.TicketRequest.*;

@RequiredArgsConstructor
@Service
public class KakaoPayApiService implements PaymentApiService {

    private final KakaoPayProperties kakaoPayProperties;

    private final PayClient kakaoPayClient;

    private final TicketService ticketService;

    private final TicketRepository ticketRepository;
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
    @Transactional
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

        TicketSoldRequestDto ticketSoldRequestDto = new TicketSoldRequestDto(email, response.getQuantity(), response.getTotalAmount(), paymentCache.getTicketId());
        ticketService.sold(ticketSoldRequestDto);

        paymentCacheRepository.delete(paymentCache);

        return new PaymentApproveDto(email, response);
    }
}
