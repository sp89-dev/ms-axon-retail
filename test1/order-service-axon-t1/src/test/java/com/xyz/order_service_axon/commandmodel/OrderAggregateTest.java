package com.xyz.order_service_axon.commandmodel;

import com.xyz.orderserviceaxon.commandmodel.order.OrderAggregate;
import com.xyz.orderserviceaxon.coreapi.commands.*;
import com.xyz.orderserviceaxon.coreapi.events.*;
import com.xyz.orderserviceaxon.coreapi.exceptions.DuplicatedOrderLineException;
import com.xyz.orderserviceaxon.coreapi.exceptions.OrderAlreadyConfirmedException;
import com.xyz.orderserviceaxon.coreapi.exceptions.UnconfirmedOrderException;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class OrderAggregateTest {

    private static final String ORDER_ID = UUID.randomUUID().toString();
    private static final String PRODUCT_ID = UUID.randomUUID().toString();
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private AggregateTestFixture<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void givenNoPriorActivity_whenCreateOrderCommand_thenShouldPublishOrderCreatedEvent() {
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(ORDER_ID, CREATED_AT))
                .expectEvents(new OrderCreatedEvent(ORDER_ID, CREATED_AT));
    }

    @Test
    void givenOrderCreated_whenAddProductCommand_thenShouldPublishProductAddedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT))
                .when(new AddProductCommand(ORDER_ID, PRODUCT_ID))
                .expectEvents(new ProductAddedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void givenOrderCreated_whenConfirmOrderCommand_thenShouldPublishOrderConfirmedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT))
                .when(new ConfirmOrderCommand(ORDER_ID))
                .expectEvents(new OrderConfirmedEvent(ORDER_ID));
    }

    @Test
    void givenOrderConfirmed_whenShipOrderCommand_thenShouldPublishOrderShippedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT),
                      new OrderConfirmedEvent(ORDER_ID))
                .when(new ShipOrderCommand(ORDER_ID))
                .expectEvents(new OrderShippedEvent(ORDER_ID));
    }

    @Test
    void givenOrderCreated_whenAddDuplicateProductCommand_thenShouldThrowException() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT),
                      new ProductAddedEvent(ORDER_ID, PRODUCT_ID))
                .when(new AddProductCommand(ORDER_ID, PRODUCT_ID))
                .expectException(DuplicatedOrderLineException.class);
    }

    @Test
    void givenOrderConfirmed_whenAddProductCommand_thenShouldThrowException() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT),
                      new OrderConfirmedEvent(ORDER_ID))
                .when(new AddProductCommand(ORDER_ID, PRODUCT_ID))
                .expectException(OrderAlreadyConfirmedException.class);
    }

    @Test
    void givenOrderCreated_whenShipOrderCommand_thenShouldThrowException() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT))
                .when(new ShipOrderCommand(ORDER_ID))
                .expectException(UnconfirmedOrderException.class);
    }

    @Test
    void givenOrderCreated_whenRemoveProductCommand_thenShouldPublishProductRemovedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT),
                      new ProductAddedEvent(ORDER_ID, PRODUCT_ID))
                .when(new RemoveProductCommand(ORDER_ID, PRODUCT_ID))
                .expectEvents(new ProductRemovedEvent(ORDER_ID, PRODUCT_ID));
    }

    @Test
    void givenOrderConfirmed_whenRemoveProductCommand_thenShouldThrowException() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT),
                      new ProductAddedEvent(ORDER_ID, PRODUCT_ID),
                      new OrderConfirmedEvent(ORDER_ID))
                .when(new RemoveProductCommand(ORDER_ID, PRODUCT_ID))
                .expectException(OrderAlreadyConfirmedException.class);
    }

    @Test
    void givenNullCommand_whenAddProductCommand_thenShouldThrowException() {
        fixture.givenNoPriorActivity()
               .when(new AddProductCommand(null, null))
               .expectException(IllegalArgumentException.class);
    }

    @Test
    void givenOrderCreated_whenRemoveOrderCommand_thenShouldPublishOrderRemovedEvent() {
        fixture.given(new OrderCreatedEvent(ORDER_ID, CREATED_AT))
               .when(new RemoveOrderCommand(ORDER_ID))
               .expectEvents(new OrderRemovedEvent(ORDER_ID));
    }
}