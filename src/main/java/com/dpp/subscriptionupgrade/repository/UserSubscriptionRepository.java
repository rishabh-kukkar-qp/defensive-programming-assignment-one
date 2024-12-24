package com.dpp.subscriptionupgrade.repository;

import com.dpp.subscriptionupgrade.entity.UserSubscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscriptions, Long> {
}
