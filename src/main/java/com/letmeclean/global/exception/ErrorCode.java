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

    /* 404 NOT_FOUND */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 사용자 정보를 찾을 수 없습니다."),
    TICKET_NOT_FOUND(NOT_FOUND, "해당 티켓 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT */
    DUPLICATE_EMAIL_CONFLICT(CONFLICT, "이미 해당 이메일 정보가 존재합니다."),
    DUPLICATE_NICKNAME_CONFLICT(CONFLICT, "이미 해당 닉네임 정보가 존재합니다."),
    DUPLICATE_TICKET_CONFLICT(CONFLICT, "이미 존재하는 티켓입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    /* 400 BAD_REQUEST */
    public static AppException throwInvalidPassword() {
        throw new AppException(INVALID_PASSWORD);
    }

    public static AppException throwInvalidToken() {
        throw new AppException(INVALID_TOKEN);
    }

    public static AppException throwBadRequestPaymentReady() {
        throw new AppException(BAD_REQUEST_PAYMENT_READY);
    }

    public static AppException throwBadRequestPaymentApprove() {
        throw new AppException(BAD_REQUEST_PAYMENT_APPROVE);
    }

    /* 404 NOT_FOUND */
    public static AppException throwMemberNotFound() {
        throw new AppException(MEMBER_NOT_FOUND);
    }

    public static AppException throwTicketNotFound() {
        throw new AppException(TICKET_NOT_FOUND);
    }

    /* 409 CONFLICT */
    public static AppException throwDuplicateEmailConflict() {
        throw new AppException(DUPLICATE_EMAIL_CONFLICT);
    }

    public static AppException throwDuplicateNicknameConflict() {
        throw new AppException(DUPLICATE_NICKNAME_CONFLICT);
    }

    public static AppException throwDuplicateTicketConflict() {
        throw new AppException(DUPLICATE_TICKET_CONFLICT);
    }
}
