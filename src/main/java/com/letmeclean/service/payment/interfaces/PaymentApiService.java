package com.letmeclean.service.payment.interfaces;

import com.letmeclean.dto.payment.request.PaymentReadyRequest;
import com.letmeclean.dto.payment.api.dto.PaymentApproveDto;
import com.letmeclean.dto.payment.api.dto.PaymentReadyDto;

public interface PaymentApiService {

    PaymentReadyDto ready(String email, PaymentReadyRequest request);

    PaymentApproveDto approve(String email, String pgToken);
}
