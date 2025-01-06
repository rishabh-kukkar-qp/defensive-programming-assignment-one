package com.dpp.subscriptionupgrade.service.impl;

import com.dpp.subscriptionupgrade.model.request.PaymentRequestModel;
import com.dpp.subscriptionupgrade.model.response.PaymentSuccessFailureResponse;
import com.dpp.subscriptionupgrade.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String PAYMENT_API_URL = "https://payment.gateway.com/api/pay";

    @Override
    public PaymentSuccessFailureResponse processPayment(PaymentRequestModel request) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.postForObject(PAYMENT_API_URL, request, PaymentSuccessFailureResponse.class);
        } catch (Exception e) {
            // Log the error and handle it appropriately
            PaymentSuccessFailureResponse errorResponse = new PaymentSuccessFailureResponse();
            errorResponse.setStatus("failure");
            errorResponse.setError(e.getMessage());
            return errorResponse;
        }
    }
}
