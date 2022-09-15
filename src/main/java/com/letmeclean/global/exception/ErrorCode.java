package com.letmeclean.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST */
    INVALID_PASSWORD(BAD_REQUEST, "유효한 비밀번호가 아닙니다."),
    INVALID_TOKEN(BAD_REQUEST, "유효한 토큰이 아닙니다."),
    BAD_REQUEST_PAYMENT_READY(BAD_REQUEST, "이미 진행중인 결제가 존재합니다."),
    BAD_REQUEST_PAYMENT_APPROVE(BAD_REQUEST, "처리하고자 하는 결제 정보가 존재하지 않습니다."),
    BAD_REQUEST_PREV_PASSWORD(BAD_REQUEST, "이전 비밀번호와 일치하지 않습니다."),
    BAD_REQUEST_COUPON_EXPIRED_TIME(BAD_REQUEST, "쿠폰의 유효기간이 생성시간보다 빠릅니다."),

    /* 404 NOT_FOUND */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 사용자 정보를 찾을 수 없습니다."),
    TICKET_NOT_FOUND(NOT_FOUND, "해당 티켓 정보를 찾을 수 없습니다."),
    COUPON_NOT_FOUND(NOT_FOUND, "해당 쿠폰 정보를 찾을 수 없습니다."),
    ISSUED_COUPON_NOT_FOUND(NOT_FOUND, "해당 발급 쿠폰 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT */
    DUPLICATE_EMAIL_CONFLICT(CONFLICT, "이미 해당 이메일 정보가 존재합니다."),
    DUPLICATE_NICKNAME_CONFLICT(CONFLICT, "이미 해당 닉네임 정보가 존재합니다."),
    DUPLICATE_TICKET_CONFLICT(CONFLICT, "이미 존재하는 티켓입니다."),

    /* 500 INTERNAL SERVER ERROR */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
