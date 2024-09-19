package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findAll(Specification<Order> spec, Pageable pageable);

}
