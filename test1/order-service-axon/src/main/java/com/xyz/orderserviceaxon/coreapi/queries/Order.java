package com.xyz.orderserviceaxon.coreapi.queries;

import java.util.HashMap;
import java.util.Map;

public class Order {
    
    private final String orderId;// no stter method
    private final Map<String, Integer> products;// no setter method
    private OrderStatus orderStatus;

    
    /**
     * Constructs a new Order with the specified order ID.
     * Initializes the products map and sets the order status to CREATED.
     *
     * @param orderId the unique identifier for the order
     */
    public Order(String orderId) {
        this.orderId = orderId;
        this.products = new HashMap<>();
        this.orderStatus = OrderStatus.CREATED;
    }
    
    public String getOrderId() {
        return orderId;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderConfirmed() {
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void setOrderShipped() {
        this.orderStatus = OrderStatus.SHIPPED;
    }

    public void setOrderRejected() {
        this.orderStatus = OrderStatus.REJECTED;
    }

    /**
     * Adds a product to the order with the specified quantity. If the product already exists,
     * the quantity is incremented by the specified amount.
     *
     * @param productId the unique identifier of the product to be added
     * @param quantity the quantity of the product to be added
     */
    public void addProduct(String productId) {
        // products.put(productId, products.getOrDefault(productId, 0) + quantity);
        products.putIfAbsent(productId, 1);
    }

    public void removeProduct(String productId) {
        products.remove(productId);
    }

    public void incrementProductInstance(String productId) {
        // products.put(productId, products.getOrDefault(productId, 0) + quantity);
        products.computeIfPresent(productId, (id, count) -> count + 1);
    }
    public void decrementProductInstance(String productId) {
        // products.put(productId, products.getOrDefault(productId, 0) + quantity);
        products.computeIfPresent(productId, (id, count) -> count - 1);
    }

    /**
     * Decrements the quantity of a specified product in the order.
     * If the resulting quantity is greater than zero, it updates the quantity.
     * If the resulting quantity is zero or less, it removes the product from the order.
     *
     * @param productId the ID of the product to decrement
     * @param quantity the amount to decrement from the current quantity
     */
    public void decrementProductQuantity(String productId, int quantity) {
        if (products.containsKey(productId)) {
            int currentQuantity = products.get(productId);
            int newQuantity = currentQuantity - quantity;
            if (newQuantity > 0) {
                products.put(productId, newQuantity);
            } else {
                products.remove(productId);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!orderId.equals(order.orderId)) return false;
        if (!products.equals(order.products)) return false;
        return orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + products.hashCode();
        result = 31 * result + orderStatus.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", products=" + products +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
