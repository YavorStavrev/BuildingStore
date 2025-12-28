package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.cart.CartResponseDTO;
import com.tu.javabuildingstore.dto.cartItem.CartItemResponseDTO;
import com.tu.javabuildingstore.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper extends CartItemMapper {

    @Mapping(source = "user.id", target = "userId")
    default CartResponseDTO toDTO(Cart cart) {
        if (cart == null) return null;

        List<CartItemResponseDTO> items = cart.getCartItems()
                .stream()
                .map(this::toDTO)  // uses CartItemMapper
                .toList();

        return new CartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                items,
                cart.getTotalPrice().doubleValue()
        );
    }
}
