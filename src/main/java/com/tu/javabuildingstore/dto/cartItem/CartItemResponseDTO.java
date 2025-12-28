package com.tu.javabuildingstore.dto.cartItem;

public record CartItemResponseDTO(
        Long id,
        Long productId,
        String productName,
        Double unitPrice,
        Integer quantity,
        Double subtotal
) {}
