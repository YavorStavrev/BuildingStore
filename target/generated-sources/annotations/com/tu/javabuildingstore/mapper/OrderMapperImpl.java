package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.order.OrderResponseDTO;
import com.tu.javabuildingstore.dto.orderItem.OrderItemResponseDTO;
import com.tu.javabuildingstore.model.Order;
import com.tu.javabuildingstore.model.OrderItem;
import com.tu.javabuildingstore.model.Product;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.model.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-28T14:32:02+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponseDTO toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        Long userId = null;
        List<OrderItemResponseDTO> items = null;
        Long id = null;
        BigDecimal totalPrice = null;
        OrderStatus status = null;
        LocalDateTime createdAt = null;

        userId = orderUserId( order );
        items = toItemResponseList( order.getOrderItems() );
        id = order.getId();
        totalPrice = order.getTotalPrice();
        status = order.getStatus();
        createdAt = order.getCreatedAt();

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO( id, userId, items, totalPrice, status, createdAt );

        return orderResponseDTO;
    }

    @Override
    public OrderItemResponseDTO toItemResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        BigDecimal unitPrice = null;
        Integer quantity = null;
        BigDecimal subtotal = null;

        productId = orderItemProductId( orderItem );
        productName = orderItemProductName( orderItem );
        unitPrice = orderItem.getUnitPrice();
        quantity = orderItem.getQuantity();
        subtotal = orderItem.getSubtotal();

        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO( productId, productName, unitPrice, quantity, subtotal );

        return orderItemResponseDTO;
    }

    @Override
    public List<OrderItemResponseDTO> toItemResponseList(List<OrderItem> items) {
        if ( items == null ) {
            return null;
        }

        List<OrderItemResponseDTO> list = new ArrayList<OrderItemResponseDTO>( items.size() );
        for ( OrderItem orderItem : items ) {
            list.add( toItemResponse( orderItem ) );
        }

        return list;
    }

    private Long orderUserId(Order order) {
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long orderItemProductId(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String orderItemProductName(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
