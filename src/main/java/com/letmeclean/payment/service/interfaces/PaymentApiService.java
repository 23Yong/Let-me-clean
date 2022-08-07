package com.letmeclean.payment.service.interfaces;

import com.letmeclean.payment.application.dto.PaymentReadyRequest;
import com.letmeclean.payment.service.dto.PaymentApproveDto;
import com.letmeclean.payment.service.dto.PaymentReadyDto;

public interface PaymentApiService {

    PaymentReadyDto ready(String email, PaymentReadyRequest request);

    PaymentApproveDto approve(String email, String pgToken);
}
