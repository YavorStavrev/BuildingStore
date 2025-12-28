package com.tu.javabuildingstore.controller;

import com.tu.javabuildingstore.dto.order.OrderRequestDTO;
import com.tu.javabuildingstore.dto.order.OrderResponseDTO;
import com.tu.javabuildingstore.model.enums.OrderStatus;
import com.tu.javabuildingstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Create order for a given user
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestParam Long userId,
                                                        @RequestBody OrderRequestDTO request) {
        OrderResponseDTO order = orderService.createOrder(userId, request);
        return ResponseEntity.ok(order);
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Get all orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    // Get orders by status
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(@RequestParam OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    // Update order status
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestParam OrderStatus status) {
        OrderResponseDTO order = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(order);
    }
}
