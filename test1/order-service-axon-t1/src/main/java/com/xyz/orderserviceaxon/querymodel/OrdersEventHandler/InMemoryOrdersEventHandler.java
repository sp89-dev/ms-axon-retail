package com.xyz.orderserviceaxon.querymodel.OrdersEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

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
import com.xyz.orderserviceaxon.coreapi.queries.OrderUpdatesQuery;
import com.xyz.orderserviceaxon.querymodel.OrderQueryService;

@Service
@ProcessingGroup("orders")
@Profile("!mongo")
public class InMemoryOrdersEventHandler implements OrderEventsHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderQueryService.class);

    private final Map<String, Order> orders = new HashMap<>();// <orderId, Order> => Order is { String orderId, Map<String, Integer> products (like [{"Mango",2}, {"apple", 3}]), OrderStatus orderStatus }
    private final QueryUpdateEmitter emitter;

    public InMemoryOrdersEventHandler(QueryUpdateEmitter emitter) {
        this.emitter = emitter;
        // super();
        // initializeOrders();
    }

    // private void initializeOrders() {
    // // Adding some initial orders for demonstration purposes
    // orders.put("order1", new Order("order1", "product1", 2, "CREATED"));
    // orders.put("order2", new Order("order2", "product2", 1, "CONFIRMED"));
    // orders.put("order3", new Order("order3", "product3", 5, "SHIPPED"));
    // }

    @Override
    @EventHandler
    public void on(OrderCreatedEvent event) {
        String orderId = event.getOrderId();
        orders.put(orderId, new Order(event.getOrderId()));
    }

    @Override
    @EventHandler
    public void on(OrderRemovedEvent event) {
        // orders.put(orderId, new Order(event.getOrderId()));
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.setOrderRejected();
            emitUpdate(order);
            return order;
        });
    }

    @Override
    @EventHandler
    public void on(ProductAddedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.addProduct(event.getProductId());
            emitUpdate(order);
            return order;
        });
    }

    @Override
    @EventHandler
    public void on(ProductRemovedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.removeProduct(event.getProductId());
            emitUpdate(order);
            return order;
        });
    }

    @Override
    @EventHandler
    public void on(ProductCountIncrementedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.incrementProductInstance(event.getProductId());
            emitUpdate(order);
            return order;
        }); 
    }

    @Override
    @EventHandler
    public void on(ProductCountDecrementedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, orders) -> {
            orders.decrementProductInstance(event.getProductId());
            emitUpdate(orders);
            return orders;
        });
    }

    @Override
    @EventHandler
    public void on(OrderConfirmedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.setOrderConfirmed();
            emitUpdate(order);
            return order;
        });
    }

    @Override
    @EventHandler
    public void on(OrderShippedEvent event) {
        orders.computeIfPresent(event.getOrderId(), (orderId, order) -> {
            order.setOrderShipped();
            emitUpdate(order);
            return order;
        });
    }

    @Override
    @QueryHandler
    public List<Order> handle(FindAllOrderedProductsQuery query) {

        logger.info("handle FindAllOrderedProductsQuery");
        // return List.copyOf(orders.values());
        logger.info("orders: {}", orders);
        // orders.clear();
        // logger.info("orders: {}", orders);

        List<Order> filderedOrders =  new ArrayList<>(orders.values());
        // List<Order> filderedOrders = orders.values().stream()
        //         .filter(order -> order.getProducts().isEmpty())
        //         .toList();
        logger.info("filderedOrders: {}", filderedOrders);
        return filderedOrders;
    }

    /**
     * Emits an update for a specific order to notify subscribers of changes.
     * This method uses the emitter to broadcast the updated order details
     * to all listeners subscribed to the {@link OrderUpdatesQuery} query
     * with a matching order ID.
     *
     * @param order The order object containing the updated details to be emitted.
     */
    private void emitUpdate(Order order) {
        emitter.emit(OrderUpdatesQuery.class, query -> order.getOrderId().equals(query.getOrderId()), order);
    }

}
