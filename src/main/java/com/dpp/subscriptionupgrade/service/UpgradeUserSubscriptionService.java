package com.dpp.subscriptionupgrade.service;

import com.dpp.subscriptionupgrade.model.request.UpgradeUserSubscriptionRequestModel;
import com.dpp.subscriptionupgrade.model.response.PaymentSuccessFailureResponse;

public interface UpgradeUserSubscriptionService {

    PaymentSuccessFailureResponse upgradeUserSubscription(String token, UpgradeUserSubscriptionRequestModel requestModel);
}
