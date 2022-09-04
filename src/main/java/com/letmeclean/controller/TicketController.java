package com.letmeclean.controller;

import com.letmeclean.global.constants.ResponseConstants;
import com.letmeclean.dto.ticket.request.TicketRequest.TicketSaveRequestDto;
import com.letmeclean.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/tickets")
    public ResponseEntity<Void> register(@RequestBody TicketSaveRequestDto ticketSaveRequestDto) {
        ticketService.register(ticketSaveRequestDto);
        return ResponseConstants.CREATED;
    }
}
