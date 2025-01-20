package com.dpp.subscriptionupgrade.model.request;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSubscriptionRequestModel {
    private Long ID;

    private Long subscriptionID;

    private Long userID;

    private String transactionID;

    private Timestamp createdTs;

    private Timestamp expirationTs;
}
