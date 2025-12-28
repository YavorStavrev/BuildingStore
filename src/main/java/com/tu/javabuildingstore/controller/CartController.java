package com.tu.javabuildingstore.controller;

import com.tu.javabuildingstore.dto.cart.CartResponseDTO;
import com.tu.javabuildingstore.dto.cartItem.CartItemRequestDTO;
import com.tu.javabuildingstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Get cart for specific user
    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    // Add item to cart
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem(@RequestParam Long userId,
                                                   @RequestBody CartItemRequestDTO dto) {
        return ResponseEntity.ok(cartService.addItem(userId, dto));
    }

    // Update cart item quantity
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateItem(@RequestParam Long userId,
                                                      @PathVariable Long itemId,
                                                      @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateItem(userId, itemId, quantity));
    }

    // Remove item from cart
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> removeItem(@RequestParam Long userId,
                                                      @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }

    // Clear cart
    @DeleteMapping
    public ResponseEntity<CartResponseDTO> clearCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}
