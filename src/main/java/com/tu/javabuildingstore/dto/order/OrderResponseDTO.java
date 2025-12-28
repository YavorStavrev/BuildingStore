package com.tu.javabuildingstore.dto.order;

import com.tu.javabuildingstore.dto.orderItem.OrderItemResponseDTO;
import com.tu.javabuildingstore.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        Long userId,
        List<OrderItemResponseDTO> items,
        BigDecimal totalPrice,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
