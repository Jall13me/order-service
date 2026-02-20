package com.micro.order.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clientId",nullable = false)
    private String clientId;

    @Column(name = "productName",nullable = false,length = 200)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false,precision=10,scale=2)
    private BigDecimal price;

    @Column(name = "totalAmount",nullable = false,precision = 10,scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private OrderStatus status;

    @Column(name = "createdAt",updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private boolean active = true;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.active = true;

        if (this.totalAmount == null && this.price != null && this.quantity != null){
            this.totalAmount = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }

        if(this.status == null){
            this.status = OrderStatus.PENDING;
        }
    }
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();

        if (this.price != null && this.quantity != null){
            this.totalAmount = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
    }
}
