package com.dpp.subscriptionupgrade.model.request;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestModel {

    private Long ID;

    private String userName;

    private Timestamp createdTs;

    private Timestamp expirationTs;
}
