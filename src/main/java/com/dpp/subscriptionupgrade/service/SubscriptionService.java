package com.dpp.subscriptionupgrade.service;

import com.dpp.subscriptionupgrade.entity.Subscriptions;
import com.dpp.subscriptionupgrade.model.request.SubscriptionRequestModel;

import java.util.List;

public interface SubscriptionService {
    List<Subscriptions> findAll();

    Subscriptions findById(Long id);

    Subscriptions save(SubscriptionRequestModel requestModel);

    Subscriptions update(Long id, SubscriptionRequestModel requestModel);

    void deleteById(Long id);

}
