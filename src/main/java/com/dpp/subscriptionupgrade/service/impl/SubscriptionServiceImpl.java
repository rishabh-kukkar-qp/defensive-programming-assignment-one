package com.dpp.subscriptionupgrade.service.impl;

import com.dpp.subscriptionupgrade.entity.Subscriptions;
import com.dpp.subscriptionupgrade.model.request.SubscriptionRequestModel;
import com.dpp.subscriptionupgrade.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public List<Subscriptions> findAll() {
        return null;
    }

    @Override
    public Subscriptions findById(Long id) {
        return null;
    }

    @Override
    public Subscriptions save(SubscriptionRequestModel requestModel) {
        return null;
    }

    @Override
    public Subscriptions update(Long id, SubscriptionRequestModel requestModel) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
