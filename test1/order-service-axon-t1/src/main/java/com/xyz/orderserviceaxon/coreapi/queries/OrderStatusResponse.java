package com.xyz.orderserviceaxon.coreapi.queries;

public enum OrderStatusResponse {

    CREATED, CONFIRMED, SHIPPED, REJECTED, UNKNOWN;

    static OrderStatusResponse toResponse(OrderStatus orderStatus) {
        switch (orderStatus) {
            case CREATED:
                return CREATED;
            case CONFIRMED:
                return CONFIRMED;
            case SHIPPED:
                return SHIPPED;
            case REJECTED:
                return REJECTED;
            default:
                return UNKNOWN;
        }
    }
    
}
