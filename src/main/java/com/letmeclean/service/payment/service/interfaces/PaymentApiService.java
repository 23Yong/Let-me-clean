package com.letmeclean.service.payment.service.interfaces;

import com.letmeclean.dto.payment.request.PaymentReadyRequest;
import com.letmeclean.dto.payment.dto.PaymentApproveDto;
import com.letmeclean.dto.payment.dto.PaymentReadyDto;

public interface PaymentApiService {

    PaymentReadyDto ready(String email, PaymentReadyRequest request);

    PaymentApproveDto approve(String email, String pgToken);
}
