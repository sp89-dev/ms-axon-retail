package com.xyz.orderserviceaxon.coreapi.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductRemovedEvent {
    private final String productId;
    private final String orderId;
}
