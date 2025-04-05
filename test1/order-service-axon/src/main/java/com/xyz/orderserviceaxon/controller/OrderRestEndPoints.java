package com.xyz.orderserviceaxon.controller;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.RestController;

import com.xyz.orderserviceaxon.coreapi.commands.AddProductCommand;
import com.xyz.orderserviceaxon.coreapi.commands.ConfirmOrderCommand;
import com.xyz.orderserviceaxon.coreapi.commands.CreateOrderCommand;
import com.xyz.orderserviceaxon.coreapi.commands.DecrementProductCountCommand;
import com.xyz.orderserviceaxon.coreapi.commands.IncrementProductCountCommand;
import com.xyz.orderserviceaxon.coreapi.commands.RemoveOrderCommand;
import com.xyz.orderserviceaxon.coreapi.commands.ShipOrderCommand;
import com.xyz.orderserviceaxon.coreapi.queries.OrderResponse;
import com.xyz.orderserviceaxon.querymodel.OrderQueryService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class OrderRestEndPoints {

    private final CommandGateway commandGateway;
    private final OrderQueryService orderQueryService;

    public OrderRestEndPoints(OrderQueryService orderQueryService, CommandGateway commandGateway) {
        this.orderQueryService = orderQueryService;
        this.commandGateway = commandGateway;
    }

    @PostMapping("/orders")
    public CompletableFuture<String> postCreateOrder() {
        return postCreateOrder(UUID.randomUUID().toString());
    }

    @PostMapping("/order/{order-id}")
    public CompletableFuture<String> postCreateOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new CreateOrderCommand(orderId));
    }
    @DeleteMapping("/order/{order-id}")
    public CompletableFuture<String> deleteCreateOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new RemoveOrderCommand(orderId));
    }

    @PostMapping("/order/{order-id}/product/{product-id}")
    public CompletableFuture<Void> postAddProduct(@PathVariable("order-id") String orderId,
            @PathVariable("product-id") String productId) {
        return commandGateway.send(new AddProductCommand(orderId, productId));
    }

    @PostMapping("/order/{order-id}/product/{product-id}/increment")
    public CompletableFuture<Void> postIncrementProduct(@PathVariable("order-id") String orderId,
            @PathVariable("product-id") String productId) {
        return commandGateway.send(new IncrementProductCountCommand(orderId, productId));
    }

    @PostMapping("/order/{order-id}/product/{product-id}/decrement")
    public CompletableFuture<Void> postDecrementProduct(@PathVariable("order-id") String orderId,
            @PathVariable("product-id") String productId) {
        return commandGateway.send(new DecrementProductCountCommand(orderId, productId));
    }

    @PostMapping("/order/{order-id}/product/confirmed")
    public CompletableFuture<Void> postConfirmOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new ConfirmOrderCommand(orderId));
    }

    @PostMapping("/order/{order-id}/ship")
    public CompletableFuture<Void> postShipOrder(@PathVariable("order-id") String orderId) {
        return commandGateway.send(new ShipOrderCommand(orderId));
    }

    @PostMapping("/ship-order")
    public CompletableFuture<Void> postShipOrder() {
        String orderId = UUID.randomUUID().toString();

        return commandGateway.send(new CreateOrderCommand(orderId))
                .thenCompose(result -> commandGateway.send(new AddProductCommand(orderId, "product-1")))
                .thenCompose(result -> commandGateway.send(new ConfirmOrderCommand(orderId)))
                .thenCompose(result -> commandGateway.send(new ShipOrderCommand(orderId)));
    }

    @PostMapping("/ship-unconfirmed-order")
    public CompletableFuture<Void> postShipUnconfirmedOrder() {
        String orderId = UUID.randomUUID().toString();
        return commandGateway.send(new CreateOrderCommand(orderId))
                .thenCompose(result -> commandGateway.send(new AddProductCommand(orderId, "product-1")))
                // This throws an exception, as an Order cannot be shipped if it has not been
                // confirmed yet.
                .thenCompose(result -> commandGateway.send(new ShipOrderCommand(orderId)));
    }

    /**
     * Method to find all orders
     * 
     * @return CompletableFuture<List<OrderResponse>>
     */
    @GetMapping("/all-orders")
    public CompletableFuture<List<OrderResponse>> getFindAllOrders() {
        return orderQueryService.findAllOrders();
    }

    // @GetMapping(path = "all-orders-streaming", produces =
    // MediaType.TEXT_EVENT_STREAM_VALUE)
    // public Flux<> getAllOrdersStreaming() {
    // return new String();
    // }

    @GetMapping("/total-shipped/{product-id}")
    public Integer getTotalShipped(@PathVariable("product-id") String productId) {
        return orderQueryService.totalShipped(productId);
    }

    // @GetMapping(path="/order-updates/{order-id}", produces =
    // MediaType.TEXT_EVENT_STREAM_VALUE)
    // public Flux<OrderResponse> getOrderUpdates(@PathVariable("order-id") String
    // orderId) {
    // return orderQueryService.orderUpdates(orderId);
    // }

}