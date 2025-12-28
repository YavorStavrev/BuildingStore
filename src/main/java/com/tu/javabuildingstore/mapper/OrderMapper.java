package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.order.OrderResponseDTO;
import com.tu.javabuildingstore.dto.orderItem.OrderItemResponseDTO;
import com.tu.javabuildingstore.model.Order;
import com.tu.javabuildingstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "items")
    OrderResponseDTO toResponse(Order order);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDTO toItemResponse(OrderItem orderItem);

    List<OrderItemResponseDTO> toItemResponseList(List<OrderItem> items);
}
