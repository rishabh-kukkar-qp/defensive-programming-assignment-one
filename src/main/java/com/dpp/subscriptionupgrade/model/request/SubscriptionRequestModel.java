package com.dpp.subscriptionupgrade.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequestModel {
    private Long ID;
    private Long subscriptionDurationInDays;
    private Long createdBy;
    private Long updatedBy;
    private String name;
    private Double amount;
    private Timestamp subscriptionValidTill;
}
