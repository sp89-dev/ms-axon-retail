package com.xyz.order_service_axon.commandmodel;

import com.xyz.orderserviceaxon.commandmodel.order.OrderAggregate;
import com.xyz.orderserviceaxon.coreapi.commands.CreateOrderCommand;
import com.xyz.orderserviceaxon.coreapi.events.OrderCreatedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderAggregateTest_one {

    private static final String ORDER_ID = UUID.randomUUID()
            .toString();

    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private FixtureConfiguration<OrderAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(OrderAggregate.class);
    }

    @Test
    void givenNoPriorActivity_whenCreateOrderCommand_thenShouldPublishOrderCreatedEvent() {
        // Given no prior activity, when a CreateOrderCommand is sent, then expect an OrderCreatedEvent
        fixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(ORDER_ID, CREATED_AT))
                .expectEvents(new OrderCreatedEvent(ORDER_ID, CREATED_AT));
    }


}