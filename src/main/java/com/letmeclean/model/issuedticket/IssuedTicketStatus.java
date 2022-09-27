package com.letmeclean.model.issuedticket;

public enum IssuedTicketStatus {

    TICKET_NOT_USED {
        @Override
        public boolean isUsedTicket() {
            return false;
        }
    },
    TICKET_USED {
        @Override
        public boolean isUsedTicket() {
           return true;
        }
    };

    public abstract boolean isUsedTicket();
}
