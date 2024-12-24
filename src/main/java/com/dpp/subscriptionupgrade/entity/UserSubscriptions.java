package com.dpp.subscriptionupgrade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserSubscriptions {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long subscriptionID;
    @Column
    private Long userID;
    @Column
    private Long transactionID;
    @Column
    private Timestamp createdTs;
    @Column
    private Timestamp expirationTs;

    //Mysql logic to add Subscription
    public void addUserSubscription() {

    }
}
