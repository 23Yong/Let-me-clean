package com.letmeclean.model.order;

public enum OrderState {

    REQUESTED("요청완료"),
    ACCEPTED("요청승인"),
    COMPLETED("수거완료");

    private final String description;

    OrderState(String description) {
        this.description = description;
    }
}
