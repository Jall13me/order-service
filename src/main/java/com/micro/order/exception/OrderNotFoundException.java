package com.micro.order.exception;

import lombok.Getter;

@Getter
public class OrderNotFoundException extends RuntimeException {

    private final Long orderId;

    public OrderNotFoundException(Long orderId) {
        super(String.valueOf(orderId));
        this.orderId = orderId;
    }

}
