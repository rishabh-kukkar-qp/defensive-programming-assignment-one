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
import java.util.Optional;

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
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new BadRequestException("Invalid User !!!!!.");
        }
        User user = optionalUser.get();
        Optional<Subscriptions> optionalSubscriptions = subscriptionRepository.findById(requestModel.getSubscriptionID());
        if (optionalSubscriptions.isEmpty()) {
            throw new BadRequestException("Invalid Subscription Details !!!!!.");
        }
        Subscriptions subscriptions = optionalSubscriptions.get();
        if (!userId.equals(requestModel.getUserID())) {
            throw new BadRequestException("Subscription Upgrade for self user is allowed.");
        }

        CreditCardValidator.validateCreditCardDetails(requestModel);

        PaymentSuccessFailureResponse paymentSuccessFailureResponse = paymentService.processPayment(
                PaymentRequestModel.builder()
                        .amount(requestModel.getAmount())
                        .cardNumber(requestModel.getCardNumber())
                        .cvv(requestModel.getCvv())
                        .expiryDate(requestModel.getExpiryDate())
                        .build());
        if ("success".equals(paymentSuccessFailureResponse.getStatus())) {
            if (user.getActiveSubscription() > 0) {
                Optional<UserSubscriptions> optionalLastActiveSubscription
                        = userSubscriptionRepository.findById(optionalUser.get().getActiveSubscription());
                if (optionalLastActiveSubscription.isEmpty()) {
                    createAndUpgradeUserSubscriptionData(user, subscriptions, paymentSuccessFailureResponse);
                } else {
                    UserSubscriptions userSubscriptions = optionalLastActiveSubscription.get();
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

                    if (userSubscriptions.getExpirationTs().after(currentTimestamp)) {
                        long remainingMillis = userSubscriptions.getExpirationTs().getTime() - currentTimestamp.getTime();
                        long remainingDays = remainingMillis / (1000 * 60 * 60 * 24); // 5
                        Long allDays = remainingDays + subscriptions.getSubscriptionDurationInDays();

                        Timestamp extendedExpiration = getExpirationTimestamp(allDays);
                        UserSubscriptions newSubscription = UserSubscriptions.builder()
                                .createdTs(currentTimestamp)
                                .userID(userId)
                                .subscriptionID(subscriptions.getID())
                                .transactionID(paymentSuccessFailureResponse.getTransactionId())
                                .expirationTs(extendedExpiration)
                                .build();
                        userSubscriptionRepository.save(newSubscription);
                    } else {
                        createAndUpgradeUserSubscriptionData(user, subscriptions, paymentSuccessFailureResponse);
                    }
                }
            } else {
                createAndUpgradeUserSubscriptionData(user, subscriptions, paymentSuccessFailureResponse);
            }
        }
        return paymentSuccessFailureResponse;
    }

    private void createAndUpgradeUserSubscriptionData(User user, Subscriptions subscriptions, PaymentSuccessFailureResponse paymentSuccessFailureResponse) {
        UserSubscriptions userSubscriptions = UserSubscriptions.builder()
                .subscriptionID(subscriptions.getID())
                .userID(user.getID())
                .transactionID(paymentSuccessFailureResponse.getTransactionId())
                .createdTs(Timestamp.from(Instant.now()))
                .expirationTs(getExpirationTimestamp(subscriptions.getSubscriptionDurationInDays()))
                .build();
        userSubscriptionRepository.save(userSubscriptions);

        user.setActiveSubscription(userSubscriptions.getID());
        userRepository.save(user);
    }

    private Timestamp getExpirationTimestamp(Long subscriptionDurationInDays) {
        Timestamp currentTs = new Timestamp(System.currentTimeMillis());
        LocalDateTime localDateTime = currentTs.toLocalDateTime();
        LocalDateTime updatedDateTime = localDateTime.plusDays(subscriptionDurationInDays);
        Timestamp updatedTimestamp = Timestamp.valueOf(updatedDateTime);
        return updatedTimestamp;
    }
}
