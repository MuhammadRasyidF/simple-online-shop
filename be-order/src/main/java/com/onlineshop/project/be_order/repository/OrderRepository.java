package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
