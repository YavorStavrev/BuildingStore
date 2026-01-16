package com.tu.javabuildingstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tu.javabuildingstore.dto.order.OrderRequestDTO;
import com.tu.javabuildingstore.dto.order.OrderResponseDTO;
import com.tu.javabuildingstore.dto.orderItem.OrderItemRequestDTO;
import com.tu.javabuildingstore.model.enums.OrderStatus;
import com.tu.javabuildingstore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private OrderResponseDTO sampleOrder;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        sampleOrder = new OrderResponseDTO(
                1L,
                1L,
                List.of(),
                new BigDecimal("100"),
                OrderStatus.NEW,
                LocalDateTime.now()
        );
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderItemRequestDTO item = new OrderItemRequestDTO(1L, 2);
        OrderRequestDTO request = new OrderRequestDTO(List.of(item));

        Mockito.when(orderService.createOrder(eq(1L), any(OrderRequestDTO.class)))
                .thenReturn(sampleOrder);

        mockMvc.perform(post("/api/orders")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void testGetOrderById() throws Exception {
        Mockito.when(orderService.getOrderById(1L)).thenReturn(sampleOrder);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testGetOrdersByUser() throws Exception {
        Mockito.when(orderService.getOrdersByUser(1L)).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testGetOrdersByStatus() throws Exception {
        Mockito.when(orderService.getOrdersByStatus(OrderStatus.NEW)).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/orders")
                        .param("status", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        Mockito.when(orderService.updateOrderStatus(1L, OrderStatus.SHIPPED)).thenReturn(sampleOrder);

        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }
}
