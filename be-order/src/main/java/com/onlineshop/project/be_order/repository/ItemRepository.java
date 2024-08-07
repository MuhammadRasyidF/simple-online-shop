package com.onlineshop.project.be_order.repository;

import com.onlineshop.project.be_order.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
