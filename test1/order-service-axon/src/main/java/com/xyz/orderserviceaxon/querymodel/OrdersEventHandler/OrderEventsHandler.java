/**
 * Interface for handling various order-related events and queries in the system.
 * This interface defines methods to handle events such as order creation, confirmation,
 * shipping, and product-related changes, as well as a method to handle queries for
 * retrieving all ordered products.
 */
package com.xyz.orderserviceaxon.querymodel.OrdersEventHandler;

import java.util.List;

import com.xyz.orderserviceaxon.coreapi.events.OrderConfirmedEvent;
import com.xyz.orderserviceaxon.coreapi.events.OrderCreatedEvent;
import com.xyz.orderserviceaxon.coreapi.events.OrderRemovedEvent;
import com.xyz.orderserviceaxon.coreapi.events.OrderShippedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductAddedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductCountDecrementedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductCountIncrementedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductRemovedEvent;
import com.xyz.orderserviceaxon.coreapi.queries.FindAllOrderedProductsQuery;
import com.xyz.orderserviceaxon.coreapi.queries.Order;

public interface OrderEventsHandler {
    void on(OrderCreatedEvent event);
    void on(OrderRemovedEvent event);

    void on(OrderConfirmedEvent event);

    void on(OrderShippedEvent event);

    void on(ProductAddedEvent event);

    void on(ProductCountIncrementedEvent event);

    void on(ProductCountDecrementedEvent event);

    void on(ProductRemovedEvent event);

    List<Order> handle(FindAllOrderedProductsQuery query);

}