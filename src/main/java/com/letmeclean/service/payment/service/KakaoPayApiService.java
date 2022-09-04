package com.letmeclean.service.payment.service;

import com.letmeclean.dto.ticket.request.TicketRequest;
import com.letmeclean.global.exception.ErrorCode;
import com.letmeclean.global.redis.paymentcache.PaymentCache;
import com.letmeclean.global.redis.paymentcache.RedisPaymentCacheRepository;
import com.letmeclean.api.KakaoPayProperties;
import com.letmeclean.api.PayClient;
import com.letmeclean.dto.payment.api.request.KakaoPayApproveRequest;
import com.letmeclean.dto.payment.api.request.KakaoPayReadyRequest;
import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;
import com.letmeclean.dto.payment.api.response.KakaoPayReadyResponse;
import com.letmeclean.dto.payment.request.PaymentReadyRequest;
import com.letmeclean.dto.payment.dto.PaymentApproveDto;
import com.letmeclean.dto.payment.dto.PaymentReadyDto;
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

    private final TicketRepository ticketRepository;
    private final RedisPaymentCacheRepository paymentCacheRepository;

    @Override
    @Transactional
    public PaymentReadyDto ready(String email, PaymentReadyRequest request) {
        String paymentNumber = UUID.randomUUID().toString();
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
    public PaymentApproveDto approve(String email, String pgToken) {
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
