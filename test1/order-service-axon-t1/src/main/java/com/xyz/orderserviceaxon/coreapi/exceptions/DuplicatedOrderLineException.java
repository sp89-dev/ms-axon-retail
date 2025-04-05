package com.xyz.orderserviceaxon.coreapi.exceptions;

public class DuplicatedOrderLineException extends IllegalStateException {
    public DuplicatedOrderLineException(String productId) {
        super("Cannot duplicate order line for product identifier [" + productId + "]");
    }
}
