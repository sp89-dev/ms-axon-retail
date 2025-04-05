package com.xyz.orderserviceaxon.coreapi.exceptions;

public class UnconfirmedOrderException extends IllegalStateException {
    
    public UnconfirmedOrderException(String orderId) {
        super("Cannot ship an order ["+orderId+"] which has not been confirmed yet.");
    }
    
}
