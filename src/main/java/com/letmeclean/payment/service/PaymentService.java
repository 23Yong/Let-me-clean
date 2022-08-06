package com.letmeclean.payment.service;

import com.letmeclean.payment.domain.Payment;
import com.letmeclean.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
