package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.dto.order.OrderRequestDTO;
import com.tu.javabuildingstore.dto.order.OrderResponseDTO;
import com.tu.javabuildingstore.dto.orderItem.OrderItemRequestDTO;
import com.tu.javabuildingstore.exception.ResourceNotFoundException;
import com.tu.javabuildingstore.mapper.OrderMapper;
import com.tu.javabuildingstore.model.Order;
import com.tu.javabuildingstore.model.OrderItem;
import com.tu.javabuildingstore.model.Product;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.model.enums.OrderStatus;
import com.tu.javabuildingstore.repository.OrderRepository;
import com.tu.javabuildingstore.repository.ProductRepository;
import com.tu.javabuildingstore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    // Create order from request
    public OrderResponseDTO createOrder(Long userId, OrderRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, userId));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .build();

        List<OrderItem> orderItems = request.items().stream()
                .map(itemRequest -> mapToOrderItem(itemRequest, order))
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    // Map request item → order item
    private OrderItem mapToOrderItem(OrderItemRequestDTO itemRequest, Order order) {
        Product product = productRepository.findById(itemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException(Product.class, itemRequest.productId()));

        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(itemRequest.quantity())
                .unitPrice(product.getPrice())
                .subtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())))
                .build();
    }

    // Get order by ID
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, orderId));
        return orderMapper.toResponse(order);
    }

    // Get orders by user
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(User.class, userId));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }

    // Get orders by status
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orders.stream().map(orderMapper::toResponse).collect(Collectors.toList());
    }

    // Update order status
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class, orderId));
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return orderMapper.toResponse(updated);
    }
}
