package com.micro.order.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "Client ID is required")
    @Positive(message = "Client ID must be positive")
    private Long clientId;

    @NotBlank(message = "Product name is required")
    @Size(max = 200,message = "Product name cannot exceed 200 characters")
    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1,message = "Quantity must be at least 1")
    @Max(value = 1000,message = "Quantity cannot exceed 1000")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01",message = "Price must be greater than 0")
    @DecimalMax(value ="999999.99",message = "Price cannot exceed 999999.99")
    private BigDecimal price;
}
