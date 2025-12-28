package com.tu.javabuildingstore.dto.orderItem;

import java.math.BigDecimal;

public record OrderItemResponseDTO(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal
) {
}

