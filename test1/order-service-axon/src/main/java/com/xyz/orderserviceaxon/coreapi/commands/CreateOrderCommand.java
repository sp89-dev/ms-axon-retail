package com.xyz.orderserviceaxon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Command to create a new order.
 * This command is used to initiate the creation of an order in the system.
 * 
 * An instance of this class contains the necessary information to identify
 * the order being created.
 * 
 * Annotations:
 * - {@code @AllArgsConstructor}: Generates a constructor with 1 parameter for
 * each field in the class.
 * - {@code @Getter}: Generates getter methods for all fields.
 * - {@code @EqualsAndHashCode}: Generates `equals` and `hashCode` methods.
 * - {@code @ToString}: Generates a `toString` method.
 * - {@code @TargetAggregateIdentifier}: Marks the `orderId` field as the
 * identifier for the aggregate.
 * 
 * Fields:
 * - {@code orderId}: The unique identifier for the order.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;

}
