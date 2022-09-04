package com.letmeclean.dto.issuedticket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class IssuedTicketDetailResponse {

    private String email;
    private String ticketName;
    private String ticketDescription;
    private LocalDateTime issuedAt;
}
