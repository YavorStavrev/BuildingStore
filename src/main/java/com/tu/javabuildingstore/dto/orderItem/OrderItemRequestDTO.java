package com.tu.javabuildingstore.dto.orderItem;

public record OrderItemRequestDTO(
        Long productId,
        Integer quantity
) {
}
