package com.xyz.orderserviceaxon.querymodel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import com.xyz.orderserviceaxon.coreapi.queries.FindAllOrderedProductsQuery;
import com.xyz.orderserviceaxon.coreapi.queries.Order;
import com.xyz.orderserviceaxon.coreapi.queries.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderQueryService {

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryService.class);
    private final QueryGateway queryGateway;

    // constructor to initialize the queryGateway
    public OrderQueryService(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    /**
     * Method to find all orders
     * 
     * @return CompletableFuture<List<OrderResponse>>
     * @throws Exception if any error occurs
     */
    public CompletableFuture<List<OrderResponse>> findAllOrders() {
        // return all orders using queryGateway
        logger.info("======>>>>>Executing findAllOrders query");
        return queryGateway.query(new FindAllOrderedProductsQuery(), ResponseTypes.multipleInstancesOf(Order.class))
                // .thenApply(result -> result.stream()
                //         .map(OrderResponse::new)
                //         .collect(java.util.stream.Collectors.toList()))
                // .exceptionally(e -> {
                //     throw new RuntimeException("Error occurred while fetching all orders", e);
                // });

                .thenApply(result -> {
                    logger.info("Stream processing {} orders", result.size());
                    return result.stream()
                        .map(OrderResponse::new)
                        .collect(java.util.stream.Collectors.toList());
                })
                .exceptionally(e -> {
                    throw new RuntimeException("Error occurred while fetching all orders", e);
                });
    }

    public Integer totalShipped(String productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'totalShipped'");
    }

    // public Flux<OrderResponse> orderUpdates(String orderId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'orderUpdates'");
    // }

}
