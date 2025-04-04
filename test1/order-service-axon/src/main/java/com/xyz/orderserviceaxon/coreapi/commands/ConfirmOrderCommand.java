package com.xyz.orderserviceaxon.coreapi.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ConfirmOrderCommand {
    
    @TargetAggregateIdentifier
    private final String orderId;
}
