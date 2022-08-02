package com.letmeclean.payment.api;

import com.letmeclean.global.utils.SecurityUtil;
import com.letmeclean.payment.api.dto.request.KakaoPayApproveRequest;
import com.letmeclean.payment.api.dto.request.KakaoPayReadyRequest;
import com.letmeclean.payment.api.dto.response.KakaoPayApproveResponse;
import com.letmeclean.payment.api.dto.response.KakaoPayReadyResponse;
import org.springframework.web.bind.annotation.RequestHeader;

public interface PayClient {

    KakaoPayReadyResponse ready(String authorization, KakaoPayReadyRequest kakaoPayReadyRequest);

    KakaoPayApproveResponse approve(String authorization, KakaoPayApproveRequest kakaoPayApproveRequest);
}
