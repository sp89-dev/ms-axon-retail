package com.xyz.orderserviceaxon.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orders/test")
public class OrderControllerTest {

    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;

    public OrderControllerTest(QueryGateway queryGateway, CommandGateway commandGateway) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable String id) {
        // Implement logic to get order by id
        return "Order details for id: " + id;
    }

    @PostMapping
    public String createOrder(@RequestBody String order) {
        // Implement logic to create a new order
        return "Order created: " + order;
    }
}