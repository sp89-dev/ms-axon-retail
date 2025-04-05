package com.xyz.orderserviceaxon.coreapi.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class OrderRemovedEvent {

    private final String orderId;
}
