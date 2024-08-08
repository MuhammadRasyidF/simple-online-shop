package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.isActive = :isActive WHERE c.id = :id")
    void updateIsActiveById(Integer id, Boolean isActive);

}
