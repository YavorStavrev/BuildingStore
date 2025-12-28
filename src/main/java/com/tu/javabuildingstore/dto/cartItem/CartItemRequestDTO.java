package com.tu.javabuildingstore.dto.cartItem;

public record CartItemRequestDTO(
        Long productId,
        Integer quantity
) {}