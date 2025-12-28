package com.tu.javabuildingstore.dto.order;

import com.tu.javabuildingstore.dto.orderItem.OrderItemRequestDTO;

import java.util.List;

public record OrderRequestDTO(
        List<OrderItemRequestDTO> items
) {
}
