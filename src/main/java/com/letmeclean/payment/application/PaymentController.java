package com.letmeclean.payment.application;

import com.letmeclean.global.aop.CurrentEmail;
import com.letmeclean.payment.application.dto.PaymentReadyRequest;
import com.letmeclean.payment.service.dto.PaymentApproveDto;
import com.letmeclean.payment.service.dto.PaymentReadyDto;
import com.letmeclean.payment.service.interfaces.PaymentApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
public class PaymentController {

    private final PaymentApiService paymentApiService;

    @PostMapping("/ready")
    public ResponseEntity<PaymentReadyDto> ready(@CurrentEmail String email, @RequestBody PaymentReadyRequest request) {
        PaymentReadyDto paymentReadyDto = paymentApiService.ready(email, request);

        return ResponseEntity.ok(paymentReadyDto);
    }

    @GetMapping("/approve")
    public ResponseEntity<PaymentApproveDto> approve(@CurrentEmail String email, @RequestParam("pg_token") String pgToken) {
        PaymentApproveDto paymentApproveDto = paymentApiService.approve(email, pgToken);

        return ResponseEntity.ok(paymentApproveDto);
    }
}
