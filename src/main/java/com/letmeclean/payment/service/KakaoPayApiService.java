package com.letmeclean.payment.service;

import com.letmeclean.global.utils.SecurityUtil;
import com.letmeclean.payment.api.KakaoPayClient;
import com.letmeclean.payment.api.KakaoPayProperties;
import com.letmeclean.payment.api.dto.request.KakaoPayReadyRequest;
import com.letmeclean.payment.api.dto.response.KakaoPayReadyResponse;
import com.letmeclean.payment.application.dto.PaymentReadyRequest;
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
    private final TicketRepository ticketRepository;

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

        return PaymentReadyDto.kakaoToPaymentReadyDto(response);
    }
}
