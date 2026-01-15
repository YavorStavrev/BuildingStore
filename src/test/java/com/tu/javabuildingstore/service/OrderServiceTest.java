package com.training.building_store.service;

import com.training.building_store.dto.order.OrderRequestDTO;
import com.training.building_store.dto.order.OrderResponseDTO;
import com.training.building_store.dto.orderItem.OrderItemRequestDTO;
import com.training.building_store.exception.ResourceNotFoundException;
import com.training.building_store.mapper.OrderMapper;
import com.training.building_store.model.*;
import com.training.building_store.model.enums.OrderStatus;
import com.training.building_store.repository.OrderRepository;
import com.training.building_store.repository.ProductRepository;
import com.training.building_store.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderMapper orderMapper;
    private OrderService orderService;

    private User user;
    private Product product;
    private Order order;
    private OrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderMapper = mock(OrderMapper.class);

        orderService = new OrderService(orderRepository, userRepository, productRepository, orderMapper);

        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        product = Product.builder()
                .id(1L)
                .name("Hammer")
                .price(new BigDecimal("50"))
                .stockQuantity(100)
                .build();

        order = Order.builder()
                .id(1L)
                .user(user)
                .status(OrderStatus.NEW)
                .totalPrice(new BigDecimal("100"))
                .createdAt(LocalDateTime.now())
                .build();

        responseDTO = new OrderResponseDTO(
                1L, 1L, List.of(), new BigDecimal("100"), OrderStatus.NEW, LocalDateTime.now()
        );
    }

    @Test
    void testCreateOrder_Success() {
        OrderItemRequestDTO itemRequest = new OrderItemRequestDTO(1L, 2);
        OrderRequestDTO request = new OrderRequestDTO(List.of(itemRequest));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(ArgumentMatchers.any(Order.class))).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.createOrder(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.userId());
        verify(orderRepository).save(ArgumentMatchers.any(Order.class));
    }

    @Test
    void testCreateOrder_UserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        OrderRequestDTO request = new OrderRequestDTO(List.of());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(99L, request));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(99L));
    }

    @Test
    void testGetOrdersByUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        List<OrderResponseDTO> result = orderService.getOrdersByUser(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetOrdersByStatus_Success() {
        when(orderRepository.findByStatus(OrderStatus.NEW)).thenReturn(List.of(order));
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        List<OrderResponseDTO> result = orderService.getOrdersByStatus(OrderStatus.NEW);

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

        assertNotNull(result);
        verify(orderRepository).save(order);
    }

    @Test
    void testUpdateOrderStatus_NotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrderStatus(99L, OrderStatus.SHIPPED));
    }
}
