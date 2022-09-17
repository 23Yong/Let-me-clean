package com.letmeclean.controller;

import com.letmeclean.dto.Response;
import com.letmeclean.dto.ticket.request.TicketSaveRequest;
import com.letmeclean.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/tickets")
@RestController
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public Response<Void> register(@RequestBody TicketSaveRequest request) {
        ticketService.register(request.toDto());
        return Response.success();
    }
}
