package com.letmeclean.controller;

import com.letmeclean.dto.Response;
import com.letmeclean.dto.order.request.OrdersRequest;
import com.letmeclean.global.aop.CurrentEmail;
import com.letmeclean.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrdersService ordersService;

    @PostMapping
    public Response<Void> pickUpOrder(
            @CurrentEmail String email,
            @RequestBody OrdersRequest request
    ) {
        ordersService.pickUpOrder(request.toDto(email));
        return Response.success();
    }
}
