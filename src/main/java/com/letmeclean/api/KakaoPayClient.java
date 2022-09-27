package com.letmeclean.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.letmeclean.global.utils.SecurityUtil;
import com.letmeclean.dto.payment.api.request.KakaoPayApproveRequest;
import com.letmeclean.dto.payment.api.request.KakaoPayReadyRequest;
import com.letmeclean.dto.payment.api.response.KakaoPayApproveResponse;
import com.letmeclean.dto.payment.api.response.KakaoPayReadyResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class KakaoPayClient implements PayClient {

    private static final String HOST = "https://kapi.kakao.com";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoPayClient() {
        PropertyNamingStrategy snakeCase = PropertyNamingStrategies.SNAKE_CASE;
        objectMapper.setPropertyNamingStrategy(snakeCase);
    }

    public KakaoPayReadyResponse ready(
            @RequestHeader(SecurityUtil.AUTHORIZATION_HEADER) String authorization,
            KakaoPayReadyRequest kakaoPayReadyRequest) {
        HttpHeaders headers = getHttpHeaders(authorization);

        MultiValueMap<String, String> params = convert(kakaoPayReadyRequest);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            return restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyResponse.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public KakaoPayApproveResponse approve(
            @RequestHeader(SecurityUtil.AUTHORIZATION_HEADER) String authorization,
            KakaoPayApproveRequest kakaoPayApproveRequest) {
        HttpHeaders headers = getHttpHeaders(authorization);

        MultiValueMap<String, String> params = convert(kakaoPayApproveRequest);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);
        try {
            return restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApproveResponse.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders getHttpHeaders(String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityUtil.AUTHORIZATION_HEADER, authorization);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        return headers;
    }

    private MultiValueMap<String, String> convert(Object kakaoPayRequest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(kakaoPayRequest, new TypeReference<Map<String, String>>() {});
        params.setAll(map);
        return params;
    }
}
