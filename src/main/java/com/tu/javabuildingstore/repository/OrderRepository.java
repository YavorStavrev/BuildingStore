package com.tu.javabuildingstore.repository;

import com.tu.javabuildingstore.model.Order;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // all orders for specific user
    List<Order> findByUser(User user);

    // all order by status
    List<Order> findByStatus(OrderStatus status);

}
