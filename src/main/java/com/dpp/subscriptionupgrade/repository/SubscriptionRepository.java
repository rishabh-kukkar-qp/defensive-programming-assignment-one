package com.dpp.subscriptionupgrade.repository;

import com.dpp.subscriptionupgrade.entity.Subscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscriptions, Long> {
}
