package com.dpp.subscriptionupgrade.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Subscriptions {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column
    private Long subscriptionDurationInDays;

    @Column
    private Long createdBy;

    @Column
    private Long updatedBy;

    @Column
    private String name;

    @Column
    private Double amount;

    @Column
    private Timestamp createdTs;

    @Column
    private Timestamp updatedTs;

    @Column
    private Timestamp subscriptionValidTill;

}
