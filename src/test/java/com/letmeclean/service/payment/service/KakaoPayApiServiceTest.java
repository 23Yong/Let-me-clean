package com.letmeclean.service.payment.service;

import com.letmeclean.api.KakaoPayClient;
import com.letmeclean.api.KakaoPayProperties;
import com.letmeclean.dto.payment.api.dto.PaymentApproveDto;
import com.letmeclean.dto.payment.api.dto.PaymentReadyDto;
import com.letmeclean.dto.payment.api.request.KakaoPayApproveRequest;
import com.letmeclean.dto.payment.api.request.KakaoPayReadyRequest;
import com.letmeclean.dto.payment.api.response.Amount;
import com.letmeclean.dto.payment.api.response.CardInfo;
import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;
import com.letmeclean.dto.payment.api.response.KakaoPayReadyResponse;
import com.letmeclean.dto.payment.request.PaymentReadyRequest;
import com.letmeclean.fixture.MemberEntityFixture;
import com.letmeclean.fixture.TicketEntityFixture;
import com.letmeclean.global.redis.paymentcache.PaymentCache;
import com.letmeclean.global.redis.paymentcache.RedisPaymentCacheRepository;
import com.letmeclean.model.member.Member;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.model.ticket.Ticket;
import com.letmeclean.model.ticket.TicketRepository;
import com.letmeclean.service.TicketService;
import com.letmeclean.service.payment.KakaoPayApiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class KakaoPayApiServiceTest {

    @Mock
    KakaoPayClient kakaoPayClient;
    @Mock
    MemberRepository memberRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    RedisPaymentCacheRepository redisPaymentCacheRepository;
    @Mock
    KakaoPayProperties kakaoPayProperties;

    @InjectMocks
    KakaoPayApiService kakaoPayApiService;

    @DisplayName("카카오페이 서버로 단건결제준비 요청을 보내면 올바른 응답을 받는다.")
    @Test
    void 카카오페이_결제준비() {
        // given
        PaymentReadyRequest paymentReadyRequest = new PaymentReadyRequest(1L, 4);
        Member member = MemberEntityFixture.get();
        Ticket ticket = TicketEntityFixture.get();

        KakaoPayReadyResponse response = createReadyResponse();

        given(kakaoPayProperties.getApprovalUrl()).willReturn("");
        given(kakaoPayProperties.getCancelUrl()).willReturn("");
        given(kakaoPayProperties.getFailUrl()).willReturn("");
        given(kakaoPayProperties.getAuthorization()).willReturn("KAKAO");

        given(memberRepository.existsByEmail(member.getEmail())).willReturn(true);
        given(ticketRepository.findById(anyLong())).willReturn(Optional.of(ticket));
        given(kakaoPayClient.ready(anyString(), any(KakaoPayReadyRequest.class))).willReturn(response);
        given(redisPaymentCacheRepository.findByEmail(member.getEmail())).willReturn(Optional.empty());

        // when
        PaymentReadyDto actual = kakaoPayApiService.ready(member.getEmail(), paymentReadyRequest);

        // then
        assertThat(actual.tid()).isEqualTo(response.getTid());
        assertThat(actual.nextRedirectAppUrl()).isEqualTo(response.getNextRedirectAppUrl());
        assertThat(actual.nextRedirectMobileUrl()).isEqualTo(response.getNextRedirectMobileUrl());
        assertThat(actual.nextRedirectPcUrl()).isEqualTo(response.getNextRedirectPcUrl());
        assertThat(actual.androidAppScheme()).isEqualTo(response.getAndroidAppScheme());
        assertThat(actual.iosAppScheme()).isEqualTo(response.getIosAppScheme());

        then(memberRepository).should().existsByEmail(member.getEmail());
        then(ticketRepository).should().findById(anyLong());
        then(redisPaymentCacheRepository).should().findByEmail(member.getEmail());
        then(redisPaymentCacheRepository).should().save(any(PaymentCache.class));
    }

    @DisplayName("카카오페이 서버로 단건결제승인 요청을 보내면 올바른 응답을 받는다.")
    @Test
    void 카카오페이_결제승인() {
        // given
        Member member = MemberEntityFixture.get();
        Ticket ticket = TicketEntityFixture.get();
        KakaoPayReadyResponse readyResponse = createReadyResponse();
        PaymentCache paymentCache = PaymentCache.of(member.getEmail(), ticket.getName(), readyResponse.getTid(), 2L, "1111");
        KakaoPayApproveResponse approveResponse = createApproveResponse();

        given(redisPaymentCacheRepository.findByEmail(member.getEmail())).willReturn(Optional.of(paymentCache));
        given(kakaoPayProperties.getAuthorization()).willReturn("KAKAO");
        given(kakaoPayClient.approve(anyString(), any(KakaoPayApproveRequest.class))).willReturn(approveResponse);

        // when
        PaymentApproveDto actual = kakaoPayApiService.approve(member.getEmail(), "pgToken");

        // then
        then(redisPaymentCacheRepository).should().findByEmail(member.getEmail());
        then(kakaoPayClient).should().approve(anyString(), any(KakaoPayApproveRequest.class));
        then(redisPaymentCacheRepository).should().delete(paymentCache);

        assertThat(actual.email()).isEqualTo(member.getEmail());
        assertThat(actual.tid()).isEqualTo(approveResponse.getTid());
        assertThat(actual.ticketName()).isEqualTo(approveResponse.getItemName());
        assertThat(actual.quantity()).isEqualTo(3);
    }

    private KakaoPayReadyResponse createReadyResponse() {
        return new KakaoPayReadyResponse(
                "tid",
                "nextRedirectUrl",
                "nextRedirectUrl",
                "nextRedirectUrl",
                "appScheme",
                "appScheme",
                LocalDateTime.now()
        );
    }

    private KakaoPayApproveResponse createApproveResponse() {
        return new KakaoPayApproveResponse(
                "aid",
                "tid",
                "cid",
                "tid",
                "partnerOrderId",
                "partnerUserid",
                "paymentMethodType",
                new Amount(),
                new CardInfo(),
                "ticket",
                "code",
                3,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "payload"
        );
    }
}