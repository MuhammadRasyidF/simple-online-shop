package com.onlineshop.project.be_order.dto.response;

import com.onlineshop.project.be_order.model.Customer;
import com.onlineshop.project.be_order.model.Item;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private Integer orderId;

    private UUID code;

    private Date date;

    private BigDecimal totalPrice;

    private Integer quantity;

    private Customer customer;

    private Item item;
}
