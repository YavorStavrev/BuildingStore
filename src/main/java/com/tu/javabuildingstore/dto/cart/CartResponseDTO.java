package com.tu.javabuildingstore.dto.cart;

import com.tu.javabuildingstore.dto.cartItem.CartItemResponseDTO;

import java.util.List;

public record CartResponseDTO(
        Long id,
        Long userId,
        List<CartItemResponseDTO> items,
        Double totalPrice
) {
}