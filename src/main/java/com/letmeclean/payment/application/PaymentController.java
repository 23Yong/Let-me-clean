package com.letmeclean.payment.application;

import com.letmeclean.payment.application.dto.PaymentReadyRequest;
import com.letmeclean.payment.service.dto.PaymentReadyDto;
import com.letmeclean.payment.service.interfaces.PaymentApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/payments")
@RestController
public class PaymentController {

    private final PaymentApiService paymentApiService;

    @PostMapping("/ready")
    public ResponseEntity<PaymentReadyDto> ready(@RequestBody @Valid PaymentReadyRequest request) {
        PaymentReadyDto paymentReadyDto = paymentApiService.ready(request);

        return ResponseEntity.ok(paymentReadyDto);
    }
}
