package com.xyz.orderserviceaxon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Command to increment the count of a product in an order.
 * This command is typically used in a CQRS (Command Query Responsibility Segregation) 
 * and Event Sourcing architecture to signal the intention to update the product count.
 *
 * <p>Fields:
 * <ul>
 *   <li><b>productId</b>: The unique identifier of the product whose count is to be incremented.</li>
 *   <li><b>orderId</b>: The unique identifier of the order associated with the product.</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *   <li><b>@TargetAggregateIdentifier</b>: Indicates that the productId field is the identifier 
 *   of the aggregate that this command targets.</li>
 * </ul>
 *
 * <p>Overrides:
 * <ul>
 *   <li><b>equals</b>: Compares this command with another object for equality based on productId and orderId.</li>
 *   <li><b>hashCode</b>: Generates a hash code based on productId and orderId.</li>
 *   <li><b>toString</b>: Provides a string representation of the command for debugging purposes.</li>
 * </ul>
 *
 * <p>Constructor:
 * <ul>
 *   <li>Accepts productId and orderId as parameters to initialize the command.</li>
 * </ul>
 */
public class IncrementProductCountCommand {

    @TargetAggregateIdentifier
    private final String productId;
    private final String orderId;

    public IncrementProductCountCommand(String productId, String orderId) {
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IncrementProductCountCommand that = (IncrementProductCountCommand) o;

        if (!productId.equals(that.productId))
            return false;
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
        return "IncrementProductCountCommand{" +
                "productId='" + productId + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }
}
