package com.letmeclean.service.payment;

import com.letmeclean.dto.ticket.TicketSoldDto;
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
import com.letmeclean.model.coupon.Coupon;
import com.letmeclean.model.coupon.CouponRepository;
import com.letmeclean.model.isseudcoupon.IssuedCoupon;
import com.letmeclean.model.isseudcoupon.IssuedCouponRepository;
import com.letmeclean.model.member.MemberRepository;
import com.letmeclean.service.payment.interfaces.PaymentApiService;
import com.letmeclean.model.ticket.Ticket;
import com.letmeclean.model.ticket.TicketRepository;
import com.letmeclean.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KakaoPayApiService implements PaymentApiService {

    private final KakaoPayProperties kakaoPayProperties;

    private final PayClient kakaoPayClient;

    private final TicketService ticketService;

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final RedisPaymentCacheRepository paymentCacheRepository;

    private String paymentNumber;

    @Override
    @Transactional
    public PaymentReadyDto ready(String email, PaymentReadyRequest request) {
        if (!memberRepository.existsByEmail(email)) {
            throw new LetMeCleanException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", email));
        }
        Ticket ticket = ticketRepository.findById(request.ticketId())
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.TICKET_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", request.ticketId())));

        generatePaymentNumber();

        KakaoPayReadyResponse response = getKakaoPayReadyResponse(request, email, ticket);

        savePaymentInfoInCache(email, ticket, request.issuedCouponId(), response);

        return PaymentReadyDto.kakaoToPaymentReadyDto(response);
    }

    private void generatePaymentNumber() {
        paymentNumber = UUID.randomUUID().toString();
    }

    @Override
    @Transactional
    public PaymentApproveDto approve(String email, String pgToken) {
        PaymentCache paymentCache = paymentCacheRepository.findByEmail(email)
                .orElseThrow(() -> new LetMeCleanException(ErrorCode.BAD_REQUEST_PAYMENT_APPROVE, String.format("%s 님의 요청한 구매 정보를 찾을 수 없습니다.", email)));

        KakaoPayApproveResponse response = getKakaoPayApproveResponse(paymentCache, email, pgToken);

        ticketService.soldAllTickets(TicketSoldDto.of(email, response.getQuantity(), response.getTotalAmount(), paymentCache.getTicketId()));

        useIssuedCoupon(paymentCache);

        return PaymentApproveDto.of(email, response);
    }

    private void useIssuedCoupon(PaymentCache paymentCache) {
        Long issuedCouponId = paymentCache.getIssuedCouponId();
        if (issuedCouponId != null) {
            IssuedCoupon issuedCoupon = issuedCouponRepository.findById(issuedCouponId)
                    .orElseThrow(() -> new LetMeCleanException(ErrorCode.ISSUED_COUPON_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", issuedCouponId)));

            issuedCoupon.useCoupon();
        }
        paymentCacheRepository.delete(paymentCache);
    }

    private int getTotalAmount(PaymentReadyRequest request, Ticket ticket) {
        int totalAmount = ticket.getPrice() * request.ticketQuantity();
        if (request.couponId() != null) {
            Coupon coupon = couponRepository.findById(request.couponId())
                    .orElseThrow(() -> new LetMeCleanException(ErrorCode.COUPON_NOT_FOUND, String.format("%s 를(을) 찾을 수 없습니다.", request.couponId())));

            totalAmount = coupon.getDiscountType().getDiscountedPrice(totalAmount, coupon.getDiscountPrice());
        }
        return totalAmount;
    }

    private void savePaymentInfoInCache(String email, Ticket ticket, Long issuedCouponId, KakaoPayReadyResponse response) {
        PaymentCache paymentCache = PaymentCache.of(
                email,
                ticket.getName(),
                response.getTid(),
                ticket.getId(),
                issuedCouponId,
                paymentNumber
        );

        paymentCacheRepository.findByEmail(email)
                .ifPresentOrElse(
                        p -> { throw new LetMeCleanException(ErrorCode.BAD_REQUEST_PAYMENT_READY, String.format("이미 존재하는 구매 요청입니다.")); },
                        () -> paymentCacheRepository.save(paymentCache)
                );
    }

    private KakaoPayReadyResponse getKakaoPayReadyResponse(PaymentReadyRequest request, String email, Ticket ticket) {
        int totalAmount = getTotalAmount(request, ticket);

        KakaoPayReadyRequest kakaoPayReadyRequest = KakaoPayReadyRequest.of(
                paymentNumber,
                email,
                ticket.getName(),
                request.ticketQuantity(),
                totalAmount,
                kakaoPayProperties.getApprovalUrl(),
                kakaoPayProperties.getCancelUrl(),
                kakaoPayProperties.getFailUrl()
        );

        return kakaoPayClient.ready(kakaoPayProperties.getAuthorization(), kakaoPayReadyRequest);
    }

    private KakaoPayApproveResponse getKakaoPayApproveResponse(PaymentCache paymentCache, String email, String pgToken) {
        KakaoPayApproveRequest request = KakaoPayApproveRequest.of(paymentCache.getTid(), paymentCache.getPaymentNumber(), email, pgToken);
        return kakaoPayClient.approve(kakaoPayProperties.getAuthorization(), request);
    }
}
