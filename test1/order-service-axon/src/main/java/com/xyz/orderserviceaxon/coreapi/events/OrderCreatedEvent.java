package com.xyz.orderserviceaxon.coreapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCreatedEvent {

    private final String orderId;
}
