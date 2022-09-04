package com.letmeclean.api;

import com.letmeclean.dto.payment.api.request.KakaoPayApproveRequest;
import com.letmeclean.dto.payment.api.request.KakaoPayReadyRequest;
import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;
import com.letmeclean.dto.payment.api.response.KakaoPayReadyResponse;

public interface PayClient {

    KakaoPayReadyResponse ready(String authorization, KakaoPayReadyRequest kakaoPayReadyRequest);

    KakaoPayApproveResponse approve(String authorization, KakaoPayApproveRequest kakaoPayApproveRequest);
}
