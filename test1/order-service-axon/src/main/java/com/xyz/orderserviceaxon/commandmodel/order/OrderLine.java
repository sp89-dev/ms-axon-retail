package com.xyz.orderserviceaxon.commandmodel.order;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.EntityId;

import com.xyz.orderserviceaxon.coreapi.commands.DecrementProductCountCommand;
import com.xyz.orderserviceaxon.coreapi.commands.IncrementProductCountCommand;
import com.xyz.orderserviceaxon.coreapi.events.ProductCountDecrementedEvent;
import com.xyz.orderserviceaxon.coreapi.events.ProductCountIncrementedEvent;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@EqualsAndHashCode
@ToString
public class OrderLine {

    @EntityId
    private String productId;
    private Integer count;
    private boolean orderConfirmed;

    public OrderLine(String productId) {
        this.productId = productId;
        this.count = 1;
    }

    @CommandHandler
    public void handle(IncrementProductCountCommand command) {
        if (orderConfirmed) {
            throw new IllegalStateException("Order is already confirmed");
        }
        apply(new ProductCountIncrementedEvent(command.getOrderId(), productId));
    }

    @EventSourcingHandler
    public void on(ProductCountIncrementedEvent event) {
        this.count++;
    }

    @CommandHandler
    public void handle(DecrementProductCountCommand command) {
        if (orderConfirmed) {
            throw new IllegalStateException("Order is already confirmed");
        }
        if (this.count <= 0) {
            throw new IllegalStateException("Product count cannot be less than zero");
        }
        apply(new ProductCountDecrementedEvent(command.getOrderId(), productId));
    }

    @EventSourcingHandler
    public void on(ProductCountDecrementedEvent event) {
        this.count--;
    }
}
