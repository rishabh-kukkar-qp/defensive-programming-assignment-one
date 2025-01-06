package com.dpp.subscriptionupgrade.service;

import com.dpp.subscriptionupgrade.model.request.PaymentRequestModel;
import com.dpp.subscriptionupgrade.model.response.PaymentSuccessFailureResponse;

public interface PaymentService {

    PaymentSuccessFailureResponse processPayment(PaymentRequestModel request);
}
