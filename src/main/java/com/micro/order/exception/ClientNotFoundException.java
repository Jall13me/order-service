package com.micro.order.exception;

import lombok.Getter;

@Getter
public class ClientNotFoundException extends RuntimeException {

    private final Long clientId;

    public ClientNotFoundException(Long clientId) {
        super(String.valueOf(clientId));
        this.clientId = clientId;
    }

}