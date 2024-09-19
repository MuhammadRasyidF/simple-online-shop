package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    Page<Item> findAll(Specification<Item> spec, Pageable pageable);

}
