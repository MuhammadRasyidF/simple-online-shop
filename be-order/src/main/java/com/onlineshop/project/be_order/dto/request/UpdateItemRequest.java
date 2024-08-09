package com.onlineshop.project.be_order.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UpdateItemRequest {

    private String itemName;

    private Integer stock;

    private BigDecimal price;

    private Boolean isAvailable;

    private Date lastReStock;

}
