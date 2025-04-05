package com.xyz.orderserviceaxon.coreapi.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class OrderCreatedEvent {

    private final String orderId;
    private final LocalDateTime createdAt; // Timestamp for order creation

    // public OrderCreatedEvent(String orderId, LocalDateTime createdAt) {
    //     this.orderId = orderId;
    //     // this.createdAt = LocalDateTime.now(); // Initialize createdAt with the current timestamp
    //     this.createdAt = createdAt; // Initialize createdAt with the current timestamp
    // }
}

