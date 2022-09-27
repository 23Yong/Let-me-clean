package com.letmeclean.api;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KakaoPayProperties {

    @Value("${api.kakao.pay.url}")
    private String url;

    @Value("${api.kakao.pay.approval}")
    private String approval;

    @Value("${api.kakao.pay.cancel}")
    private String cancel;

    @Value("${api.kakao.pay.fail}")
    private String fail;

    @Value("${api.kakao.pay.prefix}")
    private String prefix;

    @Value("${api.kakao.pay.key}")
    private String key;

    public String getApprovalUrl() {
        return url + approval;
    }

    public String getCancelUrl() {
        return url + cancel;
    }

    public String getFailUrl() {
        return url + fail;
    }

    public String getAuthorization() {
        return prefix + " " + key;
    }
}
