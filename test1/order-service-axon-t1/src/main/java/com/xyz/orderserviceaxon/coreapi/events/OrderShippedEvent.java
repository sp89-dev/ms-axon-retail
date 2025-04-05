package com.xyz.orderserviceaxon.coreapi.events;

public class OrderShippedEvent {
    
    private final String orderId;

    public OrderShippedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
