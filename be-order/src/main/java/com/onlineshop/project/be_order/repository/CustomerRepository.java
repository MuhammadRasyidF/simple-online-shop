package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
