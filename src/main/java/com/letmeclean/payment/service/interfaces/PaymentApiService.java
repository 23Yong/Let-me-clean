package com.letmeclean.payment.service.interfaces;

import com.letmeclean.payment.application.dto.PaymentReadyRequest;
import com.letmeclean.payment.service.dto.PaymentReadyDto;

public interface PaymentApiService {

    PaymentReadyDto ready(PaymentReadyRequest request);
}
