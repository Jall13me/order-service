package com.micro.order.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long clientId) {
        super("Client with id " + clientId + " not found");
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}
