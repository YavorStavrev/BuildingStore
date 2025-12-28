package com.tu.javabuildingstore.repository;

import com.tu.javabuildingstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
