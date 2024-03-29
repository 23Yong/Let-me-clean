package com.letmeclean.dto.payment.api.dto;

import com.letmeclean.dto.payment.api.response.KakaoPayReadyResponse;

public record PaymentReadyDto(
        // 결제 고유 번호
        String tid,
        // 요청한 클라이언트(Client)가 모바일 앱일 경우
        String nextRedirectAppUrl,
        // 요청 클라이언트가 모바일 웹일 경우
        String nextRedirectMobileUrl,
        // 요청 클라이언트가 PC 웹일 경우
        String nextRedirectPcUrl,
        // 카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme)
        String androidAppScheme,
        // 카카오페이 결제 화면으로 이동하는 IOS 앱 스킴
        String iosAppScheme
) {

    public static PaymentReadyDto kakaoToPaymentReadyDto(KakaoPayReadyResponse response) {
        return new PaymentReadyDto(
                response.getTid(),
                response.getNextRedirectAppUrl(),
                response.getNextRedirectMobileUrl(),
                response.getNextRedirectPcUrl(),
                response.getAndroidAppScheme(),
                response.getIosAppScheme()
        );
    }
}
