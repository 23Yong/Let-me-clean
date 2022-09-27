package com.letmeclean.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

    private static final String SUCCESS_CODE = "SUCCESS";
    private String resultCode;
    private T result;

    public static Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>(SUCCESS_CODE, result);
    }

    public static Response<Void> success() {
        return new Response<>(SUCCESS_CODE, null);
    }
}
