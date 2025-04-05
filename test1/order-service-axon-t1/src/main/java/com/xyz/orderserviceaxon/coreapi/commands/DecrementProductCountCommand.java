package com.xyz.orderserviceaxon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class DecrementProductCountCommand {
    
    @TargetAggregateIdentifier
    private final String productId;
    private final String orderId;

    public DecrementProductCountCommand(String productId, String orderId) {
        this.productId = productId;
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public String getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DecrementProductCountCommand that = (DecrementProductCountCommand) o;

        if (!productId.equals(that.productId)) return false;
        return orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + orderId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DecrementProductCountCommand{" +
                "productId='" + productId + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
