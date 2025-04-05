package com.xyz.orderserviceaxon.coreapi.queries;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class OrderUpdatesQuery {
    private final String orderId;
}
