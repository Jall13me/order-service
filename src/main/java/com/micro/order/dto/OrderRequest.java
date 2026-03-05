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

    @NotNull(message = "{validation.clientId.required}")
    @Positive(message = "{validation.clientId.positive}")
    private Long clientId;

    @NotBlank(message = "{validation.productName.required}")
    @Size(max = 200,message = "{validation.productName.size}")
    private String productName;

    @NotNull(message = "{validation.quantity.required}")
    @Min(value = 1,message = "{validation.quantity.min}")
    @Max(value = 1000,message = "{validation.quantity.max}")
    private Integer quantity;

    @NotNull(message = "{validation.price.required}")
    @DecimalMin(value = "0.01",message = "{validation.price.min}")
    @DecimalMax(value ="999999.99",message = "{validation.price.max}")
    private BigDecimal price;
}
