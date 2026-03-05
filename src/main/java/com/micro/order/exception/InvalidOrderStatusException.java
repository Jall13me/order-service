package com.micro.order.exception;
import com.micro.order.model.OrderStatus;
import lombok.Getter;

@Getter
public class InvalidOrderStatusException extends RuntimeException {
    private final OrderStatus currentStatus;
    private final OrderStatus newStatus;

    public  InvalidOrderStatusException(OrderStatus currentStatus, OrderStatus newStatus) {
        super(currentStatus + "->" + newStatus);
        this.currentStatus = currentStatus;
        this.newStatus = newStatus;
    }

    public InvalidOrderStatusException(String message) {
        super(message);
        this.currentStatus = null;
        this.newStatus = null;
    }

}
