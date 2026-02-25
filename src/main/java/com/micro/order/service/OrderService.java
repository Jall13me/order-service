package com.micro.order.service;

import com.micro.order.dto.OrderRequest;
import com.micro.order.dto.OrderResponse;
import com.micro.order.exception.InvalidOrderStatusException;
import com.micro.order.exception.OrderNotFoundException;
import com.micro.order.mapper.OrderMapper;
import com.micro.order.model.Order;
import com.micro.order.model.OrderStatus;
import com.micro.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating order for client ID: {}", request.getClientId());

        Order order = orderMapper.toEntity(request);
        Order savedOrder = orderRepository.save(order);

        log.info("Order created with ID: {} - Total: {}",
                savedOrder.getId(), savedOrder.getTotalAmount());

        return orderMapper.toResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        log.info("Getting all active orders");

        List<Order> orders = orderRepository.findAllByActiveTrue();
        return orderMapper.toResponseList(orders);
    }

    public List<OrderResponse> getAllOrdersIncludingDisabled() {
        log.info("Getting all orders including disabled");

        List<Order> orders = orderRepository.findAll();
        return orderMapper.toResponseList(orders);
    }

    public OrderResponse getOrderById(Long id) {
        log.info("Getting order with ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getOrdersByClientId(Long clientId) {
        log.info("Getting orders for client ID: {}", clientId);

        List<Order> orders = orderRepository.findByClientId(clientId);
        return orderMapper.toResponseList(orders);
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        log.info("Getting orders with status: {}", status);

        List<Order> orders = orderRepository.findByStatus(status);
        return orderMapper.toResponseList(orders);
    }
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        log.info("Updating order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        if (order.isDisabled()){
            throw new InvalidOrderStatusException("Cannot update order status when disable");
        }

        OrderStatus currentStatus = order.getStatus();
        Long currentClientId = order.getClientId();

        orderMapper.updateEntityFromRequest(request, order);

        order.setStatus(currentStatus);
        order.setClientId(currentClientId);

        Order updatedOrder = orderRepository.save(order);

        log.info("Order updated ID: {}", id);

        return orderMapper.toResponse(updatedOrder);
    }
    @Transactional
    public OrderResponse partialUpdateOrder(Long id, OrderRequest request) {
        log.info("Partially updating order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        OrderStatus currentStatus = order.getStatus();
        Long currentClientId = order.getClientId();

        orderMapper.updateEntityFromRequest(request, order);

        order.setStatus(currentStatus);
        order.setClientId(currentClientId);

        Order updatedOrder = orderRepository.save(order);

        log.info("Order partially updated ID: {}", id);

        return orderMapper.toResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        log.info("Updating order {} status to: {}", id, newStatus);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.isDisabled()){
            throw new InvalidOrderStatusException("Cannot update order status when disable");
        }

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);

        log.info("Order {} status updated to: {}", id, newStatus);

        return orderMapper.toResponse(updatedOrder);
    }

    @Transactional
    public void disableOrder(Long id) {
        log.info("Disabling order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.isDisabled()){
            throw new InvalidOrderStatusException("Order is already disabled");
        }

        order.disable();
        orderRepository.save(order);

        log.info("Order deleted ID: {}", id);

    }

    public OrderResponse enableOrder(Long id) {
        log.info("Enabling order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.isActive()){
            throw new InvalidOrderStatusException("Order is already active");
        }

        order.enable();
        Order enabledOrder = orderRepository.save(order);
        log.info("Order enabled ID: {}", id);
        return orderMapper.toResponse(enabledOrder);
    }

    public OrderResponse cancelOrder(Long id) {
        log.info("Cancelling order ID: {}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException(
                    "Cannot cancel order that is already " + order.getStatus());
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        log.info("Order cancelled ID: {}", id);

        return orderMapper.toResponse(cancelledOrder);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus newStatus) {
        if (current == newStatus) {
            return;
        }

        switch (current) {
            case PENDING:
                if (newStatus != OrderStatus.PROCESSING &&
                        newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusException(
                            "Cannot change status from PENDING to " + newStatus);
                }
                break;
            case PROCESSING:
                if (newStatus != OrderStatus.SHIPPED &&
                        newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusException(
                            "Cannot change status from PROCESSING to " + newStatus);
                }
                break;
            case SHIPPED:
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new InvalidOrderStatusException(
                            "Cannot change status from SHIPPED to " + newStatus);
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new InvalidOrderStatusException(
                        "Cannot change status from " + current + " to " + newStatus);
        }
    }
}