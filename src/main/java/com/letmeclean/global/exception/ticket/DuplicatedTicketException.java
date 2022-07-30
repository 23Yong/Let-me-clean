package com.letmeclean.global.exception.ticket;

public class DuplicatedTicketException extends RuntimeException {

    private static final DuplicatedTicketException INSTANCE = new DuplicatedTicketException();

    public static DuplicatedTicketException getInstance() {
        return INSTANCE;
    }
}
