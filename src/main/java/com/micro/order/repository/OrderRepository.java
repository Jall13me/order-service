package com.micro.order.repository;
import com.micro.order.model.Order;
import com.micro.order.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClient(Long clientId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByClientAndStatus(Long clientId, OrderStatus status);
    List<Order> findByActiveTrue();

    Long countByClientId(Long clientId);
}
