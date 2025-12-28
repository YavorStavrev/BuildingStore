package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.cartItem.CartItemResponseDTO;
import com.tu.javabuildingstore.model.CartItem;

public interface CartItemMapper {

    default CartItemResponseDTO toDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        return new CartItemResponseDTO(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getUnitPrice().doubleValue(),
                cartItem.getQuantity(),
                cartItem.getSubtotal().doubleValue()
        );
    }
}
