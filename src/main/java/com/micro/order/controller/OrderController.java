package com.micro.order.controller;

import com.micro.order.dto.OrderRequest;
import com.micro.order.dto.OrderResponse;
import com.micro.order.model.OrderStatus;
import com.micro.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {

        log.info("POST /orders - Creating order for client: {}", request.getClientId());

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "false")boolean includeDisable) {
        log.info("GET /orders - Getting all orders,including disable: {}", includeDisable);

        List<OrderResponse> orders = includeDisable ? orderService.getAllOrdersIncludingDisabled() : orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        log.info("GET /orders/{} - Getting order by ID", id);

        OrderResponse order = orderService.getOrderById(id);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByClientId(
            @PathVariable Long clientId) {

        log.info("GET /orders/client/{} - Getting orders for client", clientId);

        List<OrderResponse> orders = orderService.getOrdersByClientId(clientId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(
            @PathVariable OrderStatus status) {

        log.info("GET /orders/status/{} - Getting orders by status", status);

        List<OrderResponse> orders = orderService.getOrdersByStatus(status);

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest request) {

        log.info("PUT /orders/{} - Updating order", id);

        OrderResponse response = orderService.updateOrder(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> partialUpdateOrder(
            @PathVariable Long id,
            @RequestBody OrderRequest request) {

        log.info("PATCH /orders/{} - Partial update", id);

        OrderResponse response = orderService.partialUpdateOrder(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus newStatus) {

        log.info("PATCH /orders/{}/status - Updating status to: {}", id, newStatus);

        OrderResponse response = orderService.updateOrderStatus(id, newStatus);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        log.info("PATCH /orders/{}/cancel - Cancelling order", id);

        OrderResponse response = orderService.cancelOrder(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("DELETE /orders/{} - Disabling order (soft delete)", id);

        orderService.disableOrder(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/enable")
    public ResponseEntity<OrderResponse> enableOrder(@PathVariable Long id) {
        log.info("POST /orders/{} - Enabling order", id);

        OrderResponse response = orderService.enableOrder(id);
        return ResponseEntity.ok(response);
    }

}