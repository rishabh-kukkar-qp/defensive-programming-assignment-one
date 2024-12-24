package com.dpp.subscriptionupgrade.repository;

import com.dpp.subscriptionupgrade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
