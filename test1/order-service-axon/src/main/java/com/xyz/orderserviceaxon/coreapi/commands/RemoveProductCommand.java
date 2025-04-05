package com.xyz.orderserviceaxon.coreapi.commands;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class RemoveProductCommand {
    private final String orderId;
    private final String productId;
    
}
