package com.micro.order.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(long orderId) {
        super("Order with id " + orderId + " not found");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
