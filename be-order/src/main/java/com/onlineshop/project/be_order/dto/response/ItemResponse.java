package com.onlineshop.project.be_order.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class ItemResponse {

    private Integer itemId;

    private String itemName;

    private UUID code;

    private Integer stock;

    private BigDecimal price;

    private Boolean isAvailable;

    private Date lastReStock;

}
