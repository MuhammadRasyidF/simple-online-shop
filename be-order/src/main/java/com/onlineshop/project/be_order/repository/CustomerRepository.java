package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.isActive = :isActive WHERE c.id = :id")
    void updateIsActiveById(Integer id, Boolean isActive);

}
