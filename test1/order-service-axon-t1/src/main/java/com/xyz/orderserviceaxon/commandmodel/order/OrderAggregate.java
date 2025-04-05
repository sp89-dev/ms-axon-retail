package com.xyz.orderserviceaxon.commandmodel.order;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xyz.orderserviceaxon.coreapi.commands.AddProductCommand;
import com.xyz.orderserviceaxon.coreapi.commands.ConfirmOrderCommand;
import com.xyz.orderserviceaxon.coreapi.commands.CreateOrderCommand;
import com.xyz.orderserviceaxon.coreapi.commands.RemoveOrderCommand;
import com.xyz.orderserviceaxon.coreapi.commands.RemoveProductCommand;
import com.xyz.orderserviceaxon.coreapi.commands.ShipOrderCommand;
import com.xyz.orderserviceaxon.coreapi.events.OrderConfirmedEvent;
import com.xyz.orderserviceaxon.coreapi.events.OrderCreatedEvent;
import com.xyz.orderserviceaxon.coreapi.events.OrderRemovedEvent;
import com.xyz.orderserviceaxon.coreapi.events.OrderShippedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductAddedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductRemovedEvent;
import com.xyz.orderserviceaxon.coreapi.exceptions.DuplicatedOrderLineException;
import com.xyz.orderserviceaxon.coreapi.exceptions.OrderAlreadyConfirmedException;
import com.xyz.orderserviceaxon.coreapi.exceptions.UnconfirmedOrderException;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.HashMap;
import java.util.Map;

@Aggregate
public class OrderAggregate {
    private static final Logger logger = LoggerFactory.getLogger(OrderAggregate.class);

    /**
     * The unique identifier for the order aggregate.
     * This field is annotated with @AggregateIdentifier, which is used by Axon
     * Framework
     * to identify and associate events with this aggregate instance.
     */
    @AggregateIdentifier
    private String orderId;
    private boolean orderConfirmed;

    protected OrderAggregate() {
        // Required by Axon framework to build a defult Aggregate prior to Event Sourcing
    }

    /**
     * A map of order lines associated with this order.
     * Each entry in the map represents an individual order line, identified by a
     * unique key.
     * The @AggregateMember annotation indicates that the OrderLine objects are part
     * of this aggregate.
     */
    // A map to store the order lines associated with this order.
    // Each order line is identified by a unique product ID.
    @AggregateMember
    private Map<String, OrderLine> orderLines;

    // Command handler for creating a new order.
    // This constructor is invoked when a CreateOrderCommand is dispatched.
    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        // Apply an OrderCreatedEvent to record the creation of the order.
        apply(new OrderCreatedEvent(command.getOrderId(), command.getCreatedAt()));
    }

    @CommandHandler
    public void handle(RemoveOrderCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("RemoveOrderCommand cannot be null");
        }
        logger.info("Handling RemoveOrderCommand for orderId: {}", command.getOrderId());
        // Apply an OrderCreatedEvent to record the creation of the order.
        apply(new OrderRemovedEvent(command.getOrderId()));
    }

    // Event sourcing handler for the OrderCreatedEvent.
    // This method updates the aggregate's state when the event is applied.
    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        // Set the order ID and mark the order as not confirmed.
        this.orderId = event.getOrderId();
        this.orderLines = new HashMap<>();
        this.orderConfirmed = false;
    }

    @EventSourcingHandler
    public void on(OrderRemovedEvent event) {
        logger.info("Handling OrderRemovedEvent for orderId: {}", event.getOrderId());

        // IncompatibleAggregateException: Aggregate identifier must be non-null after applying an event.
        //  Make sure the aggregate identifier is initialized at the latest when handling the creation event.
        // this.orderId = null; //clear the aggregate identifier // donot clear the aggregate identifier
        this.orderLines.clear(); //clear the order lines
        this.orderConfirmed = false; //reset the order confirmed status
    }

    // Command handler for confirming an order.
    // This method is invoked when a confirmOrder command is dispatched.
    @CommandHandler
    public void on(ConfirmOrderCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("ConfirmOrderCommand cannot be null");
        }
        if (orderConfirmed) {
            throw new IllegalStateException("Order is already confirmed");
        }
        logger.info("Handling ConfirmOrderCommand for orderId: {}", orderId);
        apply(new OrderConfirmedEvent(orderId));
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        this.orderConfirmed = true;
    }

    @CommandHandler
    public void handle(AddProductCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("AddProductCommand cannot be null");
        }
        if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException(orderId);
        }
        if (orderLines.containsKey(command.getProductId())) {
            throw new DuplicatedOrderLineException(command.getProductId());
        }
        logger.info("Handling AddProductCommand for orderId: {}, productId: {}", orderId, command.getProductId());
        apply(new ProductAddedEvent(orderId, command.getProductId()));
    }

    @EventSourcingHandler
    public void on(ProductAddedEvent event) {
        this.orderLines.put(event.getProductId(), new OrderLine(event.getProductId()));
    }

    /////////////////////////////////////////////////////////////////////////////
    @CommandHandler
    public void handle(ShipOrderCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("ShipOrderCommand cannot be null");
        }
        if (!orderConfirmed) {
            throw new UnconfirmedOrderException(orderId);
        }
        logger.info("Handling ShipOrderCommand for orderId: {}", orderId);
        apply(new OrderShippedEvent(orderId));
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent event) {
        // Do nothing
    }

    @CommandHandler
    public void handle(RemoveProductCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("RemoveProductCommand cannot be null");
        }
        if (orderConfirmed) {
            throw new OrderAlreadyConfirmedException("Cannot remove product from an order which has been confirmed.");
        }
        if (!orderLines.containsKey(command.getProductId())) {
            throw new IllegalStateException("Cannot remove product from an order which does not contain the product.");
        }
        logger.info("Handling RemoveProductCommand for orderId: {}, productId: {}", orderId, command.getProductId());
        apply(new ProductRemovedEvent(orderId, command.getProductId()));
    }

    @EventSourcingHandler
    public void on(ProductRemovedEvent event) {
        this.orderLines.remove(event.getProductId());
    }
}