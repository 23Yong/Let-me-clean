package com.letmeclean.dto.payment.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPayReadyResponse {

    // 결제 고유 번호
    private String tid;

    // 요청한 클라이언트(Client)가 모바일 앱일 경우
    private String nextRedirectAppUrl;

    // 요청 클라이언트가 모바일 웹일 경우
    private String nextRedirectMobileUrl;

    // 요청 클라이언트가 PC 웹일 경우
    private String nextRedirectPcUrl;

    // 카카오페이 결제 화면으로 이동하는 Android 앱 스킴(Scheme)
    private String androidAppScheme;

    // 카카오페이 결제 화면으로 이동하는 IOS 앱 스킴
    private String iosAppScheme;

    // 결제 준비 요청 시간
    private LocalDateTime createAt;
}
