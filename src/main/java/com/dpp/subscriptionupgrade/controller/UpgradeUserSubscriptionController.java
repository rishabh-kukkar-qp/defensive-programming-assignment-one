package com.dpp.subscriptionupgrade.controller;

import com.dpp.subscriptionupgrade.model.JsonResponse;
import com.dpp.subscriptionupgrade.model.request.UpgradeUserSubscriptionRequestModel;
import com.dpp.subscriptionupgrade.model.response.PaymentSuccessFailureResponse;
import com.dpp.subscriptionupgrade.service.UpgradeUserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/upgrade-user-subscription")

public class UpgradeUserSubscriptionController {

    @Autowired
    private UpgradeUserSubscriptionService upgradeUserSubscriptionService;


    @PostMapping
    public ResponseEntity<PaymentSuccessFailureResponse> saveAsset(@RequestHeader(value = "Authorization", required = false) String token,
                                                  @RequestBody UpgradeUserSubscriptionRequestModel requestModel) {
        return new ResponseEntity<PaymentSuccessFailureResponse>(upgradeUserSubscriptionService.upgradeUserSubscription(token, requestModel), HttpStatus.CREATED);
    }


}
