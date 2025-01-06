package com.dpp.subscriptionupgrade.service.impl;

import com.dpp.subscriptionupgrade.entity.Subscriptions;
import com.dpp.subscriptionupgrade.entity.User;
import com.dpp.subscriptionupgrade.entity.UserSubscriptions;
import com.dpp.subscriptionupgrade.exception.BadRequestException;
import com.dpp.subscriptionupgrade.model.request.PaymentRequestModel;
import com.dpp.subscriptionupgrade.model.request.UpgradeUserSubscriptionRequestModel;
import com.dpp.subscriptionupgrade.model.response.PaymentSuccessFailureResponse;
import com.dpp.subscriptionupgrade.repository.SubscriptionRepository;
import com.dpp.subscriptionupgrade.repository.UserRepository;
import com.dpp.subscriptionupgrade.repository.UserSubscriptionRepository;
import com.dpp.subscriptionupgrade.service.PaymentService;
import com.dpp.subscriptionupgrade.service.UpgradeUserSubscriptionService;
import com.dpp.subscriptionupgrade.utils.CreditCardValidator;
import com.dpp.subscriptionupgrade.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class UpgradeUserSubscriptionServiceImpl implements UpgradeUserSubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;


    @Autowired
    private PaymentService paymentService;


    @Override
    public PaymentSuccessFailureResponse upgradeUserSubscription(String token,
                                                                 UpgradeUserSubscriptionRequestModel requestModel) {
        Long userId = Util.getUserIdFromToken(token);
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new BadRequestException("Invalid User !!!!!.");
        }
        Subscriptions subscriptions = subscriptionRepository.getById(requestModel.getSubscriptionID());
        if (subscriptions == null) {
            throw new BadRequestException("Invalid Subscription Details !!!!!.");
        }

        if (!userId.equals(requestModel.getUserID())) {
            throw new BadRequestException("Subscription Upgrade for self user is allowed.");
        }
        CreditCardValidator.validateCreditCardDetails(requestModel);

        PaymentSuccessFailureResponse paymentSuccessFailureResponse = paymentService.processPayment(PaymentRequestModel.builder()
                .amount(requestModel.getAmount())
                .cardNumber(requestModel.getCardNumber())
                .cvv(requestModel.getCvv())
                .expiryDate(requestModel.getExpiryDate())
                .build());
        if ("success".equals(paymentSuccessFailureResponse.getStatus())) {
            UserSubscriptions userSubscriptions = UserSubscriptions.builder()
                    .subscriptionID(subscriptions.getID())
                    .userID(userId)
                    .transactionID(paymentSuccessFailureResponse.getTransactionId())
                    .createdTs(Timestamp.from(Instant.now()))
                    .expirationTs(getExpirationTimestamp(subscriptions.getSubscriptionDurationInDays()))
                    .build();
            userSubscriptionRepository.save(userSubscriptions);
        }

        return paymentSuccessFailureResponse;
    }

    private Timestamp getExpirationTimestamp(Long subscriptionDurationInDays) {
        Timestamp currentTs = new Timestamp(System.currentTimeMillis());
        LocalDateTime localDateTime = currentTs.toLocalDateTime();
        LocalDateTime updatedDateTime = localDateTime.plusDays(subscriptionDurationInDays);
        Timestamp updatedTimestamp = Timestamp.valueOf(updatedDateTime);
        return updatedTimestamp;
    }
}
