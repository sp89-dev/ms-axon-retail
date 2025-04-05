package com.xyz.orderserviceaxon.coreapi.queries;

import java.util.Map;
import static com.xyz.orderserviceaxon.coreapi.queries.OrderStatusResponse.toResponse;

public class OrderResponse {
    
    private String orderId;
    private Map<String, Integer> products;
    private OrderStatusResponse orderStatus;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.products = order.getProducts();
        this.orderStatus = toResponse(order.getOrderStatus());
    }

    public OrderResponse() {
    }

    public String getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public OrderStatusResponse getOrderStatus() {
        return orderStatus;
    }
}
