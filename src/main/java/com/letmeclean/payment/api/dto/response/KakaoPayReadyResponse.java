package com.letmeclean.payment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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

    @ConstructorProperties({"tid", "next_redirect_pc_url", "created_at"})
    public KakaoPayReadyResponse(String tid, String nextRedirectPcUrl, LocalDateTime createAt) {
        this.tid = tid;
        this.nextRedirectPcUrl = nextRedirectPcUrl;
        this.createAt = createAt;
    }
}
