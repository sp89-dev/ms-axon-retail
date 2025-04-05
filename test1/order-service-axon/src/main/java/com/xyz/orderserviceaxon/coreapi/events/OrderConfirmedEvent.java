package com.xyz.orderserviceaxon.coreapi.events;

public class OrderConfirmedEvent {
    private final String orderId;

    public OrderConfirmedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}